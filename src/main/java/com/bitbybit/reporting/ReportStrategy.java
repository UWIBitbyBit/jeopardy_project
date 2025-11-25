package com.bitbybit.reporting;

import com.bitbybit.model.Player;
import com.bitbybit.logging.GameEvent;

import java.util.List;

public interface ReportStrategy {
    void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath);
}
