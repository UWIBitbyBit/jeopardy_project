package com.bitbybit.game;

public class FinishedState implements GameState {

    @Override
    public void displayState() {
        System.out.println("\n==== GAME FINISHED ====");
    }

    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Thank you for playing!");
        // no further state transition
    }

    @Override
    public void changeState(GameContext ctx) {
        // No-op: this is the terminal state
    }
}
