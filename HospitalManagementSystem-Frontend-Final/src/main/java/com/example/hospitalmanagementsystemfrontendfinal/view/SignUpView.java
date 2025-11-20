package com.example.hospitalmanagementsystemfrontendfinal.view;

import com.example.hospitalmanagementsystemfrontendfinal.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class SignUpView {
    private ScrollPane signUpRoot;
    private TextField nameField;
    private ToggleGroup genderGroup;
    private DatePicker dobPicker;
    private TextField emailField;
    private TextField phoneField;
    private PasswordField passwordField;
    private PasswordField passwordFieldSignUp2;
    private ToggleGroup roleGroup;
    private ComboBox<String> departmentComboBox;
    private Button signUpButton;
    private Button backButton;
    private final String BASE_URL = "http://localhost:8080/api";

    public ScrollPane getSignUpView() {
        signUpRoot = new ScrollPane();
        signUpRoot.setFitToWidth(true);
        signUpRoot.setFitToHeight(true);

        VBox signUpBox = new VBox(10);
        signUpBox.setStyle("-fx-padding: 20;");
        signUpBox.setAlignment(Pos.CENTER);

        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0b90ad")),
                new Stop(1, Color.web("#89ddef"))
        );

        signUpBox.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(300);

        Label genderLabel = new Label("Gender:");
        genderLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        RadioButton femaleRadio = new RadioButton("Female");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        maleRadio.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        femaleRadio.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        HBox genderBox = new HBox(10, maleRadio, femaleRadio);
        genderBox.setAlignment(Pos.CENTER);

        Label dobLabel = new Label("Date of Birth:");
        dobLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        dobPicker = new DatePicker();
        dobPicker.setStyle("-fx-font-size: 16px;");
        dobPicker.setPromptText("Select your date of birth");

        dobPicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(300);

        Label phoneLabel = new Label("Phone number:");
        phoneLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        phoneField = new TextField();
        phoneField.setPromptText("Enter your number");
        phoneField.setMaxWidth(300);

        Label passwordLabelSignUp = new Label("Password:");
        passwordLabelSignUp.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);

        Label passwordLabelSignUp2 = new Label("Recheck password:");
        passwordLabelSignUp2.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        passwordFieldSignUp2 = new PasswordField();
        passwordFieldSignUp2.setPromptText("Enter password again");
        passwordFieldSignUp2.setMaxWidth(300);

        roleGroup = new ToggleGroup();
        RadioButton patientRadio = new RadioButton("Patient");
        RadioButton doctorRadio = new RadioButton("Doctor");
        patientRadio.setToggleGroup(roleGroup);
        doctorRadio.setToggleGroup(roleGroup);

        patientRadio.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        doctorRadio.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        HBox roleBox = new HBox(10, patientRadio, doctorRadio);
        roleBox.setAlignment(Pos.CENTER);

        Label departmentLabel = new Label("Department:");
        departmentLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        departmentComboBox = new ComboBox<>();
        departmentComboBox.setPromptText("Select Department");
        departmentLabel.setVisible(false);
        departmentComboBox.setVisible(false);

        doctorRadio.setOnAction(e -> {
            departmentLabel.setVisible(true);
            departmentComboBox.setVisible(true);
            List<String> departments = getAllDepartments();
            departmentComboBox.getItems().setAll(departments);
        });

        patientRadio.setOnAction(e -> {
            departmentLabel.setVisible(false);
            departmentComboBox.setVisible(false);
        });

        signUpButton = new Button("Sign Up");
        backButton = new Button("Back to Login");

        signUpBox.getChildren().addAll(nameLabel, nameField, genderLabel, genderBox, dobLabel, dobPicker,
                emailLabel, emailField, phoneLabel, phoneField, passwordLabelSignUp, passwordField,
                passwordLabelSignUp2, passwordFieldSignUp2, roleBox, departmentLabel, departmentComboBox,
                signUpButton, backButton);

        signUpRoot.setContent(signUpBox);
        return signUpRoot;
    }

    public TextField getNameField() { return nameField; }
    public ToggleGroup getGenderGroup() { return genderGroup; }
    public DatePicker getDobPicker() { return dobPicker; }
    public TextField getEmailField() { return emailField; }
    public TextField getPhoneField() { return phoneField; }
    public PasswordField getPasswordField() { return passwordField; }
    public PasswordField getPasswordFieldSignUp2() { return passwordFieldSignUp2; }
    public ToggleGroup getRoleGroup() { return roleGroup; }
    public ComboBox<String> getDepartmentComboBox() { return departmentComboBox; }
    public Button getSignUpButton() { return signUpButton; }
    public Button getBackButton() { return backButton; }

    public List<String> getAllDepartments() {
        String url = BASE_URL + "/departments/names";

        String response = ApiClient.executeAPI("GET", url, "");

        if (response != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

}
