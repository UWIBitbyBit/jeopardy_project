package com.bitbybit.input;

import com.bitbybit.model.Question;

public interface QuestionLoader {
    Question[] loadQuestions(String filepath);
}