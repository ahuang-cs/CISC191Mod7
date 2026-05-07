package edu.sdccd.cisc191.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ProtoContractTodoTest {

    @Test
    void clientProtoAddsSummaryToJoinMatchResponse() throws IOException {
        String proto = Files.readString(Path.of("src/main/proto/game_service.proto"));

        assertTrue(proto.contains("string summary = 5;"),
                "TODO 5: Add `string summary = 5;` to JoinMatchResponse in the client proto.");
    }
}
