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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Callback;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-agendar-cita-clientes.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            
            inicializarComponentes();
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        btnGuardarCita.setDisable(true); 

        //Bloquear las fechas festivas o domingos
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Si la fecha es en el pasado, es domingo, o es festivo -> deshabilitar
                if (item.isBefore(LocalDate.now()) || 
                    item.getDayOfWeek() == DayOfWeek.SUNDAY || 
                    esDiaFestivo(item)) {
                    
                    setDisable(true); // Bloquea el clic
                    setStyle("-fx-background-color: #ffc0cb;"); // Pone la celda rojita/rosa
                }
            }
        };
        dpFecha.setDayCellFactory(dayCellFactory); // Le aplicamos la regla al calendario
        // ------------------------------------------

        // Llenar años 
        int anioActual = LocalDate.now().getYear();
        for (int i = anioActual + 1; i >= 1990; i--) {
            cbAnio.getItems().add(i);
        }

        // --- HORARIO DE 9:00 AM A 5:30 PM ---
        cbHora.getItems().clear(); // Limpiar por precaución
        for (int i = 9; i <= 17; i++) {
            cbHora.getItems().add(LocalTime.of(i, 0));  // Agrega las horas en punto (ej. 17:00)
            cbHora.getItems().add(LocalTime.of(i, 30)); // Agrega las medias horas (ej. 17:30)
        }
        // Como el ciclo termina en 18, la última hora añadida será exactamente las 17:30 (5:30 PM)
    }
    

    @FXML
    private void verificarProgreso() {
        //Validar que los campos no estén vacíos
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

    /**
     * Calcula si una fecha es día de asueto obligatorio en México
     */
    private boolean esDiaFestivo(LocalDate fecha) {
        int mes = fecha.getMonthValue();
        int dia = fecha.getDayOfMonth();
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        // Festivos de fecha exacta
        if (mes == 1 && dia == 1) return true; // Año Nuevo
        if (mes == 5 && dia == 1) return true; // Día del Trabajo
        if (mes == 9 && dia == 16) return true; // Independencia
        if (mes == 12 && dia == 25) return true; // Navidad

        // Festivos de fecha móvil (Lunes)
        // 1er Lunes de Febrero (Constitución)
        if (mes == 2 && diaSemana == DayOfWeek.MONDAY && dia <= 7) return true;
        // 3er Lunes de Marzo (Natalicio Benito Juárez)
        if (mes == 3 && diaSemana == DayOfWeek.MONDAY && dia >= 15 && dia <= 21) return true;
        // 3er Lunes de Noviembre (Revolución)
        if (mes == 11 && diaSemana == DayOfWeek.MONDAY && dia >= 15 && dia <= 21) return true;
        
        // Transición del Poder Ejecutivo (1 de Octubre, cada 6 años empezando en 2024)
        if (mes == 10 && dia == 1 && (fecha.getYear() - 2024) % 6 == 0) return true;

        return false;
    }
}