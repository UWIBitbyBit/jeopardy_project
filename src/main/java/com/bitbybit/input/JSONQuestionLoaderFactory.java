package com.bitbybit.input;

/**
 * A factory class for creating {@link JSONQuestionLoader} instances.
 * This factory is responsible for determining if a given filepath
 * is suitable for a JSON loader and then instantiating the loader.
 */
public class JSONQuestionLoaderFactory extends QuestionLoaderFactory {

    /**
     * Creates a new {@link JSONQuestionLoader} if the provided filepath
     * ends with ".json" (case-insensitive).
     *
     * @param filepath The path to the question file.
     * @return A new instance of {@link JSONQuestionLoader}.
     * @throws IllegalArgumentException if the filepath does not have a ".json" extension.
     */
    @Override
    public QuestionLoader createQuestionLoader(String filepath) {

        if (filepath == null || !filepath.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new JSONQuestionLoader();
    }
}
