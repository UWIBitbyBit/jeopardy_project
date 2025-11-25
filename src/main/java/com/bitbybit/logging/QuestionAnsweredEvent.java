package com.bitbybit.logging;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
public class QuestionAnsweredEvent implements GameEvent {
    private final Player player;
    private final Question question;
    private final boolean correct;
    private final String answerGiven;

    public QuestionAnsweredEvent(Player player, Question question, boolean correct, String answerGiven) {
        this.player = player;
        this.question = question;
        this.correct = correct;
        this.answerGiven = answerGiven;
    }
    public Player getPlayer() {
        return player;
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
    @Override
        public String getType() {
            return "QUESTION_ANSWERED";
        }
}
