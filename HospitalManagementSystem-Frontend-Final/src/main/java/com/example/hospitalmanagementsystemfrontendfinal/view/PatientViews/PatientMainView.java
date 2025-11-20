package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PatientMainView {
    private BorderPane root;
    private PatientController controller;
    private String patientId;
    private boolean menuOpen = false;
    private Button menuBtn;
    private HBox userHeader;

    public PatientMainView(PatientController controller, String patientId) {
        this.controller = controller;
        this.patientId = patientId;
        this.root = new BorderPane();
        initializeUI();
    }

    private void initializeUI() {
        PatientMenuView menuView = new PatientMenuView(controller);
        PatientDashboardView dashboardView = new PatientDashboardView(controller);

        menuView.getView().setTranslateX(220);
        menuView.getView().setStyle("-fx-background-color: #2c3e50; -fx-control-inner-background: #2c3e50;");
        root.setRight(menuView.getView());

        menuBtn = new Button("☰");
        menuBtn.setStyle("-fx-font-size: 20px; -fx-background-color: #3498db; -fx-text-fill: white;");
        menuBtn.setOnAction(e -> toggleMenu(menuView.getView()));

        this.userHeader = createUserHeader();
        root.setTop(userHeader);
        root.setCenter(dashboardView.getView());
    }

    public void updateUserHeader() {
        String[] patientInfo = controller.getPatientInfo();
        userHeader.getChildren().clear();

        ImageView photo = new PatientProfileView(controller).createProfileImageView(
                patientId,
                patientInfo[3],
                "patients"
        );

        VBox credentials = new VBox();
        Text usernameTxt = new Text(patientInfo[0]);
        Text phoneTxt = new Text(patientInfo[1]);
        Text emailTxt = new Text(patientInfo[2]);

        usernameTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        phoneTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        emailTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");

        credentials.getChildren().addAll(usernameTxt, phoneTxt, emailTxt);
        credentials.setSpacing(2);
        credentials.setPadding(new Insets(10, 10, 10, 10));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        userHeader.getChildren().addAll(photo, credentials, spacer, menuBtn);
    }

    private void toggleMenu(VBox menu) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), menu);
        transition.setToX(menuOpen ? 220 : 0);
        menuOpen = !menuOpen;
        transition.play();
    }

    private HBox createUserHeader() {
        HBox user = new HBox();
        VBox credentials = new VBox();

        String[] patientInfo = controller.getPatientInfo();
        ImageView photo = new PatientProfileView(controller).createProfileImageView(
                patientId,
                patientInfo[3],
                "patients"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        user.getChildren().addAll(photo, credentials, spacer, menuBtn);
        user.setBackground(new Background(new BackgroundFill(Color.web("#B7C9E2"), null, null)));

        Text usernameTxt = new Text(patientInfo[0]);
        Text phoneTxt = new Text(patientInfo[1]);
        Text emailTxt = new Text(patientInfo[2]);

        usernameTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        phoneTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        emailTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");

        credentials.getChildren().addAll(usernameTxt, phoneTxt, emailTxt);
        credentials.setSpacing(2);
        credentials.setPadding(new Insets(10, 10, 10, 10));

        return user;
    }

    public BorderPane getView() {
        return root;
    }
}
