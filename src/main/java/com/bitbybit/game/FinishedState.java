package com.bitbybit.game;

import com.bitbybit.reporting.ReportGenerator;
import com.bitbybit.reporting.TextReportStrategy;
import com.bitbybit.reporting.PdfReportStrategy;
import com.bitbybit.reporting.DocxReportStrategy;



public class FinishedState implements GameState {

    @Override
    public void displayState() {
        System.out.println("\n==== GAME FINISHED ====");
    }

    @Override
    public void executeState(GameContext ctx) {
        System.out.println("Thank you for playing!");

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
}
