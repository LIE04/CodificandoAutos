package mx.uam.ayd.proyecto.presentacion.pedidos;

import java.util.List;
import java.util.stream.Collectors; // Importación para el manejo de listas (Streams)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

// Importación de ControlsFX para el autocompletado
import org.controlsfx.control.textfield.TextFields;

import mx.uam.ayd.proyecto.negocio.ServicioPedido;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;
import mx.uam.ayd.proyecto.negocio.modelo.Pedido;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion; 
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Controlador para la ventana de Gestión y Seguimiento de Pedidos (HU-30)
 * 
 * @author Erik LIE04
 */
@Component
public class ControladorPedidos {

    @Autowired
    private ServicioPedido servicioPedido;

    @Autowired
    private VentanaPedidos ventana;

    // Controles de la Vista 
    @FXML private ComboBox<Distribuidor> cbDistribuidor;
    @FXML private TextField txtRefaccion;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtIdReparacion;
    @FXML private CheckBox chkInventario;
    @FXML private Button btnRegistrar;

    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, Integer> colIdPedido;
    @FXML private TableColumn<Pedido, String> colDistribuidor; 
    @FXML private TableColumn<Pedido, String> colRefaccion;    
    @FXML private TableColumn<Pedido, Integer> colCantidad;
    @FXML private TableColumn<Pedido, Reparacion> colIdReparacion;
    @FXML private TableColumn<Pedido, String> colEstado;

    @FXML private Button btnCancelarPedido;
    @FXML private Button btnMarcarEntregado;

    // Inicialización
    public void inicia() {
        ventana.muestra(this);
    }
    
    /**
     * Este método se ejecuta automáticamente cuando JavaFX carga la ventana
     */
    @FXML
    public void initialize() {
        // 1. Configurar la tabla
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        
        colDistribuidor.setCellValueFactory(cellData -> {
            Distribuidor distribuidor = cellData.getValue().getDistribuidor();
            return new SimpleStringProperty(distribuidor != null ? distribuidor.getNombre() : "");
        });

        colRefaccion.setCellValueFactory(cellData -> {
            Refaccion refaccion = cellData.getValue().getRefaccion();
            return new SimpleStringProperty(refaccion != null ? refaccion.getNombre() : "");
        });

        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colIdReparacion.setCellValueFactory(new PropertyValueFactory<>("reparacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoPedido"));

        // 2. Llenar el ComboBox de distribuidores
        cargarDistribuidores();
        configurarFormatoDistribuidor();

        // 3. Llenar la tabla con los pedidos existentes
        actualizarTabla();

        // 4. Lógica del CheckBox
        chkInventario.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { 
                txtIdReparacion.setDisable(true);
                txtIdReparacion.clear();
            } else { 
                txtIdReparacion.setDisable(false);
            }
        });

        // 5. Configurar el evento del botón Registrar
        btnRegistrar.setOnAction(event -> registrarPedido());

        // 6. Lógica de activación de botones de la tabla.
        btnCancelarPedido.setOnAction(event -> cambiarEstadoPedido("Cancelado"));
        btnMarcarEntregado.setOnAction(event -> cambiarEstadoPedido("Entregado"));

        tablaPedidos.getSelectionModel().selectedItemProperty().addListener(
            (observable, pedidoAnterior, pedidoNuevo) -> {
                boolean deshabilitar = (pedidoNuevo == null);
                btnCancelarPedido.setDisable(deshabilitar);
                btnMarcarEntregado.setDisable(deshabilitar);
            }
        );

        // 7. Configurar autocompletado visual
        configurarAutocompletado();
    }

    // Métodos de Acción y Lógica Visual

    private void cargarDistribuidores() {
        List<Distribuidor> distribuidores = servicioPedido.obtenerDistribuidores();
        ObservableList<Distribuidor> items = FXCollections.observableArrayList(distribuidores);
        cbDistribuidor.setItems(items);
    }

    private void actualizarTabla() {
        List<Pedido> pedidos = servicioPedido.recuperarPedidos();
        ObservableList<Pedido> items = FXCollections.observableArrayList(pedidos);
        tablaPedidos.setItems(items);
    }

    private void registrarPedido() {
        try {
            Distribuidor distribuidorSeleccionado = cbDistribuidor.getValue();
            String nombreRefaccion = txtRefaccion.getText();
            String cantidadTexto = txtCantidad.getText();
            
            if (distribuidorSeleccionado == null || nombreRefaccion.isEmpty() || cantidadTexto.isEmpty()) {
                mostrarMensaje("Error", "Por favor llene todos los campos obligatorios.", AlertType.WARNING);
                return;
            }

            int cantidad = Integer.parseInt(cantidadTexto);
            Refaccion refaccionReal = servicioPedido.buscarRefaccionPorNombre(nombreRefaccion);
            
            if (refaccionReal == null) {
                mostrarMensaje("Error", "La refacción '" + nombreRefaccion + "' no existe en el catálogo. Verifique el nombre.", AlertType.ERROR);
                return; 
            }

            Reparacion reparacionReal = null; 
            
            if (!chkInventario.isSelected() && !txtIdReparacion.getText().isEmpty()) {
                int idReparacion = Integer.parseInt(txtIdReparacion.getText());
                reparacionReal = servicioPedido.buscarReparacionPorId(idReparacion);
                
                if (reparacionReal == null) {
                    mostrarMensaje("Error", "La reparación con ID " + idReparacion + " no existe en el sistema.", AlertType.ERROR);
                    return; 
                }
            }

            servicioPedido.crearPedido(distribuidorSeleccionado, refaccionReal, cantidad, reparacionReal);
            mostrarMensaje("Éxito", "El pedido se registró correctamente.", AlertType.INFORMATION);
            limpiarFormulario();
            actualizarTabla(); 

        } catch (NumberFormatException e) {
            mostrarMensaje("Error", "La cantidad y el ID de reparación deben ser números.", AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            mostrarMensaje("Error de validación", e.getMessage(), AlertType.WARNING);
        }
    }

    private void limpiarFormulario() {
        cbDistribuidor.setValue(null);
        txtRefaccion.clear();
        txtCantidad.clear();
        txtIdReparacion.clear();
        chkInventario.setSelected(false);
    }

    private void mostrarMensaje(String titulo, String contenido, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void configurarFormatoDistribuidor() {
        cbDistribuidor.setConverter(new StringConverter<Distribuidor>() {
            @Override
            public String toString(Distribuidor d) {
                return (d == null) ? "" : d.getNombre();
            }
            @Override
            public Distribuidor fromString(String string) {
                return null;
            }
        });
    }

    private void cambiarEstadoPedido(String nuevoEstado) {
        Pedido pedidoSeleccionado = tablaPedidos.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado == null) {
            mostrarMensaje("Atención", "Debe seleccionar un pedido de la tabla primero.", AlertType.WARNING);
            return;
        }

        if (nuevoEstado.equals(pedidoSeleccionado.getEstadoPedido())) {
            mostrarMensaje("Información", "El pedido ya se encuentra en estado: " + nuevoEstado, AlertType.INFORMATION);
            return;
        }

        try {
            servicioPedido.actualizarEstadoPedido(pedidoSeleccionado, nuevoEstado);
            mostrarMensaje("Éxito", "El pedido se marcó como " + nuevoEstado + ".", AlertType.INFORMATION);
            actualizarTabla(); 
            
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo actualizar el estado: " + e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * Vincula el campo de texto (TextField) de refacciones con el catálogo de la BD,
     * desplegando sugerencias (autocompletado) mientras el usuario escribe.
     */
    private void configurarAutocompletado() {
        // 1. Obtenemos todas las refacciones disponibles en la base de datos
        // NOTA: Asegúrate de tener el método obtenerRefacciones() implementado en ServicioPedido.
        List<Refaccion> todasLasRefacciones = servicioPedido.obtenerRefacciones();
        
        // 2. Extraemos únicamente los nombres (Strings) utilizando Streams de Java
        List<String> nombresRefacciones = todasLasRefacciones.stream()
                .map(Refaccion::getNombre)
                .collect(Collectors.toList());

        // 3. Vinculamos la lista de nombres al componente visual con ControlsFX
        TextFields.bindAutoCompletion(txtRefaccion, nombresRefacciones);
    }
}