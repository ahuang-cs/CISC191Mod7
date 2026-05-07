package edu.sdccd.cisc191.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void buildJoinLogMessageFormatsRankedMatch() {
        String message = GameController.buildJoinLogMessage("Ada", "Hard", true);

        assertEquals("Joining ranked match as Ada on Hard difficulty...", message);
    }

    @Test
    void buildJoinLogMessageTrimsAndDefaultsInput() {
        String message = GameController.buildJoinLogMessage("   ", "   ", false);

        assertEquals("Joining casual match as Player on Normal difficulty...", message);
    }
}
