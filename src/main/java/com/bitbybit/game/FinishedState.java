package com.bitbybit.game;

import com.bitbybit.model.Player;
import com.bitbybit.reporting.ReportGenerator;
import com.bitbybit.reporting.TextReportStrategy;
import com.bitbybit.reporting.PdfReportStrategy;
import com.bitbybit.reporting.DocxReportStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FinishedState implements GameState {

    @Override
    public void displayState() {
        System.out.println("\n==================================== GAME FINISHED =======================================");
    }

    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Thank you for playing!");

        displayFinalScores(ctx.getPlayers()); // Call to display final scores and winner

        // Generate report
        ReportGenerator generator = ctx.getReportGenerator();

        System.out.println("\nDo you want to generate a game report? (yes/no)");
        String response = ctx.getScanner().nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            System.out.println("Choose report format (text, pdf, docx):");
            String format = ctx.getScanner().nextLine().trim().toLowerCase();
            String outputPath = "game_report_" + System.currentTimeMillis(); // Unique filename

            switch (format) {
                case "text":
                    generator.setStrategy(new TextReportStrategy());
                    break;
                case "pdf":
                    generator.setStrategy(new PdfReportStrategy());
                    break;
                case "docx":
                    generator.setStrategy(new DocxReportStrategy());
                    break;
                default:
                    System.out.println("Invalid format. Generating text report by default.");
                    generator.setStrategy(new TextReportStrategy());
                    break;
            }
            generator.generateReport(ctx.getGameEvents(), ctx.getPlayers(), outputPath);
            System.out.println("Report generated successfully at: " + outputPath + "." + format);
        }
       
    }

    @Override
    public void changeState(GameContext ctx) {
        // no further state transition
    }

    private void displayFinalScores(List<Player> players) {
        System.out.println("\n======================================== FINAL SCORES ===========================================");

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
