module com.excape {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires freetts;
    requires junit;
    requires javafx.media;
    // requires hamcrest.core; // leave commented if it caused problems compiling

    // You already had this: JavaFX needs reflection access to your UI/controller package.
    opens com.excape to javafx.fxml;

    // NEW: make your game model package visible outside the module
    exports com.model;
    // Let JUnit reflectively call @Test methods and access classes in com.model
    opens com.model to junit;

    // Keep exporting your UI / app package if you need it publicly
    exports com.excape;
}
