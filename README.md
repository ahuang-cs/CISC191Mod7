# JavaFX + gRPC 1v1 Game Lab

## GitHub Classroom Assignment

In this lab, you will complete a two-module Maven project:

| Module | Purpose |
|---|---|
| `game-server` | gRPC server that owns match state and game rules |
| `game-client` | JavaFX client using FXML, MVC, JavaFX `Task`, and gRPC |

The baseline app already builds and runs as a simple 1v1 match simulator. The server creates matches, chooses a random winner, and returns simple match history. Your job is to complete the new TODOs so the app shows a consistent **match summary** across the JavaFX UI, MVC model, controller, gRPC client, `.proto` contract, and gRPC server.

---

## Learning Goals

By the end of this lab, you should be able to:

- Build a multi-module Maven project.
- Run a Java gRPC server.
- Run a JavaFX client.
- Use FXML to define and update a JavaFX UI.
- Connect FXML controls to a controller.
- Use MVC-style helper methods to keep display logic out of event handlers.
- Use JavaFX `Task` to keep the UI responsive.
- Modify a `.proto` file and update both client and server code.
- Use unit tests to guide feature completion.

---

## Required Tools

- Eclipse Temurin JDK 21
- Maven 3.9+
- Git
- IntelliJ IDEA, VS Code, Eclipse, or another Java IDE

---

## Project Structure

```text
javafx-grpc-1v1-game-lab
├── game-server
│   ├── src/main/java
│   ├── src/main/proto
│   └── src/test/java
├── game-client
│   ├── src/main/java
│   ├── src/main/resources
│   ├── src/main/proto
│   └── src/test/java
├── .github/workflows/classroom.yml
└── pom.xml
```

---

## Build the Project

From the root folder:

```bash
mvn clean install
```

---

## Run the gRPC Server

Open a terminal:

```bash
cd game-server
mvn exec:java
```

Expected output:

```text
1v1 gRPC Game Server started on port 50051
```

Leave this terminal running.

---

## Run the JavaFX Client

Open a second terminal:

```bash
cd game-client
mvn javafx:run
```

Use the app to:

1. Enter a player name.
2. Choose whether the match is ranked.
3. Pick a difficulty.
4. Click **Join Match**.
5. Click **Play Match**.
6. Click **Load Match History**.

---

## Run Tests

From the root folder:

```bash
mvn test
```

The GitHub Classroom workflow will also run tests automatically when you push.

---

# Required TODOs

## TODO 1: FXML layer

File:

```text
game-client/src/main/resources/view/game-client.fxml
```

Add a new label to the bottom status area so the user can see the current match summary.

Required label:

```xml
<Label fx:id="matchSummaryLabel" text="Summary: No match" />
```

Recommended location: place it near the existing player, opponent, and winner labels.

Test that checks this layer:

```text
game-client/src/test/java/edu/sdccd/cisc191/model/FxmlTodoTest.java
```

---

## TODO 2: MVC/model layer

File:

```text
game-client/src/main/java/edu/sdccd/cisc191/model/MatchViewModel.java
```

Complete:

```java
public String buildMatchSummary(String difficulty, boolean ranked)
```

Required behavior:

| Situation | Expected result |
|---|---|
| No joined match | `No match` |
| Joined ranked hard match | `Match match-001: Ada vs Bot (Hard, ranked)` |
| Blank difficulty | Use `Normal` |
| `ranked == true` | Use `ranked` |
| `ranked == false` | Use `casual` |

Test that checks this layer:

```text
game-client/src/test/java/edu/sdccd/cisc191/model/MatchViewModelTest.java
```

---

## TODO 3: Controller layer

File:

```text
game-client/src/main/java/edu/sdccd/cisc191/controller/GameController.java
```

Complete:

```java
public static String buildJoinLogMessage(String playerName, String difficulty, boolean ranked)
```

Required behavior:

| Input | Expected result |
|---|---|
| `"Ada", "Hard", true` | `Joining ranked match as Ada on Hard difficulty...` |
| blank player, blank difficulty, false | `Joining casual match as Player on Normal difficulty...` |

Then use the helper in `handleJoinMatch()` so the controller does not build the message inline.

Test that checks this layer:

```text
game-client/src/test/java/edu/sdccd/cisc191/controller/GameControllerTest.java
```

---

## TODO 4: gRPC client layer

File:

```text
game-client/src/main/java/edu/sdccd/cisc191/service/GameGrpcClient.java
```

Complete:

```java
public static JoinMatchRequest buildJoinMatchRequest(String playerName, String difficulty, boolean ranked)
```

Required behavior:

- Return a `JoinMatchRequest`.
- Trim `playerName` and `difficulty`.
- Use `Player` when the player name is null or blank.
- Use `Normal` when difficulty is null or blank.
- Preserve the ranked value.

Then update `joinMatchTask()` to use this helper.

Test that checks this layer:

```text
game-client/src/test/java/edu/sdccd/cisc191/service/GameGrpcClientTest.java
```

---

## TODO 5: gRPC contract layer

Update both proto files:

```text
game-server/src/main/proto/game_service.proto
game-client/src/main/proto/game_service.proto
```

Add a server-created summary field to `JoinMatchResponse`:

```proto
string summary = 5;
```

After changing the `.proto` files, rebuild the project so Maven regenerates the gRPC Java classes:

```bash
mvn clean install
```

Tests that check this layer:

```text
game-server/src/test/java/edu/sdccd/cisc191/server/ProtoContractTodoTest.java
game-client/src/test/java/edu/sdccd/cisc191/model/ProtoContractTodoTest.java
```

---

## TODO 6: gRPC server layer

File:

```text
game-server/src/main/java/edu/sdccd/cisc191/server/GameServiceImpl.java
```

Complete:

```java
public static String buildJoinSummary(
        String matchId,
        String playerName,
        String opponentName,
        String difficulty,
        boolean ranked
)
```

Required behavior:

| Situation | Expected result |
|---|---|
| Blank match id | `No match` |
| Joined ranked hard match | `Match match-001: Ada vs Bot (Hard, ranked)` |
| Blank player | Use `Player` |
| Blank opponent | Use `Bot` |
| Blank difficulty | Use `Normal` |

Then update `joinMatch()` to set the new proto summary field:

```java
.setSummary(buildJoinSummary(...))
```

Test that checks this layer:

```text
game-server/src/test/java/edu/sdccd/cisc191/server/GameServiceImplTest.java
```

---

## Final integration requirement

After all TODOs are complete:

1. `mvn clean install` should pass.
2. The JavaFX UI should show the summary label.
3. Joining a match should update the summary using the server-provided summary.
4. Resetting the local view should return the summary to `Summary: No match`.

---

# Optional Extension

Choose one:

| Option | Feature |
|---|---|
| A | Add server streaming for live match updates |
| B | Add opponent counterattack logic |
| C | Add a scoreboard |
| D | Add CSS styling |
| E | Add a second FXML screen |
| F | Add more tests |

---

# README Reflection

Answer these questions in your pull request or in a short reflection file:

1. What is the purpose of FXML in this project?
2. What is the controller responsible for?
3. What is the model responsible for?
4. What is the gRPC server responsible for?
5. Why should JavaFX network calls run inside a `Task` instead of directly in the button handler?
6. What changed in the `.proto` file?
7. Why do both the client and server need matching `.proto` files?
8. What does Maven regenerate after a `.proto` change?
9. How did you complete the FXML TODO?
10. How did you complete the MVC/model TODO?
11. How did you complete the controller TODO?
12. How did you complete the gRPC client TODO?
13. How did you complete the gRPC server TODO?
14. Which unit test helped you the most, and why?

---

# GitHub Classroom Notes

Autograding does not replace manual review. Your instructor may still inspect your TODOs, README, and code quality.
