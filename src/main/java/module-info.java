module org.example.briefi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.briefi to javafx.fxml;
    exports org.example.briefi;

    exports org.example.briefi.interfaces;
    opens org.example.briefi.interfaces to javafx.fxml;

    opens org.example.briefi.models to javafx.fxml;
    exports org.example.briefi.models;

    opens org.example.briefi.controllers to javafx.fxml;
    exports org.example.briefi.controllers;
}