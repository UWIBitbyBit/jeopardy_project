package com.bitbybit.game;

import com.bitbybit.input.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class IntroStateRunner {
    public static void main(String[] args) {
        // Create a factory for testing
        QuestionLoaderFactory factory = createMultiFormatFactory();

        // Create the intro state with the factory
        IntroState introState = new IntroState(factory);

        // Create game context with intro state
        GameContext context = new GameContext(introState);

        // Simulate user input (entering a filename)
        simulateUserInput("sample_game_JSON.json");

        // Capture console output for verification
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the intro state
        introState.displayState();
        introState.executeState(context);

        // Restore original System.out
        System.setOut(originalOut);

        // Print the captured output
        System.out.println("Test Output:");
        System.out.println(outputStream.toString());

        // Verify state transition
        System.out.println("\nVerifying state transition:");
        if (context.getState() instanceof PlayingState) {
            System.out.println("Successfully transitioned to PlayingState");
        } else {
            System.out.println(" Failed to transition to PlayingState");
        }
    }

    // Helper method to simulate user input
    private static void simulateUserInput(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    // Create a factory that can handle multiple file formats
    private static QuestionLoaderFactory createMultiFormatFactory() {
        return new QuestionLoaderFactory() {
            @Override
            public QuestionLoader createQuestionLoader(String filepath) {
                if (filepath.toLowerCase().endsWith(".csv")) {
                    return new CSVQuestionLoaderFactory().createQuestionLoader(filepath);
                } else if (filepath.toLowerCase().endsWith(".json")) {
                    return new JSONQuestionLoaderFactory().createQuestionLoader(filepath);
                } else if (filepath.toLowerCase().endsWith(".xml")) {
                    return new XMLQuestionLoaderFactory().createQuestionLoader(filepath);
                } else {
                    throw new IllegalArgumentException("Unsupported file type: " + filepath);
                }
            }
        };
    }
}