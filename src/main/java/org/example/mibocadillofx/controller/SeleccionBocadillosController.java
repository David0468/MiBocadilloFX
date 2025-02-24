package org.example.mibocadillofx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.model.Pedido;
import org.example.mibocadillofx.service.BocadilloService;
import org.example.mibocadillofx.service.PedidoService;
import org.example.mibocadillofx.util.SessionManager;

import java.io.IOException;

public class SeleccionBocadillosController {

    @FXML
    private Text bocadilloFrioName;
    @FXML
    private Text bocadilloFrioIngredients;
    @FXML
    private Text bocadilloFrioAllergens;

    @FXML
    private Text bocadilloCalienteName;
    @FXML
    private Text bocadilloCalienteIngredients;
    @FXML
    private Text bocadilloCalienteAllergens;

    @FXML
    private Button btnBocadilloFrio;
    @FXML
    private Button btnBocadilloCaliente;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCerrarSesion;

    private BocadilloService bocadilloService;
    private PedidoService pedidoService;

    // Identificador del alumno (clave primaria de la tabla 'alumnos', es decir, el nombre)
    private String currentAlumnoId;

    @FXML
    public void initialize() {
        bocadilloService = new BocadilloService();
        pedidoService = new PedidoService();

        // En producción, currentAlumnoId se establecerá desde el LoginController.
        if (currentAlumnoId == null) {
            currentAlumnoId = "Juan Perez"; // Valor de prueba
        }

        loadBocadillosDelDia();

        btnBocadilloFrio.setOnAction(e -> handleSelectBocadillo("frio"));
        btnBocadilloCaliente.setOnAction(e -> handleSelectBocadillo("caliente"));
        btnCancelar.setOnAction(e -> handleCancelPedido());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());

        // Actualización en tiempo real: refrescar cada 60 segundos
        Timeline refresco = new Timeline(new KeyFrame(Duration.seconds(60), event -> loadBocadillosDelDia()));
        refresco.setCycleCount(Timeline.INDEFINITE);
        refresco.play();
    }

    // Setter para recibir el identificador del alumno desde el LoginController
    public void setCurrentAlumnoId(String alumnoId) {
        this.currentAlumnoId = alumnoId;
        limpiarSeleccion(); // Limpiar visualmente al iniciar sesión
        loadBocadillosDelDia(); // Cargar datos nuevos del nuevo alumno
    }


    private void loadBocadillosDelDia() {
        Bocadillo frio = bocadilloService.getBocadilloDelDia("frio");
        if (frio != null) {
            bocadilloFrioName.setText(frio.getNombre());
            bocadilloFrioIngredients.setText("Ingredientes: " + frio.getIngredientes());
            bocadilloFrioAllergens.setText("Alérgenos: " + frio.getAlergenos());
            bocadilloFrioName.setStyle("-fx-fill: white;"); // Resetear a color predeterminado
        } else {
            bocadilloFrioName.setText("No hay bocadillo");
            bocadilloFrioIngredients.setText("");
            bocadilloFrioAllergens.setText("");
            bocadilloFrioName.setStyle("-fx-fill: white;");
        }

        Bocadillo caliente = bocadilloService.getBocadilloDelDia("caliente");
        if (caliente != null) {
            bocadilloCalienteName.setText(caliente.getNombre());
            bocadilloCalienteIngredients.setText("Ingredientes: " + caliente.getIngredientes());
            bocadilloCalienteAllergens.setText("Alérgenos: " + caliente.getAlergenos());
            bocadilloCalienteName.setStyle("-fx-fill: white;");
        } else {
            bocadilloCalienteName.setText("No hay bocadillo");
            bocadilloCalienteIngredients.setText("");
            bocadilloCalienteAllergens.setText("");
            bocadilloCalienteName.setStyle("-fx-fill: white;");
        }

        // Verificar si el alumno ya tiene un pedido para el día y marcar el bocadillo seleccionado
        Pedido currentPedido = pedidoService.getPedidoDelDia(currentAlumnoId);
        if (currentPedido != null) {
            String pedidoBocadillo = currentPedido.getIdBocadillo();
            if (frio != null && pedidoBocadillo.equals(frio.getNombre())) {
                bocadilloFrioName.setStyle("-fx-fill: #2d633b;");
            }
            if (caliente != null && pedidoBocadillo.equals(caliente.getNombre())) {
                bocadilloCalienteName.setStyle("-fx-fill: #2d633b;");
            }
        }
    }

    private void handleSelectBocadillo(String tipo) {
        Bocadillo seleccionado = bocadilloService.getBocadilloDelDia(tipo);
        if (seleccionado != null) {
            boolean exito = pedidoService.createOrReplacePedido(currentAlumnoId, seleccionado);
            if (exito) {
                System.out.println("Pedido creado/reemplazado: " + seleccionado.getNombre());
            } else {
                System.out.println("Error al procesar el pedido.");
            }
            loadBocadillosDelDia(); // Recargar para actualizar la indicación visual
        } else {
            System.out.println("No hay bocadillo disponible para el tipo " + tipo);
        }
    }

    private void handleCancelPedido() {
        boolean exito = pedidoService.cancelPedido(currentAlumnoId);
        if (exito) {
            System.out.println("Pedido cancelado.");
        } else {
            System.out.println("No se pudo cancelar el pedido.");
        }
        loadBocadillosDelDia();
    }

    public void limpiarSeleccion() {
        // Limpiar el texto y los estilos visuales
        bocadilloFrioName.setStyle("-fx-fill: white;");
        bocadilloCalienteName.setStyle("-fx-fill: white;");
    }

    private void cerrarSesion() {
        limpiarSeleccion(); // Limpia la selección visual

        SessionManager.cerrarSesion(); // Limpiar usuario actual

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/mibocadillofx/css/login.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Mi Bocata - Login");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error al cerrar sesión y cargar el login.");
        }
    }
}
