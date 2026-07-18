package mx.uam.ayd.proyecto.presentacion.registrarServicio;

import java.time.LocalDate;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.IOException;

import mx.uam.ayd.proyecto.negocio.modelo.Servicio;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Ventana para el historial de servicio (HU-29) usando JavaFX con FXML.
 *
 * El mecánico primero busca un vehículo por sus placas; al encontrarlo, se
 * muestran sus datos junto con el historial de servicios y el formulario
 * para registrar uno nuevo.
 */
@Component
public class VentanaRegistrarServicio {

	private Stage stage;
	private ControlRegistrarServicio control;

	@FXML
	private TextField textFieldPlacas;

	@FXML
	private Label labelVehiculo;

	@FXML
	private Label labelMensaje;

	@FXML
	private DatePicker datePickerFecha;

	@FXML
	private TextField textFieldDescripcion;

	@FXML
	private TextField textFieldPiezasUtilizadas;

	@FXML
	private TextField textFieldCostoManoObra;

	@FXML
	private TextArea textAreaObservaciones;

	@FXML
	private TableView<Servicio> tableHistorial;

	@FXML
	private TableColumn<Servicio, LocalDate> fechaColumn;

	@FXML
	private TableColumn<Servicio, String> descripcionColumn;

	@FXML
	private TableColumn<Servicio, String> piezasColumn;

	@FXML
	private TableColumn<Servicio, Double> costoColumn;

	@FXML
	private TableColumn<Servicio, String> observacionesColumn;

	private boolean initialized = false;

	/**
	 * Constructor without UI initialization
	 */
	public VentanaRegistrarServicio() {
		// Don't initialize JavaFX components in constructor
	}

	/**
	 * Initialize UI components on the JavaFX application thread
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
			stage.setTitle("Historial de Servicio");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-registrar-servicio.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 600, 550);
			stage.setScene(scene);

			fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
			descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
			piezasColumn.setCellValueFactory(new PropertyValueFactory<>("piezasUtilizadas"));
			costoColumn.setCellValueFactory(new PropertyValueFactory<>("costoManoObra"));
			observacionesColumn.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Establece el controlador asociado a esta ventana
	 *
	 * @param control El controlador asociado
	 */
	public void setControlRegistrarServicio(ControlRegistrarServicio control) {
		this.control = control;
	}

	/**
	 * Muestra la ventana en blanco, a la espera de que se busque un vehículo
	 */
	public void muestra() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::muestra);
			return;
		}

		initializeUI();

		textFieldPlacas.setText("");
		labelVehiculo.setText("");
		labelMensaje.setText("");
		limpiaFormulario();
		tableHistorial.setItems(FXCollections.observableArrayList());

		stage.show();
	}

	/**
	 * Muestra la información del vehículo encontrado junto con su historial
	 *
	 * @param vehiculo el vehículo encontrado
	 * @param historial el historial de servicios de ese vehículo
	 */
	public void muestraVehiculo(Vehiculo vehiculo, List<Servicio> historial) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestraVehiculo(vehiculo, historial));
			return;
		}

		String nombreCliente = vehiculo.getCliente() == null ? "sin dueño registrado"
				: vehiculo.getCliente().getNombre();

		labelVehiculo.setText(vehiculo.getMarca() + " " + vehiculo.getModelo() + " (" + vehiculo.getAnio() + ") - "
				+ vehiculo.getPlacas() + " - Cliente: " + nombreCliente);
		labelMensaje.setText("");

		actualizaHistorial(historial);
	}

	/**
	 * Muestra un mensaje indicando que no se encontró ningún vehículo con esas placas
	 */
	public void muestraVehiculoNoEncontrado() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::muestraVehiculoNoEncontrado);
			return;
		}

		labelVehiculo.setText("");
		labelMensaje.setText("No se encontró ningún vehículo con esas placas");
		tableHistorial.setItems(FXCollections.observableArrayList());
	}

	/**
	 * Refresca la tabla de historial de servicios
	 *
	 * @param historial la lista de servicios a mostrar
	 */
	public void actualizaHistorial(List<Servicio> historial) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.actualizaHistorial(historial));
			return;
		}

		ObservableList<Servicio> data = FXCollections.observableArrayList(historial);
		tableHistorial.setItems(data);
	}

	/**
	 * Limpia los campos del formulario de captura de un nuevo servicio
	 */
	public void limpiaFormulario() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::limpiaFormulario);
			return;
		}

		datePickerFecha.setValue(null);
		textFieldDescripcion.setText("");
		textFieldPiezasUtilizadas.setText("");
		textFieldCostoManoObra.setText("");
		textAreaObservaciones.setText("");
	}

	/**
	 * Muestra un diálogo con un mensaje
	 *
	 * @param mensaje El mensaje a mostrar
	 */
	public void muestraDialogoConMensaje(String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestraDialogoConMensaje(mensaje));
			return;
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Información");
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	/**
	 * Oculta o muestra la ventana
	 */
	public void setVisible(boolean visible) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.setVisible(visible));
			return;
		}

		if (!initialized) {
			if (visible) {
				initializeUI();
			} else {
				return;
			}
		}

		if (visible) {
			stage.show();
		} else {
			stage.hide();
		}
	}

	// FXML Event Handlers

	@FXML
	private void handleBuscarVehiculo() {
		control.buscaVehiculo(textFieldPlacas.getText());
	}

	@FXML
	private void handleGuardar() {
		LocalDate fecha = datePickerFecha.getValue();
		String descripcion = textFieldDescripcion.getText();

		if (fecha == null || descripcion == null || descripcion.trim().isEmpty()) {
			muestraDialogoConMensaje("La fecha y la descripción del trabajo son obligatorias");
			return;
		}

		double costoManoObra;
		try {
			costoManoObra = Double.parseDouble(textFieldCostoManoObra.getText().trim());
		} catch (NumberFormatException e) {
			muestraDialogoConMensaje("El costo de mano de obra debe ser un número");
			return;
		}

		control.registraServicio(fecha, descripcion, textFieldPiezasUtilizadas.getText(), costoManoObra,
				textAreaObservaciones.getText());
	}

	@FXML
	private void handleCancelar() {
		limpiaFormulario();
		control.termina();
	}
}
