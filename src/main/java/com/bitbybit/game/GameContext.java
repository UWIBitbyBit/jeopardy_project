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

public class GameContext extends ObservableGame {
    private final Scanner scanner;
    private GameState state;
    private Question[] questions;
    private final List<Player> players = new ArrayList<>(); // Directly manage players here
    private final List<GameEvent> gameEvents = new ArrayList<>(); // Directly manage game events here
    private final ReportGenerator reportGenerator;

    public GameContext(GameState initialState) {
        this(initialState, new Scanner(System.in));
    }

    public GameContext(GameState initialState, Scanner scanner) {
        this.state = initialState;
        this.scanner = scanner;
        this.reportGenerator = new ReportGenerator();
        this.reportGenerator.setStrategy(new TextReportStrategy()); // Set a default strategy
    }

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

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Return a copy to prevent external modification
    }

    public List<GameEvent> getGameEvents() {
        return new ArrayList<>(gameEvents); // Return a copy to prevent external modification
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public void run() {
        state.executeState(this);
    }
}
