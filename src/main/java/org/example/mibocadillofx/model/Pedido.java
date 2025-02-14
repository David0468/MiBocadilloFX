package org.example.mibocadillofx.model;

import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
@IdClass(PedidoId.class)
public class Pedido {

    @Id
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Id
    @Column(name = "id_alumno")
    private String idAlumno;

    @Id
    @Column(name = "id_bocadillo")
    private String idBocadillo;

    @Column(name = "precio")
    private double precio;

    @Column(name = "fecha_recogida")
    private LocalDate fechaRecogida;

    @ManyToOne
    @JoinColumn(name = "id_bocadillo", referencedColumnName = "nombre", insertable = false, updatable = false)
    private Bocadillo bocadillo;

    @ManyToOne
    @JoinColumn(name = "id_alumno", referencedColumnName = "nombre", insertable = false, updatable = false)
    private Alumno alumno;

    public Pedido() {
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getIdBocadillo() {
        return idBocadillo;
    }

    public void setIdBocadillo(String idBocadillo) {
        this.idBocadillo = idBocadillo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaRecogida() {
        return fechaRecogida;
    }

    public void setFechaRecogida(LocalDate fechaRecogida) {
        this.fechaRecogida = fechaRecogida;
    }

    public Bocadillo getBocadillo() {
        return bocadillo;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public String getCurso() {
        return (alumno != null) ? alumno.getIdCurso() : "Desconocido";
    }

    // Métodos para JavaFX TableView
    public StringProperty nombreAlumnoProperty() {
        return new SimpleStringProperty(idAlumno);
    }

    public StringProperty nombreBocadilloProperty() {
        return new SimpleStringProperty(idBocadillo);
    }

    public StringProperty cursoProperty() {
        return new SimpleStringProperty(getCurso());
    }
}
