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

/**
 * Implements the {@link QuestionLoader} interface to load Jeopardy questions from a CSV file.
 * This loader handles CSV files with a specific format: Category, Value, Question, OptionA,
 * OptionB, OptionC, OptionD, CorrectAnswer. It also supports quoted values within the CSV.
 */
public class CSVQuestionLoader implements QuestionLoader {

    /**
     * Loads questions from the specified CSV file.
     * The method reads each line, parses it according to CSV rules (including quoted fields),
     * and constructs {@link Question} objects. It attempts to detect and skip a header row.
     * Categories can be numeric or string-based; string categories are mapped to sequential numbers.
     *
     * @param filepath The path to the CSV file containing the questions.
     * @return An array of {@link Question} objects loaded from the file. Returns an empty array
     *         if the file does not exist, is empty, or an {@link IOException} occurs during reading.
     */
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

    /**
     * Splits a CSV line into individual fields, correctly handling commas within
     * double-quoted strings and escaped double-quotes.
     *
     * @param line The CSV line to split.
     * @return An array of strings, where each string is a field from the CSV line.
     */
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

    /**
     * Removes leading/trailing whitespace and outer double quotes from a string.
     * Also replaces double double-quotes with single double-quotes within the string.
     *
     * @param s The string to unquote.
     * @return The unquoted and trimmed string, or null if the input was null.
     */
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
