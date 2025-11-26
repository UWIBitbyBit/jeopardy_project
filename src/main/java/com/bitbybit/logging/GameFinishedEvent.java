package com.bitbybit.logging;

/**
 * Represents a game event indicating that the Jeopardy game has finished.
 * This event is typically triggered when the game ends, either by all questions
 * being answered or a player choosing to quit.
 */
public class GameFinishedEvent implements GameEvent {
    /**
     * Returns the type of this game event, which is "GAME_FINISHED".
     *
     * @return A string constant "GAME_FINISHED".
     */
    @Override
    public String getType() {
        return "GAME_FINISHED";
    }
}
