package org.example.mibocadillofx.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PedidoId implements Serializable {
    private LocalDate fechaPedido;
    private String idAlumno;
    private String idBocadillo;

    public PedidoId() {}

    public PedidoId(LocalDate fechaPedido, String idAlumno, String idBocadillo) {
        this.fechaPedido = fechaPedido;
        this.idAlumno = idAlumno;
        this.idBocadillo = idBocadillo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoId)) return false;
        PedidoId that = (PedidoId) o;
        return Objects.equals(fechaPedido, that.fechaPedido) &&
                Objects.equals(idAlumno, that.idAlumno) &&
                Objects.equals(idBocadillo, that.idBocadillo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaPedido, idAlumno, idBocadillo);
    }
}
