package com.bitbybit.logging;

public class GameFinishedEvent implements GameEvent {
    @Override
    public String getType() {
        return "GAME_FINISHED";
    }
    
}
