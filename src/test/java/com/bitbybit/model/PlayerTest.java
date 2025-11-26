package com.bitbybit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerCreation() {
        Player player = new Player("Player1");
        assertNotNull(player);
        assertEquals("Player1", player.getName());
        assertEquals(0, player.getScore());
    }

    @Test
    void testAddScore() {
        Player player = new Player("Player1");
        player.addScore(100);
        assertEquals(100, player.getScore());
        player.addScore(50);
        assertEquals(150, player.getScore());
    }

    @Test
    void testSubtractScore() {
        Player player = new Player("Player1");
        player.addScore(200);
        player.subtractScore(50);
        assertEquals(150, player.getScore());
        player.subtractScore(200); // Should not go below 0
        assertEquals(0, player.getScore());
    }

    @Test
    void testToString() {
        Player player = new Player("Player1");
        player.addScore(100);
        String expectedToString = "Player1 (Score: 100)";
        assertEquals(expectedToString, player.toString());
    }
}
