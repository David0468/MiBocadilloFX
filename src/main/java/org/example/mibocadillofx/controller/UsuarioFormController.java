package org.example.mibocadillofx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.mibocadillofx.dao.UsuarioDAO;
import org.example.mibocadillofx.model.Usuario;

public class UsuarioFormController {

    @FXML private TextField txtCorreo;
    @FXML private TextField txtContrasena;
    @FXML private ComboBox<String> cbRol;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {  // Si es edición
            txtCorreo.setText(usuario.getMail());
            txtContrasena.setText(usuario.getContrasena());
            cbRol.setValue(usuario.getTipoUsuario());
        }
    }


    @FXML
    public void initialize() {
        btnGuardar.setOnAction(event -> guardarUsuario());
        btnCancelar.setOnAction(event -> stage.close());
    }

    private void guardarUsuario() {
        String nuevoCorreo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText().trim();
        String rol = cbRol.getValue();

        if (nuevoCorreo.isEmpty() || contrasena.isEmpty() || rol == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Todos los campos son obligatorios", ButtonType.OK);
            alerta.show();
            return;
        }

        // Verificar si el correo ya existe en la base de datos (excepto si estamos editando y el correo no cambia)
        if (usuario == null || !usuario.getMail().equals(nuevoCorreo)) {
            if (usuarioDAO.existeCorreo(nuevoCorreo)) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "El correo ya está registrado. Intente con otro.", ButtonType.OK);
                alerta.show();
                return;
            }
        }

        if (usuario == null) {  // CREAR NUEVO USUARIO
            usuarioDAO.agregarUsuario(new Usuario(nuevoCorreo, contrasena, rol));
        } else {  // EDITAR USUARIO EXISTENTE
            if (!usuario.getMail().equals(nuevoCorreo)) {  // Si el correo cambió
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                        "Has cambiado el correo. Se creará un nuevo usuario y se eliminará el anterior. ¿Deseas continuar?",
                        ButtonType.YES, ButtonType.NO);
                confirmacion.showAndWait().ifPresent(respuesta -> {
                    if (respuesta == ButtonType.YES) {
                        usuarioDAO.agregarUsuario(new Usuario(nuevoCorreo, contrasena, rol));
                        usuarioDAO.eliminarUsuario(usuario.getMail());  // Eliminar el usuario anterior
                    }
                });
            } else {  // Si el correo NO cambió, solo actualizamos los datos
                usuario.setContrasena(contrasena);
                usuario.setTipoUsuario(rol);
                usuarioDAO.actualizarUsuario(usuario);
            }
        }

        stage.close();
    }
}
