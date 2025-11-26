package com.bitbybit.game;

import com.bitbybit.model.Player;

import com.bitbybit.reporting.ReportGenerator;
import com.bitbybit.reporting.ReportStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the finished state of the Jeopardy game.
 * In this state, the game displays final scores, announces the winner,
 * and offers the option to generate a game report.
 */
public class FinishedState implements GameState {

    /**
     * Displays a message indicating that the game has finished.
     */
    @Override
    public void displayState() {
        System.out.println("\n==================================== GAME FINISHED =======================================");
    }

    /**
     * Executes the actions for the finished state, including displaying final scores,
     * announcing the winner, and generating a game report if a {@link ReportStrategy}
     * has been set in the {@link ReportGenerator} within the {@link GameContext}.
     * The report format selection is handled externally.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Thank you for playing!");

        displayFinalScores(ctx.getPlayers()); // Call to display final scores and winner

        ReportGenerator generator = ctx.getReportGenerator();
        ReportStrategy currentStrategy = generator.getStrategy();

        if (currentStrategy != null) {
            String outputPath = "game_report_" + System.currentTimeMillis(); // Unique filename
            generator.generateReport(ctx.getGameEvents(), ctx.getPlayers(), outputPath);
            System.out.println("Report generated successfully at: " + outputPath + "." + currentStrategy.getClass().getSimpleName().replace("ReportStrategy", "").toLowerCase());
        }
    }

    /**
     * Handles state transitions. In the finished state, there are no further transitions.
     *
     * @param ctx The {@link GameContext} providing access to game data and utilities.
     */
    @Override
    public void changeState(GameContext ctx) {
        // no further state transition
    }

    /**
     * Displays the final scores of all players and announces the winner.
     * Players are sorted by score in descending order.
     *
     * @param players A list of {@link Player} objects participating in the game.
     */
    private void displayFinalScores(List<Player> players) {
        System.out.println("\n=================================== FINAL SCORES ===========================================");

        // Sort players by score (highest first)
        List<Player> sortedPlayers = new ArrayList<>(players);
        Collections.sort(sortedPlayers, (p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            System.out.println((i + 1) + ". " + player.getName() + ": " + player.getScore());
        }

        // Announce winner
        if (!sortedPlayers.isEmpty()) {
            System.out.println("\nWinner: " + sortedPlayers.get(0).getName() + "!");
        }
    }
}
