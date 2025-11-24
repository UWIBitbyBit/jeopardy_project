package com.bitbybit.input;

import com.bitbybit.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVQuestionLoader implements QuestionLoader {

    @Override
    public Question[] loadQuestions(String filepath) {
        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            // If the file doesn't exist in working dir, return empty array
            return new Question[0];
        }

        List<Question> questions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            boolean first = true;

            Map<String, Integer> categoryToNumber = new HashMap<>();
            int nextCategoryNumber = 1;
            Map<Integer, Integer> questionCounts = new HashMap<>();

            for (String line : lines) {
                if (line == null)
                    continue;
                String trimmed = line.trim();
                if (trimmed.isEmpty())
                    continue;

                // If there's a header row, try to detect and skip it
                if (first) {
                    first = false;
                    String lower = trimmed.toLowerCase();
                    if (lower.startsWith("id") || lower.contains("category") || lower.contains("value")
                            || lower.contains("question")) {
                        // assume header, skip this line
                        continue;
                    }
                }

                // Split CSV respecting quoted values
                String[] parts = splitCsvLine(trimmed);

                // Only support new CSV shape: Category, Value, Question, OptionA, OptionB,
                // OptionC, OptionD, CorrectAnswer
                if (parts.length < 8) {
                    // ignore malformed lines
                    continue;
                }

                String rawCategory = unquote(parts[0]);
                String valueStr = unquote(parts[1]);
                String questionText = unquote(parts[2]);
                String optA = unquote(parts[3]);
                String optB = unquote(parts[4]);
                String optC = unquote(parts[5]);
                String optD = unquote(parts[6]);
                String correctAnswer = unquote(parts[7]);

                // Determine category number: if numeric use it; otherwise assign sequential
                // number by appearance
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

                String id = String.valueOf(catNum) + String.valueOf(questionNumber);

                int value = 0;
                try {
                    value = Integer.parseInt(valueStr);
                } catch (NumberFormatException ignored) {
                }

                Question q = new Question(id, rawCategory, value, questionText, optA, optB, optC, optD,
                        correctAnswer);
                questions.add(q);
            }
        } catch (IOException e) {
            // On error, return what we've parsed so far (or empty)
            return questions.toArray(new Question[0]);
        }

        return questions.toArray(new Question[0]);
    }

    // Splits a CSV line into fields, handling quoted commas
    private String[] splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                // toggle inQuotes (handle double-quote escape)
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // escaped quote
                    cur.append('"');
                    i++; // skip next
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        fields.add(cur.toString());
        return fields.toArray(new String[0]);
    }

    private String unquote(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) {
            t = t.substring(1, t.length() - 1);
            // replace double double-quotes with single double-quote
            t = t.replace("\"\"", "\"");
        }
        return t;
    }
}