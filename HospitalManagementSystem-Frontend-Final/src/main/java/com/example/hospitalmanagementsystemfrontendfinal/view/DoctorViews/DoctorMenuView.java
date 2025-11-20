package com.example.hospitalmanagementsystemfrontendfinal.view.DoctorViews;

import com.example.hospitalmanagementsystemfrontendfinal.controller.DoctorController;
import com.example.hospitalmanagementsystemfrontendfinal.model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class DoctorMenuView {
    private final DoctorController controller;
    private VBox view;

    public DoctorMenuView(DoctorController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        view = new VBox();
        view.setPadding(new Insets(15));
        view.setStyle("-fx-background-color: #2c3e50;");
        view.setPrefWidth(220);

        ListView<MenuItem> listView = createMenuListView();
        view.getChildren().add(listView);
    }

    private ListView<MenuItem> createMenuListView() {
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(
                new MenuItem("Profile", "/profile.png"),
                new MenuItem("Dashboard", "/dashboard.png"),
                new MenuItem("Schedule", "/schedule.png"),
                new MenuItem("Patient List", "/ptlist.png"),
                new MenuItem("Requested Appointments", "/requested.png"),
                new MenuItem("Logout", "/logout.png")
        );

        ListView<MenuItem> listView = new ListView<>(menuItems);
        listView.setCellFactory(this::createMenuCell);
        listView.setStyle("-fx-border-color: #2c3e50; -fx-background-color: #2c3e50; -fx-control-inner-background: #2c3e50;");
        listView.setOnMouseClicked(this::handleMenuSelection);

        return listView;
    }

    private ListCell<MenuItem> createMenuCell(ListView<MenuItem> param) {
        return new ListCell<>() {
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
        };
    }

    private void handleMenuSelection(MouseEvent event) {
        MenuItem selected = ((ListView<MenuItem>) event.getSource()).getSelectionModel().getSelectedItem();
        if (selected != null) {
            controller.handleMenuSelection(selected.getText());
        }
    }

    public VBox getView() {
        return view;
    }
}
