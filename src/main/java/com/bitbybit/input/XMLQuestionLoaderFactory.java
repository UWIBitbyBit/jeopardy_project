package com.bitbybit.input;

public class XMLQuestionLoaderFactory implements QuestionLoaderFactory {
    @Override
    public QuestionLoader createQuestionLoader(String filepath) {
        if (!filepath.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new XMLQuestionLoader();
    }
}