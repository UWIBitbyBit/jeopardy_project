package com.bitbybit.game;

import com.bitbybit.input.QuestionLoaderFactory;
import com.bitbybit.input.CSVQuestionLoader;
import com.bitbybit.input.QuestionLoader;
import com.bitbybit.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class IntroStateTest {

    private IntroState introState;
    private QuestionLoaderFactory factory;
    private GameContext gameContext;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        // Create a concrete factory implementation
        factory = new QuestionLoaderFactory() {
            @Override
            public QuestionLoader createQuestionLoader(String filepath) {
                if (filepath.endsWith(".csv")) {
                    return new CSVQuestionLoader();
                } else if (filepath.endsWith(".json")) {
                    return new com.bitbybit.input.JSONQuestionLoader();
                } else if (filepath.endsWith(".xml")) {
                    return new com.bitbybit.input.XMLQuestionLoader();
                }
                throw new IllegalArgumentException("Unsupported file format");
            }
        };
        introState = new IntroState(factory);
    }

    @Test
    void testDisplayState() {
        // This test verifies that displayState outputs the intro message
        // (in a real scenario, you'd redirect System.out to verify output)
        assertDoesNotThrow(() -> introState.displayState());
    }

    @Test
    void testExecuteStateSuccessfulLoading() throws IOException {
        // Create temporary CSV file with test data
        Path tempFile = Files.createTempFile("test_questions", ".csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "Science,100,What is H2O?,Hydrogen,Water,Oxygen,Salt,Water";
        Files.write(tempFile, csvContent.getBytes());

        // Setup scanner with filename
        String input = tempFile.toString() + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(in);

        // Setup game context
        gameContext = new GameContext(introState, scanner);

        // Execute intro state
        introState.executeState(gameContext);

        // Verify questions were loaded
        Question[] questions = gameContext.getQuestions();
        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("Water", questions[0].getCorrectAnswer());

        // Cleanup
        Files.delete(tempFile);
    }

    @Test
    void testExecuteStateEmptyFile() throws IOException {
        // Create empty CSV file
        Path tempFile = Files.createTempFile("empty_questions", ".csv");
        Files.write(tempFile, new byte[0]);

        String input = tempFile.toString() + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(in);

        gameContext = new GameContext(introState, scanner);

        introState.executeState(gameContext);

        // Questions should not be set if file is empty
        Question[] questions = gameContext.getQuestions();
        assertTrue(questions == null || questions.length == 0);

        Files.delete(tempFile);
    }

    @Test
    void testChangeState() {
        String input = "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(in);
        gameContext = new GameContext(introState, scanner);

        GameState currentState = gameContext.getState();
        introState.changeState(gameContext);

        // Verify state changed to PlayingState
        assertTrue(gameContext.getState() instanceof PlayingState);
        assertNotEquals(currentState, gameContext.getState());
    }

    @Test
    void testExecuteStateWithInvalidFileFormat() {
        String input = "invalid_format_file.xyz\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(in);
        gameContext = new GameContext(introState, scanner);

        assertDoesNotThrow(() -> introState.executeState(gameContext));
    }
}
