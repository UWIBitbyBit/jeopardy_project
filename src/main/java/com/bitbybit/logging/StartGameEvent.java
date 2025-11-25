package com.bitbybit.logging;

public class StartGameEvent implements GameEvent {
    @Override
    public String getType() {
        return "GAME_STARTED";
    }
}
