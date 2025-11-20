package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class DoctorPatientListView {
    private final DoctorController controller;
    private ListView<HBox> view;

    public DoctorPatientListView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new ListView<>();
        view.setItems(createPatientItems());
    }

    private ObservableList<HBox> createPatientItems() {
        ObservableList<HBox> items = FXCollections.observableArrayList();
        List<Patient> patients = controller.getPatients();

        for (Patient patient : patients) {
            items.add(createPatientItem(patient));
        }

        return items;
    }

    private HBox createPatientItem(Patient patient) {
        ImageView profileImage = controller.getProfileImage(patient.getPatientId(), patient.getGender(), "patients");
        VBox textContainer = createPatientInfoContainer(patient);
        return new HBox(10, profileImage, textContainer);
    }

    private VBox createPatientInfoContainer(Patient patient) {
        VBox container = new VBox();

        container.getChildren().addAll(
                new Label("Name: " + patient.getName()),
                new Label("Email: " + patient.getEmail()),
                new Label("Phone: " + patient.getPhoneNumber()),
                new Label("Age: " + Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears())
        );

        Button noteBtn = createNoteButton(patient);
        Button prescriptionBtn = createPrescriptionButton(patient);
        Button historyBtn = createHistoryButton(patient);

        container.getChildren().addAll(noteBtn, prescriptionBtn, historyBtn);
        setButtonMargins(container);

        return container;
    }

    private Button createNoteButton(Patient patient) {
        Button button = new Button("Add Note");
        button.setOnAction(e -> controller.showNoteDialog(patient));
        return button;
    }

    private Button createPrescriptionButton(Patient patient) {
        Button button = new Button("Add Prescription");
        button.setOnAction(e -> controller.showPrescriptionDialog(patient));
        return button;
    }

    private Button createHistoryButton(Patient patient) {
        Button button = new Button("View History");
        button.setOnAction(e -> controller.showPatientHistory(patient));
        return button;
    }

    private void setButtonMargins(VBox container) {
        VBox.setMargin(container.getChildren().get(4), new Insets(4, 0, 0, 0));
        VBox.setMargin(container.getChildren().get(5), new Insets(4, 0, 0, 0));
        VBox.setMargin(container.getChildren().get(6), new Insets(4, 0, 0, 0));
    }

    public ListView<HBox> getView() {
        return view;
    }
}