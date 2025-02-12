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
import org.example.mibocadillofx.dao.AlumnoDAO;
import org.example.mibocadillofx.model.Alumno;
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/seleccion_bocadillos.fxml"));
                Parent root = loader.load();

                // Obtén el controlador de la vista de selección
                SeleccionBocadillosController seleccionController = loader.getController();

                // Si el usuario es alumno, obten el nombre del alumno de la tabla 'alumnos'
                if ("alumno".equals(usuario.getTipoUsuario())) {
                    AlumnoDAO alumnoDAO = new AlumnoDAO();
                    Alumno alumno = alumnoDAO.getAlumnoByUserMail(usuario.getMail());
                    if (alumno != null) {
                        seleccionController.setCurrentAlumnoId(alumno.getNombre());
                    } else {
                        showAlert("Error", "No se encontró el registro del alumno.");
                        return;
                    }
                } else {
                    // Para otros tipos (por ejemplo, cocina o admin), puedes pasar otro identificador o manejarlo de otra forma.
                    seleccionController.setCurrentAlumnoId(usuario.getMail());
                }

                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Selección de Bocadillos - " + usuario.getTipoUsuario().toUpperCase());
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "No se pudo cargar la vista de selección.");
            }
        } else {
            showAlert("Error de autenticación", "Correo o contraseña incorrectos.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
