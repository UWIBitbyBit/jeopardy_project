package com.bitbybit.game;

import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
import com.bitbybit.model.QuestionBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingStateTest {

    private PlayingState playingState;
    private GameContext gameContext;
    private Question[] testQuestions;

    @BeforeEach
    void setUp() {
        playingState = new PlayingState();
        
        // Create test questions
        testQuestions = new Question[] {
                new Question("11", "Science", 100, "What is H2O?", "Hydrogen", "Water", "Oxygen", "Salt", "Water"),
                new Question("12", "Science", 200, "What planet is closest to the sun?", "Mercury", "Venus", "Earth", "Mars", "Mercury"),
                new Question("21", "History", 100, "When did WWII end?", "1945", "1946", "1944", "1943", "1945"),
                new Question("22", "History", 200, "Who was the first president?", "Washington", "Adams", "Jefferson", "Madison", "Washington")
        };
    }

    @Test
    void testDisplayState() {
        assertDoesNotThrow(() -> playingState.displayState());
    }

    @Test
    void testExecuteStateWithValidQuestions() {
        // Setup game with 1 player, 1 turn
        String input = "1\nTestPlayer\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Verify players were created
        assertFalse(gameContext.getPlayers().isEmpty());
        assertEquals(1, gameContext.getPlayers().size());
        assertEquals("TestPlayer", gameContext.getPlayers().get(0).getName());
    }

    @Test
    void testExecuteStateWithMultiplePlayers() {
        String input = "2\nAlice\nBob\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        assertEquals(2, gameContext.getPlayers().size());
        assertTrue(gameContext.getPlayers().stream().anyMatch(p -> p.getName().equals("Alice")));
        assertTrue(gameContext.getPlayers().stream().anyMatch(p -> p.getName().equals("Bob")));
    }

    @Test
    void testChangeState() {
        String input = "1\nPlayer1\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);
        playingState.changeState(gameContext);

        // Verify state changed to FinishedState
        assertTrue(gameContext.getState() instanceof FinishedState);
    }

    @Test
    void testScoreCalculationCorrectAnswer() {
        String input = "1\nPlayer1\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        Player player = gameContext.getPlayers().get(0);
        assertEquals(100, player.getScore());
    }

    @Test
    void testScoreCalculationIncorrectAnswer() {
        String input = "1\nPlayer1\nScience\n100\nHydrogen\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        Player player = gameContext.getPlayers().get(0);
        assertEquals(-100, player.getScore());
    }

    @Test
    void testNoQuestionsLoaded() {
        String input = "1\nPlayer1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(new Question[0]); // Empty questions

        playingState.executeState(gameContext);

        // Game should end due to no questions
        assertTrue(gameContext.getState() instanceof FinishedState);
    }

    @Test
    void testGameEventNotifications() {
        String input = "1\nTestPlayer\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Verify events were logged
        assertFalse(gameContext.getGameEvents().isEmpty());
        
        // Should have player joined event
        assertTrue(gameContext.getGameEvents().stream()
                .anyMatch(e -> e instanceof com.bitbybit.logging.PlayerJoinedEvent));
    }

    @Test
    void testQuitGameMidway() {
        String input = "1\nPlayer1\nquit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Game should transition to finished state
        assertTrue(gameContext.getState() instanceof FinishedState);
    }

    @Test
    void testPlayerCountValidation() {
        // Test with invalid player count (0)
        String input = "0\n1\nPlayer1\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        // Should accept the valid count (1) after rejecting invalid (0)
        assertEquals(1, gameContext.getPlayers().size());
    }

    @Test
    void testPlayerCountMaximum() {
        String input = "4\nAlice\nBob\nCharlie\nDiana\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        assertEquals(4, gameContext.getPlayers().size());
    }

    @Test
    void testDefaultPlayerName() {
        String input = "1\n\nScience\n100\nWater\nn\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(playingState, scanner);
        gameContext.setQuestions(testQuestions);

        playingState.executeState(gameContext);

        Player player = gameContext.getPlayers().get(0);
        assertEquals("Player 1", player.getName());
    }
}
