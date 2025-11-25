package com.bitbybit.reporting;

import com.bitbybit.logging.*;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class TextReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath + ".txt"))) {
            writer.println("Jeopardy Game Summary Report");
            writer.println("============================");
            writer.println();

            writer.println("Final Scores:");
            players.stream() // Changed from players.values().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                    .forEach(player -> writer.printf("- %s: %d points%n", player.getName(), player.getScore()));
            writer.println();

            writer.println("Turn-by-Turn Rundown:");
            writer.println("---------------------");
            int turn = 1;
            for (GameEvent event : gameEvents) {
                if (event instanceof QuestionAnsweredEvent) {
                    QuestionAnsweredEvent qaEvent = (QuestionAnsweredEvent) event;
                    // Find player from the list using the helper method
                    Optional<Player> playerOptional = players.stream()
                            .filter(p -> String.valueOf(p.getId()).equals(qaEvent.getPlayerId()))
                            .findFirst();
                    Player player = playerOptional.orElse(null); // Get player or null if not found
                    Question question = qaEvent.getQuestion();

                    writer.printf("Turn %d:%n", turn++);
                    writer.printf("  Player: %s%n", player != null ? player.getName() : "Unknown Player");
                    writer.printf("  Category: %s%n", question.getCategory());
                    writer.printf("  Question Value: %d%n", question.getValue());
                    writer.printf("  Question Text: %s%n", question.getQuestion());
                    writer.printf("  Given Answer: %s%n", qaEvent.getAnswerGiven());
                    writer.printf("  Correctness: %s%n", qaEvent.isCorrect() ? "Correct" : "Incorrect");
                    writer.printf("  Points Earned: %d%n", qaEvent.getPointsEarned());
                    writer.printf("  Running Total for %s: %d%n", player != null ? player.getName() : "Unknown Player", qaEvent.getRunningScore());
                    writer.println();
                }
            }

        } catch (IOException e) {
            System.err.println("Error generating text report: " + e.getMessage());
        }
    }
}
