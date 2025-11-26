package com.bitbybit.reporting;

import com.bitbybit.model.Player;
import com.bitbybit.logging.GameEvent;

import java.util.List;

/**
 * The ReportStrategy interface defines the contract for different strategies
 * of generating game reports. Implementations of this interface are responsible
 * for taking game events and player data and producing a report in a specific format.
 */
public interface ReportStrategy {
    /**
     * Generates a game report based on the provided game events and player information.
     *
     * @param gameEvents A list of {@link GameEvent}s that occurred during the game.
     * @param players A list of {@link Player}s who participated in the game.
     * @param outputPath The base path for the output file, without a file extension.
     *                   The strategy implementation should append the appropriate extension.
     */
    void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath);
}
