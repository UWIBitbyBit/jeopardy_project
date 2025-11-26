package com.bitbybit.model;

/**
 * Represents a single Jeopardy question, including its category, value,
 * question text, multiple-choice options, correct answer, and whether it has been answered.
 */
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

    /**
     * Constructs a new Question instance.
     *
     * @param id A unique identifier for the question.
     * @param category The category to which this question belongs.
     * @param value The point value of the question.
     * @param question The text of the question.
     * @param optionA The text for option A.
     * @param optionB The text for option B.
     * @param optionC The text for option C.
     * @param optionD The text for option D.
     * @param correctAnswer The correct answer (e.g., "A", "B", "C", "D").
     */
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

    /**
     * Returns the unique ID of the question.
     *
     * @return The question ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the category of the question.
     *
     * @return The question category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the point value of the question.
     *
     * @return The question value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the text of the question.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the text for option A.
     *
     * @return Option A text.
     */
    public String getOptionA() {
        return optionA;
    }

    /**
     * Returns the text for option B.
     *
     * @return Option B text.
     */
    public String getOptionB() {
        return optionB;
    }

    /**
     * Returns the text for option C.
     *
     * @return Option C text.
     */
    public String getOptionC() {
        return optionC;
    }

    /**
     * Returns the text for option D.
     *
     * @return Option D text.
     */
    public String getOptionD() {
        return optionD;
    }

    /**
     * Returns the correct answer for the question.
     *
     * @return The correct answer (e.g., "A", "B", "C", "D").
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Checks if the question has been answered.
     *
     * @return {@code true} if the question has been answered, {@code false} otherwise.
     */
    public boolean isAnswered() {
        return isAnswered;
    }

    /**
     * Marks the question as answered or unanswered.
     *
     * @param answered {@code true} to mark as answered, {@code false} to mark as unanswered.
     */
    public void markAnswered(boolean answered) {
        isAnswered = answered;
    }
}
