package com.bitbybit.model;

public class Player {
    private String name;
    private int score;
    private int id;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.id = System.identityHashCode(this);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getId() {
        return id;
    }
}
