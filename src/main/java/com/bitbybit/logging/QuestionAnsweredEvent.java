package com.bitbybit.logging;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
public class QuestionAnsweredEvent implements GameEvent {
    private final Player player;
    private final Question question;
    private final boolean correct;
    private final String answerGiven;
    private final int pointsEarned;
    private final int runningScore;

    public QuestionAnsweredEvent(Player player, Question question, boolean correct, String answerGiven, int pointsEarned, int runningScore) {
        this.player = player;
        this.question = question;
        this.correct = correct;
        this.answerGiven = answerGiven;
        this.pointsEarned = pointsEarned;
        this.runningScore = runningScore;
    }
    public Player getPlayer() {
        return player;
    }

    public String getPlayerId() {
        return String.valueOf(player.getId());
    }
    public Question getQuestion() {
        return question;
    }
    public boolean isCorrect() {
        return correct;
    }
    public String getAnswerGiven() {
        return answerGiven;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public int getRunningScore() {
        return runningScore;
    }

    @Override
        public String getType() {
            return "QUESTION_ANSWERED";
        }
}
