# JavaFX + gRPC 1v1 Game Lab

## GitHub Classroom Assignment

In this lab, you will complete a two-module Maven project:

| Module | Purpose |
|---|---|
| `game-server` | gRPC server that owns match state and game rules |
| `game-client` | JavaFX client using FXML, MVC, JavaFX `Task`, and gRPC |

The starter code already builds and runs. Your job is to complete the TODOs for FXML, MVC, and gRPC.

---

## Learning Goals

By the end of this lab, you should be able to:

- Build a multi-module Maven project.
- Run a Java gRPC server.
- Run a JavaFX client.
- Use FXML to define a JavaFX UI.
- Connect FXML controls to a controller.
- Use MVC-style organization.
- Use JavaFX `Task` to keep the UI responsive.
- Modify a `.proto` file and update both client and server code.

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
2. Choose a difficulty.
3. Pick a difficulty.
4. Click **Join Match**.

---

## Run Tests

From the root folder:

```bash
mvn test
```

The GitHub Classroom workflow will also run tests automatically when you push.

---

# Required TODOs

## TODO 1: FXML

File:

```text
game-client/src/main/resources/view/game-client.fxml
```

Add a ranked match checkbox near the player/difficulty controls.

Suggested FXML:

```xml
<CheckBox fx:id="rankedMatchCheckBox" text="Ranked" />
```

Then update the controller with:

```java
@FXML
private CheckBox rankedMatchCheckBox;
```

---

## TODO 2: MVC

File:

```text
game-client/src/main/java/edu/sdccd/cisc191/model/MatchViewModel.java
```

Add a method that checks whether the special move should be allowed.

Example:

```java
public boolean canUseSpecialMove() {
    return hasJoinedMatch() && !matchOver && opponent.getHp() > 0;
}
```

Then update:

```text
game-client/src/main/java/edu/sdccd/cisc191/controller/GameController.java
```

Use the model method before sending the `SPECIAL` action.

---

## TODO 3: gRPC

Update the gRPC contract in both modules:

```text
game-server/src/main/proto/game_service.proto
game-client/src/main/proto/game_service.proto
```

Add ranked match data to `JoinMatchRequest`:

```proto
bool ranked = 4;
```

Then update:

| File | Required Change |
|---|---|
| `GameController.java` | Send `rankedMatchCheckBox.isSelected()` to the gRPC client |
| `GameGrpcClient.java` | Add the ranked value to `JoinMatchRequest` |
| `GameServiceImpl.java` | Read `request.getRanked()` and include it in the join message |

Example server message:

```text
Joined ranked match abc-123 on Hard difficulty.
```

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

Replace this section with your answers before submitting.

1. Which Maven module is the server?
2. Which Maven module is the JavaFX client?
3. Which file defines the gRPC service contract?
4. Which file defines the FXML view?
5. Which class is the JavaFX controller?
6. Which class acts as the client-side model?
7. Why do gRPC calls run inside JavaFX `Task` objects?
8. What happens if the JavaFX client runs before the server?
9. How did you complete the FXML TODO?
10. How did you complete the MVC TODO?
11. How did you complete the gRPC TODO?
12. What was the hardest part?

---

# Grading Rubric

| Category | Points |
|---|---:|
| Multi-module Maven project builds | 15 |
| gRPC server runs | 15 |
| JavaFX client runs | 15 |
| Client joins a match through gRPC | 10 |
| Actions update HP through gRPC | 10 |
| FXML ranked checkbox completed | 10 |
| MVC special move validation completed | 10 |
| gRPC proto/client/server ranked update completed | 10 |
| Reflection answers completed | 5 |
| **Total** | **100** |

---

# GitHub Classroom Autograding

This repository includes:

```text
.github/workflows/classroom.yml
```

The workflow checks that the project builds and tests pass.

Autograding does not replace manual review. Your instructor may still inspect your TODOs, README, and code quality.
