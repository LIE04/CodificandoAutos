package mx.uam.ayd.proyecto.presentacion.informeAtrasos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Ventana para la Notificación de Atraso al Cliente (HU-34)
 * Implementada con JavaFX.
 * 
 * @author Erik LIE04
 */
@Component
public class VentanaInformeAtrasos {

    private Stage stage;
    private ControlInformeAtrasos control;
    private boolean initialized = false;

    // INICIO Modificación realizada por Erik para la HU-34
    // Se reemplazaron lblVehiculo y lblFolio por el nuevo ComboBox para los vehículos.
    @FXML private ComboBox<String> cmbVehiculosAtraso;
    // FIN Modificación Erik HU-34
    
    @FXML private ComboBox<String> cmbMotivosAtraso;
    @FXML private Button btnCancelar;
    @FXML private Button btnEnviarAviso;

    public VentanaInformeAtrasos() {
        // La inicialización se delega al hilo de JavaFX
    }

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
            stage.setTitle("Informe de Atrasos - OmniTaller");
            
            // Asegúrate de que tu archivo FXML se llame así y esté en la carpeta /fxml/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-informe-atrasos.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 550, 400); 
            stage.setScene(scene);
            
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * INICIO Modificación realizada por Erik para la HU-34
     * La ventana ahora recibe la lista completa de reparaciones para llenar el ComboBox.
     */
    public void muestra(ControlInformeAtrasos control, List<Reparacion> reparaciones, List<String> motivos) {
        this.control = control;
        
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, reparaciones, motivos));
            return;
        }
        
        initializeUI();

        // 1. Limpiamos y llenamos el ComboBox de Vehículos
        if (cmbVehiculosAtraso != null) {
            cmbVehiculosAtraso.getItems().clear();
            for (Reparacion rep : reparaciones) {
                Vehiculo v = rep.getVehiculo();
                // Creamos un formato fácil de leer y del cual podamos extraer el ID luego
                // Formato: "ID - Marca Modelo (Placas)"
                String displayText = rep.getIdReparacion() + " - " + v.getMarca() + " " + v.getModelo() + " (" + v.getPlacas() + ")";
                cmbVehiculosAtraso.getItems().add(displayText);
            }
        }

        // 2. Limpiamos y llenamos el ComboBox con las plantillas del servicio
        if (cmbMotivosAtraso != null) {
            cmbMotivosAtraso.getItems().clear();
            cmbMotivosAtraso.getItems().addAll(motivos);
        }

        stage.show();
    }
    // FIN Modificación Erik HU-34

    // --- FXML Event Handlers ---

    @FXML
    private void handleCancelar() {
        cerrar();
    }

    @FXML
    private void handleEnviarAviso() {
        String vehiculoSeleccionado = cmbVehiculosAtraso.getValue();
        String motivoSeleccionado = cmbMotivosAtraso.getValue();
        
        // INICIO Modificación realizada por Erik para la HU-34
        // Validamos que el usuario haya seleccionado un vehículo del nuevo ComboBox
        if (vehiculoSeleccionado == null || vehiculoSeleccionado.trim().isEmpty()) {
            muestraError("Por favor, seleccione un vehículo de la lista desplegable.");
            return;
        }
        // FIN Modificación Erik HU-34

        if (motivoSeleccionado == null || motivoSeleccionado.trim().isEmpty()) {
            muestraError("Por favor, seleccione un motivo de atraso de la lista desplegable.");
            return;
        }

        // Extraemos el ID del texto del ComboBox (Ej. de "14 - Nissan Sentra" a "14")
        int idReparacion = Integer.parseInt(vehiculoSeleccionado.split(" - ")[0]);

        // Pedimos confirmación al usuario
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Envío");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea registrar y enviar este aviso al cliente?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            control.enviarAviso(idReparacion, motivoSeleccionado);
        }
    }

    // --- Métodos de mensajes en el hilo de JavaFX ---

    public void muestraMensajeExito(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    public void muestraError(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }
    
    public void cerrar() {
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }
}