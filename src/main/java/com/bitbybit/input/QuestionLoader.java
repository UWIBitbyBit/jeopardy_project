package com.bitbybit.input;

import com.bitbybit.model.Question;

/**
 * The QuestionLoader interface defines the contract for classes that are
 * responsible for loading Jeopardy questions from various data sources.
 */
public interface QuestionLoader {
    /**
     * Loads an array of {@link Question} objects from the specified file path.
     *
     * @param filepath The path to the file containing the questions.
     * @return An array of {@link Question} objects. Returns an empty array if no questions are found or an error occurs.
     */
    Question[] loadQuestions(String filepath);
}
