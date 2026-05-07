package edu.sdccd.cisc191.server;

/**
 * Tracks server-wide match statistics shared by many gRPC request threads.
 */
public class MatchStatistics {

    // TODO 9: Replace these counters with a thread-safe design.
    // Recommended: AtomicInteger joinedMatchCount and AtomicInteger completedMatchCount.
    private int joinedMatchCount = 0;
    private int completedMatchCount = 0;

    /**
     * TODO 9: Make this update thread-safe.
     */
    public void recordJoin() {
        joinedMatchCount = joinedMatchCount + 1;
    }

    /**
     * TODO 9: Make this update thread-safe.
     */
    public void recordCompletion() {
        completedMatchCount = completedMatchCount + 1;
    }

    public int getJoinedMatchCount() {
        return joinedMatchCount;
    }

    public int getCompletedMatchCount() {
        return completedMatchCount;
    }

    /**
     * TODO 9: Return a readable, thread-safe statistics summary.
     *
     * Expected format:
     * Server stats: 3 joined, 2 completed
     */
    public String buildStatusLine() {
        return "TODO: server stats";
    }
}
