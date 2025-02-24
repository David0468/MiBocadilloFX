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

    private String fecha_baja;

    // Campo transitorio para los alérgenos (no se persiste en BD)
    private transient String alergenos;

    // Constructor sin argumentos (requerido por Hibernate)
    public Bocadillo() {
    }

    public Bocadillo(String nombre, String ingredientes, double precio, String tipo_bocadillo, String dia, String fecha_baja) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.precio = precio;
        this.tipo_bocadillo = tipo_bocadillo;
        this.dia = dia;
        this.fecha_baja = fecha_baja;
    }

    public Bocadillo(String nombre, String tipo_bocadillo, String dia) {
        this.nombre = nombre;
        this.tipo_bocadillo = tipo_bocadillo;
        this.dia = dia;
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

    public String getFecha_baja() {
        return fecha_baja;
    }

    public void setFecha_baja(String fecha_baja) {
        this.fecha_baja = fecha_baja;
    }

    public boolean isActivo() {
        return fecha_baja == null; // Si fecha_baja es NULL, el bocadillo está activo
    }
}
