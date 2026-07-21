package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

@Component
public class VistaCotizacion {

    @Autowired
    private ControlCotizacion control;

    // --- ELEMENTOS DE LA INTERFAZ (Inyectados desde el FXML) ---

    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Vehiculo> comboVehiculos;
    
    @FXML private TextField txtBuscarRefaccion;
    @FXML private TextField txtCantidadRefaccion;
    @FXML private Button btnAgregarRefaccion;
    
    // Puedes usar un TableView o ListView para mostrar las piezas agregadas
    @FXML private ListView<String> listaConceptosVista; 
    
    @FXML private TextArea txtFallas;
    @FXML private TextArea txtManoObra;
    @FXML private TextField txtCostoManoObra;
    
    @FXML private Label lblTotalRefacciones;
    @FXML private Label lblCostoTotal;
    
    @FXML private Button btnGuardarCotizacion;

    // --- INICIALIZACIÓN DE LA VISTA ---

    /**
     * Este método lo llamas cuando muestras la ventana por primera vez.
     */
    public void iniciar() {
        // Le decimos al control que arranque su proceso
        control.iniciarVistaCotizacion(this); 
    }

    /**
     * El control llama a este método para llenar el ComboBox inicial
     */
    public void mostrarClientes(List<Cliente> clientes) {
        comboClientes.getItems().clear();
        comboClientes.getItems().addAll(clientes);
    }

    // --- EVENTOS DE LA INTERFAZ (Acciones del usuario) ---

    /**
     * Se ejecuta cuando el usuario elige un cliente del ComboBox
     */
    @FXML
    public void onClienteSeleccionado() {
        Cliente seleccionado = comboClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Le avisamos al control
            control.clienteSeleccionado(seleccionado);
        }
    }

    /**
     * El control llama a este método para llenar el ComboBox de vehículos
     */
    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        comboVehiculos.getItems().clear();
        comboVehiculos.getItems().addAll(vehiculos);
    }

    /**
     * Se ejecuta cuando el usuario elige un vehículo. Aquí arranca el borrador.
     */
    @FXML
    public void onVehiculoSeleccionado() {
        Cliente cliente = comboClientes.getSelectionModel().getSelectedItem();
        Vehiculo vehiculo = comboVehiculos.getSelectionModel().getSelectedItem();
        
       /*  if (cliente != null && vehiculo != null) {
            control.iniciarNuevaCotizacion(cliente, vehiculo);
        } */
    }

    /**
     * Se ejecuta al presionar el botón "Agregar" en la sección de refacciones
     */
   /* @FXML
    public void onAgregarRefaccionClick() {
        String nombrePieza = txtBuscarRefaccion.getText();
        int cantidad = Integer.parseInt(txtCantidadRefaccion.getText()); // Ojo con validar que sean números
        
        try {
            // 1. Buscar la pieza
            Refaccion encontrada = control.buscarRefaccion(nombrePieza);
            
            if (encontrada != null) {
                // 2. Agregarla a la cotización
                control.agregarRefaccionACotizacion(encontrada, cantidad);
                
                // 3. Actualizar la vista visualmente (opcional, para que el usuario vea qué agregó)
                listaConceptosVista.getItems().add(cantidad + "x " + encontrada.getNombre() + " - $" + encontrada.getPrecio());
                
                // Limpiar campos
                txtBuscarRefaccion.clear();
                txtCantidadRefaccion.clear();
            } else {
                mostrarMensajeError("Refacción no encontrada en el inventario.");
            }
        } catch (IllegalArgumentException e) {
            mostrarMensajeError(e.getMessage()); // Muestra si no hay piezas suficientes
        }
    } */

    /**
     * Se ejecuta cuando el usuario presiona un botón de "Calcular" o al perder el foco en la mano de obra
     */
    @FXML
    public void onActualizarServicio() {
        String fallas = txtFallas.getText();
        String manoObra = txtManoObra.getText();
        
        // Manejo básico por si el campo está vacío
        float costoManoObra = 0;
        if (!txtCostoManoObra.getText().isEmpty()) {
            costoManoObra = Float.parseFloat(txtCostoManoObra.getText());
        }
        
        // Mandamos todo al control para que recalcule
        control.capturarDatosServicio(fallas, manoObra, costoManoObra);
    }

    /**
     * El control llama a este método para actualizar los números en pantalla
     */
    public void actualizarEtiquetasTotales(float costoRefacciones, float costoTotal) {
        lblTotalRefacciones.setText("$" + costoRefacciones);
        lblCostoTotal.setText("$" + costoTotal);
    }

    /**
     * Se ejecuta al presionar "Guardar Cotización"
     */
    @FXML
    public void onGuardarClick() {
        // Asegurarnos de capturar los últimos textos antes de guardar
        onActualizarServicio(); 
        
        control.finalizarCotizacion();
        mostrarMensajeExito("¡Cotización generada y guardada con éxito!");
        
        // Aquí podrías limpiar la ventana para una nueva cotización o cerrarla
    }

    // --- UTILIDADES ---

    private void mostrarMensajeError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarMensajeExito(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}