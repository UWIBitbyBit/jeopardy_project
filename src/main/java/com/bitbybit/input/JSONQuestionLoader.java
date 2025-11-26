package com.bitbybit.input;

import com.bitbybit.model.Question;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class JSONQuestionLoader implements QuestionLoader {

    @Override
    public Question[] loadQuestions(String filepath) {
        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            return new Question[0];
        }

        List<Question> questions = new ArrayList<>();

        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            JsonQuestion[] entries = gson.fromJson(json, JsonQuestion[].class);
            if (entries == null) {
                return new Question[0];
            }

            Map<String, Integer> categoryToNumber = new HashMap<>();
            int nextCategoryNumber = 1;
            Map<Integer, Integer> questionCounts = new HashMap<>();

            for (JsonQuestion entry : entries) {
                if (entry == null)
                    continue;

                String rawCategory = entry.Category;
                int value = entry.Value;
                String questionText = entry.Question;
                String optA = entry.Options != null ? entry.Options.A : "";
                String optB = entry.Options != null ? entry.Options.B : "";
                String optC = entry.Options != null ? entry.Options.C : "";
                String optD = entry.Options != null ? entry.Options.D : "";
                String correctAnswer = entry.CorrectAnswer;

                // Same category-number & question-number logic as CSV
                int catNum;
                try {
                    catNum = Integer.parseInt(rawCategory);
                } catch (NumberFormatException nfe) {
                    if (categoryToNumber.containsKey(rawCategory)) {
                        catNum = categoryToNumber.get(rawCategory);
                    } else {
                        catNum = nextCategoryNumber;
                        categoryToNumber.put(rawCategory, catNum);
                        nextCategoryNumber++;
                    }
                }

                int questionNumber = questionCounts.getOrDefault(catNum, 0) + 1;
                questionCounts.put(catNum, questionNumber);

                String id = String.valueOf(catNum) + questionNumber;

                Question q = new Question(id, rawCategory, value,
                        questionText, optA, optB, optC, optD, correctAnswer);
                questions.add(q);
            }

        } catch (IOException e) {
            // fall through & return whatever we've collected
        } catch (Exception e) {
            // Handle JSON parsing errors and other exceptions
            // Return what we've collected so far
        }

        return questions.toArray(new Question[0]);
    }

    // Helper DTOs that match your JSON structure
    private static class JsonQuestion {
        String Category;
        int Value;
        String Question;
        JsonOptions Options;
        String CorrectAnswer;
    }

    private static class JsonOptions {
        String A;
        String B;
        String C;
        String D;
    }
}
