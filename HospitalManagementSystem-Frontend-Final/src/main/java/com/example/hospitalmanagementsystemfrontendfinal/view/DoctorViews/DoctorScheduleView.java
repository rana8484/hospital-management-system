package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DoctorScheduleView {
    private final DoctorController controller;
    private TableView<Appointment> view;

    public DoctorScheduleView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new TableView<>();
        setupTableColumns();
        loadAppointments();
    }

    private void setupTableColumns() {
        TableColumn<Appointment, String> dateColumn = createColumn("Date", "date");
        TableColumn<Appointment, String> dayColumn = createColumn("Day", "dayName");
        TableColumn<Appointment, String> timeColumn = createColumn("Time", "time");
        TableColumn<Appointment, String> patientColumn = createColumn("Patient", "name");
        TableColumn<Appointment, Integer> ageColumn = createColumn("Age", "ageOrDepartment");

        TableColumn<Appointment, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> createActionCell());

        view.getColumns().addAll(dateColumn, dayColumn, timeColumn, patientColumn, ageColumn, actionColumn);
    }

    private <T> TableColumn<Appointment, T> createColumn(String title, String property) {
        TableColumn<Appointment, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableCell<Appointment, Void> createActionCell() {
        return new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                controller.animateButton(deleteBtn);
                deleteBtn.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    controller.confirmAndDeleteAppointment(appointment);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        };
    }

    private void loadAppointments() {
        view.setItems(controller.getDoctorAppointments());
    }

    public TableView<Appointment> getView() {
        return view;
    }
}