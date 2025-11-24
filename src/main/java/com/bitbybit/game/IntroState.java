package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.model.Question;

public class IntroState implements GameState {

    private final QuestionLoaderFactory factory;

    // Constructor injection - factory is provided at runtime
    public IntroState(QuestionLoaderFactory factory) {
        this.factory = factory;
    }

    @Override
    public void displayState() {
        System.out.println("======================================");
        System.out.println("         WELCOME TO JEOPARDY!         ");
        System.out.println("======================================");
        System.out.println("Enter the question filename (CSV, JSON, XML):");
    }

    @Override
    public void executeState(GameContext context) {

        var scanner = context.getScanner();

        // Read the filename
        String filepath = scanner.nextLine().trim();

        try {
            // Create appropriate loader from factory
            QuestionLoader loader = factory.createQuestionLoader(filepath);

            // Load questions
            Question[] questions = loader.loadQuestions(filepath);

            if (questions == null || questions.length == 0) {
                System.out.println("❌ No questions found. Try another file.");
                return;
            }

            System.out.println("✓ Loaded " + questions.length + " questions!");

            // Store questions into context
            context.setQuestions(questions);

            // Move to PlayingState
            changeState(context);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
            System.out.println("Try again.");
        }
    }

    @Override
    public void changeState(GameContext context) {
        context.setState(new PlayingState());
    }
}
