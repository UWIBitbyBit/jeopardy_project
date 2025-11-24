package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.model.Question;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        String filepath = scanner.nextLine().trim();

        try {
            // Use the factory provided at construction time
            QuestionLoader loader = factory.createQuestionLoader(filepath);
            Question[] questions = loader.loadQuestions(filepath);

            if (questions.length == 0) {
                System.out.println("❌ No questions found. Try another file.");
                return;
            }

            System.out.println("✓ Loaded " + questions.length + " questions!");

            // store into context
            context.setQuestions(questions);

            // move to next state (pass factory to next state if needed)
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