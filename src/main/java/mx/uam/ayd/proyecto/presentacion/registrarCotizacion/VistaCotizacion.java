package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Lazy;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

@Component
public class VistaCotizacion {

    @Autowired
    @Lazy
    private ControlCotizacion control;

    // --- VARIABLES DE VENTANA (Estilo VistaVehiculosEntrega) ---
    private Stage stage;
    private boolean initialized = false;

    // --- ELEMENTOS DE LA INTERFAZ (Inyectados desde el FXML) ---
    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Vehiculo> comboVehiculos;
    
    @FXML private TextField txtBuscarRefaccion;
    @FXML private TextField txtCantidadRefaccion;
    @FXML private Button btnAgregarRefaccion;
    
    @FXML private ListView<String> listaConceptosVista; 
    
    @FXML private TextArea txtFallas;
    @FXML private TextArea txtManoObra;
    @FXML private TextField txtCostoManoObra;
    
    @FXML private Label lblTotalRefacciones;
    @FXML private Label lblCostoTotal;
    
    @FXML private Button btnGuardarCotizacion;

    public VistaCotizacion() {
        // Constructor vacío
    }

    // =====================================================================
    // INICIALIZADOR FXML 
    // =====================================================================
    @FXML
    public void initialize() {
        // Se ejecuta automáticamente al cargar el FXML
        txtCantidadRefaccion.setText("1"); // Valor por defecto
    }

    // =====================================================================
    // CONFIGURACIÓN DE LA VENTANA
    // =====================================================================
    private void initializeUI() {
        if (initialized) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Generar Nueva Cotización");

            // Asegúrate de que el nombre del archivo coincida con el que guardaste
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-cotizacion.fxml"));
            loader.setController(this);
            
            Scene scene = new Scene(loader.load(), 600, 650);
            stage.setScene(scene);

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el archivo FXML: " + e.getMessage(), e);
        }
    }

    /**
     * Equivalente al mostrarInventario() de tu otro ejemplo. 
     * Muestra la ventana y arranca el caso de uso.
     */
    public void iniciar() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::iniciar);
            return;
        }

        initializeUI();
        
        // Limpiamos los campos en caso de que se vuelva a abrir la ventana
        listaConceptosVista.getItems().clear();
        txtFallas.clear();
        txtManoObra.clear();
        txtCostoManoObra.clear();
        lblTotalRefacciones.setText("$0.0");
        lblCostoTotal.setText("$0.0");

        stage.show();
        
        // Le decimos al control que arranque su proceso (para que llame a mostrarClientes)
        if (control != null) {
            control.iniciarVistaCotizacion(this); 
        }
    }

    // =====================================================================
    // MÉTODOS LLAMADOS POR EL CONTROL (Actualizan la vista)
    // =====================================================================

    public void mostrarClientes(List<Cliente> clientes) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarClientes(clientes));
            return;
        }
        comboClientes.getItems().clear();
        comboClientes.getItems().addAll(clientes);
    }

    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarVehiculos(vehiculos));
            return;
        }
        comboVehiculos.getItems().clear();
        comboVehiculos.getItems().addAll(vehiculos);
    }

    public void actualizarEtiquetasTotales(float costoRefacciones, float costoTotal) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.actualizarEtiquetasTotales(costoRefacciones, costoTotal));
            return;
        }
        lblTotalRefacciones.setText("$" + costoRefacciones);
        lblCostoTotal.setText("$" + costoTotal);
    }

    // =====================================================================
    // MANEJADORES DE EVENTOS FXML (Acciones del usuario)
    // =====================================================================

    @FXML
    public void onClienteSeleccionado() {
        Cliente seleccionado = comboClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null && control != null) {
            control.clienteSeleccionado(seleccionado);
        }
    }

    @FXML
    public void onVehiculoSeleccionado() {
        Cliente cliente = comboClientes.getSelectionModel().getSelectedItem();
        Vehiculo vehiculo = comboVehiculos.getSelectionModel().getSelectedItem();
        
        // Aquí arranca el borrador cuando el control esté listo
        /* if (cliente != null && vehiculo != null && control != null) {
            control.iniciarNuevaCotizacion(cliente, vehiculo);
        } */
    }

    @FXML
    public void onAgregarRefaccionClick() {
        String nombrePieza = txtBuscarRefaccion.getText();
        String cantTexto = txtCantidadRefaccion.getText();
        
        if (nombrePieza.isEmpty() || cantTexto.isEmpty()) {
            mostrarMensajeError("Ingrese nombre y cantidad de la refacción.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantTexto);
            
            if (control != null) {
                Refaccion encontrada = control.buscarRefaccion(nombrePieza);
                
                if (encontrada != null) {
                    control.agregarRefaccionACotizacion(encontrada, cantidad);
                    
                    listaConceptosVista.getItems().add(cantidad + "x " + encontrada.getNombre() + " - $" + encontrada.getPrecio());
                    
                    txtBuscarRefaccion.clear();
                    txtCantidadRefaccion.setText("1"); // Restaurar a 1
                } else {
                    mostrarMensajeError("Refacción no encontrada en el inventario.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("La cantidad debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            mostrarMensajeError(e.getMessage());
        }
    } 

    @FXML
    public void onActualizarServicio() {
        String fallas = txtFallas.getText();
        String manoObra = txtManoObra.getText();
        
        float costoManoObra = 0;
        if (!txtCostoManoObra.getText().isEmpty()) {
            try {
                costoManoObra = Float.parseFloat(txtCostoManoObra.getText());
            } catch (NumberFormatException e) {
                // Ignorar si el usuario teclea algo que no sea un número mientras escribe
            }
        }
        
        if (control != null) {
            control.capturarDatosServicio(fallas, manoObra, costoManoObra);
        }
    }

    @FXML
    public void onGuardarClick() {
        onActualizarServicio(); 
        
        if (control != null) {
            control.finalizarCotizacion();
            mostrarMensajeExito("¡Cotización generada y guardada con éxito!");
            
            if (stage != null) {
                stage.close(); // Cierra la ventana tras guardar
            }
        }
    }

    // =====================================================================
    // UTILIDADES
    // =====================================================================

    private void mostrarMensajeError(String mensaje) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarMensajeError(mensaje));
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarMensajeExito(String mensaje) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarMensajeExito(mensaje));
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}