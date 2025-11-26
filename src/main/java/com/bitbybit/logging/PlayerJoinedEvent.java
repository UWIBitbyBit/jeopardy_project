package com.bitbybit.logging;
import com.bitbybit.model.Player;

/**
 * Represents a game event indicating that a player has joined the game.
 * This event carries information about the {@link Player} who joined.
 */
public class PlayerJoinedEvent implements GameEvent {
    private final Player player;

    /**
     * Constructs a new PlayerJoinedEvent with the specified player.
     *
     * @param player The {@link Player} who joined the game.
     */
    public PlayerJoinedEvent(Player player) {
        this.player = player;
    }

    /**
     * Returns the {@link Player} associated with this event.
     *
     * @return The player who joined.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the type of this game event, which is "PLAYER_JOINED".
     *
     * @return A string constant "PLAYER_JOINED".
     */
    @Override
        public String getType() {
            return "PLAYER_JOINED";
        }
}
