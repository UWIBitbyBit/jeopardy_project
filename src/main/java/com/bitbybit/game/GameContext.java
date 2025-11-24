package com.bitbybit.game;

import com.bitbybit.model.Question;

public class GameContext {
    private GameState state;
    private Question[] questions;

    public GameContext(GameState initialState) {
        setState(initialState);
    }

    public void setState(GameState newState) {
        this.state = newState;
        this.state.displayState();
    }

    // Add this to GameContext.java
    public GameState getState() {
        return state;
    }

    public void run() {
        while (state != null) {
            state.executeState(this);
        }
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Question[] getQuestions() {
        return questions;
    }
}
