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
 * Vista para consultar y editar el inventario de refacciones
 */
@Component
public class VistaInventario {

    /** Escenario o ventana principal de la interfaz */
    private Stage stage;
    
    /** Control del módulo de inventario */
    private ControlInventario control;

    /** Campo de texto para ingresar el término de búsqueda */
    @FXML
    private TextField textFieldBusqueda;

    /** Tabla para desplegar la lista de refacciones */
    @FXML
    private TableView<Refaccion> tableRefacciones;

    /** Columna para mostrar el identificador de la refacción */
    @FXML
    private TableColumn<Refaccion, Integer> idColumn;

    /** Columna para mostrar el nombre de la refacción */
    @FXML
    private TableColumn<Refaccion, String> nombreColumn;

    /** Columna para mostrar el precio de la refacción */
    @FXML
    private TableColumn<Refaccion, Float> precioColumn;

    /** Columna para mostrar las existencias de la refacción */
    @FXML
    private TableColumn<Refaccion, Integer> existenciasColumn;

    /** Bandera que indica si la interfaz gráfica ya fue inicializada */
    private boolean initialized = false;

    /**
     * Constructor por defecto de la vista
     */
    public VistaInventario() {
        
    }

    /**
     * Carga la interfaz FXML e inicializa las columnas de la tabla de refacciones
     */
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

    /**
     * Establece la referencia del control asociado a esta vista
     * 
     * @param control Control del inventario
     */
    public void setControlInventario(ControlInventario control) {
        this.control = control;
    }

    /**
     * Muestra la ventana y carga las refacciones recibidas en la tabla
     * 
     * @param refacciones Lista de refacciones a desplegar
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
     * Actualiza la tabla con los resultados filtrados de la búsqueda
     * 
     * @param coincidencias Lista de refacciones que coinciden con la búsqueda
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
     * Despliega un mensaje de éxito y solicita al control refrescar el inventario
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

    /**
     * Maneja el evento de búsqueda tomando el texto ingresado y enviándolo al control
     */
    @FXML
    private void handleBuscar() {
        control.buscarRefaccion(textFieldBusqueda.getText());
    }

    /**
     * Maneja el evento para solicitar la edición de la refacción seleccionada en la tabla
     */
    @FXML
    private void handlePresionaEditar() {
        // Obtener la refacción seleccionada de la tabla
        Refaccion seleccionada = tableRefacciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            muestraDialogoConMensaje("Por favor, selecciona una refacción de la tabla para editar.");
            return;
        }

        control.solicitarEdicion(seleccionada);
    }

    /**
     * Maneja el evento del botón cerrar ocultando la ventana
     */
    @FXML
    private void handleCerrar() {
        stage.hide();
    }

    /**
     * Muestra un diálogo de alerta informativa con un mensaje especificado
     * 
     * @param mensaje Texto a desplegar en el diálogo
     */
    private void muestraDialogoConMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}