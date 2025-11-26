package com.bitbybit.logging;

/**
 * Represents an event indicating the start of a new game.
 * This event is typically logged when a game session begins.
 */
public class StartGameEvent implements GameEvent {
    /**
     * Returns the type of this game event.
     *
     * @return A string representing the event type, which is "GAME_STARTED".
     */
    @Override
    public String getType() {
        return "GAME_STARTED";
    }
}
