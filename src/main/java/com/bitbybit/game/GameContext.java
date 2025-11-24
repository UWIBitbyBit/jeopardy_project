package com.bitbybit.game;

import java.util.Scanner;
import com.bitbybit.model.Question;

public class GameContext {
    private final Scanner scanner;
    private GameState state;
    private Question[] questions;

    public GameContext(GameState initialState) {
        this(initialState, new Scanner(System.in));
    }

    public GameContext(GameState initialState, Scanner scanner) {
        this.state = initialState;
        this.scanner = scanner;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
