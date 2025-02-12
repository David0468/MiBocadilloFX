package org.example.mibocadillofx.service;

import org.example.mibocadillofx.dao.BocadilloDAO;
import org.example.mibocadillofx.model.Bocadillo;

public class BocadilloService {
    private BocadilloDAO bocadilloDAO;

    public BocadilloService() {
        bocadilloDAO = new BocadilloDAO();
    }

    public Bocadillo getBocadilloDelDia(String tipo) {
        return bocadilloDAO.getBocadilloDelDia(tipo);
    }
}
