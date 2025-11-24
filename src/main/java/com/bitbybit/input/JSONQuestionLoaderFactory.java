package com.bitbybit.input;

public class JSONQuestionLoaderFactory implements QuestionLoaderFactory {
    @Override
    public QuestionLoader createQuestionLoader(String filepath) {
        if (!filepath.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new JSONQuestionLoader();
    }

}
