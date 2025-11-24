package com.bitbybit.input;

public class CSVQuestionLoaderFactory {

    public QuestionLoader createQuestionLoader(String filepath) {

        if (!filepath.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new CSVQuestionLoader();
    }
}
