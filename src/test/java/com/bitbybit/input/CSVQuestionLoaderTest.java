package com.bitbybit.input;

import com.bitbybit.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CSVQuestionLoaderTest {

    private CSVQuestionLoader loader;

    @BeforeEach
    void setUp() {
        loader = new CSVQuestionLoader();
    }

    @Test
    void testLoadQuestionsFromValidCSV(@TempDir Path tempDir) throws IOException {
        // Create a test CSV file
        Path csvFile = tempDir.resolve("questions.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "Science,100,What is H2O?,Hydrogen,Water,Oxygen,Salt,Water\n" +
                "Science,200,What planet is closest to the sun?,Mercury,Venus,Earth,Mars,Mercury";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

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
        Question[] questions = loader.loadQuestions("nonexistent_file.csv");
        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsWithQuotedCommas(@TempDir Path tempDir) throws IOException {
        // Test CSV parsing with quoted fields containing commas
        Path csvFile = tempDir.resolve("questions_quoted.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "History,100,\"What year did, World War II end?\",1945,1946,1944,1943,1945";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("What year did, World War II end?", questions[0].getQuestion());
    }

    @Test
    void testLoadQuestionsSkipsHeader(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("questions_with_header.csv");
        String csvContent = "id,category,value,question,optionA,optionB,optionC,optionD,answer\n" +
                "Geography,50,What is the capital of France?,Paris,London,Berlin,Madrid,Paris";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("Geography", questions[0].getCategory());
    }

    @Test
    void testLoadQuestionsHandlesEmptyLines(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("questions_with_empty_lines.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "\n" +
                "Sports,100,What is the national sport of Canada?,Hockey,Baseball,Basketball,Football,Hockey\n" +
                "\n";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("Hockey", questions[0].getCorrectAnswer());
    }

    @Test
    void testLoadQuestionsWithMalformedLines(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("questions_malformed.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "Science,100,Complete question,A,B,C,D,A\n" +
                "Incomplete,200\n" + // Malformed line with too few fields
                "Math,300,2+2=?,3,4,5,6,4";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(2, questions.length); // Only complete questions loaded
    }

    @Test
    void testLoadQuestionsAssignsCategories(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("questions_categories.csv");
        String csvContent = "Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n" +
                "Science,100,Q1?,A,B,C,D,A\n" +
                "Science,200,Q2?,A,B,C,D,A\n" +
                "History,100,Q3?,A,B,C,D,A";

        Files.write(csvFile, csvContent.getBytes());

        Question[] questions = loader.loadQuestions(csvFile.toString());

        assertNotNull(questions);
        assertEquals(3, questions.length);
        assertEquals("Science", questions[0].getCategory());
        assertEquals("Science", questions[1].getCategory());
        assertEquals("History", questions[2].getCategory());
    }
}
