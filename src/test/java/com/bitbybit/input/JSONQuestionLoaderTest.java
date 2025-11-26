package com.bitbybit.input;

import com.bitbybit.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JSONQuestionLoaderTest {

    private JSONQuestionLoader loader;

    @BeforeEach
    void setUp() {
        loader = new JSONQuestionLoader();
    }

    @Test
    void testLoadQuestionsFromValidJSON(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("questions.json");
        String jsonContent = """
                [
                  {
                    "Category": "Science",
                    "Value": 100,
                    "Question": "What is H2O?",
                    "Options": {"A": "Hydrogen", "B": "Water", "C": "Oxygen", "D": "Salt"},
                    "CorrectAnswer": "Water"
                  },
                  {
                    "Category": "Science",
                    "Value": 200,
                    "Question": "What planet is closest to the sun?",
                    "Options": {"A": "Mercury", "B": "Venus", "C": "Earth", "D": "Mars"},
                    "CorrectAnswer": "Mercury"
                  }
                ]
                """;

        Files.write(jsonFile, jsonContent.getBytes());

        Question[] questions = loader.loadQuestions(jsonFile.toString());

        assertNotNull(questions);
        assertEquals(2, questions.length);
        assertEquals("Science", questions[0].getCategory());
        assertEquals(100, questions[0].getValue());
        assertEquals("What is H2O?", questions[0].getQuestion());
        assertEquals("Water", questions[0].getCorrectAnswer());
        assertEquals("Mercury", questions[1].getCorrectAnswer());
    }

    @Test
    void testLoadQuestionsFromNonexistentFile() {
        Question[] questions = loader.loadQuestions("nonexistent_file.json");
        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsWithDifferentCategories(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("questions_categories.json");
        String jsonContent = """
                [
                  {
                    "Category": "History",
                    "Value": 100,
                    "Question": "When did WWII end?",
                    "Options": {"A": "1945", "B": "1946", "C": "1944", "D": "1943"},
                    "CorrectAnswer": "1945"
                  },
                  {
                    "Category": "Geography",
                    "Value": 150,
                    "Question": "What is the capital of France?",
                    "Options": {"A": "Paris", "B": "Lyon", "C": "Marseille", "D": "Nice"},
                    "CorrectAnswer": "Paris"
                  }
                ]
                """;

        Files.write(jsonFile, jsonContent.getBytes());

        Question[] questions = loader.loadQuestions(jsonFile.toString());

        assertNotNull(questions);
        assertEquals(2, questions.length);
        assertEquals("History", questions[0].getCategory());
        assertEquals("Geography", questions[1].getCategory());
    }

    @Test
    void testLoadQuestionsHandlesEmptyArray(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("questions_empty.json");
        String jsonContent = "[]";

        Files.write(jsonFile, jsonContent.getBytes());

        Question[] questions = loader.loadQuestions(jsonFile.toString());

        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsHandlesInvalidJSON(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("questions_invalid.json");
        String jsonContent = "{ invalid json }";

        Files.write(jsonFile, jsonContent.getBytes());

        Question[] questions = loader.loadQuestions(jsonFile.toString());

        // Should return empty array for invalid JSON instead of throwing exception
        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsWithAllFields(@TempDir Path tempDir) throws IOException {
        Path jsonFile = tempDir.resolve("questions_all_fields.json");
        String jsonContent = """
                [
                  {
                    "Category": "Math",
                    "Value": 500,
                    "Question": "What is 2+2?",
                    "Options": {"A": "3", "B": "4", "C": "5", "D": "6"},
                    "CorrectAnswer": "4"
                  }
                ]
                """;

        Files.write(jsonFile, jsonContent.getBytes());

        Question[] questions = loader.loadQuestions(jsonFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("Math", questions[0].getCategory());
        assertEquals(500, questions[0].getValue());
        assertEquals("4", questions[0].getOptionB());
        assertEquals("4", questions[0].getCorrectAnswer());
        assertFalse(questions[0].isAnswered());
    }
}
