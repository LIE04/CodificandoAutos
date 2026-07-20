package mx.uam.ayd.proyecto.presentacion.listarInventario;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 * Ventana para consultar y editar el inventario de refacciones (HU-12) usando JavaFX con FXML.
 */
@Component
public class VistaInventario {

    private Stage stage;
    private ControlInventario control;

    @FXML
    private TextField textFieldBusqueda;

    @FXML
    private TableView<Refaccion> tableRefacciones;

    // Columnas adaptadas al modelo Refaccion
    @FXML
    private TableColumn<Refaccion, String> idColumn;

    @FXML
    private TableColumn<Refaccion, String> nombreColumn;

    @FXML
    private TableColumn<Refaccion, Double> precioColumn;

    @FXML
    private TableColumn<Refaccion, Integer> existenciasColumn;

    private boolean initialized = false;

    public VistaInventario() {
        
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
            stage.setTitle("Inventario de Refacciones");

            // Asegúrate de crear este archivo en tu carpeta de resources/fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-consultar-inventario.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 450);
            stage.setScene(scene);

            // Vinculación de columnas con los atributos de la clase Refaccion
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
            existenciasColumn.setCellValueFactory(new PropertyValueFactory<>("existencias"));

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlInventario(ControlInventario control) {
        this.control = control;
    }

    /**
     * Equivalente a mostrarInventario(refaccion) del diagrama.
     */
    public void mostrarInventario(List<Refaccion> refacciones) {
        if (!Platform.isFxApplicationThread()) {
           Platform.runLater(() -> this.mostrarInventario(refacciones)); 
           return;
        }

        initializeUI();
        textFieldBusqueda.setText("");
        
        ObservableList<Refaccion> data = FXCollections.observableArrayList(refacciones);
        tableRefacciones.setItems(data);

        stage.show();
    }

    /**
     * Equivalente a retornarCoincidencias(coincidencias) del diagrama.
     */
    public void retornarCoincidencias(List<Refaccion> coincidencias) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.retornarCoincidencias(coincidencias));
            return;
        }

        ObservableList<Refaccion> data = FXCollections.observableArrayList(coincidencias);
        tableRefacciones.setItems(data);
    }

    /**
     * Equivalente a mostrarEdicion() del diagrama.
     */
    public void mostrarEdicion() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::mostrarEdicion);
            return;
        }

        Refaccion seleccionada = tableRefacciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            muestraDialogoConMensaje("Selecciona una refacción de la tabla para editar");
            return;
        }

        // Aquí idealmente abrirías otra ventana pequeña (Dialog) con campos de texto para editar.
        // A modo de ejemplo, mostramos el aviso.
        muestraDialogoConMensaje("Abriendo ventana de edición para: " + seleccionada.getNombre());
        
        // Simulación de que el usuario editó y presionó "Guardar" en esa nueva ventana:
        // control.verificarEdicion(seleccionada.getId(), "Nuevo Nombre", 150.0, 10);
    }

    /**
     * Equivalente a actualizarLista() del diagrama.
     */
    public void actualizarLista() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::actualizarLista);
            return;
        }
        
        muestraDialogoConMensaje("¡La refacción se actualizó correctamente!");
        // Volvemos a pedirle al control que cargue la lista fresca desde la base de datos
        control.SolicitarInventario(); 
    }

    // =====================================================================
    // MANEJADORES DE EVENTOS FXML (Llamados desde la interfaz gráfica)
    // =====================================================================

    @FXML
    private void handleBuscar() {
        // Le pasamos el texto al control para que haga el filtrado interno
        control.buscarRefaccion(textFieldBusqueda.getText());
    }

    @FXML
    private void handlePresionaEditar() {
        // Dispara la acción hacia el control según tu diagrama de secuencia
        control.solicitarEdicion();
    }

    @FXML
    private void handleCerrar() {
        stage.hide();
    }

    private void muestraDialogoConMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}