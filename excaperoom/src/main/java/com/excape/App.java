package com.excape;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/AveriaSerifLibre-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/AveriaSerifLibre-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/AveriaSerifLibre-BoldItalic.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/AveriaSerifLibre-Italic.ttf"), 12);

        scene = new Scene(loadFXML("landing"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}