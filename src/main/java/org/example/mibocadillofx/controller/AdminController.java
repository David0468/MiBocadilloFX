package org.example.mibocadillofx.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.mibocadillofx.dao.BocadilloDAO;
import org.example.mibocadillofx.dao.PedidoDAO;
import org.example.mibocadillofx.dao.UsuarioDAO;
import org.example.mibocadillofx.model.Bocadillo;
import org.example.mibocadillofx.model.Pedido;
import org.example.mibocadillofx.model.Usuario;
import org.example.mibocadillofx.service.PedidoService;
import org.example.mibocadillofx.util.SessionManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdminController {

    @FXML private Button btnCerrarSesion;

    // ** GESTIÓN DE USUARIOS **
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colCorreoUsuario, colContrasenaUsuario, colTipoUsuario;
    @FXML private Button btnAgregarUsuario, btnEditarUsuario, btnEliminarUsuario;

    // ** GESTIÓN DE PEDIDOS **
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colFechaPedido, colUsuarioPedido, colBocadilloPedido, colEstadoPedido, colAccionPedido;
    @FXML private Button btnFiltrarPedidos;
    @FXML private DatePicker dpFiltroFecha;
    @FXML private TextField txtFiltroUsuario;

    // ** GESTIÓN DE BOCADILLOS **
    @FXML private TableView<Bocadillo> tablaBocadillos;
    @FXML private TableColumn<Bocadillo, String> colNombreBocadillo, colIngredientesBocadillo, colTipoBocadillo, colDiaBocadillo, colEstadoBocadillo;
    @FXML private TableColumn<Bocadillo, Double> colPrecioBocadillo;
    @FXML private Button btnAgregarBocadillo, btnEditarBocadillo, btnCambiarEstadoBocadillo, btnEliminarBocadillo;

    // ** DASHBOARD Y CONFIGURACIÓN **
    @FXML private Label lblPedidosTotales, lblIngresosTotales, lblPedidosPendientes;
    @FXML private TextField txtDescuentoGlobal, txtLimitePedidos;
    @FXML private Button btnGuardarConfiguracion;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final BocadilloDAO bocadilloDAO = new BocadilloDAO();

    @FXML
    public void initialize() {
        configurarTablas();
        cargarDatosIniciales();
        actualizarEstadisticas();

        btnCerrarSesion.setOnAction(event -> cerrarSesion());

        // Gestión de usuarios
        btnAgregarUsuario.setOnAction(event -> agregarUsuario());
        btnEditarUsuario.setOnAction(event -> editarUsuario());
        btnEliminarUsuario.setOnAction(event -> eliminarUsuario());

        // Gestión de pedidos
        btnFiltrarPedidos.setOnAction(event -> cargarPedidosFiltrados());

        // Gestión de bocadillos
        btnAgregarBocadillo.setOnAction(event -> abrirFormularioBocadillo(null));
        btnEditarBocadillo.setOnAction(event -> {
            Bocadillo seleccionado = tablaBocadillos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                abrirFormularioBocadillo(seleccionado);
            } else {
                mostrarAlerta("Seleccione un bocadillo para editar.");
            }
        });
        btnCambiarEstadoBocadillo.setOnAction(event -> {
            Bocadillo bocadilloSeleccionado = tablaBocadillos.getSelectionModel().getSelectedItem();
            if (bocadilloSeleccionado != null) {
                cambiarEstadoBocadillo(bocadilloSeleccionado);
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecciona un bocadillo primero.", ButtonType.OK);
                alerta.show();
            }
        });
        btnEliminarBocadillo.setOnAction(event -> eliminarBocadillo());


        // Configuración
        btnGuardarConfiguracion.setOnAction(event -> guardarConfiguracion());
    }

    /** ==============================
     *  CONFIGURACIÓN DE TABLAS
     *  ============================== */
    private void configurarTablas() {
        //colUsuario
        colCorreoUsuario.setCellValueFactory(new PropertyValueFactory<>("mail"));
        colContrasenaUsuario.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        colTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));

        // Depuración: Ver si la tabla tiene columnas configuradas correctamente
        System.out.println("Columnas de tablaUsuarios:");
        System.out.println("Correo: " + colCorreoUsuario.getText());
        System.out.println("Contraseña: " + colContrasenaUsuario.getText());
        System.out.println("Rol: " + colTipoUsuario.getText());

        //colPedidos
        colFechaPedido.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFechaPedido()).asString()
        );
        colUsuarioPedido.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAlumno().getNombre())
        );
        colBocadilloPedido.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBocadillo().getNombre())
        );
        colEstadoPedido.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaRecogida() != null ? "Entregado" : "Pendiente")
        );

        colAccionPedido.setCellFactory(col -> new TableCell<>() {
            private final Button btnCancelar = new Button("Cancelar");

            {
                btnCancelar.setOnAction(event -> {
                    Pedido pedido = getTableView().getItems().get(getIndex());
                    PedidoDAO.eliminarPedido(pedido);
                    cargarPedidos();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnCancelar);
            }
        });

        //colBocadillos
        colNombreBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colIngredientesBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIngredientes()));
        colTipoBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo_bocadillo()));
        colPrecioBocadillo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("precio"));
        colDiaBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDia()));

        colEstadoBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(
                (cellData.getValue().getFecha_baja() == null) ? "Activo" : "Inactivo"
        ));
    }

    private void cargarDatosIniciales() {
        cargarUsuarios();
        cargarPedidos();
        cargarBocadillos();
    }

    /** ==============================
     *  Inicio
     *  ============================== */
    private void actualizarEstadisticas() {
        int pedidosTotales = pedidoDAO.obtenerTotalPedidos();
        double ingresosTotales = pedidoDAO.obtenerIngresosTotales();
        int pedidosPendientes = pedidoDAO.obtenerPedidosPendientes();

        lblPedidosTotales.setText(String.valueOf(pedidosTotales));
        lblIngresosTotales.setText(String.format("%.2f €", ingresosTotales));
        lblPedidosPendientes.setText(String.valueOf(pedidosPendientes));
    }
    /** ==============================
     *  GESTIÓN DE USUARIOS
     *  ============================== */
    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuarios);
        tablaUsuarios.setItems(listaUsuarios);
    }
    /*private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();

        //  Depuración: Imprimir los datos en la consola
        System.out.println("Usuarios obtenidos de la BD:");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getMail() + " - " + usuario.getContrasena() + " - " + usuario.getTipoUsuario());
        }

        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuarios);
        tablaUsuarios.setItems(listaUsuarios);
    }*/


    private void agregarUsuario() {
        abrirFormularioUsuario(null);
    }

    private void editarUsuario() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            abrirFormularioUsuario(usuarioSeleccionado);
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecciona un usuario para editar.", ButtonType.OK);
            alerta.show();
        }
    }
    private void eliminarUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Estás seguro de que deseas eliminar este usuario? Esta acción no se puede deshacer.",
                    ButtonType.YES, ButtonType.NO);
            alerta.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.YES) {
                    usuarioDAO.eliminarUsuario(usuario.getMail());
                    cargarUsuarios();
                }
            });
        }
    }


    private void abrirFormularioUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/usuario_form.fxml"));
            Parent root = loader.load();

            UsuarioFormController controlador = loader.getController();
            Stage stage = new Stage();
            controlador.setStage(stage);
            controlador.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(usuario == null ? "Agregar Usuario" : "Editar Usuario");
            stage.showAndWait();

            cargarUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /** ==============================
     *  GESTIÓN DE PEDIDOS
     *  ============================== */
    private void cargarPedidos() {
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosConDetalles();
        tablaPedidos.getItems().setAll(pedidos);
    }

    private void cargarPedidosFiltrados() {
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosPorFiltro(dpFiltroFecha.getValue(), txtFiltroUsuario.getText().trim());
        tablaPedidos.getItems().setAll(pedidos);
    }

    /** ==============================
     *  GESTIÓN DE BOCADILLOS
     *  ============================== */
    private void cargarBocadillos() {
        List<Bocadillo> bocadillos = bocadilloDAO.obtenerTodosLosBocadillosAdmin(); // Asegurar que trae inactivos también
        ObservableList<Bocadillo> listaBocadillos = FXCollections.observableArrayList(bocadillos);

        // Modificar la tabla para reflejar "Activo" o "Inactivo"
        colEstadoBocadillo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isActivo() ? "Activo" : "Inactivo")
        );

        tablaBocadillos.setItems(listaBocadillos);
    }




    private void abrirFormularioBocadillo(Bocadillo bocadillo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/mibocadillofx/fxml/bocadillos_form.fxml"));
            Parent root = loader.load();

            // Obtener controlador y asignar bocadillo + stage
            BocadilloFormController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setBocadillo(bocadillo);

            // Configurar la ventana emergente
            stage.setScene(new Scene(root));
            stage.setTitle(bocadillo == null ? "Añadir Bocadillo" : "Editar Bocadillo");
            stage.showAndWait(); // Esperar a que se cierre antes de continuar
            cargarBocadillos();  // Recargar datos después de cerrar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarEstadoBocadillo(Bocadillo bocadillo) {
        bocadilloDAO.cambiarEstadoBocadillo(bocadillo);
        cargarBocadillos(); // Recargar la lista para reflejar cambios
    }


    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK);
        alerta.show();
    }
    private void eliminarBocadillo() {
        Bocadillo bocadilloSeleccionado = tablaBocadillos.getSelectionModel().getSelectedItem();

        if (bocadilloSeleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecciona un bocadillo para eliminar.", ButtonType.OK);
            alerta.show();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Estás seguro de que quieres eliminar el bocadillo " + bocadilloSeleccionado.getNombre() + "?",
                ButtonType.YES, ButtonType.NO);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                bocadilloDAO.eliminarBocadillo(bocadilloSeleccionado.getNombre());
                cargarBocadillos(); // Recargar la tabla después de eliminar
            }
        });
    }



    /** ==============================
     *  CONFIGURACIÓN Y CIERRE DE SESIÓN
     *  ============================== */
    private void guardarConfiguracion() {
        System.out.println("Configuración guardada");
    }

    private void cerrarSesion() {
        SessionManager.cerrarSesion();
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
        }
    }
}
