package org.example.mibocadillofx.service;

import org.example.mibocadillofx.dao.PedidoDAO;
import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.model.Pedido;

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

    // Método para obtener el pedido actual del día para un alumno
    public Pedido getPedidoDelDia(String idAlumno) {
        return pedidoDAO.getPedidoDelDia(idAlumno);
    }
}
