package com.bitbybit.game;

import com.bitbybit.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class FinishedStateTest {

    private FinishedState finishedState;
    private GameContext gameContext;
    private Question[] testQuestions;

    @BeforeEach
    void setUp() {
        finishedState = new FinishedState();
        
        testQuestions = new Question[] {
                new Question("11", "Science", 100, "What is H2O?", "Hydrogen", "Water", "Oxygen", "Salt", "Water"),
                new Question("12", "Science", 200, "What planet is closest to the sun?", "Mercury", "Venus", "Earth", "Mars", "Mercury")
        };
    }

    @Test
    void testDisplayState() {
        assertDoesNotThrow(() -> finishedState.displayState());
    }

    @Test
    void testExecuteStateWithoutReport() {
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(finishedState, scanner);
        gameContext.setQuestions(testQuestions);

        // Add some test players
        gameContext.notifyObservers(new com.bitbybit.logging.PlayerJoinedEvent(
                new com.bitbybit.model.Player("TestPlayer")));

        assertDoesNotThrow(() -> finishedState.executeState(gameContext));
    }

    @Test
    void testExecuteStateWithTextReport() {
        String input = "yes\ntext\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(finishedState, scanner);
        gameContext.setQuestions(testQuestions);

        gameContext.notifyObservers(new com.bitbybit.logging.PlayerJoinedEvent(
                new com.bitbybit.model.Player("TestPlayer")));

        // This test verifies the method runs without throwing exceptions
        // In a real scenario, you'd verify file output
        assertDoesNotThrow(() -> finishedState.executeState(gameContext));
    }

    @Test
    void testExecuteStateWithInvalidReportFormat() {
        String input = "yes\ninvalid\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(finishedState, scanner);
        gameContext.setQuestions(testQuestions);

        gameContext.notifyObservers(new com.bitbybit.logging.PlayerJoinedEvent(
                new com.bitbybit.model.Player("TestPlayer")));

        // Should default to text format
        assertDoesNotThrow(() -> finishedState.executeState(gameContext));
    }

    @Test
    void testChangeState() {
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(finishedState, scanner);

        GameState originalState = gameContext.getState();
        finishedState.changeState(gameContext);

        // FinishedState does not change state further
        assertEquals(originalState, gameContext.getState());
    }

    @Test
    void testGameContextIntegration() {
        String input = "no\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        
        gameContext = new GameContext(finishedState, scanner);
        gameContext.setQuestions(testQuestions);

        com.bitbybit.model.Player player = new com.bitbybit.model.Player("Winner");
        player.addScore(500);
        gameContext.notifyObservers(new com.bitbybit.logging.PlayerJoinedEvent(player));

        assertFalse(gameContext.getPlayers().isEmpty());
        assertEquals("Winner", gameContext.getPlayers().get(0).getName());
    }
}
