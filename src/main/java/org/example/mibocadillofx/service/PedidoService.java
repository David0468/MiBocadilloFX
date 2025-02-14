package org.example.mibocadillofx.service;

import org.example.mibocadillofx.dao.PedidoDAO;
import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.model.Pedido;

import java.time.LocalDate;
import java.util.List;

public class PedidoService {
    private PedidoDAO pedidoDAO;

    public PedidoService() {
        pedidoDAO = new PedidoDAO();
    }

    public boolean createOrReplacePedido(String idAlumno, Bocadillo bocadillo) {
        if (bocadillo == null) return false;
        return pedidoDAO.createOrReplacePedido(idAlumno, bocadillo.getNombre(), bocadillo.getPrecio());
    }

    public boolean cancelPedido(String idAlumno) {
        return pedidoDAO.cancelPedido(idAlumno);
    }

    public Pedido getPedidoDelDia(String idAlumno) {
        return pedidoDAO.getPedidoDelDia(idAlumno);
    }

    public void marcarComoEntregado(Pedido pedido) {
        if (pedido != null) {
            pedido.setFechaRecogida(LocalDate.now());
            pedidoDAO.actualizarPedido(pedido);
        }
    }

    public void desmarcarComoEntregado(Pedido pedido) {
        if (pedido != null) {
            pedido.setFechaRecogida(null);
            pedidoDAO.actualizarPedido(pedido);
        }
    }

    public List<Pedido> obtenerPedidosPorFecha(LocalDate fecha) {
        return pedidoDAO.obtenerPedidosPorFecha(fecha);
    }

    public List<String> obtenerCursosDisponibles() {
        return pedidoDAO.obtenerCursosDisponibles();
    }
}
