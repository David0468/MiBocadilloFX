package org.example.mibocadillofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Se carga el FXML de login desde el directorio de recursos
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // Cargar el archivo CSS
        scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());
        stage.setTitle("Mi Bocata - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
