package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DoctorDailyScheduleView {
    private final DoctorController controller;
    private VBox view;

    public DoctorDailyScheduleView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new VBox(10);
        view.getChildren().add(createTitleLabel());

        ObservableList<Appointment> appointments = controller.getDailyAppointments();

        if (appointments.isEmpty()) {
            view.getChildren().add(createNoAppointmentsLabel());
            view.setStyle("-fx-padding: 20px 0 0 20px;");
        } else {
            view.getChildren().add(createAppointmentsTable(appointments));
        }
    }

    private Label createTitleLabel() {
        Label title = new Label("Today's Appointments:");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px;");
        return title;
    }

    private Label createNoAppointmentsLabel() {
        Label noAppointmentsLabel = new Label("No appointments today!");
        noAppointmentsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return noAppointmentsLabel;
    }

    private TableView<Appointment> createAppointmentsTable(ObservableList<Appointment> appointments) {
        TableView<Appointment> tableView = new TableView<>();

        TableColumn<Appointment, LocalDate> dateColumn = createColumn("Date", "date");
        TableColumn<Appointment, String> dayColumn = createColumn("Day", "dayName");
        TableColumn<Appointment, String> timeColumn = createColumn("Time", "time");
        TableColumn<Appointment, String> patientColumn = createColumn("Patient", "name");
        TableColumn<Appointment, Integer> ageColumn = createColumn("Age", "ageOrDepartment");

        tableView.getColumns().addAll(dateColumn, dayColumn, timeColumn, patientColumn, ageColumn);
        tableView.setItems(appointments);

        return tableView;
    }

    private <T> TableColumn<Appointment, T> createColumn(String title, String property) {
        TableColumn<Appointment, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    public VBox getView() {
        return view;
    }
}
