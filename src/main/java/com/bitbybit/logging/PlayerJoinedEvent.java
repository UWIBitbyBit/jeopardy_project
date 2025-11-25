package com.bitbybit.logging;
import com.bitbybit.model.Player;

public class PlayerJoinedEvent implements GameEvent {
    private final Player player;

    public PlayerJoinedEvent(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }
    @Override
        public String getType() {
            return "PLAYER_JOINED";
        }
}
