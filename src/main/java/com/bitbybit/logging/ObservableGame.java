package com.bitbybit.logging;
import java.util.ArrayList;
import java.util.List;

/**
 * The ObservableGame class serves as a base for game components that need to
 * notify observers about various game events. It implements the observer pattern,
 * allowing multiple {@link GameObserver} instances to register and receive event notifications.
 */
public class ObservableGame {
    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Adds a {@link GameObserver} to the list of observers.
     * The added observer will be notified of all subsequent game events.
     *
     * @param observer The {@link GameObserver} to add.
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes a {@link GameObserver} from the list of observers.
     * The removed observer will no longer receive game event notifications.
     *
     * @param observer The {@link GameObserver} to remove.
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered {@link GameObserver}s about a specific {@link GameEvent}.
     * Each observer's {@link GameObserver#onEvent(GameEvent)} method will be called.
     *
     * @param event The {@link GameEvent} to notify observers about.
     */
    public void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onEvent(event);
        }
    }
}
