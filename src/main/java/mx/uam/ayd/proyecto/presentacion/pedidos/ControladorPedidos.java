package mx.uam.ayd.proyecto.presentacion.pedidos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

import mx.uam.ayd.proyecto.negocio.ServicioPedido;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;
import mx.uam.ayd.proyecto.negocio.modelo.Pedido;
// Importamos Refaccion y Reparacion si las llegas a instanciar aqui
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
    @FXML private TableColumn<Pedido, Distribuidor> colDistribuidor;
    @FXML private TableColumn<Pedido, Refaccion> colRefaccion;
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
        // 1. Configurar la tabla (La talacha de enlazar columnas con los atributos de la entidad)
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<>("distribuidor"));
        colRefaccion.setCellValueFactory(new PropertyValueFactory<>("refaccion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colIdReparacion.setCellValueFactory(new PropertyValueFactory<>("reparacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoPedido"));

        // 2. Llenar el ComboBox de distribuidores
        cargarDistribuidores();
        configurarFormatoDistribuidor();

        // 3. Llenar la tabla con los pedidos existentes
        actualizarTabla();

        // 4. Lógica del CheckBox: Si se marca "Para inventario", se bloquea el campo de reparación
        chkInventario.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // Si está marcado
                txtIdReparacion.setDisable(true);
                txtIdReparacion.clear();
            } else { // Si se desmarca
                txtIdReparacion.setDisable(false);
            }
        });

        // 5. Configurar el evento del botón Registrar
        btnRegistrar.setOnAction(event -> registrarPedido());

        // Nuevos eventos para los botones de la tabla
        btnCancelarPedido.setOnAction(event -> cambiarEstadoPedido("Cancelado"));
        btnMarcarEntregado.setOnAction(event -> cambiarEstadoPedido("Entregado"));
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
            // Recolectar datos de la vista
            Distribuidor distribuidorSeleccionado = cbDistribuidor.getValue();
            String nombreRefaccion = txtRefaccion.getText();
            String cantidadTexto = txtCantidad.getText();
            
            // Validaciones visuales básicas
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

            // Llamamos al servicio para guardar
            servicioPedido.crearPedido(distribuidorSeleccionado, refaccionReal, cantidad, reparacionReal);

            // Si todo sale bien
            mostrarMensaje("Éxito", "El pedido se registró correctamente.", AlertType.INFORMATION);
            limpiarFormulario();
            actualizarTabla(); // Recargamos la tabla para ver el nuevo pedido

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

    /**
     * Este método le enseña al ComboBox cómo mostrar los objetos Distribuidor en texto
     */
    private void configurarFormatoDistribuidor() {
        cbDistribuidor.setConverter(new StringConverter<Distribuidor>() {
            @Override
            public String toString(Distribuidor d) {
                // Asumiendo que Distribuidor tiene un método getNombre()
                return (d == null) ? "" : d.toString(); // Cámbialo a d.getNombre() si lo tienen
            }
            @Override
            public Distribuidor fromString(String string) {
                return null;
            }
        });
    }
    /**
     * Método genérico para actualizar el estado del pedido seleccionado en la tabla.
     */
    private void cambiarEstadoPedido(String nuevoEstado) {
        // 1. Obtener el pedido que el usuario seleccionó con el clic
        Pedido pedidoSeleccionado = tablaPedidos.getSelectionModel().getSelectedItem();

        // 2. Validar que haya seleccionado algo
        if (pedidoSeleccionado == null) {
            mostrarMensaje("Atención", "Debe seleccionar un pedido de la tabla primero.", AlertType.WARNING);
            return;
        }

        // 3. (Opcional pero recomendado) Validar que no estemos asignando el mismo estado
        if (nuevoEstado.equals(pedidoSeleccionado.getEstadoPedido())) {
            mostrarMensaje("Información", "El pedido ya se encuentra en estado: " + nuevoEstado, AlertType.INFORMATION);
            return;
        }

        try {
            // 4. Llamar al servicio de negocio
            servicioPedido.actualizarEstadoPedido(pedidoSeleccionado, nuevoEstado);
            
            // 5. Refrescar la interfaz
            mostrarMensaje("Éxito", "El pedido se marcó como " + nuevoEstado + ".", AlertType.INFORMATION);
            actualizarTabla(); 
            
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo actualizar el estado: " + e.getMessage(), AlertType.ERROR);
        }
    }
}