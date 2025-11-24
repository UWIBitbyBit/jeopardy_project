package com.bitbybit.game;

public class PlayingState implements GameState {
    @Override
    public void changeState(GameContext context) {
        context.setState(new FinishedState());
    }

    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Executing Playing State logic...");
        // Add logic for playing state here
        // TODO: Implement game playing logic
    }

    @Override
    public void displayState() {
        System.out.println("Displaying Playing State...");
        // Add display logic for playing state here
        // TODO: Implement display logic
    }
}