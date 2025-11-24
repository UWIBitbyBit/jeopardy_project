package com.bitbybit.game;

import com.bitbybit.input.*;
import com.bitbybit.model.Question;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A simple runner to test gameplay flow:
 * IntroState → PlayingState → FinishedState
 */
public class GameTestRunner {
    public static void main(String[] args) {

        QuestionLoaderFactory factory = createFactory();
        IntroState introState = new IntroState(factory);

        String simulatedInput = String.join("\n", Arrays.asList(
                "sample_game_JSON.json",
                "2",
                "Alice",
                "Bob",

                // --- Turn 1: Alice ---
                "File Handling",
                "100",
                "A",
                "y",

                // --- Turn 2: Bob ---
                "Arrays",
                "200",
                "B",
                "n"));

        Scanner testScanner = new Scanner(
                new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        GameContext context = new GameContext(introState, testScanner);

        ByteArrayOutputStream outCapture = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outCapture));

        runState(context); // Intro
        runState(context); // Playing
        runState(context); // Finished

        System.setOut(originalOut);

        System.out.println("========== GAME TEST OUTPUT ==========");
        System.out.println(outCapture.toString());
        System.out.println("======================================");

        System.out.println("Final state: " + context.getState().getClass().getSimpleName());
    }

    private static void runState(GameContext context) {
        if (context.getState() == null)
            return;
        context.getState().displayState();
        context.getState().executeState(context);
    }

    // ---- Simulates System.in for testing ----
    private static void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
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