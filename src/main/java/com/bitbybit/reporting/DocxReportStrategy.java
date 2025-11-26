package com.bitbybit.reporting;

import com.bitbybit.logging.GameEvent;
import com.bitbybit.logging.QuestionAnsweredEvent;
import com.bitbybit.model.Player;
import com.bitbybit.model.Question;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Implements the {@link ReportStrategy} interface to generate game reports in DOCX format.
 * This strategy uses Apache POI to create a Word document summarizing game events and player scores.
 */
public class DocxReportStrategy implements ReportStrategy {

    /**
     * Generates a DOCX report summarizing the game events and final player scores.
     * The report includes a title, a table of final scores, and a turn-by-turn rundown
     * of all answered questions.
     *
     * @param gameEvents A list of {@link GameEvent}s that occurred during the game.
     * @param players A list of {@link Player}s who participated in the game.
     * @param outputPath The base path for the output file (e.g., "game_report").
     *                   The ".docx" extension will be appended automatically.
     */
    @Override
    public void generateReport(List<GameEvent> gameEvents, List<Player> players, String outputPath) {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(outputPath + ".docx")) {

            // Title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Jeopardy Game Summary Report");
            titleRun.setFontSize(20);
            titleRun.setBold(true);

            XWPFParagraph subtitle = document.createParagraph();
            subtitle.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subtitleRun = subtitle.createRun();
            subtitleRun.setText("============================");
            subtitleRun.setFontSize(16);
            document.createParagraph().createRun().setText("\n"); // New line

            // Final Scores
            XWPFParagraph scoresTitle = document.createParagraph();
            scoresTitle.createRun().setText("Final Scores:");
            scoresTitle.createRun().setFontSize(14);
            scoresTitle.createRun().setBold(true);

            XWPFTable scoreTable = document.createTable(1, 2);
            scoreTable.setWidth("50%");
            scoreTable.getRow(0).getCell(0).setText("Player Name");
            scoreTable.getRow(0).getCell(1).setText("Score");
            scoreTable.getRow(0).getCell(0).getParagraphs().get(0).getRuns().get(0).setBold(true);
            scoreTable.getRow(0).getCell(1).getParagraphs().get(0).getRuns().get(0).setBold(true);


            players.stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                    .forEach(player -> {
                        XWPFTableRow row = scoreTable.createRow();
                        row.getCell(0).setText(player.getName());
                        row.getCell(1).setText(String.valueOf(player.getScore()));
                    });
            document.createParagraph().createRun().setText("\n"); // New line

            // Turn-by-Turn Rundown
            XWPFParagraph rundownTitle = document.createParagraph();
            rundownTitle.createRun().setText("Turn-by-Turn Rundown:");
            rundownTitle.createRun().setFontSize(14);
            rundownTitle.createRun().setBold(true);
            document.createParagraph().createRun().setText("---------------------");
            document.createParagraph().createRun().setText("\n"); // New line

            int turn = 1;
            for (GameEvent event : gameEvents) {
                if (event instanceof QuestionAnsweredEvent) {
                    QuestionAnsweredEvent qaEvent = (QuestionAnsweredEvent) event;
                    // Find player from the list using the helper method
                    Optional<Player> playerOptional = players.stream()
                            .filter(p -> String.valueOf(p.getId()).equals(qaEvent.getPlayerId()))
                            .findFirst();
                    Player player = playerOptional.orElse(null);
                    Question question = qaEvent.getQuestion();

                    XWPFParagraph turnPara = document.createParagraph();
                    turnPara.createRun().setText("Turn " + (turn++));
                    turnPara.createRun().setBold(true);

                    document.createParagraph().createRun().setText("  Player: " + (player != null ? player.getName() : "Unknown Player"));
                    document.createParagraph().createRun().setText("  Category: " + question.getCategory());
                    document.createParagraph().createRun().setText("  Question Value: " + question.getValue());
                    document.createParagraph().createRun().setText("  Question Text: " + question.getQuestion());
                    document.createParagraph().createRun().setText("  Given Answer: " + qaEvent.getAnswerGiven());
                    document.createParagraph().createRun().setText("  Correctness: " + (qaEvent.isCorrect() ? "Correct" : "Incorrect"));
                    document.createParagraph().createRun().setText("  Points Earned: " + qaEvent.getPointsEarned());
                    document.createParagraph().createRun().setText("  Running Total for " + (player != null ? player.getName() : "Unknown Player") + ": " + qaEvent.getRunningScore());
                    document.createParagraph().createRun().setText("\n"); // New line
                }
            }

            document.write(out);

        } catch (IOException e) {
            System.err.println("Error generating DOCX report: " + e.getMessage());
        }
    }
}
