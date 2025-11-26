package com.bitbybit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class QuestionBoardTest {

    private QuestionBoard questionBoard;
    private Question q1, q2, q3;

    @BeforeEach
    void setUp() {
        q1 = new Question("Q1", "Category1", 100, "Q1 Text", "A", "B", "C", "D", "A");
        q2 = new Question("Q2", "Category1", 200, "Q2 Text", "A", "B", "C", "D", "B");
        q3 = new Question("Q3", "Category2", 100, "Q3 Text", "A", "B", "C", "D", "C");
        List<Question> questions = Arrays.asList(q1, q2, q3);
        questionBoard = new QuestionBoard(questions);
    }

    @Test
    void testQuestionBoardCreation() {
        assertNotNull(questionBoard);
        assertEquals(2, questionBoard.getCategories().size());
        assertTrue(questionBoard.getCategories().contains("Category1"));
        assertTrue(questionBoard.getCategories().contains("Category2"));
    }

    @Test
    void testGetQuestion() {
        assertEquals(q1, questionBoard.getQuestion("Category1", 100));
        assertEquals(q3, questionBoard.getQuestion("Category2", 100));
        assertNull(questionBoard.getQuestion("Category1", 300)); // Non-existent value
        assertNull(questionBoard.getQuestion("NonExistentCategory", 100)); // Non-existent category
    }

    @Test
    void testMarkQuestionAsAnswered() {
        assertFalse(q1.isAnswered());
        questionBoard.markQuestionAsAnswered("Category1", 100);
        assertTrue(q1.isAnswered());
        assertFalse(q2.isAnswered()); // Other questions should remain unanswered
    }

    @Test
    void testIsBoardEmpty() {
        assertFalse(questionBoard.isBoardEmpty());
        questionBoard.markQuestionAsAnswered("Category1", 100);
        questionBoard.markQuestionAsAnswered("Category1", 200);
        questionBoard.markQuestionAsAnswered("Category2", 100);
        assertTrue(questionBoard.isBoardEmpty());
    }

    @Test
    void testGetAvailableQuestions() {
        List<Question> category1Questions = questionBoard.getAvailableQuestions("Category1");
        assertEquals(2, category1Questions.size());
        assertTrue(category1Questions.contains(q1));
        assertTrue(category1Questions.contains(q2));

        questionBoard.markQuestionAsAnswered("Category1", 100);
        category1Questions = questionBoard.getAvailableQuestions("Category1");
        assertEquals(1, category1Questions.size());
        assertFalse(category1Questions.contains(q1));
        assertTrue(category1Questions.contains(q2));

        List<Question> nonExistentCategoryQuestions = questionBoard.getAvailableQuestions("NonExistentCategory");
        assertTrue(nonExistentCategoryQuestions.isEmpty());
    }

    @Test
    void testGetCategories() {
        List<String> categories = questionBoard.getCategories();
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Category1"));
        assertTrue(categories.contains("Category2"));
    }
}
