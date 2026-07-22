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

@Component
public class VistaEditarRefaccion {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExistencia;

    private ControlInventario control;
    private Stage stage;

    public void inicia(ControlInventario control, Refaccion refaccion) {

        this.control = control;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> inicia(control, refaccion));
            return;
        }

        try {
            this.stage = new Stage();
            this.stage.setTitle("Modificar Refacción");

            // Carga el archivo FXML que acabamos de crear
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

    @FXML
    private void handleCancelar() {
        stage.close();
    }

        private void muestraDialogoConMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
