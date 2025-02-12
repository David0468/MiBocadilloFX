package org.example.mibocadillofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SeleccionBocadillos extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/seleccion_bocadillos.fxml"));
            VBox root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Selección de Bocadillos");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz FXML");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
