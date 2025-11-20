package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class PatientHistoryView {
    private VBox view;
    private PatientController controller;

    public PatientHistoryView(PatientController controller) {
        this.controller = controller;
        this.view = new VBox(10);
        initializeView();
    }

    private void initializeView() {
        view.setStyle("-fx-padding: 10px;");

        Label title = new Label("My Medical History");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 10px 0;");

        Label prescriptionsTitle = new Label("Prescriptions History");
        prescriptionsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");

        VBox prescriptionsBox = new VBox(5);
        prescriptionsBox.setStyle("-fx-padding: 0 0 0 15px;");

        Map<String, List<String>> prescriptions = controller.getAllPrescriptions();

        if (prescriptions.isEmpty()) {
            Label noPrescriptionsLabel = new Label("No prescription history available");
            noPrescriptionsLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
            prescriptionsBox.getChildren().add(noPrescriptionsLabel);
        } else {
            for (Map.Entry<String, List<String>> entry : prescriptions.entrySet()) {
                Label dateLabel = new Label(entry.getKey() + ":");
                dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

                VBox medsBox = new VBox(3);
                for (String medication : entry.getValue()) {
                    Label medLabel = new Label("   - " + medication);
                    medLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
                    medsBox.getChildren().add(medLabel);
                }

                if (prescriptionsBox.getChildren().size() > 0) {
                    Separator separator = new Separator();
                    separator.setPadding(new Insets(5, 0, 5, 0));
                    prescriptionsBox.getChildren().add(separator);
                }

                prescriptionsBox.getChildren().addAll(dateLabel, medsBox);
            }
        }

        Label appointmentsTitle = new Label("Appointment History");
        appointmentsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15px 0 5px 0;");

        VBox appointmentsBox = new VBox(5);
        appointmentsBox.setStyle("-fx-padding: 0 0 0 15px;");

        List<Appointment> appointments = controller.getAllPreviousAppointments();

        if (appointments.isEmpty()) {
            Label noAppointmentsLabel = new Label("No previous appointments found");
            noAppointmentsLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
            appointmentsBox.getChildren().add(noAppointmentsLabel);
        } else {
            for (Appointment appointment : appointments) {
                String line = String.format(
                        "%s %s - %s, %s",
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getName(),
                        appointment.getAgeOrDepartment()
                );

                Label appointmentLabel = new Label(line);
                appointmentLabel.setStyle("-fx-font-size: 12px;");
                appointmentsBox.getChildren().add(appointmentLabel);
            }
        }

        view.getChildren().addAll(
                title,
                prescriptionsTitle,
                prescriptionsBox,
                appointmentsTitle,
                appointmentsBox
        );
    }

    public VBox getView() {
        return view;
    }
}
