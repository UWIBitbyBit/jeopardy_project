package com.bitbybit.model;

import java.util.*;

/**
 * Represents the Jeopardy game board, managing a collection of {@link Question}s.
 * It provides methods to retrieve categories, available question values within categories,
 * specific questions, and to mark questions as answered.
 */
public class QuestionBoard {
    private final List<Question> questions;

    /**
     * Constructs a new QuestionBoard with the given list of questions.
     *
     * @param questions A list of {@link Question} objects to populate the board.
     */
    public QuestionBoard(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
    }

    /**
     * Retrieves a sorted list of all unique categories present on the board.
     * The categories are returned in their original format.
     *
     * @return A {@link List} of category names (Strings), sorted alphabetically.
     */
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

    /**
     * Normalizes a category string by trimming whitespace, converting to lowercase,
     * and removing all internal spaces. This is used for case-insensitive and
     * space-insensitive matching.
     *
     * @param category The category string to normalize.
     * @return The normalized category string.
     */
    private String normalizeCategory(String category) {
        return category.trim().toLowerCase().replaceAll("\\s+", "");
    }

    /**
     * Retrieves a sorted list of available point values for questions within a specific category.
     * Only values for unanswered questions in the given category are returned.
     * The category matching is case-insensitive and space-insensitive.
     *
     * @param category The category name to search for.
     * @return A {@link List} of available integer values, sorted numerically.
     */
    public List<Integer> getAvailableValues(String category) {
        String normalizedCategory = normalizeCategory(category);
        List<Integer> values = new ArrayList<>();
        for (Question q : questions) {
            if (q != null &&
                    normalizeCategory(q.getCategory()).equals(normalizedCategory) &&
                    !q.isAnswered()) {
                values.add(q.getValue());
            }
        }
        Collections.sort(values);
        return values;
    }

    /**
     * Retrieves a specific {@link Question} from the board based on its category and value.
     * Only unanswered questions are considered. The category matching is case-insensitive
     * and space-insensitive.
     *
     * @param category The category of the question.
     * @param value The point value of the question.
     * @return The matching {@link Question} object, or {@code null} if not found or already answered.
     */
    public Question getQuestion(String category, int value) {
        String normalizedCategory = normalizeCategory(category);
        for (Question q : questions) {
            if (q != null &&
                    normalizeCategory(q.getCategory()).equals(normalizedCategory) &&
                    q.getValue() == value &&
                    !q.isAnswered()) {
                return q;
            }
        }
        return null;
    }

    /**
     * Marks a specific question as answered. The category matching is case-insensitive
     * and space-insensitive.
     *
     * @param category The category of the question to mark.
     * @param value The point value of the question to mark.
     */
    public void markQuestionAsAnswered(String category, int value) {
        String normalizedCategory = normalizeCategory(category);
        for (Question q : questions) {
            if (q != null &&
                    normalizeCategory(q.getCategory()).equals(normalizedCategory) &&
                    q.getValue() == value &&
                    !q.isAnswered()) {
                q.markAnswered(true);
                return;
            }
        }
    }

    /**
     * Checks if all questions on the board have been answered.
     *
     * @return {@code true} if all questions are answered, {@code false} otherwise.
     */
    public boolean isBoardEmpty() {
        for (Question q : questions) {
            if (q != null && !q.isAnswered()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves a list of all available (unanswered) questions within a specific category.
     * The category matching is case-insensitive and space-insensitive.
     *
     * @param category The category name to search for.
     * @return A {@link List} of available {@link Question} objects in the specified category.
     */
    public List<Question> getAvailableQuestions(String category) {
        String normalizedCategory = normalizeCategory(category);
        List<Question> availableQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q != null && normalizeCategory(q.getCategory()).equals(normalizedCategory) && !q.isAnswered()) {
                availableQuestions.add(q);
            }
        }
        return availableQuestions;
    }
}
