package org.example.mibocadillofx.service;

import org.example.mibocadillofx.dao.UsuarioDAO;
import org.example.mibocadillofx.model.Usuario;

public class LoginService {

    private final UsuarioDAO usuarioDAO;

    public LoginService() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica al usuario comparando correo y contraseña.
     * @param mail Correo del usuario.
     * @param contrasena Contraseña ingresada.
     * @return Objeto Usuario si la autenticación es correcta, o null en caso contrario.
     */
    public Usuario autenticar(String mail, String contrasena) {
        Usuario usuario = usuarioDAO.getUsuarioByMail(mail);
        if (usuario != null && usuario.getContrasena() != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }
}
