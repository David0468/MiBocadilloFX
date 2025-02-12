package org.example.mibocadillofx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SeleccionBocadillosController {

    @FXML
    private Button btnBocadilloFrio;

    @FXML
    private Button btnBocadilloCaliente;

    @FXML
    private Button btnCancelar;

    @FXML
    private Text selectedBocadillo;

    @FXML
    public void initialize() {
        btnBocadilloFrio.setOnAction(event -> seleccionarBocadillo("Bocadillo Frío"));
        btnBocadilloCaliente.setOnAction(event -> seleccionarBocadillo("Bocadillo Caliente"));
        btnCancelar.setOnAction(event -> cancelarPedido());
    }

    private void seleccionarBocadillo(String bocadillo) {
        System.out.println("Has seleccionado: " + bocadillo);
    }

    private void cancelarPedido() {
        System.out.println("Pedido cancelado");
    }
}
