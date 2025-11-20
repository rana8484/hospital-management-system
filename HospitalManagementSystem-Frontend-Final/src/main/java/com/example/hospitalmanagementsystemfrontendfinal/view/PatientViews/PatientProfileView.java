package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;

public class PatientProfileView {
    private ScrollPane view;
    private PatientController controller;
    private ImageView profileImage;
    private TextField emailField;
    private TextField phoneField;
    private PasswordField oldPasswordField;
    private PasswordField newPasswordField;

    public PatientProfileView(PatientController controller) {
        this.controller = controller;
        this.view = new ScrollPane();
        initializeView();
    }

    private void initializeView() {
        VBox profileLayout = new VBox(15);
        profileLayout.setAlignment(Pos.CENTER_LEFT);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setPrefWidth(400);

        Label title = new Label("Edit Profile");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        String[] patientInfo = controller.getPatientInfo();
        profileImage = createProfileImageView(
                controller.getPtId(), patientInfo[3], "patients"
        );
        profileImage.setFitWidth(100);
        profileImage.setFitHeight(100);

        Button changePicBtn = new Button("Edit photo");
        changePicBtn.setOnAction(e -> controller.handleChangePhoto(profileImage));

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setText(patientInfo[1]);

        Label phoneLabel = new Label("Phone Number:");
        phoneField = new TextField();
        phoneField.setText(patientInfo[2]);

        Label oldPassLabel = new Label("Old Password:");
        oldPasswordField = new PasswordField();

        Label newPassLabel = new Label("New Password:");
        newPasswordField = new PasswordField();

        Button updateProfileBtn = new Button("Update Profile");
        updateProfileBtn.setOnAction(e ->
                controller.handleUpdateProfile(emailField.getText(), phoneField.getText()));

        Button updatePasswordBtn = new Button("Update Password");
        updatePasswordBtn.setOnAction(e ->
                controller.handleUpdatePassword(oldPasswordField.getText(), newPasswordField.getText()));

        profileLayout.getChildren().addAll(
                title, profileImage, changePicBtn,
                emailLabel, emailField,
                phoneLabel, phoneField,
                oldPassLabel, oldPasswordField,
                newPassLabel, newPasswordField,
                updateProfileBtn, updatePasswordBtn
        );

        view.setContent(profileLayout);
        view.setFitToWidth(true);
        view.setPadding(new Insets(10));
    }

    public void clearPasswordFields() {
        Platform.runLater(() -> {
            oldPasswordField.clear();
            newPasswordField.clear();
        });
    }

    public ImageView createProfileImageView(String userId, String gender, String table) {
        byte[] imageData = new byte[0];
        if (table.equals("patients")) {
            imageData = controller.getPatientProfilePhoto(userId);
        } else if (table.equals("doctors")) {
            imageData = controller.getDoctorProfilePhoto(userId);
        }
        ImageView imageView;

        if (imageData != null) {
            imageView = new ImageView(new Image(new ByteArrayInputStream(imageData)));
        } else {
            String defaultImagePath = gender.equalsIgnoreCase("Female") ? "/female.png" : "/male.png";
            imageView = new ImageView(defaultImagePath);
        }
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setStyle("-fx-margin: 20px;");

        return imageView;
    }

    public ScrollPane getView() {
        return view;
    }
}