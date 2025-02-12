package org.example.mibocadillofx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.mibocadillofx.model.Usuario;
import org.example.mibocadillofx.service.LoginService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private LoginService loginService;

    @FXML
    public void initialize() {
        loginService = new LoginService();
        loginButton.setOnAction(event -> login());
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        Usuario usuario = loginService.autenticar(email, password);

        if (usuario != null) {
            String tipoUsuario = usuario.getTipoUsuario();
            String fxmlARecargar = "";
            // Redirige según el tipo de usuario
            switch (tipoUsuario) {
                case "admin":
                    fxmlARecargar = "/org/example/mibocadillofx/fxml/admin.fxml";
                    break;
                case "cocina":
                    fxmlARecargar = "/org/example/mibocadillofx/fxml/cocina.fxml";
                    break;
                case "alumno":
                    fxmlARecargar = "/org/example/mibocadillofx/fxml/seleccion_bocadillos.fxml";
                    break;
                default:
                    mostrarAlerta("Error", "Tipo de usuario desconocido.");
                    return;
            }

            // Obtener el stage actual a partir de un nodo (en este caso, loginButton)
            Stage stage = (Stage) loginButton.getScene().getWindow();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlARecargar));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Mi Bocata - " + tipoUsuario.toUpperCase());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cargar la vista: " + fxmlARecargar);
            }
        } else {
            mostrarAlerta("Autenticación fallida", "Correo o contraseña incorrectos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
