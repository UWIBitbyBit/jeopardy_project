package com.bitbybit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    void testQuestionCreation() {
        Question question = new Question("Q1", "Category1", 100, "Question Text", "A", "B", "C", "D", "A");
        assertNotNull(question);
        assertEquals("Q1", question.getId());
        assertEquals("Category1", question.getCategory());
        assertEquals(100, question.getValue());
        assertEquals("Question Text", question.getQuestion());
        assertEquals("A", question.getOptionA());
        assertEquals("B", question.getOptionB());
        assertEquals("C", question.getOptionC());
        assertEquals("D", question.getOptionD());
        assertEquals("A", question.getCorrectAnswer());
        assertFalse(question.isAnswered());
    }

    @Test
    void testMarkAnswered() {
        Question question = new Question("Q1", "Category1", 100, "Question Text", "A", "B", "C", "D", "A");
        question.markAnswered(true);
        assertTrue(question.isAnswered());
        question.markAnswered(false);
        assertFalse(question.isAnswered());
    }
}
