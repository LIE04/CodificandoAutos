package mx.uam.ayd.proyecto.presentacion.registrarCita;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class VentanaRegistrarCita {

    private Stage stage;
    private ControlRegistrarCita control;

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private ComboBox<Integer> cbAnio;
    @FXML private TextField txtPlacas;
    @FXML private TextField txtKilometraje;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<LocalTime> cbHora;
    @FXML private Button btnGuardarCita;

    public void muestra(ControlRegistrarCita control) {
        this.control = control;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control));
            return;
        }
        
        try {
            stage = new Stage();
            stage.setTitle("Registrar Nueva Cita");
            // Asegúrate de que el FXML esté en la carpeta resources/fxml/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-agendar-cita-clientes.fxml"));
            loader.setController(this); // Nosotros somos el controlador
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            
            inicializarComponentes();
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        btnGuardarCita.setDisable(true); // Deshabilitado por defecto
        
        // Llenar años (ej. del 2000 al año actual + 1)
        int anioActual = LocalDate.now().getYear();
        for (int i = anioActual + 1; i >= 1990; i--) {
            cbAnio.getItems().add(i);
        }

        // Llenar horas disponibles (ej. 9 AM a 5 PM cada hora)
        for (int i = 9; i <= 17; i++) {
            cbHora.getItems().add(LocalTime.of(i, 0));
            cbHora.getItems().add(LocalTime.of(i, 30));
        }
    }

    @FXML
    private void verificarProgreso() {
        // Valida que los campos no estén vacíos
        boolean camposLlenos = txtNombre.getText() != null && !txtNombre.getText().trim().isEmpty() &&
                txtTelefono.getText() != null && !txtTelefono.getText().trim().isEmpty() &&
                txtMarca.getText() != null && !txtMarca.getText().trim().isEmpty() &&
                txtModelo.getText() != null && !txtModelo.getText().trim().isEmpty() &&
                cbAnio.getValue() != null &&
                txtPlacas.getText() != null && !txtPlacas.getText().trim().isEmpty() &&
                txtKilometraje.getText() != null && !txtKilometraje.getText().trim().isEmpty() &&
                dpFecha.getValue() != null &&
                cbHora.getValue() != null;

        btnGuardarCita.setDisable(!camposLlenos);
    }

    @FXML
    private void subirDatos() {
        try {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            int anio = cbAnio.getValue();
            String placas = txtPlacas.getText().trim();
            double kilometraje = Double.parseDouble(txtKilometraje.getText().trim());
            LocalDate fecha = dpFecha.getValue();
            LocalTime hora = cbHora.getValue();

            control.registrarCitaCompleta(nombre, telefono, marca, modelo, anio, placas, kilometraje, fecha, hora);
        } catch (NumberFormatException e) {
            mostrarMensajeError("El kilometraje debe ser un número válido.");
        }
    }

    public void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cerrar() {
        if (stage != null) {
            stage.close();
        }
    }
}