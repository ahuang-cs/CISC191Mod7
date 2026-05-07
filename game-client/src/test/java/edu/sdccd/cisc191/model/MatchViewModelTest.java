package edu.sdccd.cisc191.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    void buildMatchSummaryFormatsJoinedRankedMatch() {
        MatchViewModel model = new MatchViewModel();
        model.setMatchId("match-001");
        model.getPlayer().setName("Ada");
        model.getOpponent().setName("Bot");

        assertEquals("Match match-001: Ada vs Bot (Hard, ranked)",
                model.buildMatchSummary("Hard", true));
    }

    @Test
    void buildMatchSummaryHandlesNoMatchAndDefaultDifficulty() {
        MatchViewModel model = new MatchViewModel();

        assertEquals("No match", model.buildMatchSummary("   ", false));
    }


    @Test
    void recordCompletedMatchUsesThreadSafeCounterDesign() throws Exception {
        Method method = MatchViewModel.class.getDeclaredMethod("recordCompletedMatchThreadSafely", String.class);

        boolean hasAtomicIntegerField = Arrays.stream(MatchViewModel.class.getDeclaredFields())
                .anyMatch(field -> field.getType().equals(AtomicInteger.class));
        boolean methodIsSynchronized = Modifier.isSynchronized(method.getModifiers());

        assertTrue(hasAtomicIntegerField || methodIsSynchronized,
                "TODO 7: Use an AtomicInteger field or make recordCompletedMatchThreadSafely synchronized.");
    }

    @Test
    void recordCompletedMatchDoesNotLoseConcurrentUpdates() throws Exception {
        MatchViewModel model = new MatchViewModel();
        int totalUpdates = 1_000;
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < totalUpdates; i++) {
            executorService.submit(() -> model.recordCompletedMatchThreadSafely("Ada"));
        }

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        assertEquals(totalUpdates, model.getCompletedMatchCount(),
                "TODO 7: The completed-match count should not lose updates when many threads record results.");
        assertTrue(model.isMatchOver());
        assertEquals("Ada", model.getWinnerName());
    }
}
