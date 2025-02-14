package org.example.mibocadillofx.util;

import org.example.mibocadillofx.model.Usuario;

public class SessionManager {
    private static Usuario usuarioActual;

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean isSesionIniciada() {
        return usuarioActual != null;
    }
}
