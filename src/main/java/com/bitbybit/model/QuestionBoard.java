package com.bitbybit.model;

import java.util.*;

public class QuestionBoard {
    private final List<Question> questions;

    public QuestionBoard(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
    }

    public List<String> getCategories() {
        Set<String> categoriesSet = new HashSet<>();
        for (Question q : questions) {
            if (q != null) {
                categoriesSet.add(q.getCategory());
            }
        }
        List<String> categoriesList = new ArrayList<>(categoriesSet);
        Collections.sort(categoriesList);
        return categoriesList;
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

    public void markQuestionAsAnswered(String category, int value) {
        Question question = getQuestion(category, value);
        if (question != null) {
            question.markAnswered(true);
        }
    }

    public boolean isBoardEmpty() {
        for (Question q : questions) {
            if (q != null && !q.isAnswered()) {
                return false;
            }
        }
        return true;
    }

    public List<Question> getAvailableQuestions(String category) {
        List<Question> availableQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q != null && q.getCategory().equals(category) && !q.isAnswered()) {
                availableQuestions.add(q);
            }
        }
        return availableQuestions;
    }
}
