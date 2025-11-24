package com.bitbybit.input;

public class XMLQuestionLoaderFactory extends QuestionLoaderFactory {

    @Override
    public QuestionLoader createQuestionLoader(String filepath) {

        if (filepath == null || !filepath.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new XMLQuestionLoader();
    }
}
