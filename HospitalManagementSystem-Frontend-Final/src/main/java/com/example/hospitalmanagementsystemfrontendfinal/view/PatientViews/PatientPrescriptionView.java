package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class PatientPrescriptionView {
    private VBox view;
    private PatientController controller;

    public PatientPrescriptionView(PatientController controller) {
        this.controller = controller;
        this.view = new VBox();
        initializeView();
    }

    private void initializeView() {
        view.setPadding(new Insets(10));

        Label title = new Label("Prescriptions:");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        view.getChildren().add(title);

        Map<String, List<String>> prescriptions = controller.getAllPrescriptions();

        if (prescriptions.isEmpty()) {
            Label noPrescriptionsLabel = new Label("No prescriptions yet");
            noPrescriptionsLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
            view.getChildren().add(noPrescriptionsLabel);
        } else {
            for (Map.Entry<String, List<String>> entry : prescriptions.entrySet()) {
                Label dateLabel = new Label(entry.getKey() + " :");
                dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #005eff; -fx-font-weight: bold;");

                VBox medsBox = new VBox(3);
                for (String medication : entry.getValue()) {
                    Label medLabel = new Label("   - " + medication);
                    medLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
                    medsBox.getChildren().add(medLabel);
                }

                VBox entryBox = new VBox(5, dateLabel, medsBox);
                entryBox.setPadding(new Insets(5, 10, 5, 10));
                entryBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

                view.getChildren().add(entryBox);
            }
        }
    }

    public VBox getView() {
        return view;
    }
}
