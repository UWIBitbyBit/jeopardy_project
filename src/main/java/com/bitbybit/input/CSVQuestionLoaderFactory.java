package com.bitbybit.input;

/**
 * A factory class for creating {@link CSVQuestionLoader} instances.
 * This factory is responsible for determining if a given filepath
 * is suitable for a CSV loader and then instantiating the loader.
 */
public class CSVQuestionLoaderFactory extends QuestionLoaderFactory {

    /**
     * Creates a new {@link CSVQuestionLoader} if the provided filepath
     * ends with ".csv" (case-insensitive).
     *
     * @param filepath The path to the question file.
     * @return A new instance of {@link CSVQuestionLoader}.
     * @throws IllegalArgumentException if the filepath is null or does not
     *                                  have a ".csv" extension.
     */
    @Override
    public QuestionLoader createQuestionLoader(String filepath) {

        if (filepath == null || !filepath.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new CSVQuestionLoader();
    }
}
