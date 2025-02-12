package org.example.mibocadillofx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class CocinaController {

    @FXML
    private Label lblTotalPedidos, lblPendientes, lblEntregados;

    @FXML
    private ComboBox<String> cmbCursos;

    @FXML
    private TableView<?> tablaPendientes, tablaEntregados;

    @FXML
    public void initialize() {
        cmbCursos.getItems().addAll("Todos", "1º ESO", "2º ESO", "3º ESO", "4º ESO", "1º Grado Medio", "2º Grado Medio", "FP Básica 1", "FP Básica 2");
        cmbCursos.getSelectionModel().select("Todos");
    }
}
