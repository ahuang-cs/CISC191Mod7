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

1. Student completed the FXML ranked match checkbox.
2. Student wired the checkbox into `GameController`.
3. Student extended the `.proto` file with ranked match data.
4. Student updated both client and server gRPC logic.
5. Student added client-side MVC validation for special move.
6. Student completed README reflection questions.
