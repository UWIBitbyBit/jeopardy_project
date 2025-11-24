package com.bitbybit.model;

public class Question {
    private String id;
    private String catergory;
    private int value;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private boolean isAnswered;

    public Question(String id, String catergory, int value, String question, String optionA, String optionB,
            String optionC, String optionD, String correctAnswer) {
        this.id = id;
        this.catergory = catergory;
        this.value = value;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.isAnswered = false;
    }

    public String getId() {
        return id;
    }

    public String getCatergory() {
        return catergory;
    }

    public int getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void markAnswered(boolean answered) {
        isAnswered = answered;
    }
}
