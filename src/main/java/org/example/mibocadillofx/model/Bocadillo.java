package org.example.mibocadillofx.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bocadillos")
public class Bocadillo {

    @Id
    private String nombre;
    private String ingredientes;
    private double precio;
    private String tipo_bocadillo;  // "frio" o "caliente"
    private String dia;             // Por ejemplo, "martes", "miércoles", etc.

    // Campo transitorio para los alérgenos (no se persiste en BD)
    private transient String alergenos;

    // Constructor sin argumentos (requerido por Hibernate)
    public Bocadillo() {
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getIngredientes() {
        return ingredientes;
    }
    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getTipo_bocadillo() {
        return tipo_bocadillo;
    }
    public void setTipo_bocadillo(String tipo_bocadillo) {
        this.tipo_bocadillo = tipo_bocadillo;
    }
    public String getDia() {
        return dia;
    }
    public void setDia(String dia) {
        this.dia = dia;
    }
    public String getAlergenos() {
        return alergenos;
    }
    public void setAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }
}
