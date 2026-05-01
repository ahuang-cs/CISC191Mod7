package edu.sdccd.cisc191.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchViewModelTest {

    @Test
    void resetLocalStateClearsMatchAndWinner() {
        MatchViewModel model = new MatchViewModel();

        model.setMatchId("abc123");
        model.getPlayer().setName("Ada");
        model.getOpponent().setName("Bot");
        model.setWinnerName("Ada");
        model.setMatchOver(true);

        model.resetLocalState();

        assertNull(model.getMatchId());
        assertEquals("Player", model.getPlayer().getName());
        assertEquals("Opponent", model.getOpponent().getName());
        assertEquals("", model.getWinnerName());
        assertFalse(model.isMatchOver());
    }

    @Test
    void hasJoinedMatchRequiresNonBlankMatchId() {
        MatchViewModel model = new MatchViewModel();

        assertFalse(model.hasJoinedMatch());

        model.setMatchId("");
        assertFalse(model.hasJoinedMatch());

        model.setMatchId("match-001");
        assertTrue(model.hasJoinedMatch());
    }

    @Test
    void canPlayMatchRequiresJoinedAndActiveMatch() {
        MatchViewModel model = new MatchViewModel();

        assertFalse(model.canPlayMatch());

        model.setMatchId("match-001");
        assertTrue(model.canPlayMatch());

        model.setMatchOver(true);
        assertFalse(model.canPlayMatch());
    }

    @Test
    void playerNameDefaultsWhenBlank() {
        Player player = new Player("");

        assertEquals("Player", player.getName());

        player.setName("Ada");
        assertEquals("Ada", player.getName());

        player.setName("   ");
        assertEquals("Player", player.getName());
    }
}
