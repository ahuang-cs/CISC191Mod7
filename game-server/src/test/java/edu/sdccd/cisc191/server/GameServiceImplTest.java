package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {

    @Test
    void joinMatchCreatesMatchWithExpectedValues() {
        GameServiceImpl service = new GameServiceImpl();

        TestObserver<JoinMatchResponse> observer = new TestObserver<>();

        service.joinMatch(
                JoinMatchRequest.newBuilder()
                        .setPlayerName("Ada")
                        .setDifficulty("Hard")
                        .setRanked(true)
                        .build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertFalse(observer.value.getMatchId().isBlank());
        assertEquals("Ada", observer.value.getPlayerName());
        assertEquals("Bot (Hard)", observer.value.getOpponentName());
        assertTrue(observer.value.getMessage().contains("ranked"));
        assertTrue(observer.value.getMessage().contains("Hard"));
    }

    @Test
    void playMatchChoosesWinnerAndLoser() {
        GameServiceImpl service = new GameServiceImpl();

        TestObserver<JoinMatchResponse> joinObserver = new TestObserver<>();

        service.joinMatch(
                JoinMatchRequest.newBuilder()
                        .setPlayerName("Ada")
                        .setDifficulty("Normal")
                        .build(),
                joinObserver
        );

        String matchId = joinObserver.value.getMatchId();

        TestObserver<MatchResultResponse> playObserver = new TestObserver<>();

        service.playMatch(
                PlayMatchRequest.newBuilder()
                        .setMatchId(matchId)
                        .setPlayerName("Ada")
                        .build(),
                playObserver
        );

        assertTrue(playObserver.completed);
        assertNull(playObserver.error);
        assertNotNull(playObserver.value);
        assertEquals(matchId, playObserver.value.getMatchId());
        assertFalse(playObserver.value.getWinnerName().isBlank());
        assertFalse(playObserver.value.getLoserName().isBlank());
        assertNotEquals(playObserver.value.getWinnerName(), playObserver.value.getLoserName());
    }

    @Test
    void loadMatchHistoryReturnsMultipleEntries() {
        GameServiceImpl service = new GameServiceImpl();

        TestObserver<MatchHistoryResponse> observer = new TestObserver<>();

        service.loadMatchHistory(
                MatchHistoryRequest.newBuilder()
                        .setPlayerName("Ada")
                        .build(),
                observer
        );

        assertTrue(observer.completed);
        assertNull(observer.error);
        assertNotNull(observer.value);
        assertTrue(observer.value.getMatchesCount() >= 3);
    }


    @Test
    void buildJoinSummaryFormatsRankedMatch() {
        String summary = GameServiceImpl.buildJoinSummary(
                "match-001",
                "Ada",
                "Bot",
                "Hard",
                true
        );

        assertEquals("Match match-001: Ada vs Bot (Hard, ranked)", summary);
    }

    @Test
    void buildJoinSummaryDefaultsMissingValues() {
        String summary = GameServiceImpl.buildJoinSummary(
                "   ",
                null,
                "   ",
                "   ",
                false
        );

        assertEquals("No match", summary);
    }

    @Test
    void serverStatisticsTrackConcurrentJoins() throws Exception {
        GameServiceImpl service = new GameServiceImpl();
        int totalJoins = 250;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < totalJoins; i++) {
            int playerNumber = i;
            executorService.submit(() -> service.joinMatch(
                    JoinMatchRequest.newBuilder()
                            .setPlayerName("Player" + playerNumber)
                            .setDifficulty("Normal")
                            .build(),
                    new TestObserver<>()
            ));
        }

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        assertEquals(totalJoins, service.getStatisticsForTesting().getJoinedMatchCount(),
                "TODO 9: gRPC request threads should update shared server statistics safely.");
    }

    private static class TestObserver<T> implements StreamObserver<T> {
        private T value;
        private Throwable error;
        private boolean completed;

        @Override
        public void onNext(T value) {
            this.value = value;
        }

        @Override
        public void onError(Throwable throwable) {
            this.error = throwable;
        }

        @Override
        public void onCompleted() {
            this.completed = true;
        }
    }
}
