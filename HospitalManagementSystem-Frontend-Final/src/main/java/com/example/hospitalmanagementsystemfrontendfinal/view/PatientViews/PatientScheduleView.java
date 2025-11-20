package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import com.example.hospitalmanagementsystemfrontendfinal.model.Appointment;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.time.LocalDate;

public class PatientScheduleView {
    private TableView<Appointment> view;
    private PatientController controller;

    public PatientScheduleView(PatientController controller) {
        this.controller = controller;
        this.view = new TableView<>();
        initializeView();
    }

    private void initializeView() {


        TableColumn<Appointment, LocalDate> dateColumn = createColumn("Date", "date");
        TableColumn<Appointment, String> dayColumn = createColumn("Day", "dayName");
        TableColumn<Appointment, String> timeColumn = createColumn("Time", "time");
        TableColumn<Appointment, String> doctorColumn = createColumn("Doctor", "name");
        TableColumn<Appointment, Integer> departmentColumn = createColumn("Department", "ageOrDepartment");
        TableColumn<Appointment, Void> actionColumn = new TableColumn<>("Action");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                animateButton(deleteBtn);
                deleteBtn.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    controller.confirmAndDeleteAppointment(appointment);
                });
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });


        view.getColumns().addAll(dateColumn, dayColumn, timeColumn, doctorColumn, departmentColumn, actionColumn);
        view.setItems(FXCollections.observableArrayList(controller.getAppointments()));
    }

    private <T> TableColumn<Appointment, T> createColumn(String title, String property) {
        TableColumn<Appointment, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private void animateButton(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    public TableView<Appointment> getView() {
        return view;
    }
}
