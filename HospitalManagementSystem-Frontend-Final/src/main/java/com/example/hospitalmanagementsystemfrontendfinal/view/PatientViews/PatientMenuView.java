package com.example.hospitalmanagementsystemfrontendfinal.view.PatientViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.PatientController;
import com.example.hospitalmanagementsystemfrontendfinal.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PatientMenuView {
    private VBox view;
    private PatientController controller;

    public PatientMenuView(PatientController controller) {
        this.controller = controller;
        this.view = new VBox();
        initializeView();
    }

    private void initializeView() {
        view.setPadding(new Insets(15));
        view.setStyle("-fx-background-color: #2c3e50;");
        view.setPrefWidth(220);

        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(
                new MenuItem("Profile", "/profile.png"),
                new MenuItem("Dashboard", "/dashboard.png"),
                new MenuItem("Schedule", "/schedule.png"),
                new MenuItem("Doctor List", "/drlist.png"),
                new MenuItem("Prescriptions", "/prescription.png"),
                new MenuItem("History", "/history.png"),
                new MenuItem("Logout", "/logout.png")
        );

        ListView<MenuItem> listView = new ListView<>(menuItems);
        listView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(getClass().getResourceAsStream(item.getIconPath())));
                    imageView.setFitWidth(24);
                    imageView.setFitHeight(24);
                    setText(item.getText());
                    setGraphic(imageView);
                    setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
                }
            }
        });

        listView.setStyle("-fx-border-color: #2c3e50;");

        listView.setOnMouseClicked(event -> {
            MenuItem selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                switch (selected.getText()) {
                    case "Profile":
                        controller.showProfileView();
                        break;
                    case "Dashboard":
                        controller.showDashboard();
                        break;
                    case "Schedule":
                        controller.showScheduleView();
                        break;
                    case "Doctor List":
                        controller.showDoctorListView();
                        break;
                    case "Prescriptions":
                        controller.showPrescriptionView();
                        break;
                    case "History":
                        controller.showHistoryView();
                        break;
                    case "Logout":
                        controller.logout();
                        break;
                }
            }
        });

        view.getChildren().add(listView);
    }

    public VBox getView() {
        return view;
    }
}
