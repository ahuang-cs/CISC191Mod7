package edu.sdccd.cisc191.controller;

import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.model.MatchViewModel;
import edu.sdccd.cisc191.service.GameGrpcClient;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GameController {

    @FXML
    private TextField playerNameField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label playerLabel;

    @FXML
    private Label opponentLabel;

    @FXML
    private Label winnerLabel;

    @FXML
    private TextArea matchLog;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private CheckBox rankedMatchCheckBox;

    private final MatchViewModel match = new MatchViewModel();

    private final GameGrpcClient grpcClient = new GameGrpcClient("localhost", 50051);

    @FXML
    private void initialize() {
        difficultyComboBox.getItems().addAll("Easy", "Normal", "Hard");
        difficultyComboBox.setValue("Normal");

        match.resetLocalState();
        updateView();
        matchLog.appendText("Client loaded. Start the gRPC server, then click Join Match.\n");
    }

    @FXML
    private void handleJoinMatch() {
        String playerName = getPlayerName();
        String difficulty = difficultyComboBox.getValue();
        boolean ranked = rankedMatchCheckBox.isSelected();

        statusLabel.setText("Status: Joining match...");
        matchLog.appendText("Joining " + (ranked ? "ranked" : "casual")
                + " match as " + playerName
                + " on " + difficulty + " difficulty...\n");

        Task<JoinMatchResponse> task = grpcClient.joinMatchTask(
                playerName,
                difficulty,
                ranked
        );

        task.setOnSucceeded(event -> {
            JoinMatchResponse response = task.getValue();

            match.setMatchId(response.getMatchId());
            match.getPlayer().setName(response.getPlayerName());
            match.getOpponent().setName(response.getOpponentName());
            match.setMatchOver(false);
            match.setWinnerName("");

            statusLabel.setText("Status: Match ready");
            matchLog.appendText(response.getMessage() + "\n");

            updateView();
        });

        task.setOnFailed(event -> {
            statusLabel.setText("Status: Server unavailable");
            matchLog.appendText("Could not join match. Is the gRPC server running?\n");
            matchLog.appendText("Error: " + task.getException().getMessage() + "\n");
        });

        runInBackground(task);
    }

    @FXML
    private void handlePlayMatch() {
        if (!match.canPlayMatch()) {
            matchLog.appendText("Join a match before playing, or reset after a completed match.\n");
            return;
        }

        statusLabel.setText("Status: Playing match...");
        matchLog.appendText("Server is choosing a random winner...\n");

        Task<MatchResultResponse> task = grpcClient.playMatchTask(
                match.getMatchId(),
                match.getPlayer().getName()
        );

        task.setOnSucceeded(event -> {
            MatchResultResponse response = task.getValue();

            match.setWinnerName(response.getWinnerName());
            match.setMatchOver(true);

            statusLabel.setText(response.getPlayerWon()
                    ? "Status: You won!"
                    : "Status: You lost.");

            matchLog.appendText(response.getMessage() + "\n");
            updateView();
        });

        task.setOnFailed(event -> {
            statusLabel.setText("Status: Match failed");
            matchLog.appendText("Could not play match.\n");
            matchLog.appendText("Error: " + task.getException().getMessage() + "\n");
        });

        runInBackground(task);
    }

    @FXML
    private void handleLoadHistory() {
        String playerName = getPlayerName();

        matchLog.appendText("Loading match history from gRPC server...\n");

        Task<MatchHistoryResponse> task = grpcClient.loadMatchHistoryTask(playerName);

        task.setOnSucceeded(event -> {
            MatchHistoryResponse response = task.getValue();

            matchLog.appendText("Match history:\n");
            for (String line : response.getMatchesList()) {
                matchLog.appendText("- " + line + "\n");
            }
        });

        task.setOnFailed(event -> {
            matchLog.appendText("Could not load match history.\n");
            matchLog.appendText("Error: " + task.getException().getMessage() + "\n");
        });

        runInBackground(task);
    }

    @FXML
    private void handleResetLocalView() {
        match.resetLocalState();
        statusLabel.setText("Status: Local view reset");
        matchLog.appendText("Local client view reset. Click Join Match for a new server match.\n");
        updateView();
    }

    private String getPlayerName() {
        String typedName = playerNameField.getText();

        if (typedName == null || typedName.isBlank()) {
            return "Player";
        }

        return typedName.trim();
    }

    private void updateView() {
        playerLabel.setText("Player: " + match.getPlayer().getName());
        opponentLabel.setText("Opponent: " + match.getOpponent().getName());

        if (match.getWinnerName().isBlank()) {
            winnerLabel.setText("Winner: TBD");
        } else {
            winnerLabel.setText("Winner: " + match.getWinnerName());
        }
    }

    private void runInBackground(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
