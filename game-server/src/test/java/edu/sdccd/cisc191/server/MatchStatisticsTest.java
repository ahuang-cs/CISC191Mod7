package edu.sdccd.cisc191.server;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MatchStatisticsTest {

    @Test
    void countersUseThreadSafeDesign() throws Exception {
        Method recordJoin = MatchStatistics.class.getDeclaredMethod("recordJoin");
        Method recordCompletion = MatchStatistics.class.getDeclaredMethod("recordCompletion");

        boolean hasAtomicIntegerField = Arrays.stream(MatchStatistics.class.getDeclaredFields())
                .anyMatch(field -> field.getType().equals(AtomicInteger.class));
        boolean methodsAreSynchronized = Modifier.isSynchronized(recordJoin.getModifiers())
                && Modifier.isSynchronized(recordCompletion.getModifiers());

        assertTrue(hasAtomicIntegerField || methodsAreSynchronized,
                "TODO 9: Use AtomicInteger counters or synchronized record methods for server statistics.");
    }

    @Test
    void countersDoNotLoseConcurrentUpdates() throws Exception {
        MatchStatistics statistics = new MatchStatistics();
        int totalUpdates = 2_000;
        ExecutorService executorService = Executors.newFixedThreadPool(12);

        for (int i = 0; i < totalUpdates; i++) {
            executorService.submit(statistics::recordJoin);
            executorService.submit(statistics::recordCompletion);
        }

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        assertEquals(totalUpdates, statistics.getJoinedMatchCount(),
                "TODO 9: joinedMatchCount should not lose updates under concurrent gRPC-style calls.");
        assertEquals(totalUpdates, statistics.getCompletedMatchCount(),
                "TODO 9: completedMatchCount should not lose updates under concurrent gRPC-style calls.");
    }

    @Test
    void buildStatusLineFormatsCounts() {
        MatchStatistics statistics = new MatchStatistics();
        statistics.recordJoin();
        statistics.recordJoin();
        statistics.recordJoin();
        statistics.recordCompletion();
        statistics.recordCompletion();

        assertEquals("Server stats: 3 joined, 2 completed", statistics.buildStatusLine());
    }
}
