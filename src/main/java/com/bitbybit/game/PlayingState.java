package com.bitbybit.game;

import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
import com.bitbybit.model.QuestionBoard;
import com.bitbybit.logging.PlayerJoinedEvent;
import com.bitbybit.logging.GameFinishedEvent;
import com.bitbybit.logging.QuestionAnsweredEvent;
import com.bitbybit.logging.SelectPlayerCountEvent;
import com.bitbybit.logging.SelectCategoryEvent;
import com.bitbybit.logging.SelectQuestionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The PlayingState class represents the main gameplay state of the Jeopardy game.
 * In this state, players take turns selecting categories and values, answering questions,
 * and accumulating scores. It manages player turns, displays the game board,
 * and handles transitions to the {@link FinishedState}.
 */
public class PlayingState implements GameState {
    private GameContext context;
    private QuestionBoard board;

    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Scanner scanner;
    private boolean gameActive = true;

    /**
     * Displays the current game state, including a "GAME IN PROGRESS" header
     * and the live scores of all players.
     */
    @Override
    public void displayState() {
        if (!players.isEmpty()) {
            System.out.println("\n=============================================GAME IN PROGRESS======================================================");
            displayScores();
            System.out.println("===================================================================================================================");
        }
    }

    /**
     * Executes the main logic for the playing state. This includes initializing the
     * question board, setting up players (if not already done), managing player turns,
     * prompting for category and value selection, evaluating answers, updating scores,
     * and checking for game termination conditions.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    @Override
    public void executeState(GameContext ctx) {
        this.context = ctx;
        this.scanner = ctx.getScanner();

        // Initialize board from context questions once
        if (board == null) {
            Question[] qs = context.getQuestions();
            if (qs == null || qs.length == 0) {
                System.out.println("No questions loaded into context!");
                gameActive = false;
                changeState(ctx);
                return;
            }
            board = new QuestionBoard(Arrays.asList(qs));
        }

        // First time setup - initialize players
        if (players.isEmpty()) {
            setupPlayers();
            displayState();
        }

        // Check if game should end
        if (!gameActive || board.isBoardEmpty()) {
            System.out.println("\nGame over!");
            changeState(ctx);
            return;
        }

        // Current player's turn
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("\n" + currentPlayer.getName() + "'s turn");

        // Display available categories and values
        displayAvailableQuestions();

        // Player selects category and value
        String category = promptForCategory();
        if (category.equalsIgnoreCase("quit")) {
            gameActive = false;
            changeState(ctx);
            return;
        }
        // Log category selection
        context.notifyObservers(new SelectCategoryEvent(category, currentPlayer.getName()));

        int value = promptForValue(category);
        if (value == -1) {
            System.out.println("Invalid value. Try again.");
            return;
        }
        // Log question selection
        context.notifyObservers(new SelectQuestionEvent(category, value, currentPlayer.getName()));

        // Get the question (board handles "already answered")
        Question question = board.getQuestion(category, value);
        if (question == null) {
            System.out.println("Question not found or already answered. Try again.");
            return;
        }

        System.out.println("\nQuestion: " + question.getQuestion());
        System.out.println("A) " + question.getOptionA());
        System.out.println("B) " + question.getOptionB());
        System.out.println("C) " + question.getOptionC());
        System.out.println("D) " + question.getOptionD());

        System.out.print("Your answer (A/B/C/D): ");
        String playerAnswer = scanner.nextLine().trim();

        // Evaluate answer
        boolean correct = evaluateAnswer(question, playerAnswer);
        int pointsEarned;

        if (correct) {
            pointsEarned = value;
        } else {
            pointsEarned = -value; // Deduct points for incorrect answers
        }

        int runningScore = currentPlayer.getScore() + pointsEarned;
        // Update score and mark question as answered
        currentPlayer.addScore(pointsEarned);
        board.markQuestionAsAnswered(question.getCategory(), question.getValue());
        context.notifyObservers(
                new QuestionAnsweredEvent(currentPlayer, question, correct, playerAnswer, pointsEarned, runningScore));

        // Display result
        if (correct) {
            System.out.println("Correct! You earned " + pointsEarned + " points.");
        } else {
            System.out.println("Incorrect. The correct answer was: " + question.getCorrectAnswer());
        }

        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        // Ask if player wants to continue
        System.out.print("\nContinue playing? (y/n): ");
        String continueResponse = scanner.nextLine().trim().toLowerCase();
        if (!continueResponse.equals("y")) {
            gameActive = false;
            changeState(ctx);
        }
    }

    /**
     * Handles state transitions. If the game is no longer active or the board is empty,
     * it transitions to the {@link FinishedState}.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    @Override
    public void changeState(GameContext ctx) {
        if (!gameActive || (board != null && board.isBoardEmpty())) {
            ctx.setState(new FinishedState());
            context.notifyObservers(new GameFinishedEvent());
        }
    }

    /**
     * Sets up the players for the game by prompting the user for the number of players
     * and their names. Players are added to the game context and observers are notified.
     */
    private void setupPlayers() {
        System.out.println("\nHow many players? (1-4)");
        int numPlayers = 0;

        while (numPlayers < 1 || numPlayers > 4) {
            try {
                String line = scanner.nextLine().trim();
                numPlayers = Integer.parseInt(line);
                if (numPlayers < 1 || numPlayers > 4) {
                    System.out.println("Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        // Log player count selection
        context.notifyObservers(new SelectPlayerCountEvent(numPlayers));

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name for Player " + (i + 1) + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + (i + 1);
            }
            players.add(new Player(name));
            context.notifyObservers(new PlayerJoinedEvent(players.get(i)));
        }
    }

    /**
     * Displays the current scores of all players.
     */
    private void displayScores() {
        System.out.println("--- LIVE SCORES ---");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

    /**
     * Displays the categories and their available question values on the board.
     */
    private void displayAvailableQuestions() {
        System.out.println("\nAvailable Categories:");

        for (String category : board.getCategories()) {
            System.out.print("- " + category + " (Values: ");

            // Pass the original category name to getAvailableValues, which will normalize it internally
            List<Integer> availableValues = board.getAvailableValues(category);
            for (int i = 0; i < availableValues.size(); i++) {
                System.out.print(availableValues.get(i));
                if (i < availableValues.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }
    }

    /**
     * Prompts the current player to select a category.
     *
     * @return The selected category name, or "quit" if the player wants to end the game.
     */
    /**
     * Prompts the current player to select a category.
     * The input is trimmed, converted to lowercase, and all spaces are removed
     * to ensure case-insensitive and space-insensitive matching.
     *
     * @return The normalized selected category name, or "quit" if the player wants to end the game.
     */
    private String promptForCategory() {
        System.out.print("\nSelect a category (or type 'quit' to end): ");
        return scanner.nextLine().trim().toLowerCase().replaceAll("\\s+", "");
    }

    /**
     * Prompts the current player to select a question value within a chosen category.
     *
     * @param category The selected category.
     * @return The selected question value, or -1 if the input is invalid.
     */
    private int promptForValue(String category) {
        System.out.print("Select a value: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Evaluates the player's answer against the correct answer for a given question.
     * The comparison is case-insensitive.
     *
     * @param question The {@link Question} being answered.
     * @param playerAnswer The answer provided by the player.
     * @return {@code true} if the player's answer is correct, {@code false} otherwise.
     */
    private boolean evaluateAnswer(Question question, String playerAnswer) {
        return question.getCorrectAnswer().equalsIgnoreCase(playerAnswer);
    }
}
