package com.bitbybit.logging;

public class SelectQuestionEvent implements GameEvent {
    private final String category;
    private final int value;
    private final String playerName;
    public SelectQuestionEvent(String category, int value, String playerName) {
        this.category = category;
        this.value = value;
        this.playerName = playerName;
    }
    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getPlayerName() { return playerName; }
    @Override
    public String getType() {
        return "SELECT_QUESTION";
    }
}
