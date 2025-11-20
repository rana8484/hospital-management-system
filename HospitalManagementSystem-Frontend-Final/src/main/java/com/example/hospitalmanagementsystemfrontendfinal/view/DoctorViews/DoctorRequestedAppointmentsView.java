package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;

public class DoctorRequestedAppointmentsView {
    private final DoctorController controller;
    private ListView<VBox> view;

    public DoctorRequestedAppointmentsView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new ListView<>();
        view.setItems(createAppointmentItems());
    }

    private ObservableList<VBox> createAppointmentItems() {
        ObservableList<VBox> items = FXCollections.observableArrayList();

        for (Appointment appt : controller.getRequestedAppointments()) {
            items.add(createAppointmentItem(appt));
        }

        return items;
    }

    private VBox createAppointmentItem(Appointment appt) {
        VBox vbox = new VBox(10);
        vbox.getChildren().add(createAppointmentDetailsLabel(appt));
        vbox.getChildren().add(createActionButtons(appt, vbox));
        return vbox;
    }

    private Label createAppointmentDetailsLabel(Appointment appt) {
        String details = String.format("Appointment:\nDate: %s\nTime: %s\nPatient: %s",
                appt.getDate(), appt.getTime(), appt.getName());
        return new Label(details);
    }

    private HBox createActionButtons(Appointment appt, VBox container) {
        HBox buttonBox = new HBox(10);

        Button acceptBtn = createAcceptButton(appt, container);
        Button rejectBtn = createRejectButton(appt, container);

        buttonBox.getChildren().addAll(acceptBtn, rejectBtn);
        return buttonBox;
    }

    private Button createAcceptButton(Appointment appt, VBox container) {
        Button btn = new Button("Accept");
        btn.setStyle("-fx-background-color: #006400; -fx-text-fill: white; -fx-font-weight: bold;");
        controller.animateButton(btn);

        btn.setOnAction(e -> {
            if (controller.acceptAppointment(appt.getApptId())) {
                view.getItems().remove(container);
            }
        });

        return btn;
    }

    private Button createRejectButton(Appointment appt, VBox container) {
        Button btn = new Button("Reject");
        btn.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-weight: bold;");
        controller.animateButton(btn);

        btn.setOnAction(e -> {
            if (controller.rejectAppointment(appt.getApptId())) {
                view.getItems().remove(container);
            }
        });

        return btn;
    }

    public ListView<VBox> getView() {
        return view;
    }
}