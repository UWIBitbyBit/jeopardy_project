package com.bitbybit.logging;

public class LoadFileEvent implements GameEvent {
    @Override
    public String getType() {
        return "FILE_LOADED";
    }
}
