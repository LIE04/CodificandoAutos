package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VistaCotizacion {

    // --- Elementos de JavaFX (Vinculados a tu FXML) ---
    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Vehiculo> comboVehiculos;
    
    // Panel de Refacciones
    @FXML private TextField txtBuscarPieza;
    @FXML private Button btnBuscarPieza;
    @FXML private ListView<Refaccion> listRefaccionesEncontradas; 
    @FXML private TextField txtCantidadRefaccion;
    @FXML private Button btnAgregarRefaccion;
    
    // Panel de Servicios
    @FXML private TextArea txtFallas;
    @FXML private TextArea txtManoObra;
    @FXML private TextField txtCostoManoObra;
    @FXML private Button btnActualizarServicio;
    
    // Totales y Guardar
    @FXML private Label lblSubtotal;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;
    @FXML private Button btnGuardarCotizacion;

    private ControlCotizacion control;

    // --- Métodos de configuración inicial ---
    public void iniciar(ControlCotizacion control) {
        this.control = control;

        try {
            // 1. Cargar el archivo FXML (Asegúrate de que la ruta sea correcta según tu proyecto)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-cotizacion.fxml")); 
            
            // 2. ¡EL PASO CLAVE! Le decimos a JavaFX: "Usa ESTA instancia de Spring como controlador"
            loader.setController(this);
            
            // 3. Crear la ventana
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrar Cotización");
            
            // 4. Mostrar la ventana
            stage.show();
            
            // 5. AHORA SÍ, los elementos ya no son null, podemos bloquear y configurar
            bloquearEdicion();
            configurarEventos();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al cargar la ventana de cotización.");
        }

        
    }

    private void configurarEventos() {
        // Evento cuando el usuario elige un cliente en el ComboBox
        comboClientes.setOnAction(event -> {
            Cliente seleccionado = comboClientes.getSelectionModel().getSelectedItem();
            control.onClienteSeleccionado(seleccionado);
        });

        // Evento cuando el usuario elige un vehículo en el ComboBox
        comboVehiculos.setOnAction(event -> {
            Vehiculo seleccionado = comboVehiculos.getSelectionModel().getSelectedItem();
            control.onVehiculoSeleccionado(seleccionado);
        });
    }

    // --- Métodos llamados por el Controlador (Flujo hacia la UI) ---
    public void mostrarClientes(List<Cliente> clientesDisponibles) {
        comboClientes.setItems(FXCollections.observableArrayList(clientesDisponibles));
    }

    public void mostrarVehiculos(List<Vehiculo> vehiculosDelCliente) {
        comboVehiculos.setItems(FXCollections.observableArrayList(vehiculosDelCliente));
        comboVehiculos.setDisable(false);
    }

    public void permitirEdicion() {
        // Habilita los campos tras validar la cita del vehículo
        txtBuscarPieza.setDisable(false);
        btnBuscarPieza.setDisable(false);
        listRefaccionesEncontradas.setDisable(false);
        txtCantidadRefaccion.setDisable(false);
        btnAgregarRefaccion.setDisable(false);
        
        txtFallas.setDisable(false);
        txtManoObra.setDisable(false);
        txtCostoManoObra.setDisable(false);
        btnActualizarServicio.setDisable(false);
        btnGuardarCotizacion.setDisable(false);
    }

    private void bloquearEdicion() {
        comboVehiculos.setDisable(true);
        txtBuscarPieza.setDisable(true);
        btnBuscarPieza.setDisable(true);
        listRefaccionesEncontradas.setDisable(true);
        txtCantidadRefaccion.setDisable(true);
        btnAgregarRefaccion.setDisable(true);
        txtFallas.setDisable(true);
        txtManoObra.setDisable(true);
        txtCostoManoObra.setDisable(true);
        btnActualizarServicio.setDisable(true);
        btnGuardarCotizacion.setDisable(true);
    }

    public void mostrarRefaccion(List<Refaccion> encontrada) {
        listRefaccionesEncontradas.setItems(FXCollections.observableArrayList(encontrada));
    }

    public void recalcularTotales() {
        // Aquí puedes realizar el cálculo visual sumando los elementos en pantalla
        // o si prefieres, el controlador podría devolverte los totales ya calculados.
        actualizarEtiquetasTotales(0.0f, 0.0f, 0.0f); // Valores de ejemplo
    }

    private void actualizarEtiquetasTotales(float subtotal, float iva, float total) {
        lblSubtotal.setText("$ " + String.format("%.2f", subtotal));
        lblIva.setText("$ " + String.format("%.2f", iva));
        lblTotal.setText("$ " + String.format("%.2f", total));
    }

    public void mostrarMensajeExito() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Cotización guardada exitosamente.");
        alert.showAndWait();
        // Aquí podrías limpiar la ventana o cerrarla
    }

    public void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- Métodos FXML (Disparados desde botones en la UI) ---

    @FXML
    public void accionBuscarRefaccion() {
        String nombrePieza = txtBuscarPieza.getText();
        control.onBuscarRefaccionClick(nombrePieza);
    }

    @FXML
    public void accionAgregarRefaccion() {
        Refaccion seleccionada = listRefaccionesEncontradas.getSelectionModel().getSelectedItem();
        try {
            int cantidad = Integer.parseInt(txtCantidadRefaccion.getText());
            control.onAgregarRefaccion(seleccionada, cantidad);
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese una cantidad válida.");
        }
    }

    @FXML
    public void accionActualizarServicio() {
        String fallas = txtFallas.getText();
        String manoObra = txtManoObra.getText();
        try {
            float costo = Float.parseFloat(txtCostoManoObra.getText());
            control.onActualizarServicio(fallas, manoObra, costo);
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese un costo válido para la mano de obra.");
        }
    }

    @FXML
    public void accionGuardarCotizacion() {
        control.onGuardarClick();
    }
}