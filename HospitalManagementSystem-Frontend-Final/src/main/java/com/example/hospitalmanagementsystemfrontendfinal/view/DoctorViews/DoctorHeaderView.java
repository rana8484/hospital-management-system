package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DoctorHeaderView {
    private final DoctorController controller;
    private HBox view;

    public DoctorHeaderView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new HBox();
        VBox credentials = new VBox();

        String[] userInfo = controller.getUserInfo();
        ImageView photo = controller.getProfileImage(controller.getDrId(), userInfo[3],"doctors");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        view.getChildren().addAll(photo, credentials, spacer, controller.getMenuBtn());
        view.setBackground(new Background(new BackgroundFill(Color.web("#B7C9E2"), null, null)));

        Text usernameTxt = new Text(userInfo[0]);
        usernameTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        Text phoneTxt = new Text(userInfo[2]);
        phoneTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        Text emailTxt = new Text(userInfo[1]);
        emailTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");
        Text departmentTxt = new Text(userInfo[4]);
        departmentTxt.setStyle("-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");

        credentials.getChildren().addAll(usernameTxt, phoneTxt, emailTxt, departmentTxt);
        credentials.setSpacing(2);
        credentials.setPadding(new Insets(10, 10, 10, 10));
    }

    public HBox getView() {
        return view;
    }
}

