package com.example.hospitalmanagementsystemfrontendfinal.controller;

import com.example.hospitalmanagementsystemfrontendfinal.ApiClient;
import com.example.hospitalmanagementsystemfrontendfinal.view.LoginView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginController {
    private LoginView loginView;
    private final String BASE_URL = "http://localhost:8080/api";

    public LoginController() {
        this.loginView = new LoginView();
    }

    public void showLoginView(Stage stage) {
        Scene scene = loginView.getLoginScene(stage);

        loginView.getLoginBtn().setOnAction(e -> handleLogin(stage));
        loginView.getSignUpText().setOnMouseClicked(e -> showSignUpView(stage));

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.show();
    }

    private void handleLogin(Stage stage) {
        String userId = loginView.getIdField().getText();
        String password = loginView.getPasswordField().getText();
        try {
            if (userId.charAt(0) == 'D') {
                boolean success = verifyDoctorLogin(userId, password);
                if (success) {
                    new DoctorController(userId, stage).showDoctorView();
                } else {
                    showAlert("Incorrect ID or password!");
                }
            } else if (userId.charAt(0) == 'P') {
                boolean success = verifyPatientLogin(userId, password);
                if (success) {
                    new PatientController(userId, stage).showPatientView();
                } else {
                    showAlert("Incorrect ID or password!");
                }
            } else {
                showAlert("Invalid user ID format!");
            }
        } catch (Exception ex) {
            showAlert("Error during login: " + ex.getMessage());
        }
    }

    private void showSignUpView(Stage stage) {
        SignUpController signUpController = new SignUpController(this);
        signUpController.showSignUpView(stage, loginView.getLoginScene(stage));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean verifyDoctorLogin(String doctorId, String password) {
        String url = BASE_URL + "/doctors/login";

        String formParams = "doctorId=" + URLEncoder.encode(doctorId, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("POST", url, formParams);

        return response != null && response.trim().equalsIgnoreCase("Login successful");
    }

    public boolean verifyPatientLogin(String patientId, String password) {
        String url = BASE_URL + "/patients/login";

        String formParams = "patientId=" + URLEncoder.encode(patientId, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        String response = ApiClient.executeAPI("POST", url, formParams);

        return response != null && response.trim().equalsIgnoreCase("Login successful");
    }


}
