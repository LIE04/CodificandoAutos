package mx.uam.ayd.proyecto.presentacion.controlCalidad;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Ventana para la Verificación de Escáner (HU-40) usando JavaFX y FXML
 * 
 * @author Erik LIE04
 */
@Component
public class VentanaControlCalidad {

    private Stage stage;
    private ControlControlCalidad control;
    private boolean initialized = false;
    
    private Reparacion reparacionActual;
    private boolean esEscaneoLimpio = false;

    // Componentes inyectados desde el archivo FXML
    @FXML private Button btnEscaneoLimpio;
    @FXML private Button btnPresentaFallas;
    @FXML private TextField txtCodigosPersistentes;
    @FXML private Label lblInstruccionCodigos;
    @FXML private Button btnGuardarContinuar;

    public VentanaControlCalidad() {
        // La inicialización de la UI se delega al hilo de JavaFX
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
            stage.setTitle("Verificación de Escáner");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-control-calidad.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 450);
            stage.setScene(scene);
            
            // Estado inicial oculto para el campo de fallas
            lblInstruccionCodigos.setVisible(false);
            txtCodigosPersistentes.setVisible(false);
            
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestra(ControlControlCalidad control, Reparacion reparacion) {
        this.control = control;
        this.reparacionActual = reparacion;
        
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, reparacion));
            return;
        }
        
        initializeUI();
        
        // Reiniciamos la vista por si se abre una segunda vez en la misma sesión
        esEscaneoLimpio = false;
        txtCodigosPersistentes.setText("");
        lblInstruccionCodigos.setVisible(false);
        txtCodigosPersistentes.setVisible(false);
        btnEscaneoLimpio.setStyle("-fx-background-color: #228B22; -fx-text-fill: white;");
        btnPresentaFallas.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white;");
        
        stage.show();
    }

    // --- FXML Event Handlers ---

    @FXML
    private void handleEscaneoLimpio() {
        esEscaneoLimpio = true;
        lblInstruccionCodigos.setVisible(false);
        txtCodigosPersistentes.setVisible(false);
        txtCodigosPersistentes.setText("");
        
        // Estilo visual de selección para JavaFX
        btnEscaneoLimpio.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-background-color: #228B22; -fx-text-fill: white;");
        btnPresentaFallas.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white;");
    }

    @FXML
    private void handlePresentaFallas() {
        esEscaneoLimpio = false;
        lblInstruccionCodigos.setVisible(true);
        txtCodigosPersistentes.setVisible(true);
        
        // Estilo visual de selección para JavaFX
        btnPresentaFallas.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-background-color: #DC143C; -fx-text-fill: white;");
        btnEscaneoLimpio.setStyle("-fx-background-color: #228B22; -fx-text-fill: white;");
    }

    @FXML
    private void handleGuardarContinuar() {
        if (esEscaneoLimpio) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de marcar el escaneo como limpio? El vehículo pasará a entrega.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                control.registrarEscaneoLimpio(reparacionActual.getIdReparacion());
            }
        } else {
            String codigos = txtCodigosPersistentes.getText();
            if (codigos == null || codigos.trim().isEmpty()) {
                muestraError("Debe ingresar los códigos de falla detectados.");
            } else {
                control.registrarFallasPersistentes(reparacionActual.getIdReparacion(), codigos);
            }
        }
    }

    // --- Métodos de respuesta y cierre gestionados en el hilo correcto ---

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
    
    public void muestraMensajeAdvertencia(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
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