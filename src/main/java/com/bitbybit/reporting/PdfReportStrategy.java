package com.bitbybit.reporting;

import com.bitbybit.logging.GameEvent;
import com.bitbybit.logging.QuestionAnsweredEvent;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.IOException;
import java.util.List;
import java.util.Optional; // Added for Optional

public class PdfReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath) {
        try (PdfWriter writer = new PdfWriter(outputPath + ".pdf");
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Jeopardy Game Summary Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20));
            document.add(new Paragraph("============================")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Final Scores:")
                    .setFontSize(14));
            Table scoreTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
            scoreTable.setWidth(UnitValue.createPercentValue(50));
            scoreTable.addHeaderCell(new Paragraph("Player Name").setBold());
            scoreTable.addHeaderCell(new Paragraph("Score").setBold());

            players.stream() // Changed from players.values().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                    .forEach(player -> {
                        scoreTable.addCell(player.getName());
                        scoreTable.addCell(String.valueOf(player.getScore()));
                    });
            document.add(scoreTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Turn-by-Turn Rundown:")
                    .setFontSize(14));
            document.add(new Paragraph("---------------------"));
            document.add(new Paragraph("\n"));

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

                    document.add(new Paragraph("Turn " + (turn++))
                            .setBold());
                    document.add(new Paragraph("  Player: " + (player != null ? player.getName() : "Unknown Player")));
                    document.add(new Paragraph("  Category: " + question.getCategory()));
                    document.add(new Paragraph("  Question Value: " + question.getValue()));
                    document.add(new Paragraph("  Question Text: " + question.getQuestion()));
                    document.add(new Paragraph("  Given Answer: " + qaEvent.getAnswerGiven()));
                    document.add(new Paragraph("  Correctness: " + (qaEvent.isCorrect() ? "Correct" : "Incorrect")));
                    document.add(new Paragraph("  Points Earned: " + qaEvent.getPointsEarned()));
                    document.add(new Paragraph("  Running Total for " + (player != null ? player.getName() : "Unknown Player") + ": " + qaEvent.getRunningScore()));
                    document.add(new Paragraph("\n"));
                }
            }

        } catch (IOException e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
        }
    }
}
