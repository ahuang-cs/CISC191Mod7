package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

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
