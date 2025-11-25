package com.bitbybit.logging;

public class SelectCategoryEvent implements GameEvent {
    private final String category;
    private final String playerName;
    public SelectCategoryEvent(String category, String playerName) {
        this.category = category;
        this.playerName = playerName;
    }
    public String getCategory() { return category; }
    public String getPlayerName() { return playerName; }
    @Override
    public String getType() {
        return "SELECT_CATEGORY";
    }
}
