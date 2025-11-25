package com.bitbybit.logging;

public class GameStartedEvent implements GameEvent {
    @Override
    public String getType() {
        return "GAME_STARTED";
    }
}
