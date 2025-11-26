package com.bitbybit.logging;

/**
 * The GameEvent interface represents an event that occurs during the Jeopardy game.
 * Implementations of this interface provide a type string to identify the event.
 */
public interface GameEvent {
    /**
     * Returns the type of the game event as a string.
     *
     * @return A string representing the event type (e.g., "GAME_STARTED", "QUESTION_ANSWERED").
     */
    String getType();
}
