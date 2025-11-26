package com.bitbybit.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
import com.bitbybit.logging.GameEvent;
import com.bitbybit.logging.ObservableGame;
import com.bitbybit.logging.PlayerJoinedEvent;
import com.bitbybit.reporting.ReportGenerator;
import com.bitbybit.reporting.TextReportStrategy; // Default for now

/**
 * The GameContext class manages the overall state and flow of the Jeopardy game.
 * It holds references to the current game state, questions, players,
 * a scanner for user input, and a report generator.
 * It also extends {@link ObservableGame} to manage game events and notify observers.
 */
public class GameContext extends ObservableGame {
    private final Scanner scanner;
    private GameState state;
    private Question[] questions;
    private final List<Player> players = new ArrayList<>();
    private final List<GameEvent> gameEvents = new ArrayList<>();
    private final ReportGenerator reportGenerator;

    /**
     * Constructs a new GameContext with a given initial state and a default scanner
     * for system input.
     *
     * @param initialState The initial {@link GameState} of the game.
     */
    public GameContext(GameState initialState) {
        this(initialState, new Scanner(System.in));
    }

    /**
     * Constructs a new GameContext with a given initial state and a specified scanner.
     * Initializes the report generator with a default text report strategy.
     *
     * @param initialState The initial {@link GameState} of the game.
     * @param scanner The {@link Scanner} to be used for user input.
     */
    public GameContext(GameState initialState, Scanner scanner) {
        this.state = initialState;
        this.scanner = scanner;
        this.reportGenerator = new ReportGenerator();
        this.reportGenerator.setStrategy(new TextReportStrategy()); // Set a default strategy
    }

    /**
     * Notifies all registered observers about a game event and collects the event.
     * If the event is a {@link PlayerJoinedEvent}, the player is added to the list of players.
     *
     * @param event The {@link GameEvent} to be processed.
     */
    @Override
    public void notifyObservers(GameEvent event) {
        super.notifyObservers(event); // Notify other observers if any
        gameEvents.add(event); // Collect all events
        if (event instanceof PlayerJoinedEvent) {
            PlayerJoinedEvent pje = (PlayerJoinedEvent) event;
            // Ensure we only add players once, or update if needed.
            // For simplicity, assuming players are added once at the start.
            if (!players.contains(pje.getPlayer())) {
                players.add(pje.getPlayer());
            }
        }
    }

    /**
     * Returns the current {@link GameState}.
     *
     * @return The current game state.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets the current {@link GameState}.
     *
     * @param state The new game state to set.
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Returns the array of {@link Question}s loaded for the game.
     *
     * @return An array of questions.
     */
    public Question[] getQuestions() {
        return questions;
    }

    /**
     * Sets the array of {@link Question}s for the game.
     *
     * @param questions The array of questions to set.
     */
    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    /**
     * Returns the {@link Scanner} used for user input.
     *
     * @return The scanner instance.
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Returns a copy of the list of {@link Player}s currently in the game.
     *
     * @return A new {@link ArrayList} containing the players.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Return a copy to prevent external modification
    }

    /**
     * Returns a copy of the list of {@link GameEvent}s that have occurred during the game.
     *
     * @return A new {@link ArrayList} containing the game events.
     */
    public List<GameEvent> getGameEvents() {
        return new ArrayList<>(gameEvents); // Return a copy to prevent external modification
    }

    /**
     * Returns the {@link ReportGenerator} instance used for generating game reports.
     *
     * @return The report generator.
     */
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    /**
     * Runs the current state's execution logic.
     */
    public void run() {
        state.executeState(this);
    }
}
