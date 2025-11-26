package com.bitbybit.game;

import com.bitbybit.logging.GameObserver;
import com.bitbybit.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameContextTest {

    private GameContext gameContext;
    private GameState mockState;
    private GameObserver mockObserver;
    private Question[] questions;

    private Scanner testScanner;

    @BeforeEach
    void setUp() {
        String input = "test\n"; // Provide some dummy input for the scanner
        InputStream in = new ByteArrayInputStream(input.getBytes());
        testScanner = new Scanner(in);

        mockState = mock(GameState.class);
        mockObserver = mock(GameObserver.class);

        questions = new Question[] {
                new Question("Q1", "Category1", 100, "Q1 Text", "A", "B", "C", "D", "A"),
                new Question("Q2", "Category2", 200, "Q2 Text", "A", "B", "C", "D", "B")
        };

        gameContext = new GameContext(mockState, testScanner);
        gameContext.addObserver(mockObserver);
        gameContext.setQuestions(questions);
    }

    @Test
    void testInitialState() {
        assertNotNull(gameContext.getScanner());
        assertEquals(testScanner, gameContext.getScanner());
        assertArrayEquals(questions, gameContext.getQuestions());
        assertEquals(mockState, gameContext.getState());
    }

    @Test
    void testSetState() {
        GameState newState = mock(GameState.class);
        gameContext.setState(newState);
        assertEquals(newState, gameContext.getState());
    }

    @Test
    void testRunGame() {
        gameContext.run(); // Assuming a run method exists or will be added
        verify(mockState, times(1)).executeState(gameContext);
    }

    @Test
    void testNotifyObservers() {
        ArgumentCaptor<com.bitbybit.logging.GameEvent> eventCaptor = ArgumentCaptor
                .forClass(com.bitbybit.logging.GameEvent.class);
        com.bitbybit.logging.GameEvent testEvent = mock(com.bitbybit.logging.GameEvent.class);

        gameContext.notifyObservers(testEvent);

        verify(mockObserver, times(1)).onEvent(eventCaptor.capture());
        assertEquals(testEvent, eventCaptor.getValue());
    }

    @Test
    void testRemoveObserver() {
        gameContext.removeObserver(mockObserver);
        gameContext.notifyObservers(mock(com.bitbybit.logging.GameEvent.class));
        verify(mockObserver, never()).onEvent(any());
    }
}
