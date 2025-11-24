package com.bitbybit.model;

public class Question {
    private final String id;
    private final String category;
    private final int value;
    private final String question;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String correctAnswer;
    private boolean isAnswered;

    // Note: parameter 'category' is correctly spelled now
    public Question(
            String id,
            String category,
            int value,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer) {
        this.id = id;
        this.category = category;
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

    // New, correct name
    public String getCategory() {
        return category;
    }

    // Optional: keep legacy spelling if other code/tests still call it
    public String getCatergory() {
        return category;
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
