package com.bitbybit.model;

/**
 * Represents a player in the Jeopardy game.
 * Each player has a name, a score, and a unique ID.
 */
public class Player {
    private String name;
    private int score;
    private int id;

    /**
     * Constructs a new Player with the given name.
     * The player's score is initialized to 0, and a unique ID is generated.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.id = System.identityHashCode(this); // Simple unique ID
    }

    /**
     * Returns the name of the player.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current score of the player.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds points to the player's score.
     *
     * @param points The number of points to add.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Subtracts points from the player's score. The score will not go below zero.
     *
     * @param points The number of points to subtract.
     */
    public void subtractScore(int points) {
        this.score = Math.max(0, this.score - points);
    }

    /**
     * Returns the unique ID of the player.
     *
     * @return The player's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a string representation of the player, including their name and score.
     *
     * @return A formatted string for the player.
     */
    @Override
    public String toString() {
        return name + " (Score: " + score + ")";
    }
}
