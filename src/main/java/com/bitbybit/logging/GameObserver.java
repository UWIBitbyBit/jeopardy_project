package com.bitbybit.logging;

/**
 * The GameObserver interface defines the contract for classes that wish to
 * observe and react to game events. Observers are notified whenever a
 * {@link GameEvent} occurs in the game.
 */
public interface GameObserver {
    /**
     * Called when a {@link GameEvent} occurs. Implementations should define
     * how they process or react to the specific event.
     *
     * @param event The {@link GameEvent} that has occurred.
     */
    void onEvent(GameEvent event);
}
