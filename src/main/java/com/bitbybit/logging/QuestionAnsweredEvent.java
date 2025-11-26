package com.bitbybit.logging;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;

/**
 * Represents a game event indicating that a player has answered a question.
 * This event carries details about the player, the question, the correctness
 * of the answer, the answer given, points earned, and the player's running score.
 */
public class QuestionAnsweredEvent implements GameEvent {
    private final Player player;
    private final Question question;
    private final boolean correct;
    private final String answerGiven;
    private final int pointsEarned;
    private final int runningScore;

    /**
     * Constructs a new QuestionAnsweredEvent.
     *
     * @param player The {@link Player} who answered the question.
     * @param question The {@link Question} that was answered.
     * @param correct {@code true} if the answer was correct, {@code false} otherwise.
     * @param answerGiven The answer provided by the player.
     * @param pointsEarned The points earned (or deducted) for this answer.
     * @param runningScore The player's total score after this question.
     */
    public QuestionAnsweredEvent(Player player, Question question, boolean correct, String answerGiven, int pointsEarned, int runningScore) {
        this.player = player;
        this.question = question;
        this.correct = correct;
        this.answerGiven = answerGiven;
        this.pointsEarned = pointsEarned;
        this.runningScore = runningScore;
    }

    /**
     * Returns the {@link Player} associated with this event.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the ID of the player associated with this event.
     *
     * @return The player's ID.
     */
    public String getPlayerId() {
        return String.valueOf(player.getId());
    }

    /**
     * Returns the {@link Question} associated with this event.
     *
     * @return The question.
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Checks if the answer given was correct.
     *
     * @return {@code true} if the answer was correct, {@code false} otherwise.
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * Returns the answer string provided by the player.
     *
     * @return The player's answer.
     */
    public String getAnswerGiven() {
        return answerGiven;
    }

    /**
     * Returns the points earned (or deducted) for this answer.
     *
     * @return The points earned.
     */
    public int getPointsEarned() {
        return pointsEarned;
    }

    /**
     * Returns the player's total score after this question.
     *
     * @return The running score.
     */
    public int getRunningScore() {
        return runningScore;
    }

    /**
     * Returns the type of this game event, which is "QUESTION_ANSWERED".
     *
     * @return A string constant "QUESTION_ANSWERED".
     */
    @Override
        public String getType() {
            return "QUESTION_ANSWERED";
        }
}
