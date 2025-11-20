package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PatientDoctorListView {
    private ListView<HBox> view;
    private PatientController controller;

    public PatientDoctorListView(PatientController controller) {
        this.controller = controller;
        this.view = new ListView<>();
        initializeView();
    }

    private void initializeView() {
        ObservableList<HBox> items = FXCollections.observableArrayList();
        List<Doctor> doctors = controller.getAllDoctors();

        for (Doctor doctor : doctors) {
            ImageView profileImage = new PatientProfileView(controller)
                    .createProfileImageView(doctor.getDoctorId(), doctor.getGender(), "doctors");

            VBox textContainer = new VBox();
            Button bookApptBtn = new Button("Book Appointment");
            bookApptBtn.setOnAction(e -> controller.showAppointmentBookingView(doctor));

            textContainer.getChildren().addAll(
                    new Label("Name: " + doctor.getName()),
                    new Label("Email: " + doctor.getEmail()),
                    new Label("Phone: " + doctor.getPhoneNumber()),
                    new Label("Department: " + doctor.getDepartment()),
                    bookApptBtn
            );

            HBox itemBox = new HBox(10, profileImage, textContainer);
            items.add(itemBox);
        }

        view.setItems(items);
    }

    public ListView<HBox> getView() {
        return view;
    }
}
