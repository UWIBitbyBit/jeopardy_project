package com.bitbybit.game;

public class FinishedState implements GameState {
    @Override
    public void changeState(GameContext context) {
        // No further state changes from FinishedState
    }

    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Game Over! Thank you for playing.");
        // TODO: Add any final game logic here
    }

    @Override
    public void displayState() {
        System.out.println("The game has finished.");
    }
}
