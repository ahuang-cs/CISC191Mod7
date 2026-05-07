package edu.sdccd.cisc191.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FxmlTodoTest {

    @Test
    void fxmlIncludesMatchSummaryLabel() throws IOException {
        String fxml = readResource("/view/game-client.fxml");

        assertTrue(fxml.contains("fx:id=\"matchSummaryLabel\""),
                "TODO 1: Add a Label with fx:id=\"matchSummaryLabel\" to game-client.fxml.");
        assertTrue(fxml.contains("Summary: No match"),
                "TODO 1: The new label should start with text=\"Summary: No match\".");
    }

    private String readResource(String path) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            assertNotNull(inputStream, "Could not find resource: " + path);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
