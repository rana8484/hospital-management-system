package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DoctorMainView {
    private final DoctorController controller;
    private BorderPane root;
    private VBox sidebar;

    public DoctorMainView(DoctorController controller) {
        this.controller = controller;
        this.root = controller.getRoot();
        initialize();
    }

    private void initialize() {
        initializeSidebar();

        initializeMenuButton();

        initializeHeader();

        initializeContent();
    }

    private void initializeSidebar() {
        sidebar = new DoctorMenuView(controller).getView();
        sidebar.setTranslateX(220);
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        root.setRight(sidebar);
    }

    private void initializeMenuButton() {
        Button menuBtn = controller.getMenuBtn();
        menuBtn.setStyle("-fx-font-size: 20px; -fx-background-color: #3498db; -fx-text-fill: white;");
        menuBtn.setOnAction(e -> controller.toggleMenu(sidebar));
    }

    private void initializeHeader() {
        HBox userHeader = new DoctorHeaderView(controller).getView();
        root.setTop(userHeader);
    }

    private void initializeContent() {
        root.setCenter(new DoctorDailyScheduleView(controller).getView());
    }

    public BorderPane getView() {
        return root;
    }
}

