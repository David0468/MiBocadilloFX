package org.example.mibocadillofx.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.mibocadillofx.model.Pedido;
import org.example.mibocadillofx.service.PedidoService;
import org.example.mibocadillofx.util.SessionManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CocinaController {

    @FXML private Label lblTotalFrio, lblPendientesFrio, lblEntregadosFrio;
    @FXML private Label lblTotalCaliente, lblPendientesCaliente, lblEntregadosCaliente;
    @FXML private ComboBox<String> cmbCursos;
    @FXML private TableView<Pedido> tablaPendientes, tablaEntregados;
    @FXML private TableColumn<Pedido, String> colNombrePendiente, colBocadilloPendiente, colCursoPendiente, colAccionPendiente;
    @FXML private TableColumn<Pedido, String> colNombreEntregado, colBocadilloEntregado, colCursoEntregado, colAccionEntregado;
    @FXML private Button btnCerrarSesion;

    private final PedidoService pedidoService = new PedidoService();
    private ObservableList<Pedido> pedidosPendientes = FXCollections.observableArrayList();
    private ObservableList<Pedido> pedidosEntregados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTablas();
        configurarFiltroCursos();
        cargarPedidos();

        btnCerrarSesion.setOnAction(e -> cerrarSesion());

        // Actualización en tiempo real cada 60 segundos
        Timeline refresco = new Timeline(new KeyFrame(Duration.seconds(5), event -> cargarPedidos()));
        refresco.setCycleCount(Timeline.INDEFINITE);
        refresco.play();
    }

    private void cargarPedidos() {
        LocalDate hoy = LocalDate.now();
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorFecha(hoy);

        // Dividir en pedidos pendientes y entregados
        List<Pedido> pendientesList = pedidos.stream().filter(p -> p.getFechaRecogida() == null).collect(Collectors.toList());
        List<Pedido> entregadosList = pedidos.stream().filter(p -> p.getFechaRecogida() != null).collect(Collectors.toList());
        pedidosPendientes.setAll(pendientesList);
        pedidosEntregados.setAll(entregadosList);

        // Aplicar filtro según el curso seleccionado
        String cursoSeleccionado = cmbCursos.getValue();
        if (cursoSeleccionado != null && !cursoSeleccionado.equals("Todos")) {
            ObservableList<Pedido> filtradosPendientes = pedidosPendientes.filtered(p -> p.getCurso().equals(cursoSeleccionado));
            ObservableList<Pedido> filtradosEntregados = pedidosEntregados.filtered(p -> p.getCurso().equals(cursoSeleccionado));
            tablaPendientes.setItems(filtradosPendientes);
            tablaEntregados.setItems(filtradosEntregados);
            actualizarEstadisticas(filtradosPendientes, filtradosEntregados);
        } else {
            tablaPendientes.setItems(pedidosPendientes);
            tablaEntregados.setItems(pedidosEntregados);
            actualizarEstadisticas(pedidosPendientes, pedidosEntregados);
        }
    }

    private void actualizarEstadisticas(ObservableList<Pedido> pendientes, ObservableList<Pedido> entregados) {
        long totalFrio = pendientes.stream()
                .filter(p -> p.getBocadillo() != null && "frio".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count() + entregados.stream()
                .filter(p -> p.getBocadillo() != null && "frio".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count();
        long entregadosFrio = entregados.stream()
                .filter(p -> p.getBocadillo() != null && "frio".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count();
        lblTotalFrio.setText("Total pedidos: " + totalFrio);
        lblPendientesFrio.setText("Pendientes de entregar: " + (totalFrio - entregadosFrio));
        lblEntregadosFrio.setText("Entregados: " + entregadosFrio);

        long totalCaliente = pendientes.stream()
                .filter(p -> p.getBocadillo() != null && "caliente".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count() + entregados.stream()
                .filter(p -> p.getBocadillo() != null && "caliente".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count();
        long entregadosCaliente = entregados.stream()
                .filter(p -> p.getBocadillo() != null && "caliente".equalsIgnoreCase(p.getBocadillo().getTipo_bocadillo()))
                .count();
        lblTotalCaliente.setText("Total pedidos: " + totalCaliente);
        lblPendientesCaliente.setText("Pendientes de entregar: " + (totalCaliente - entregadosCaliente));
        lblEntregadosCaliente.setText("Entregados: " + entregadosCaliente);
    }

    private void configurarTablas() {
        colNombrePendiente.setCellValueFactory(cellData -> cellData.getValue().nombreAlumnoProperty());
        colBocadilloPendiente.setCellValueFactory(cellData -> cellData.getValue().nombreBocadilloProperty());
        colCursoPendiente.setCellValueFactory(cellData -> cellData.getValue().cursoProperty());

        colAccionPendiente.setCellFactory(col -> new TableCell<>() {
            private final Button btnEntregar = new Button("Entregar");
            {
                btnEntregar.setOnAction(event -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    pedidoService.marcarComoEntregado(pedido);
                    cargarPedidos();
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEntregar);
            }
        });

        colNombreEntregado.setCellValueFactory(cellData -> cellData.getValue().nombreAlumnoProperty());
        colBocadilloEntregado.setCellValueFactory(cellData -> cellData.getValue().nombreBocadilloProperty());
        colCursoEntregado.setCellValueFactory(cellData -> cellData.getValue().cursoProperty());

        colAccionEntregado.setCellFactory(col -> new TableCell<>() {
            private final Button btnCancelarEntrega = new Button("Desmarcar");
            {
                btnCancelarEntrega.setOnAction(event -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    pedidoService.desmarcarComoEntregado(pedido);
                    cargarPedidos();
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnCancelarEntrega);
            }
        });
    }


    private void configurarFiltroCursos() {
        cmbCursos.getItems().clear();
        cmbCursos.getItems().add("Todos");
        cmbCursos.getItems().addAll(pedidoService.obtenerCursosDisponibles());
        cmbCursos.setValue("Todos");
        cmbCursos.setOnAction(event -> filtrarPorCurso(cmbCursos.getValue()));
    }

    private void filtrarPorCurso(String curso) {
        if ("Todos".equals(curso)) {
            tablaPendientes.setItems(pedidosPendientes);
            tablaEntregados.setItems(pedidosEntregados);
            actualizarEstadisticas(pedidosPendientes, pedidosEntregados);
        } else {
            ObservableList<Pedido> filtradosPendientes = pedidosPendientes.filtered(p -> p.getCurso().equals(curso));
            ObservableList<Pedido> filtradosEntregados = pedidosEntregados.filtered(p -> p.getCurso().equals(curso));
            tablaPendientes.setItems(filtradosPendientes);
            tablaEntregados.setItems(filtradosEntregados);
            actualizarEstadisticas(filtradosPendientes, filtradosEntregados);
        }
    }

    private void cerrarSesion() {
        System.out.println("Usuario antes de cerrar sesión: " + SessionManager.getUsuarioActual());

        SessionManager.cerrarSesion(); // Elimina los datos de sesión

        System.out.println("Usuario después de cerrar sesión: " + SessionManager.getUsuarioActual());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/mibocadillofx/css/login.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Mi Bocata - Login");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error al cerrar sesión y cargar el login.");
        }
    }
}
