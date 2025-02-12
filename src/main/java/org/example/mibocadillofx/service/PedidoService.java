package org.example.mibocadillofx.service;

import org.example.mibocadillofx.dao.PedidoDAO;
import org.example.mibocadillofx.model.Bocadillo;

public class PedidoService {
    private PedidoDAO pedidoDAO;

    public PedidoService() {
        pedidoDAO = new PedidoDAO();
    }

    // Método que llama al DAO para crear o reemplazar el pedido
    public boolean createOrReplacePedido(String idAlumno, Bocadillo bocadillo) {
        if (bocadillo == null) return false;
        return pedidoDAO.createOrReplacePedido(idAlumno, bocadillo.getNombre(), bocadillo.getPrecio());
    }

    // Método para cancelar el pedido
    public boolean cancelPedido(String idAlumno) {
        return pedidoDAO.cancelPedido(idAlumno);
    }
}
