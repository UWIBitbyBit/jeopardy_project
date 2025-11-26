package com.bitbybit.logging;

/**
 * Represents a game event indicating that a player has selected a question
 * (by category and value). This event carries information about the chosen
 * category, the question's value, and the player who made the selection.
 */
public class SelectQuestionEvent implements GameEvent {
    private final String category;
    private final int value;
    private final String playerName;

    /**
     * Constructs a new SelectQuestionEvent.
     *
     * @param category The name of the category from which the question was selected.
     * @param value The point value of the question selected.
     * @param playerName The name of the player who selected the question.
     */
    public SelectQuestionEvent(String category, int value, String playerName) {
        this.category = category;
        this.value = value;
        this.playerName = playerName;
    }

    /**
     * Returns the name of the category from which the question was selected.
     *
     * @return The category name.
     */
    public String getCategory() { return category; }

    /**
     * Returns the point value of the question selected.
     *
     * @return The question's value.
     */
    public int getValue() { return value; }

    /**
     * Returns the name of the player who selected the question.
     *
     * @return The player's name.
     */
    public String getPlayerName() { return playerName; }

    /**
     * Returns the type of this game event, which is "SELECT_QUESTION".
     *
     * @return A string constant "SELECT_QUESTION".
     */
    @Override
    public String getType() {
        return "SELECT_QUESTION";
    }
}
