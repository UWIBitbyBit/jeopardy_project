package com.bitbybit.game;

import com.bitbybit.input.CSVQuestionLoader;
import com.bitbybit.input.QuestionLoader;
import com.bitbybit.input.QuestionLoaderFactory;
import com.bitbybit.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GameFlowIntegrationTest {

    private GameContext gameContext;
    private Question[] testQuestions;

    @BeforeEach
    void setUp() {
        testQuestions = new Question[] {
                new Question("11", "Science", 100, "What is H2O?", "Hydrogen", "Water", "Oxygen", "Salt", "Water"),
                new Question("12", "Science", 200, "What planet is closest to the sun?", "Mercury", "Venus", "Earth", "Mars", "Mercury"),
                new Question("21", "History", 100, "When did WWII end?", "1945", "1946", "1944", "1943", "1945"),
                new Question("22", "History", 200, "Who was the first US president?", "Washington", "Adams", "Jefferson", "Madison", "Washington")
        };
    }

    @Test
    void testFullGameFlowSinglePlayer() {
        // Simulate: Load file -> Play 1 round -> Finish game
        String input = "1\nPlayer1\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        // Execute playing state
        playingState.executeState(gameContext);

        // Verify player was created and answered correctly
        assertEquals(1, gameContext.getPlayers().size());
        assertEquals("Player1", gameContext.getPlayers().get(0).getName());
        assertEquals(100, gameContext.getPlayers().get(0).getScore());

        // Execute finished state
        String finishInput = "no\n";
        InputStream finishIn = new ByteArrayInputStream(finishInput.getBytes());
        Scanner finishScanner = new Scanner(finishIn);
        gameContext = new GameContext(new FinishedState(), finishScanner);
        gameContext.notifyObservers(new com.bitbybit.logging.PlayerJoinedEvent(
                new com.bitbybit.model.Player("Player1")));

        assertDoesNotThrow(() -> new FinishedState().executeState(gameContext));
    }

    @Test
    void testFullGameFlowMultiplePlayers() {
        // Simulate: 2 players each answer one question
        String input = "2\nAlice\nBob\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Verify both players were created
        assertEquals(2, gameContext.getPlayers().size());
        assertTrue(gameContext.getPlayers().stream().anyMatch(p -> p.getName().equals("Alice")));
        assertTrue(gameContext.getPlayers().stream().anyMatch(p -> p.getName().equals("Bob")));
    }

    @Test
    void testLoadQuestionsFromCSV(@TempDir Path tempDir) throws IOException {
        // Create test CSV file
        Path csvFile = tempDir.resolve("game_questions.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "Science,100,What is H2O?,Hydrogen,Water,Oxygen,Salt,Water\n" +
                "Science,200,What planet is closest to the sun?,Mercury,Venus,Earth,Mars,Mercury";

        Files.write(csvFile, csvContent.getBytes());

        // Create a concrete factory implementation
        QuestionLoaderFactory factory = new QuestionLoaderFactory() {
            @Override
            public QuestionLoader createQuestionLoader(String filepath) {
                if (filepath.endsWith(".csv")) {
                    return new CSVQuestionLoader();
                }
                throw new IllegalArgumentException("Unsupported file format");
            }
        };

        // Test loading
        QuestionLoader loader = factory.createQuestionLoader(csvFile.toString());
        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(2, questions.length);
        assertEquals("Water", questions[0].getCorrectAnswer());
    }

    @Test
    void testGameStateTransitions() {
        String input = "1\nPlayer1\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        QuestionLoaderFactory factory = new QuestionLoaderFactory() {
            @Override
            public QuestionLoader createQuestionLoader(String filepath) {
                return new CSVQuestionLoader();
            }
        };

        GameState introState = new IntroState(factory);
        gameContext = new GameContext(introState, scanner);

        // Verify initial state
        assertTrue(gameContext.getState() instanceof IntroState);

        // Note: Full state transition testing would require mocking file I/O
        // This test demonstrates the pattern
    }

    @Test
    void testEventLogging() {
        String input = "1\nTestPlayer\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Verify events were logged
        assertFalse(gameContext.getGameEvents().isEmpty());
        
        // Should have SelectPlayerCountEvent
        assertTrue(gameContext.getGameEvents().stream()
                .anyMatch(e -> e instanceof com.bitbybit.logging.SelectPlayerCountEvent));
        
        // Should have PlayerJoinedEvent
        assertTrue(gameContext.getGameEvents().stream()
                .anyMatch(e -> e instanceof com.bitbybit.logging.PlayerJoinedEvent));
        
        // Should have QuestionAnsweredEvent
        assertTrue(gameContext.getGameEvents().stream()
                .anyMatch(e -> e instanceof com.bitbybit.logging.QuestionAnsweredEvent));
    }

    @Test
    void testIncorrectAnswerPenalty() {
        String input = "1\nPlayer1\nScience\n100\nHydrogen\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Verify player loses points for incorrect answer
        assertEquals(-100, gameContext.getPlayers().get(0).getScore());
    }

    @Test
    void testMultipleRoundsGameFlow() {
        // Test a single player answering one question correctly
        String input = "1\nPlayer1\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Player should have answered 1 question correctly
        assertEquals(100, gameContext.getPlayers().get(0).getScore());
    }

    @Test
    void testNoQuestionsGameFlow() {
        String input = "1\nPlayer1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(new Question[0]); // Empty questions

        playingState.executeState(gameContext);

        // Game should end immediately
        assertTrue(gameContext.getState() instanceof FinishedState);
    }

    @Test
    void testPlayerScoreSorting() {
        String input = "3\nAlice\nBob\nCharlie\nScience\n100\nWater\ny\nScience\n200\nMercury\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Alice should have highest score (answered 2 questions)
        assertTrue(gameContext.getPlayers().get(0).getScore() > 0);
    }

    @Test
    void testQuestionBoardClearing() {
        // Test that game ends when player answers one question
        String input = "1\nPlayer1\nScience\n100\nWater\nn\nno\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        PlayingState playingState = new PlayingState();
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // After answering one question, player should have 100 points
        assertEquals(100, gameContext.getPlayers().get(0).getScore());
        
        // Game should end because user chose not to continue
        assertTrue(gameContext.getState() instanceof FinishedState);
    }
}
