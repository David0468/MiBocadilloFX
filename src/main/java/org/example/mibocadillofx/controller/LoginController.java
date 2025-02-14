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
import org.example.mibocadillofx.util.SessionManager;

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
            SessionManager.iniciarSesion(usuario); // Guardar usuario en sesión

            String fxmlPath;
            String tituloVentana;

            switch (usuario.getTipoUsuario()) {
                case "alumno":
                    fxmlPath = "/org/example/mibocadillofx/fxml/seleccion_bocadillos.fxml";
                    tituloVentana = "Selección de Bocadillos";
                    break;
                case "cocina":
                    fxmlPath = "/org/example/mibocadillofx/fxml/cocina.fxml";
                    tituloVentana = "Cocina - Pedidos del Día";
                    break;
                case "admin":
                    fxmlPath = "/org/example/mibocadillofx/fxml/admin.fxml"; // Asegúrate de que esta vista existe
                    tituloVentana = "Administración - Gestión del Sistema";
                    break;
                default:
                    showAlert("Error", "Tipo de usuario desconocido.");
                    return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                // Si el usuario es un alumno, obtenemos su nombre desde la tabla alumnos
                if ("alumno".equals(usuario.getTipoUsuario())) {
                    AlumnoDAO alumnoDAO = new AlumnoDAO();
                    Alumno alumno = alumnoDAO.getAlumnoByUserMail(usuario.getMail());
                    if (alumno != null) {
                        SeleccionBocadillosController seleccionController = loader.getController();
                        seleccionController.setCurrentAlumnoId(alumno.getNombre());
                    } else {
                        showAlert("Error", "No se encontró el registro del alumno.");
                        return;
                    }
                }

                // Cambiar de escena
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(tituloVentana);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "No se pudo cargar la vista correspondiente.");
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
