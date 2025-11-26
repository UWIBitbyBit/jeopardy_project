package com.bitbybit.reporting;

import com.bitbybit.model.Player;
import com.bitbybit.logging.GameEvent;

import java.util.List;

/**
 * The ReportGenerator class acts as a context for the Strategy design pattern,
 * allowing different report generation strategies (e.g., PDF, DOCX, Text) to be
 * plugged in at runtime. It delegates the actual report generation to the
 * currently set {@link ReportStrategy}.
 */
public class ReportGenerator {
    private ReportStrategy strategy;

    /**
     * Sets the reporting strategy to be used for generating reports.
     *
     * @param strategy The {@link ReportStrategy} implementation to use.
     */
    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Returns the current reporting strategy.
     *
     * @return The {@link ReportStrategy} implementation currently in use.
     */
    public ReportStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Generates a report using the currently set {@link ReportStrategy}.
     *
     * @param gameEvents A list of {@link GameEvent}s that occurred during the game.
     * @param players A list of {@link Player}s who participated in the game.
     * @param outputPath The base path for the output file.
     * @throws IllegalStateException if no report strategy has been set.
     */
    public void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath) {
        if (strategy == null) {
            throw new IllegalStateException("Report strategy not set. Please set a strategy before generating a report.");
        }
        strategy.generateReport(gameEvents, players, outputPath);
    }
}
