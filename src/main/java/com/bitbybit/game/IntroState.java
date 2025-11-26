package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.model.Question;

/**
 * The IntroState class represents the initial state of the Jeopardy game.
 * In this state, the game welcomes the player and prompts for a question file.
 * It uses a {@link QuestionLoaderFactory} to load questions based on the file type.
 */
public class IntroState implements GameState {

    private final QuestionLoaderFactory factory;

    /**
     * Constructs an IntroState with a specified {@link QuestionLoaderFactory}.
     *
     * @param factory The factory responsible for creating {@link QuestionLoader} instances.
     */
    public IntroState(QuestionLoaderFactory factory) {
        this.factory = factory;
    }

    /**
     * Displays the welcome message and prompts the user to enter a question filename.
     */
    @Override
    public void displayState() {
        System.out.println("===================================================================================================================");
        System.out.println("                                               WELCOME TO JEOPARDY!                                              ");
        System.out.println("===================================================================================================================");
        System.out.println("Please Enter the question filename (CSV, JSON, XML):");
    }

    /**
     * Executes the logic for the intro state. It reads the filename from user input,
     * attempts to load questions using the provided factory, and transitions to the
     * {@link PlayingState} if questions are loaded successfully.
     *
     * @param context The {@link GameContext} providing access to game data and utilities.
     */
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
                System.out.println("No questions found. Try another file.");
                return;
            }

            System.out.println("\n--->Loaded " + questions.length + " questions!<---");

            // Store questions into context
            context.setQuestions(questions);

            // Notify observer: file loaded
            context.notifyObservers(new com.bitbybit.logging.LoadFileEvent());

            // Move to PlayingState
            changeState(context);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Try again.");
        }
    }

    /**
     * Changes the game state to {@link PlayingState}.
     *
     * @param context The {@link GameContext} providing access to game data and utilities.
     */
    @Override
    public void changeState(GameContext context) {
        context.setState(new PlayingState());
    }
}
