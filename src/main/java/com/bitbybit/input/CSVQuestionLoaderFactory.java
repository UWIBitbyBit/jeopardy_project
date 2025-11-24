package com.bitbybit.input;

public class CSVQuestionLoaderFactory extends QuestionLoaderFactory {

    @Override
    public QuestionLoader createQuestionLoader(String filepath) {

        if (filepath == null || !filepath.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Unsupported file type: " + filepath);
        }

        return new CSVQuestionLoader();
    }
}

