module com.excape {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;

    opens com.excape to javafx.fxml;
    exports com.excape;
}
