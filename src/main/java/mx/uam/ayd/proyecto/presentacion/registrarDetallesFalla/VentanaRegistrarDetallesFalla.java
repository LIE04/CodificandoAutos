package mx.uam.ayd.proyecto.presentacion.registrarDetallesFalla;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class VentanaRegistrarDetallesFalla {

    private ControlRegistrarDetallesFalla control;
    private Stage stage;

    @FXML private Label lbMarca;
    @FXML private Label lbModelo;
    @FXML private Label lbAño;
    @FXML private Label lbPlacas;
    @FXML private Label lbKilometraje;
    @FXML private TextField txtDescripcionFalla;
    @FXML private ComboBox<String> cbEstadoReparacion;

    public void muestra(ControlRegistrarDetallesFalla control) {
        this.control = control;
        
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control));
            return;
        }

        try {
            stage = new Stage();
            // Asegúrate de que el FXML esté en la carpeta resources/fxml/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-descripcion-fallas.fxml"));
            
            // Inyección manual del controlador (Tu regla estricta de UI)
            loader.setController(this);
            
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Registrar Detalles de Falla");

            // Inicializar el ComboBox con opciones
            cbEstadoReparacion.setItems(FXCollections.observableArrayList(
                "Pendiente", "En revisión", "Reparado"
            ));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAgregarFalla() {
        String descripcion = txtDescripcionFalla.getText();
        String estado = cbEstadoReparacion.getValue();
        control.agregarFalla(descripcion, estado);
    }

    public void muestraMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        if (titulo.contains("Error")) {
            alert.setAlertType(AlertType.ERROR);
        }
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cierra() {
        stage.close();
    }
}