package com.example.hospitalmanagementsystemfrontendfinal.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView {
    private Scene loginScene;
    private TextField idField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Text signUpText;

    public Scene getLoginScene(Stage stage) {
        Pane root = new Pane();
        loginScene = new Scene(root, stage.getWidth(), stage.getHeight());

        Image backgroundImage = new Image(getClass().getResource("/background_main.png").toExternalForm());

        Runnable updateBackground = () -> {
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(loginScene.getWidth(), loginScene.getHeight(), false, false, false, false)
            );
            root.setBackground(new Background(bgImage));
        };

        loginScene.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
        loginScene.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
        updateBackground.run();

        Label idLabel = new Label("ID:");
        idLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        idField = new TextField();
        idField.setPromptText("ID");
        idField.setStyle("-fx-font-size: 16px;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px;");

        loginBtn = new Button("Login");

        idLabel.setLabelFor(idField);
        passwordLabel.setLabelFor(passwordField);

        Text normalText = new Text("Don't have an account? ");
        normalText.setFill(Color.WHITE);

        signUpText = new Text("Sign up");
        signUpText.setFill(Color.BLUE);
        signUpText.setUnderline(true);

        HBox signUpLink = new HBox(normalText, signUpText);

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20;");
        vbox.getChildren().addAll(
                idLabel, idField,
                passwordLabel, passwordField,
                loginBtn,
                signUpLink
        );

        vbox.layoutXProperty().bind(root.widthProperty().divide(3).subtract(vbox.widthProperty().divide(2)));
        vbox.layoutYProperty().bind(root.heightProperty().divide(2).subtract(vbox.heightProperty().divide(2)));

        root.getChildren().add(vbox);

        return loginScene;
    }

    public TextField getIdField() {
        return idField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public Text getSignUpText() {
        return signUpText;
    }
}
