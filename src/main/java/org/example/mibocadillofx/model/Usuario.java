package org.example.mibocadillofx.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "mail")
    private String mail;

    @Column(name = "MAC")
    private String mac;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "tipo_usuario")
    private String tipoUsuario;

    // Constructor sin argumentos (requerido por Hibernate)
    public Usuario() {
    }

    // Constructor completo (opcional)
    public Usuario(String mail, String mac, String contrasena, String tipoUsuario) {
        this.mail = mail;
        this.mac = mac;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y setters
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
