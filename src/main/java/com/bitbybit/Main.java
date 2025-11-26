package com.bitbybit;

import com.bitbybit.game.InteractiveGameRunner;

public class Main {
    public static void main(String[] args) {
        // Delegate to the interactive game runner so `java -cp target/classes
        // com.bitbybit.Main`
        // starts the interactive Jeopardy game.
        InteractiveGameRunner.main(args);
    }
}