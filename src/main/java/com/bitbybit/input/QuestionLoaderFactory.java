package com.bitbybit.input;

/**
 * An abstract factory class for creating {@link QuestionLoader} instances.
 * Subclasses must implement the {@link #createQuestionLoader(String)} method
 * to provide specific question loader implementations based on the file type.
 */
public abstract class QuestionLoaderFactory {
    /**
     * Creates and returns a {@link QuestionLoader} instance based on the provided filepath.
     * The implementation of this method should determine the appropriate loader
     * (e.g., CSV, JSON, XML) based on the file extension or content.
     *
     * @param filepath The path to the question file.
     * @return A concrete implementation of {@link QuestionLoader}.
     * @throws IllegalArgumentException if the file type is not supported.
     */
    public abstract QuestionLoader createQuestionLoader(String filepath);
}
