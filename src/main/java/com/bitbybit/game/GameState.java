package com.bitbybit.game;

/**
 * The GameState interface defines the contract for different states
 * within the Jeopardy game. Each state is responsible for displaying
 * its UI/prompts, executing its specific logic, and handling transitions
 * to other states.
 */
public interface GameState {
    /**
     * Displays the user interface or prompts relevant to the current game state.
     */
    void displayState();

    /**
     * Executes the core logic of the current game state, including processing
     * user input and performing necessary game operations.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    void executeState(GameContext ctx);

    /**
     * Handles the transition from the current state to the next appropriate game state.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    void changeState(GameContext ctx);
}
