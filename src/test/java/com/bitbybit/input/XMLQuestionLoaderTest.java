package com.bitbybit.input;

import com.bitbybit.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class XMLQuestionLoaderTest {

    private XMLQuestionLoader loader;

    @BeforeEach
    void setUp() {
        loader = new XMLQuestionLoader();
    }

    @Test
    void testLoadQuestionsFromValidXML(@TempDir Path tempDir) throws IOException {
        Path xmlFile = tempDir.resolve("questions.xml");
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Questions>
                  <QuestionItem>
                    <Category>Science</Category>
                    <Value>100</Value>
                    <QuestionText>What is H2O?</QuestionText>
                    <Options>
                      <OptionA>Hydrogen</OptionA>
                      <OptionB>Water</OptionB>
                      <OptionC>Oxygen</OptionC>
                      <OptionD>Salt</OptionD>
                    </Options>
                    <CorrectAnswer>Water</CorrectAnswer>
                  </QuestionItem>
                  <QuestionItem>
                    <Category>Science</Category>
                    <Value>200</Value>
                    <QuestionText>What planet is closest to the sun?</QuestionText>
                    <Options>
                      <OptionA>Mercury</OptionA>
                      <OptionB>Venus</OptionB>
                      <OptionC>Earth</OptionC>
                      <OptionD>Mars</OptionD>
                    </Options>
                    <CorrectAnswer>Mercury</CorrectAnswer>
                  </QuestionItem>
                </Questions>
                """;

        Files.write(xmlFile, xmlContent.getBytes());

        Question[] questions = loader.loadQuestions(xmlFile.toString());

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
        Question[] questions = loader.loadQuestions("nonexistent_file.xml");
        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsWithDifferentCategories(@TempDir Path tempDir) throws IOException {
        Path xmlFile = tempDir.resolve("questions_categories.xml");
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Questions>
                  <QuestionItem>
                    <Category>History</Category>
                    <Value>100</Value>
                    <QuestionText>When did WWII end?</QuestionText>
                    <Options>
                      <OptionA>1945</OptionA>
                      <OptionB>1946</OptionB>
                      <OptionC>1944</OptionC>
                      <OptionD>1943</OptionD>
                    </Options>
                    <CorrectAnswer>1945</CorrectAnswer>
                  </QuestionItem>
                  <QuestionItem>
                    <Category>Geography</Category>
                    <Value>150</Value>
                    <QuestionText>What is the capital of France?</QuestionText>
                    <Options>
                      <OptionA>Paris</OptionA>
                      <OptionB>Lyon</OptionB>
                      <OptionC>Marseille</OptionC>
                      <OptionD>Nice</OptionD>
                    </Options>
                    <CorrectAnswer>Paris</CorrectAnswer>
                  </QuestionItem>
                </Questions>
                """;

        Files.write(xmlFile, xmlContent.getBytes());

        Question[] questions = loader.loadQuestions(xmlFile.toString());

        assertNotNull(questions);
        assertEquals(2, questions.length);
        assertEquals("History", questions[0].getCategory());
        assertEquals("Geography", questions[1].getCategory());
    }

    @Test
    void testLoadQuestionsHandlesEmptyDocument(@TempDir Path tempDir) throws IOException {
        Path xmlFile = tempDir.resolve("questions_empty.xml");
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Questions>
                </Questions>
                """;

        Files.write(xmlFile, xmlContent.getBytes());

        Question[] questions = loader.loadQuestions(xmlFile.toString());

        assertNotNull(questions);
        assertEquals(0, questions.length);
    }

    @Test
    void testLoadQuestionsHandlesMissingOptionalFields(@TempDir Path tempDir) throws IOException {
        Path xmlFile = tempDir.resolve("questions_missing_fields.xml");
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Questions>
                  <QuestionItem>
                    <Category>Science</Category>
                    <Value>100</Value>
                    <QuestionText>Basic question</QuestionText>
                    <Options>
                      <OptionA>A</OptionA>
                    </Options>
                    <CorrectAnswer>A</CorrectAnswer>
                  </QuestionItem>
                </Questions>
                """;

        Files.write(xmlFile, xmlContent.getBytes());

        Question[] questions = loader.loadQuestions(xmlFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals("Science", questions[0].getCategory());
    }

    @Test
    void testLoadQuestionsWithInvalidValue(@TempDir Path tempDir) throws IOException {
        Path xmlFile = tempDir.resolve("questions_invalid_value.xml");
        String xmlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Questions>
                  <QuestionItem>
                    <Category>Science</Category>
                    <Value>NotANumber</Value>
                    <QuestionText>What is H2O?</QuestionText>
                    <Options>
                      <OptionA>A</OptionA>
                      <OptionB>B</OptionB>
                      <OptionC>C</OptionC>
                      <OptionD>D</OptionD>
                    </Options>
                    <CorrectAnswer>A</CorrectAnswer>
                  </QuestionItem>
                </Questions>
                """;

        Files.write(xmlFile, xmlContent.getBytes());

        Question[] questions = loader.loadQuestions(xmlFile.toString());

        assertNotNull(questions);
        assertEquals(1, questions.length);
        assertEquals(0, questions[0].getValue()); // Should default to 0
    }
}
