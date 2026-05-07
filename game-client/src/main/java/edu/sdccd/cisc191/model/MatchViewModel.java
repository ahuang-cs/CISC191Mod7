package edu.sdccd.cisc191.model;

public class MatchViewModel {
    private String matchId;
    private final Player player = new Player("Player");
    private final Player opponent = new Player("Opponent");
    private boolean matchOver;
    private String winnerName = "";

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName == null ? "" : winnerName;
    }

    public boolean hasJoinedMatch() {
        return matchId != null && !matchId.isBlank();
    }

    public boolean canPlayMatch() {
        return hasJoinedMatch() && !matchOver;
    }

    /**
     * TODO 2: Complete this MVC helper.
     *
     * Return a short summary for the bottom of the JavaFX screen.
     * Expected format:
     * Match match-001: Ada vs Bot (Hard, ranked)
     *
     * Requirements:
     * - Use "No match" when matchId is null or blank.
     * - Use the current player and opponent names from this model.
     * - Use "Normal" when difficulty is null or blank.
     * - Use "ranked" when ranked is true, otherwise "casual".
     */
    public String buildMatchSummary(String difficulty, boolean ranked) {
        return "TODO: build match summary";
    }

    public void resetLocalState() {
        matchId = null;
        player.setName("Player");
        opponent.setName("Opponent");
        matchOver = false;
        winnerName = "";
    }
}
