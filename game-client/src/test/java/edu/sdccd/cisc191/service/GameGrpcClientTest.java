package edu.sdccd.cisc191.service;

import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameGrpcClientTest {

    @Test
    void buildJoinMatchRequestTrimsInputAndPreservesRanked() {
        JoinMatchRequest request = GameGrpcClient.buildJoinMatchRequest("  Ada  ", "  Hard  ", true);

        assertEquals("Ada", request.getPlayerName());
        assertEquals("Hard", request.getDifficulty());
        assertTrue(request.getRanked());
    }

    @Test
    void buildJoinMatchRequestDefaultsBlankInput() {
        JoinMatchRequest request = GameGrpcClient.buildJoinMatchRequest(null, "   ", false);

        assertEquals("Player", request.getPlayerName());
        assertEquals("Normal", request.getDifficulty());
        assertFalse(request.getRanked());
    }
}
