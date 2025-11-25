package com.bitbybit.logging;

public class SelectPlayerCountEvent implements GameEvent {
    private final int count;
    public SelectPlayerCountEvent(int count) {
        this.count = count;
    }
    public int getCount() { return count; }
    @Override
    public String getType() {
        return "SELECT_PLAYER_COUNT";
    }
}
