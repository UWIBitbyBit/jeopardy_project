package com.bitbybit;

import com.bitbybit.game.InteractiveGameRunner;

/**
 * Main class to start the Jeopardy game.
 * This class delegates the execution to the {@link InteractiveGameRunner}.
 */
public class Main {
    /**
     * The main method that serves as the entry point for the application.
     * It initializes and starts the interactive Jeopardy game.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Delegate to the interactive game runner so `java -cp target/classes
        // com.bitbybit.Main`
        // starts the interactive Jeopardy game.
        InteractiveGameRunner.main(args);
    }
}
