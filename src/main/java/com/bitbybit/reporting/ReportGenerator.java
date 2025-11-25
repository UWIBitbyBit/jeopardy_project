package com.bitbybit.reporting;

import com.bitbybit.model.Player;
import com.bitbybit.logging.GameEvent;

import java.util.List;

public class ReportGenerator {
    private ReportStrategy strategy;

    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath) {
        if (strategy == null) {
            throw new IllegalStateException("Report strategy not set. Please set a strategy before generating a report.");
        }
        strategy.generateReport(gameEvents, players, outputPath);
    }
}
