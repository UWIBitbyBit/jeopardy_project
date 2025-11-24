package com.bitbybit.input;

import com.bitbybit.model.Question;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public class XMLQuestionLoader implements QuestionLoader {

    @Override
    public Question[] loadQuestions(String filepath) {
        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            return new Question[0];
        }

        List<Question> questions = new ArrayList<>();

        Map<String, Integer> categoryToNumber = new HashMap<>();
        int nextCategoryNumber = 1;
        Map<Integer, Integer> questionCounts = new HashMap<>();

        try (InputStream in = Files.newInputStream(path)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("QuestionItem");
            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                Element elem = (Element) node;

                String rawCategory = getText(elem, "Category");
                String valueStr = getText(elem, "Value");
                String questionText = getText(elem, "QuestionText");

                Element optionsElem = getChildElement(elem, "Options");
                String optA = optionsElem != null ? getText(optionsElem, "OptionA") : "";
                String optB = optionsElem != null ? getText(optionsElem, "OptionB") : "";
                String optC = optionsElem != null ? getText(optionsElem, "OptionC") : "";
                String optD = optionsElem != null ? getText(optionsElem, "OptionD") : "";

                String correctAnswer = getText(elem, "CorrectAnswer");

                int value = 0;
                try {
                    value = Integer.parseInt(valueStr.trim());
                } catch (NumberFormatException ignored) {
                }

                // Same category/ID scheme as CSV
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
            // return what we have so far
        } catch (Exception e) {
            // XML parse errors – also just return collected questions
        }

        return questions.toArray(new Question[0]);
    }

    private String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0)
            return "";
        return list.item(0).getTextContent().trim();
    }

    private Element getChildElement(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0)
            return null;
        Node n = list.item(0);
        return (n.getNodeType() == Node.ELEMENT_NODE) ? (Element) n : null;
    }
}
