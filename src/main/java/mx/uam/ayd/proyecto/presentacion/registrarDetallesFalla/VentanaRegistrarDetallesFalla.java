package mx.uam.ayd.proyecto.presentacion.registrarDetallesFalla;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@Component
public class VentanaRegistrarDetallesFalla {

    private ControlRegistrarDetallesFalla control;
    private Stage stage;

    @FXML private ComboBox<Vehiculo> cbVehiculos;
    @FXML private ComboBox<String> cbEstadoReparacion;
    @FXML private TextField txtDescripcionFalla;
    
    @FXML private Label lbMarca;
    @FXML private Label lbModelo;
    @FXML private Label lbAño;
    @FXML private Label lbPlacas;
    @FXML private Label lbKilometraje;

    public void muestra(ControlRegistrarDetallesFalla control, List<Vehiculo> vehiculos) {
        this.control = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, vehiculos));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-descripcion-fallas.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            // Configurar el ComboBox de Vehículos
            cbVehiculos.setItems(FXCollections.observableArrayList(vehiculos));
            cbVehiculos.setConverter(new StringConverter<Vehiculo>() {
                @Override
                public String toString(Vehiculo v) {
                    if (v == null) return "";
                    // Ajusta "getCliente().getNombre()" según tu modelo real
                    String nombreCliente = v.getCliente() != null ? v.getCliente().getNombre() : "Sin cliente";
                    return nombreCliente + " - " + v.getMarca() + " " + v.getModelo() + " (" + v.getPlacas() + ")";
                }
                @Override
                public Vehiculo fromString(String string) { return null; }
            });

            // Configurar el ComboBox de Estatus
            cbEstadoReparacion.setItems(FXCollections.observableArrayList(
                "En espera", "Listo para entregar", "Entregado"
            ));

            stage = new Stage();
            stage.setTitle("Registrar Detalles de Falla");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVehiculoSeleccionado() {
        Vehiculo vehiculoSeleccionado = cbVehiculos.getValue();
        
        if (vehiculoSeleccionado != null) {
            lbMarca.setText(vehiculoSeleccionado.getMarca());
            lbModelo.setText(vehiculoSeleccionado.getModelo());
            lbAño.setText(String.valueOf(vehiculoSeleccionado.getAnio()));
            lbPlacas.setText(vehiculoSeleccionado.getPlacas());
            lbKilometraje.setText(String.valueOf(vehiculoSeleccionado.getKilometraje()));
        }
    }

    @FXML
    private void handleAgregarFalla() {
        Vehiculo vehiculoSeleccionado = cbVehiculos.getValue();
        String descripcion = txtDescripcionFalla.getText();
        String estado = cbEstadoReparacion.getValue();

        // Validaciones visuales antes de mandar al control
        if (vehiculoSeleccionado == null) {
            muestraMensaje("Error", "Por favor, selecciona un vehículo de la lista.");
            return;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            muestraMensaje("Error", "La descripción de la falla no puede estar vacía.");
            return;
        }
        if (estado == null || estado.isEmpty()) {
            muestraMensaje("Error", "Por favor, selecciona el estado de la reparación.");
            return;
        }

        // Enviamos todo al control
        control.agregarFalla(descripcion, estado, vehiculoSeleccionado);
    }

    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }

    public void muestraMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}