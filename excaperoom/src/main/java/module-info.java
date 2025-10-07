module com.excape {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    //requires hamcrest.core;

    opens com.excape to javafx.fxml;
    exports com.excape;
}
