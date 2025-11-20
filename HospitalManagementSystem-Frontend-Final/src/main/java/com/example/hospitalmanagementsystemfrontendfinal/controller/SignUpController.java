package com.example.hospitalmanagementsystemfrontendfinal.controller;

import com.example.hospitalmanagementsystemfrontendfinal.ApiClient;
import com.example.hospitalmanagementsystemfrontendfinal.view.SignUpView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class SignUpController {
    private SignUpView signUpView;
    private final String BASE_URL = "http://localhost:8080/api";
    LoginController loginController;

    public SignUpController(LoginController loginController) {
        this.signUpView = new SignUpView();
        this.loginController = loginController;
    }

    public void showSignUpView(Stage stage, Scene previousScene) {
        ScrollPane signUpRoot = signUpView.getSignUpView();
        Scene signUpScene = new Scene(signUpRoot, stage.getWidth(), stage.getHeight());

        signUpView.getSignUpButton().setOnAction(e -> handleSignUp(stage));
        signUpView.getBackButton().setOnAction(e -> loginController.showLoginView(stage));

        stage.setScene(signUpScene);
        stage.setMaximized(true);
    }

    private void handleSignUp(Stage stage) {
        String name = signUpView.getNameField().getText();
        String gender = signUpView.getGenderGroup().getSelectedToggle() == null ? "" :
                ((RadioButton)signUpView.getGenderGroup().getSelectedToggle()).getText();
        LocalDate dob = signUpView.getDobPicker().getValue();
        String email = signUpView.getEmailField().getText();
        String phone = signUpView.getPhoneField().getText();
        String password = signUpView.getPasswordField().getText();
        String recheckPassword = signUpView.getPasswordFieldSignUp2().getText();
        RadioButton selectedRole = (RadioButton) signUpView.getRoleGroup().getSelectedToggle();
        String role = selectedRole == null ? "" : selectedRole.getText();
        String department = (role.equals("Doctor")) ? signUpView.getDepartmentComboBox().getValue() : null;

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() ||
                recheckPassword.isEmpty() || dob == null || gender.isEmpty() || role.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email format! Please enter a valid email.");
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            showAlert("Invalid phone number! It must be exactly 8 digits long.");
            return;
        }

        if (!password.equals(recheckPassword)) {
            showAlert("Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showAlert("Password must be at least 6 characters.");
            return;
        }

        if (role.equals("Doctor") && (department == null || department.isEmpty())) {
            showAlert("Please select a department.");
            return;
        }

        try {
            String userId = "";

            if (role.equals("Doctor")) {
                userId = addDoctor(name, password, dob, gender, email, phone, department);
            } else if (role.equals("Patient")) {
                userId = addPatient(name, password, dob, gender, email, phone);
            }

            if (userId != null && !userId.isEmpty()) {
                showAlert("User registered successfully! Your ID is: " + userId);
            } else {
                showAlert("Error while registering user. Email might already be in use.");
            }

        } catch (Exception e) {
            showAlert("Error during registration: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "^[0-9]{8}$";
        return phone.matches(phoneRegex);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String addDoctor(String name, String password, LocalDate dob, String gender, String email, String phone, String departmentName) {
        try {
            String url = BASE_URL + "/doctors/add";
            String urlParameters =
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString()) +
                            "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8.toString()) +
                            "&dob=" + URLEncoder.encode(dob.toString(), StandardCharsets.UTF_8.toString()) +
                            "&gender=" + URLEncoder.encode(gender, StandardCharsets.UTF_8.toString()) +
                            "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()) +
                            "&phone=" + URLEncoder.encode(phone, StandardCharsets.UTF_8.toString()) +
                            "&departmentName=" + URLEncoder.encode(departmentName, StandardCharsets.UTF_8.toString());

            String response = ApiClient.executeAPI("POST", url, urlParameters);

            if (response != null) {
                System.out.println("Doctor added successfully: " + response);
                return response.trim();
            } else {
                System.out.println("Failed to add doctor.");
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addPatient(String name, String password, LocalDate dob, String gender, String email, String phone) {
        try {
            String url =  BASE_URL + "/patients/add";
            String urlParameters =
                    "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString()) +
                            "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8.toString()) +
                            "&dob=" + URLEncoder.encode(dob.toString(), StandardCharsets.UTF_8.toString()) +
                            "&gender=" + URLEncoder.encode(gender, StandardCharsets.UTF_8.toString()) +
                            "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()) +
                            "&phone=" + URLEncoder.encode(phone, StandardCharsets.UTF_8.toString());

            String response = ApiClient.executeAPI("POST",url, urlParameters);

            if (response != null) {
                System.out.println("Patient added successfully: " + response);
                return response.trim();
            } else {
                System.out.println("Failed to add patient.");
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
