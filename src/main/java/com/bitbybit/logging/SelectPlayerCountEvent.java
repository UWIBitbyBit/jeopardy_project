package com.bitbybit.logging;

/**
 * Represents a game event indicating that the player count has been selected.
 * This event carries the number of players chosen for the game.
 */
public class SelectPlayerCountEvent implements GameEvent {
    private final int count;

    /**
     * Constructs a new SelectPlayerCountEvent with the specified player count.
     *
     * @param count The number of players selected.
     */
    public SelectPlayerCountEvent(int count) {
        this.count = count;
    }

    /**
     * Returns the selected player count.
     *
     * @return The number of players.
     */
    public int getCount() { return count; }

    /**
     * Returns the type of this game event, which is "SELECT_PLAYER_COUNT".
     *
     * @return A string constant "SELECT_PLAYER_COUNT".
     */
    @Override
    public String getType() {
        return "SELECT_PLAYER_COUNT";
    }
}
