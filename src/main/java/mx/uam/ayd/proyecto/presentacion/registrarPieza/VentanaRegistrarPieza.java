package mx.uam.ayd.proyecto.presentacion.registrarPieza;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.IOException;

import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 * Ventana para el registro de piezas (HU-31) usando JavaFX con FXML.
 *
 * Presenta el formulario de captura y, debajo, la tabla de últimos
 * registros del inventario para retroalimentar al mecánico.
 */
@Component
public class VentanaRegistrarPieza {

	private Stage stage;
	private ControlRegistrarPieza control;

	@FXML
	private TextField textFieldNombre;

	@FXML
	private TextField textFieldCantidad;

	@FXML
	private TextField textFieldProveedor;

	@FXML
	private TextField textFieldCosto;

	@FXML
	private DatePicker datePickerFechaRecepcion;

	@FXML
	private TableView<Refaccion> tablePiezas;

	@FXML
	private TableColumn<Refaccion, String> nombreColumn;

	@FXML
	private TableColumn<Refaccion, Integer> cantidadColumn;

	@FXML
	private TableColumn<Refaccion, String> proveedorColumn;

	@FXML
	private TableColumn<Refaccion, Float> costoColumn;

	@FXML
	private TableColumn<Refaccion, LocalDate> fechaColumn;

	private boolean initialized = false;

	/**
	 * Constructor without UI initialization
	 */
	public VentanaRegistrarPieza() {
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
			stage.setTitle("Registrar Pieza");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-registrar-pieza.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 500, 450);
			stage.setScene(scene);

			nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
			cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("existencia"));
			proveedorColumn.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
			costoColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
			fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaRecepcion"));

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
	public void setControlRegistrarPieza(ControlRegistrarPieza control) {
		this.control = control;
	}

	/**
	 * Muestra la ventana con la tabla de últimos registros
	 *
	 * @param piezas La lista de piezas registradas
	 */
	public void muestra(List<Refaccion> piezas) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(piezas));
			return;
		}

		initializeUI();

		limpiaFormulario();
		actualizaTabla(piezas);

		stage.show();
	}

	/**
	 * Refresca la tabla de últimos registros con la lista dada
	 *
	 * @param piezas La lista de piezas registradas
	 */
	public void actualizaTabla(List<Refaccion> piezas) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.actualizaTabla(piezas));
			return;
		}

		ObservableList<Refaccion> data = FXCollections.observableArrayList(piezas);
		tablePiezas.setItems(data);
	}

	/**
	 * Limpia los campos del formulario de captura
	 */
	public void limpiaFormulario() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::limpiaFormulario);
			return;
		}

		textFieldNombre.setText("");
		textFieldCantidad.setText("");
		textFieldProveedor.setText("");
		textFieldCosto.setText("");
		datePickerFechaRecepcion.setValue(null);
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
	private void handleGuardar() {
		String nombre = textFieldNombre.getText();
		String proveedor = textFieldProveedor.getText();
		LocalDate fechaRecepcion = datePickerFechaRecepcion.getValue();

		if (nombre == null || nombre.trim().isEmpty() || proveedor == null || proveedor.trim().isEmpty()
				|| fechaRecepcion == null) {
			muestraDialogoConMensaje("Todos los campos son obligatorios");
			return;
		}

		int cantidad;
		double costo;

		try {
			cantidad = Integer.parseInt(textFieldCantidad.getText().trim());
		} catch (NumberFormatException e) {
			muestraDialogoConMensaje("La cantidad debe ser un número entero");
			return;
		}

		try {
			costo = Double.parseDouble(textFieldCosto.getText().trim());
		} catch (NumberFormatException e) {
			muestraDialogoConMensaje("El costo debe ser un número");
			return;
		}

		control.registraPieza(nombre, cantidad, proveedor, fechaRecepcion, costo);
	}

	@FXML
	private void handleCancelar() {
		limpiaFormulario();
		control.termina();
	}
}
