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
    private TableColumn<Refaccion, Integer> idColumn;

    @FXML
    private TableColumn<Refaccion, String> nombreColumn;

    @FXML
    private TableColumn<Refaccion, Float> precioColumn;

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
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idRefaccion"));
            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
            existenciasColumn.setCellValueFactory(new PropertyValueFactory<>("existencia"));

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
        // 1. Obtener la refacción seleccionada de la tabla
        Refaccion seleccionada = tableRefacciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            muestraDialogoConMensaje("Por favor, selecciona una refacción de la tabla para editar.");
            return;
        }

        control.solicitarEdicion(seleccionada);
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