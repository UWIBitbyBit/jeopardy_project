package com.bitbybit.input;

import com.bitbybit.model.Question;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Implements the {@link QuestionLoader} interface to load Jeopardy questions from a JSON file.
 * This loader uses the Gson library to parse JSON files into a structured format,
 * and then converts them into {@link Question} objects.
 */
public class JSONQuestionLoader implements QuestionLoader {

    /**
     * Loads questions from the specified JSON file.
     * The method reads the JSON content, parses it using Gson, and constructs
     * {@link Question} objects. It handles category mapping similar to the CSV loader,
     * assigning sequential numbers to string-based categories if necessary.
     *
     * @param filepath The path to the JSON file containing the questions.
     * @return An array of {@link Question} objects loaded from the file. Returns an empty array
     *         if the file does not exist, is empty, or an error occurs during reading or parsing.
     */
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

    /**
     * Helper DTO (Data Transfer Object) class to represent the structure of a question
     * object within the JSON file.
     */
    private static class JsonQuestion {
        String Category;
        int Value;
        String Question;
        JsonOptions Options;
        String CorrectAnswer;
    }

    /**
     * Helper DTO class to represent the structure of the options for a question
     * within the JSON file.
     */
    private static class JsonOptions {
        String A;
        String B;
        String C;
        String D;
    }
}
