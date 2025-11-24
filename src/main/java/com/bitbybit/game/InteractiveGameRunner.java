package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.input.CSVQuestionLoaderFactory;
import com.bitbybit.input.JSONQuestionLoaderFactory;
import com.bitbybit.input.XMLQuestionLoaderFactory;

public class InteractiveGameRunner {

    public static void main(String[] args) {
        // 1. Build a factory that can load CSV / JSON / XML
        QuestionLoaderFactory factory = createFactory();

        // 2. Start in IntroState
        IntroState intro = new IntroState(factory);

        // 3. GameContext owns the shared Scanner(System.in)
        GameContext context = new GameContext(intro);

        System.out.println("Starting interactive Jeopardy game...\n");

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
        context.getState().executeState(context);

        System.out.println("\nGame loop ended. Goodbye!");
    }

    // ---- Multi-format question loader factory ----
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
