package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class PatientDashboardView {
    private VBox view;
    private PatientController controller;

    public PatientDashboardView(PatientController controller) {
        this.controller = controller;
        this.view = new VBox();
        initializeView();
    }

    private void initializeView() {
        Label title = new Label("Today's Appointments:");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px;");

        TableView<Appointment> tableView = new TableView<>();
        TableColumn<Appointment, LocalDate> dateColumn = createColumn("Date", "date");
        TableColumn<Appointment, String> dayColumn = createColumn("Day", "dayName");
        TableColumn<Appointment, String> timeColumn = createColumn("Time", "time");
        TableColumn<Appointment, String> doctorColumn = createColumn("Doctor", "name");
        TableColumn<Appointment, Integer> departmentColumn = createColumn("Department", "ageOrDepartment");

        tableView.getColumns().addAll(dateColumn, dayColumn, timeColumn, doctorColumn, departmentColumn);

        List<Appointment> appointments = controller.getDailyAppointments();
        if (appointments.isEmpty()) {
            Label noAppointmentsLabel = new Label("No appointments today!");
            noAppointmentsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            view.getChildren().addAll(title, noAppointmentsLabel);
            view.setStyle("-fx-padding: 20px 0 0 20px;");
        } else {
            tableView.setItems(FXCollections.observableArrayList(appointments));
            view.getChildren().addAll(title, tableView);
        }
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
