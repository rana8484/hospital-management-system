package com.example.hospitalmanagementsystemfrontendfinal;

import com.example.hospitalmanagementsystemfrontendfinal.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLoginView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
