package com.bitbybit.game;

public interface GameState {
    void displayState(); // show UI / prompts

    void executeState(GameContext ctx); // perform logic, read input

    void changeState(GameContext ctx); // transition to next state

}
