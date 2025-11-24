package com.bitbybit.input;

import com.bitbybit.model.Question;

public class FileInputTestRunner {

    public static void main(String[] args) {

        // Paths to your sample files (relative to project root)
        String csvFile = "sample_game_CSV.csv";
        String jsonFile = "sample_game_JSON.json";
        String xmlFile = "sample_game_XML.xml";

        // Use the FACTORY OBJECTS (instance methods), not static calls
        testLoader("CSV TEST",
                new CSVQuestionLoaderFactory().createQuestionLoader(csvFile),
                csvFile);

        testLoader("JSON TEST",
                new JSONQuestionLoaderFactory().createQuestionLoader(jsonFile),
                jsonFile);

        testLoader("XML TEST",
                new XMLQuestionLoaderFactory().createQuestionLoader(xmlFile),
                xmlFile);
    }

    private static void testLoader(String label, QuestionLoader loader, String filepath) {
        System.out.println("================================");
        System.out.println("Running " + label + " on: " + filepath);
        System.out.println("================================");

        // Call the loader with the filepath (your new interface)
        Question[] questions = loader.loadQuestions(filepath);

        if (questions == null || questions.length == 0) {
            System.out.println("❌ No questions loaded.\n");
            return;
        }

        System.out.println("✓ Loaded " + questions.length + " questions.\n");

        // Print first few questions as a sanity check
        for (int i = 0; i < Math.min(5, questions.length); i++) {
            Question q = questions[i];

            // ⚠ If you don't have these getters yet, either add them,
            // or temporarily comment out the ones that don’t exist.
            System.out.println(
                    q.getId() + " | " +
                            q.getCategory() + " | " +
                            q.getValue() + " | " +
                            q.getQuestion() // or whatever your getter is called
            );
        }

        System.out.println();
    }
}
