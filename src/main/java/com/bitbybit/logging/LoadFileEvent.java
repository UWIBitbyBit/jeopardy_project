package com.bitbybit.logging;

/**
 * Represents a game event indicating that a question file has been successfully loaded.
 * This event is typically triggered after the {@link com.bitbybit.input.QuestionLoader}
 * has finished parsing questions from a specified file.
 */
public class LoadFileEvent implements GameEvent {
    /**
     * Returns the type of this game event, which is "FILE_LOADED".
     *
     * @return A string constant "FILE_LOADED".
     */
    @Override
    public String getType() {
        return "FILE_LOADED";
    }
}
