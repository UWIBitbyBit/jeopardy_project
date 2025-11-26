package com.bitbybit.logging;

/**
 * Represents a game event indicating that a new Jeopardy game has started.
 * This event is typically triggered at the very beginning of a game session.
 */
public class GameStartedEvent implements GameEvent {
    /**
     * Returns the type of this game event, which is "GAME_STARTED".
     *
     * @return A string constant "GAME_STARTED".
     */
    @Override
    public String getType() {
        return "GAME_STARTED";
    }
}
