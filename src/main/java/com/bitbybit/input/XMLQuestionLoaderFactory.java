package com.bitbybit.input;

/**
 * A factory class for creating {@link XMLQuestionLoader} instances.
 * This factory is responsible for determining if a given filepath
 * is suitable for an XML loader and then instantiating the loader.
 */
public class XMLQuestionLoaderFactory extends QuestionLoaderFactory {

    /**
     * Creates a new {@link XMLQuestionLoader} if the provided filepath
     * ends with ".xml" (case-insensitive).
     *
     * @param filepath The path to the question file.
     * @return A new instance of {@link XMLQuestionLoader}.
     * @throws IllegalArgumentException if the filepath is null or does not
     *                                  have an ".xml" extension.
     */
    @Override
    public QuestionLoader createQuestionLoader(String filepath) {

        if (filepath == null || !filepath.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new XMLQuestionLoader();
    }
}
