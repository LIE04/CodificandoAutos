package mx.uam.ayd.proyecto.presentacion.controlCalidad;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Ventana para la Verificación de Escáner y Control de Calidad (HU-40)
 * Implementada con JavaFX y adaptada para validación de checklist manual.
 * 
 * @author Erik LIE04
 */
@Component
public class VentanaControlCalidad {

    private Stage stage;
    private ControlControlCalidad control;
    private boolean initialized = false;
    
    // Variables de estado
    private int idReparacionActual;
    private boolean esEscaneoLimpio = false;
    private List<CheckBox> listaCasillas = new ArrayList<>();

    // Componentes inyectados desde el archivo FXML
    @FXML private VBox vboxChecklist;
    @FXML private Button btnEscaneoLimpio;
    @FXML private Button btnPresentaFallas;
    @FXML private TextField txtCodigosPersistentes;
    @FXML private Label lblInstruccionCodigos;
    @FXML private Button btnGuardarContinuar;

    public VentanaControlCalidad() {
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
            stage.setTitle("Control de Calidad - Checklist de Reparaciones");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-control-calidad.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 650, 550); // Ligeramente más grande para acomodar la lista
            stage.setScene(scene);
            
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia la ventana utilizando el mock de datos temporales.
     */
    public void muestraConMock(ControlControlCalidad control, int idReparacion, List<String> fallas) {
        this.control = control;
        this.idReparacionActual = idReparacion;
        
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestraConMock(control, idReparacion, fallas));
            return;
        }
        
        initializeUI();
        reiniciarVista();

        // 1. Apagamos el botón verde por defecto (Regla de validación)
        btnEscaneoLimpio.setDisable(true);
        
        // 2. Limpiamos el contenedor y la lista de casillas
        vboxChecklist.getChildren().clear();
        listaCasillas.clear();

        // 3. Generamos los Checkboxes dinámicamente
        for (String descripcionFalla : fallas) {
            CheckBox checkBox = new CheckBox(descripcionFalla);
            checkBox.setFont(new Font("System", 14));
            
            // Cada vez que hagan clic, validamos el total
            checkBox.setOnAction(e -> validarChecklistCompleto());
            
            listaCasillas.add(checkBox);
            vboxChecklist.getChildren().add(checkBox);
        }

        stage.show();
    }

    private void reiniciarVista() {
        esEscaneoLimpio = false;
        txtCodigosPersistentes.setText("");
        lblInstruccionCodigos.setVisible(false);
        txtCodigosPersistentes.setVisible(false);
        btnEscaneoLimpio.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        btnPresentaFallas.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
    }

    /**
     * Valida si todas las casillas están palomeadas para habilitar el botón verde
     */
    private void validarChecklistCompleto() {
        boolean todasMarcadas = true;
        for (CheckBox cb : listaCasillas) {
            if (!cb.isSelected()) {
                todasMarcadas = false;
                break;
            }
        }
        btnEscaneoLimpio.setDisable(!todasMarcadas);
        
        // Si desmarcan una, quitamos la selección visual del botón por precaución
        if (!todasMarcadas) {
            esEscaneoLimpio = false;
            btnEscaneoLimpio.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        }
    }

    // --- FXML Event Handlers ---

    @FXML
    private void handleEscaneoLimpio() {
        esEscaneoLimpio = true;
        lblInstruccionCodigos.setVisible(false);
        txtCodigosPersistentes.setVisible(false);
        txtCodigosPersistentes.setText("");
        
        btnEscaneoLimpio.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-background-color: #228B22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        btnPresentaFallas.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
    }

    @FXML
    private void handlePresentaFallas() {
        esEscaneoLimpio = false;
        lblInstruccionCodigos.setVisible(true);
        txtCodigosPersistentes.setVisible(true);
        
        btnPresentaFallas.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-background-color: #DC143C; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        btnEscaneoLimpio.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
    }

    @FXML
    private void handleGuardarContinuar() {
        if (esEscaneoLimpio) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de marcar el control de calidad como exitoso? El vehículo pasará a entrega.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                control.registrarEscaneoLimpio(idReparacionActual);
            }
        } else {
            String fallasExtra = txtCodigosPersistentes.getText();
            // Validamos que si apretó el botón rojo, no envíe el texto vacío
            if (fallasExtra == null || fallasExtra.trim().isEmpty()) {
                muestraError("Debe ingresar el detalle de las fallas adicionales detectadas.");
            } else {
                control.registrarFallasPersistentes(idReparacionActual, fallasExtra);
            }
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