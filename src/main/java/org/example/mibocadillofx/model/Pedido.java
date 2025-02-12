package org.example.mibocadillofx.model;

import jakarta.persistence.*;
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

    // Otros campos como fecha_recogida o id_descuento se pueden agregar según se requiera.
    // Para este ejemplo, nos centraremos en la creación/actualización/cancelación.

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
}
