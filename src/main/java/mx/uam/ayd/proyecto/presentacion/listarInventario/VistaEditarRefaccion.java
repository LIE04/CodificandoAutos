package mx.uam.ayd.proyecto.presentacion.listarInventario;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import org.springframework.stereotype.Component;

/**
 * Vista para la edición de una refacción
 */
@Component
public class VistaEditarRefaccion {

    /** Campo de texto para el identificador de la refacción */
    @FXML private TextField txtId;
    
    /** Campo de texto para el nombre de la refacción */
    @FXML private TextField txtNombre;
    
    /** Campo de texto para el precio de la refacción */
    @FXML private TextField txtPrecio;
    
    /** Campo de texto para las existencias de la refacción */
    @FXML private TextField txtExistencia;

    /** Control asociado a la vista */
    private ControlInventario control;
    
    /** Escenario o ventana de la interfaz gráfica */
    private Stage stage;

    /**
     * Inicializa y muestra la ventana de edición cargando los datos de la refacción
     * 
     * @param control Control de inventario asociado
     * @param refaccion Refacción seleccionada para modificar
     */
    public void inicia(ControlInventario control, Refaccion refaccion) {

        this.control = control;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> inicia(control, refaccion));
            return;
        }

        try {
            this.stage = new Stage();
            this.stage.setTitle("Modificar Refacción");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-editar-refaccion.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            // Llenar los campos con los datos actuales
            txtId.setText(String.valueOf(refaccion.getIdRefaccion()));
            txtNombre.setText(refaccion.getNombre());
            txtPrecio.setText(String.valueOf(refaccion.getPrecio()));
            txtExistencia.setText(String.valueOf(refaccion.getExistencia()));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento del botón guardar recolectando y enviando los datos modificados al control
     */
    @FXML
    private void handleGuardar() {
        try {
            // Recolectar los datos modificados
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int existencia = Integer.parseInt(txtExistencia.getText());

            // Enviar al control para que actualice la base de datos
            control.verificarEdicion(id, nombre, precio, existencia);
            
            stage.close(); // Cerrar la ventana
        } catch (NumberFormatException e) {
            muestraDialogoConMensaje("Error: El precio y la existencia deben ser números válidos.");
        }
    }

    /**
     * Maneja el evento del botón cancelar cerrando la ventana
     */
    @FXML
    private void handleCancelar() {
        stage.close();
    }

    /**
     * Muestra una ventana de diálogo informativa con el mensaje proporcionado
     * 
     * @param mensaje Texto del mensaje a desplegar en pantalla
     */
    public void muestraDialogoConMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}