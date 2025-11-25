package com.bitbybit.logging;
import java.io.FileWriter;
import java.io.IOException;


public class CSVLoggingObserver implements GameObserver {
    private final String logsDir;
    private int gameCounter = 1;
    private boolean newGame = true;
    private String currentGameId = "GAME001";
    private String currentFilePath;

    public CSVLoggingObserver(String baseDir) {
        // Create logs directory if it doesn't exist
        this.logsDir = baseDir + "/game_logs";
        java.io.File dir = new java.io.File(logsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Scan for existing game log files and set gameCounter to next available
        String[] files = dir.list((d, name) -> name.matches("game_\\d{3}\\.csv"));
        if (files != null && files.length > 0) {
            int maxNum = 0;
            for (String f : files) {
                try {
                    int num = Integer.parseInt(f.substring(5, 8));
                    if (num > maxNum) maxNum = num;
                } catch (Exception ignore) {}
            }
            gameCounter = maxNum + 1;
        }
        // Set initial file path
        this.currentFilePath = String.format("%s/game_%03d.csv", logsDir, gameCounter);
    }

    @Override
    public void onEvent(GameEvent event) {
        String eventType = event.getType();
        
        if (eventType.equals("PLAYER_JOINED") && newGame) {
            currentGameId = String.format("GAME%03d", gameCounter);
            currentFilePath = String.format("%s/game_%03d.csv", logsDir, gameCounter);
            newGame = false;
        }
        
        boolean writeHeader = false;
        java.io.File file = new java.io.File(currentFilePath);
        if (!file.exists() || file.length() == 0) {
            writeHeader = true;
        }
        
        if (eventType.equals("GAME_FINISHED")) {
            gameCounter++;
            newGame = true;
        }

        try (FileWriter out = new FileWriter(currentFilePath, true)) {
            if (writeHeader) {
                // Correct column order based on the image
                out.write("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play\n");
            }
            
            String caseId = currentGameId;
            String timestamp = java.time.LocalDateTime.now().toString();
            
            switch(eventType) {
                case "GAME_STARTED" -> {
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Start Game",
                        timestamp,
                        "",  // Category
                        "",  // Question_Value
                        "",  // Answer_Given
                        "",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "GAME_LOADED" -> {
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Load File",
                        timestamp,
                        "",  // Category
                        "",  // Question_Value
                        "",  // Answer_Given
                        "Success",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "SELECT_PLAYER_COUNT" -> {
                    var e = (SelectPlayerCountEvent) event;
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Select Player Count",
                        timestamp,
                        "",  // Category
                        String.valueOf(e.getCount()),  // Question_Value
                        "",  // Answer_Given
                        "N/A",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "PLAYER_JOINED" -> {
                    var e = (PlayerJoinedEvent) event;
                    out.write(String.join(",",
                        caseId,
                        
                        "System",// Player_ID
                        "Enter Player Name",// Activity
                        timestamp,// Timestamp
                        "",  // Category
                        "",  // Question_Value
                        e.getPlayer().getName(),  // Answer_Given
                        "N/A",  // Result
                        String.valueOf(e.getPlayer().getScore())  // Score_After_Play
                    ) + "\n");
                }
                case "SELECT_CATEGORY" -> {
                    var e = (SelectCategoryEvent) event;
                    out.write(String.join(",",
                        caseId,
                        e.getPlayerName(),  // Player_ID
                        "Select Category",
                        timestamp,
                        e.getCategory(),  // Category
                        "",  // Question_Value
                        "",  // Answer_Given
                        "",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "SELECT_QUESTION" -> {
                    var e = (SelectQuestionEvent) event;
                    out.write(String.join(",",
                        caseId,
                        e.getPlayerName(),  // Player_ID
                        "Select Question",
                        timestamp,
                        e.getCategory(),  // Category
                        String.valueOf(e.getValue()),  // Question_Value
                        "",  // Answer_Given
                        "",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "QUESTION_ANSWERED" -> {
                    var e = (QuestionAnsweredEvent) event;
                    String result = e.isCorrect() ? "Correct" : "Incorrect";
                    out.write(String.join(",",
                        caseId,
                        e.getPlayer().getName(),  // Player_ID
                        "Answer Question",
                        timestamp,
                        e.getQuestion().getCategory(),  // Category
                        String.valueOf(e.getQuestion().getValue()),  // Question_Value
                        e.getAnswerGiven(),  // Answer_Given
                        result,  // Result
                        String.valueOf(e.getPlayer().getScore())  // Score_After_Play
                    ) + "\n");
                }
                case "GENERATE_REPORT" -> {
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Generate Report",
                        timestamp,
                        "",  // Category
                        "",  // Question_Value
                        "N/A",  // Answer_Given
                        "N/A",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "GENERATE_EVENT_LOG" -> {
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Generate Event Log",
                        timestamp,
                        "",  // Category
                        "",  // Question_Value
                        "N/A",  // Answer_Given
                        "N/A",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
                case "GAME_FINISHED" -> {
                    out.write(String.join(",",
                        caseId,
                        "System",
                        "Exit Game",
                        timestamp,
                        "",  // Category
                        "",  // Question_Value
                        "",  // Answer_Given
                        "",  // Result
                        ""   // Score_After_Play
                    ) + "\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}