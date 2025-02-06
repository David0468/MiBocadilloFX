package org.example.mibocadillofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MiBocataLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));

        // Crear la escena
        Scene scene = new Scene(root, 600, 400);

        // Cargar el archivo CSS
        scene.getStylesheets().add(getClass().getResource("css/login.css").toExternalForm());

        // Configurar el escenario
        primaryStage.setTitle("Mi Bocata - Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
