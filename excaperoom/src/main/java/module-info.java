module com.excape {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.excape to javafx.fxml;
    exports com.excape;
}
