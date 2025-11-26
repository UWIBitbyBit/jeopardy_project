package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.logging.CSVLoggingObserver;
import com.bitbybit.reporting.ReportStrategy;
import com.bitbybit.reporting.TextReportStrategy;
import com.bitbybit.reporting.PdfReportStrategy;
import com.bitbybit.reporting.DocxReportStrategy;


/**
 * The InteractiveGameRunner class is responsible for setting up and running
 * the interactive Jeopardy game. It initializes the game context, manages
 * state transitions, and handles the main game loop.
 */
public class InteractiveGameRunner {

    /**
     * The main method to start the interactive Jeopardy game.
     * It sets up the question loader factory, initializes the game context
     * with an {@link IntroState}, adds a logging observer, runs the
     * game state machine until the {@link FinishedState} is reached,
     * and then handles the report generation based on user input.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // 1. Build a factory that can load CSV / JSON / XML
        QuestionLoaderFactory factory = createFactory();

        // 2. Start in IntroState
        IntroState intro = new IntroState(factory);

        // 3. GameContext owns the shared Scanner(System.in)
        GameContext context = new GameContext(intro);
        String baseDir = System.getProperty("user.dir");
        context.addObserver(new CSVLoggingObserver(baseDir));

        System.out.println("Starting interactive Jeopardy game...\n");
        context.notifyObservers(new com.bitbybit.logging.GameStartedEvent());

        // 4. Run the state machine until we hit FinishedState
        while (!(context.getState() instanceof FinishedState)) {
            GameState state = context.getState();
            if (state == null) {
                System.out.println("No active state, exiting.");
                return;
            }

            state.displayState();
            state.executeState(context);
        }

        // 5. Let FinishedState do its thing once
        context.getState().displayState();

        // Handle report generation logic in InteractiveGameRunner
        System.out.println("\nDo you want to generate a game report? (yes/no)");
        String response = context.getScanner().nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            System.out.println("Choose report format (text, pdf, docx):");
            String format = context.getScanner().nextLine().trim().toLowerCase();
            ReportStrategy strategy;

            switch (format) {
                case "text":
                    strategy = new TextReportStrategy();
                    break;
                case "pdf":
                    strategy = new PdfReportStrategy();
                    break;
                case "docx":
                    strategy = new DocxReportStrategy();
                    break;
                default:
                    System.out.println("Invalid format. Generating text report by default.");
                    strategy = new TextReportStrategy();
                    break;
            }
            context.getReportGenerator().setStrategy(strategy);
            context.getState().executeState(context); // Execute FinishedState with the chosen strategy
        } else {
            context.getState().executeState(context); // Execute FinishedState without report generation
        }

        System.out.println("\nGame loop ended. Goodbye!");
    }

    /**
     * Creates and returns a {@link QuestionLoaderFactory} that can load questions
     * from CSV, JSON, or XML files based on the file extension.
     *
     * @return A {@link QuestionLoaderFactory} instance.
     * @throws IllegalArgumentException if an unsupported file type is provided.
     */
    private static QuestionLoaderFactory createFactory() {
        return new QuestionLoaderFactory() {
            @Override
            public QuestionLoader createQuestionLoader(String filepath) {
                String lower = filepath.toLowerCase();

                if (lower.endsWith(".csv")) {
                    return new CSVQuestionLoaderFactory().createQuestionLoader(filepath);
                } else if (lower.endsWith(".json")) {
                    return new JSONQuestionLoaderFactory().createQuestionLoader(filepath);
                } else if (lower.endsWith(".xml")) {
                    return new XMLQuestionLoaderFactory().createQuestionLoader(filepath);
                } else {
                    throw new IllegalArgumentException("Unsupported file type: " + filepath);
                }
            }
        };
    }
}
