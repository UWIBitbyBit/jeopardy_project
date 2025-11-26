package com.bitbybit.logging;

/**
 * Represents a game event indicating that a player has selected a category.
 * This event carries information about the chosen category and the player who selected it.
 */
public class SelectCategoryEvent implements GameEvent {
    private final String category;
    private final String playerName;

    /**
     * Constructs a new SelectCategoryEvent.
     *
     * @param category The name of the category selected.
     * @param playerName The name of the player who selected the category.
     */
    public SelectCategoryEvent(String category, String playerName) {
        this.category = category;
        this.playerName = playerName;
    }

    /**
     * Returns the name of the selected category.
     *
     * @return The category name.
     */
    public String getCategory() { return category; }

    /**
     * Returns the name of the player who selected the category.
     *
     * @return The player's name.
     */
    public String getPlayerName() { return playerName; }

    /**
     * Returns the type of this game event, which is "SELECT_CATEGORY".
     *
     * @return A string constant "SELECT_CATEGORY".
     */
    @Override
    public String getType() {
        return "SELECT_CATEGORY";
    }
}
