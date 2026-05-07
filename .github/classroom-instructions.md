# GitHub Classroom Setup Notes for Instructor

Recommended GitHub Classroom assignment type:

- Repository visibility: Private
- Starter code: Upload/import this repository
- Online IDE: Optional
- Autograding: Use GitHub Actions

The included workflow runs:

```bash
mvn -B clean install
mvn -B -pl game-server test
mvn -B -pl game-client test
```

Suggested manual grading items:

1. Student completed the FXML match summary label.
2. Student completed the MVC summary helper in `MatchViewModel`.
3. Student completed and used the controller join-log helper.
4. Student completed and used the gRPC client request helper.
5. Student extended both `.proto` files with the `summary` field.
6. Student completed the server summary helper and set the summary on `JoinMatchResponse`.
7. Student completed the thread-safe model completed-match counter.
8. Student completed the JavaFX Application Thread helper using `Platform.isFxApplicationThread()` and `Platform.runLater(action)`.
9. Student completed the thread-safe server statistics class using `AtomicInteger` or synchronized methods.
10. Student verified the app still joins, plays, resets, and loads history.
11. Student completed peer review one week after the lab due date.
12. Student completed README reflection questions.
