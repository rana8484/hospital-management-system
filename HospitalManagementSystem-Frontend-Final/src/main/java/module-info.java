module com.example.hospitalmanagementsystemfrontendfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.json;

    // For JavaFX FXMLLoader
    opens com.example.hospitalmanagementsystemfrontendfinal to javafx.fxml;

    // For both JavaFX TableView and Jackson
    opens com.example.hospitalmanagementsystemfrontendfinal.model to javafx.base, com.fasterxml.jackson.databind;

    exports com.example.hospitalmanagementsystemfrontendfinal;
    exports com.example.hospitalmanagementsystemfrontendfinal.model;
}



//module com.example.hospitalmanagementsystemfrontendfinal {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.net.http;
//    requires com.fasterxml.jackson.databind;
//    requires java.desktop;
//
//
//    opens com.example.hospitalmanagementsystemfrontendfinal to javafx.fxml;
//    exports com.example.hospitalmanagementsystemfrontendfinal;
//}

//module com.example.hospitalmanagementsystemfrontendfinal {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.net.http;
//    requires com.fasterxml.jackson.databind;
//    requires java.desktop;
//
//    // This is for FXMLLoader (e.g., controllers)
//    opens com.example.hospitalmanagementsystemfrontendfinal to javafx.fxml;
//
//    // 👇 This line is crucial for PropertyValueFactory/TableView
//    opens com.example.hospitalmanagementsystemfrontendfinal.model to javafx.base;
//
//    // Export if needed by other modules (optional for internal use)
//    exports com.example.hospitalmanagementsystemfrontendfinal;
//    exports com.example.hospitalmanagementsystemfrontendfinal.model;
//}
