package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DoctorProfileView {
    private final DoctorController controller;
    private ScrollPane view;

    public DoctorProfileView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        VBox profileLayout = new VBox(15);
        profileLayout.setAlignment(Pos.CENTER_LEFT);
        profileLayout.setPadding(new Insets(20));
        profileLayout.setPrefWidth(400);

        Label title = new Label("Edit Profile");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ImageView profileImage = new ImageView();
        profileImage.setFitWidth(100);
        profileImage.setFitHeight(100);

        Button changePicBtn = new Button("Edit photo");
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        PasswordField oldPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();

        String[] userInfo = controller.getUserInfo();
        if (userInfo != null) {
            emailField.setText(userInfo[1]);
            phoneField.setText(userInfo[2]);
        }

        profileImage = controller.getProfileImage(controller.getDrId(), userInfo[3], "doctors");

        ImageView finalProfileImage = profileImage;
        changePicBtn.setOnAction(e -> handleImageChange(finalProfileImage, emailField, phoneField));
        Button updateProfileBtn = createUpdateProfileBtn(emailField, phoneField);
        Button updatePasswordBtn = createUpdatePasswordBtn(oldPasswordField, newPasswordField);

        profileLayout.getChildren().addAll(
                title, profileImage, changePicBtn,
                new Label("Email:"), emailField,
                new Label("Phone Number:"), phoneField,
                new Label("Old Password:"), oldPasswordField,
                new Label("New Password:"), newPasswordField,
                updateProfileBtn, updatePasswordBtn
        );

        view = new ScrollPane(profileLayout);
        view.setFitToWidth(true);
        view.setPadding(new Insets(10));
    }

    private byte[] imageData = null;

    private void handleImageChange(ImageView profileImage, TextField emailField, TextField phoneField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                imageData = Files.readAllBytes(file.toPath());
                profileImage.setImage(new Image(new ByteArrayInputStream(imageData)));
                controller.updateProfile(
                        emailField.getText(),
                        phoneField.getText(),
                        imageData
                );
            } catch (IOException ex) {
                controller.showAlert("Error loading image: " + ex.getMessage());
            }
        }
    }

    private Button createUpdateProfileBtn(TextField emailField, TextField phoneField) {
        Button btn = new Button("Update Profile");
        btn.setOnAction(e -> {
            if (!controller.isValidEmail(emailField.getText())) {
                controller.showAlert("Invalid email format!");
                return;
            }

            if (!controller.isValidPhoneNumber(phoneField.getText())) {
                controller.showAlert("Phone number must be 8 digits!");
                return;
            }

            if (controller.updateProfile(emailField.getText(), phoneField.getText(), imageData)) {
                controller.showAlert("Profile updated!");
                controller.updateHeaderView();
            } else {
                controller.showAlert("Profile update failed!");
            }
        });
        return btn;
    }

    private Button createUpdatePasswordBtn(PasswordField oldPassField, PasswordField newPassField) {
        Button btn = new Button("Update Password");
        btn.setOnAction(e -> {
            if (controller.updateDrPassword(
                    oldPassField.getText(),
                    newPassField.getText()
            )) {
                controller.showAlert("Password changed!");
            } else {
                controller.showAlert("Incorrect old password!");
            }
        });
        return btn;
    }

    public ScrollPane getView() {
        return view;
    }
}
