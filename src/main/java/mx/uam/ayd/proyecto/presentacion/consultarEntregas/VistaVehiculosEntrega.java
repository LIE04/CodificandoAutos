package mx.uam.ayd.proyecto.presentacion.consultarEntregas;

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
import mx.uam.ayd.proyecto.presentacion.listarInventario.ControlInventario;
import mx.uam.ayd.proyecto.datos.ReparacionRepository.VehiculosPendientesDTO;

/**
 * Ventana para consultar y editar el inventario de refacciones (HU-42) usando JavaFX con FXML.
 */
@Component
public class VistaVehiculosEntrega {

    private Stage stage;
    private ControlVehiculosEntrega control;

    @FXML
    private TextField textFieldBusqueda;

    @FXML
    private TableView<VehiculosPendientesDTO> tableVehiculos;

    // Columnas adaptadas a VehiculosPendientesDTO
    @FXML
    private TableColumn<VehiculosPendientesDTO, Integer> idColumn;

    @FXML
    private TableColumn<VehiculosPendientesDTO, String> nombreColumn;

    @FXML
    private TableColumn<VehiculosPendientesDTO, String> marcaColumn;

    @FXML
    private TableColumn<VehiculosPendientesDTO, String> modeloColumn;

    @FXML
    private TableColumn<VehiculosPendientesDTO, String> placasColumn;

    @FXML
    private TableColumn<VehiculosPendientesDTO, String> statusColumn;

    private boolean initialized = false;

    public VistaVehiculosEntrega() {
        
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
            stage.setTitle("Vehiculos por entregar");

            // Asegúrate de crear este archivo en tu carpeta de resources/fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-vehiculos-entregas.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 450);
            stage.setScene(scene);

            // Vinculación de columnas con los atributos de la clase Refaccion
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
            modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            placasColumn.setCellValueFactory(new PropertyValueFactory<>("placas"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("estado de servicio"));

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlVehiculosEntrega(ControlVehiculosEntrega control) {
        this.control = control;
    }

    /**
     * Equivalente a mostrarInventario(refaccion) del diagrama.
     */
    public void mostrarListaVehiculos(List<VehiculosPendientesDTO> vehiculos) {
        if (!Platform.isFxApplicationThread()) {
           Platform.runLater(() -> this.mostrarListaVehiculos(vehiculos)); 
           return;
        }

        initializeUI();
        textFieldBusqueda.setText("");
        
        ObservableList<VehiculosPendientesDTO> data = FXCollections.observableArrayList(vehiculos);
        tableVehiculos.setItems(data);

        stage.show();
    }

    /**
     * Equivalente a retornarCoincidencias(coincidencias) del diagrama.
     */
    public void retornarCoincidencias(List<VehiculosPendientesDTO> coincidencias) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.retornarCoincidencias(coincidencias));
            return;
        }

        ObservableList<VehiculosPendientesDTO> data = FXCollections.observableArrayList(coincidencias);
        tableVehiculos.setItems(data);
    }
}