package org.example.mibocadillofx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.mibocadillofx.dao.BocadilloDAO;
import org.example.mibocadillofx.model.Bocadillo;

public class BocadilloFormController {

    @FXML private TextField txtNombre;
    @FXML private TextArea txtIngredientes;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbTipoBocadillo;
    @FXML private ComboBox<String> cbDiaDisponible;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private Bocadillo bocadillo;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBocadillo(Bocadillo bocadillo) {
        this.bocadillo = bocadillo;
        if (bocadillo != null) {
            txtNombre.setText(bocadillo.getNombre());
            txtIngredientes.setText(bocadillo.getIngredientes());
            txtPrecio.setText(String.valueOf(bocadillo.getPrecio()));
            cbTipoBocadillo.setValue(bocadillo.getTipo_bocadillo());
            cbDiaDisponible.setValue(bocadillo.getDia());
            txtNombre.setDisable(true); // No permitir cambiar el nombre al editar
        }
    }

    @FXML
    public void initialize() {
        cbTipoBocadillo.getItems().addAll("frio", "caliente");
        cbDiaDisponible.getItems().addAll("lunes", "martes", "miércoles", "jueves", "viernes");

        btnGuardar.setOnAction(event -> guardarBocadillo());
        btnCancelar.setOnAction(event -> cerrarVentana()); // Llamar a método para cerrar
    }

    private void guardarBocadillo() {
        String nombre = txtNombre.getText().trim();
        String ingredientes = txtIngredientes.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String tipo = cbTipoBocadillo.getValue();
        String dia = cbDiaDisponible.getValue();

        if (nombre.isEmpty() || ingredientes.isEmpty() || precioStr.isEmpty() || tipo == null || dia == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Todos los campos son obligatorios", ButtonType.OK);
            alerta.show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El precio debe ser un número válido", ButtonType.OK);
            alerta.show();
            return;
        }

        if (bocadillo == null) {  // CREAR NUEVO BOcadillo
            bocadilloDAO.agregarBocadillo(new Bocadillo(nombre, ingredientes, precio, tipo, dia, null));
        } else {  // EDITAR BOcadillo EXISTENTE
            bocadillo.setIngredientes(ingredientes);
            bocadillo.setPrecio(precio);
            bocadillo.setTipo_bocadillo(tipo);
            bocadillo.setDia(dia);
            bocadilloDAO.actualizarBocadillo(bocadillo);
        }

        cerrarVentana(); // Cerrar ventana después de guardar
    }

    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        }
    }
}
