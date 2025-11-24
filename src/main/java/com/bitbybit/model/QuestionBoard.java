package com.bitbybit.model;

import com.bitbybit.model.Question;

import java.util.*;

public class QuestionBoard {
    private final List<Question> questions;

    public QuestionBoard(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
    }

    public Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        for (Question q : questions) {
            if (q != null) {
                categories.add(q.getCategory());
            }
        }
        return categories;
    }

    public List<Integer> getAvailableValues(String category) {
        List<Integer> values = new ArrayList<>();
        for (Question q : questions) {
            if (q != null &&
                    q.getCategory().equals(category) &&
                    !q.isAnswered()) {
                values.add(q.getValue());
            }
        }
        Collections.sort(values);
        return values;
    }

    public Question getQuestion(String category, int value) {
        for (Question q : questions) {
            if (q != null &&
                    q.getCategory().equals(category) &&
                    q.getValue() == value &&
                    !q.isAnswered()) {
                return q;
            }
        }
        return null;
    }

    public void markAnswered(Question question) {
        if (question != null) {
            question.markAnswered(true);
        }
    }

    public boolean allQuestionsAnswered() {
        for (Question q : questions) {
            if (q != null && !q.isAnswered()) {
                return false;
            }
        }
        return true;
    }
}
