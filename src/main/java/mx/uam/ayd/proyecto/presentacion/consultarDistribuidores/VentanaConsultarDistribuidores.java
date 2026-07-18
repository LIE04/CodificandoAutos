package mx.uam.ayd.proyecto.presentacion.consultarDistribuidores;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import java.io.IOException;

import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;

/**
 * Ventana para consultar distribuidores (HU-25) usando JavaFX con FXML.
 *
 * Presenta un buscador (por nombre o tipo de refacción), la tabla con la
 * información de contacto de cada distribuidor, y un botón para copiar el
 * teléfono del distribuidor seleccionado.
 */
@Component
public class VentanaConsultarDistribuidores {

	private Stage stage;
	private ControlConsultarDistribuidores control;

	@FXML
	private TextField textFieldBusqueda;

	@FXML
	private Label labelMensaje;

	@FXML
	private TableView<Distribuidor> tableDistribuidores;

	@FXML
	private TableColumn<Distribuidor, String> nombreColumn;

	@FXML
	private TableColumn<Distribuidor, String> telefonoColumn;

	@FXML
	private TableColumn<Distribuidor, String> correoColumn;

	@FXML
	private TableColumn<Distribuidor, String> direccionColumn;

	@FXML
	private TableColumn<Distribuidor, String> tipoRefaccionColumn;

	private boolean initialized = false;

	/**
	 * Constructor without UI initialization
	 */
	public VentanaConsultarDistribuidores() {
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
			stage.setTitle("Distribuidores");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-consultar-distribuidores.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 550, 420);
			stage.setScene(scene);

			nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
			telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
			correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
			direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
			tipoRefaccionColumn.setCellValueFactory(new PropertyValueFactory<>("tipoRefaccion"));

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
	public void setControlConsultarDistribuidores(ControlConsultarDistribuidores control) {
		this.control = control;
	}

	/**
	 * Muestra la ventana con la lista inicial de distribuidores
	 *
	 * @param distribuidores La lista de distribuidores a mostrar
	 * @param mensaje mensaje a mostrar (por ejemplo si no hay distribuidores), o null si no aplica
	 */
	public void muestra(List<Distribuidor> distribuidores, String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(distribuidores, mensaje));
			return;
		}

		initializeUI();

		textFieldBusqueda.setText("");
		actualizaTabla(distribuidores, mensaje);

		stage.show();
	}

	/**
	 * Refresca la tabla de distribuidores y el mensaje informativo
	 *
	 * @param distribuidores La lista de distribuidores a mostrar
	 * @param mensaje mensaje a mostrar (por ejemplo sin resultados), o null si no aplica
	 */
	public void actualizaTabla(List<Distribuidor> distribuidores, String mensaje) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.actualizaTabla(distribuidores, mensaje));
			return;
		}

		ObservableList<Distribuidor> data = FXCollections.observableArrayList(distribuidores);
		tableDistribuidores.setItems(data);

		labelMensaje.setText(mensaje == null ? "" : mensaje);
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
	private void handleBuscar() {
		control.buscaDistribuidores(textFieldBusqueda.getText());
	}

	@FXML
	private void handleCopiarTelefono() {
		Distribuidor seleccionado = tableDistribuidores.getSelectionModel().getSelectedItem();

		if (seleccionado == null) {
			muestraDialogoConMensaje("Selecciona un distribuidor de la tabla para copiar su teléfono");
			return;
		}

		ClipboardContent content = new ClipboardContent();
		content.putString(seleccionado.getTelefono());
		Clipboard.getSystemClipboard().setContent(content);

		muestraDialogoConMensaje("Teléfono copiado al portapapeles");
	}

	@FXML
	private void handleCerrar() {
		control.termina();
	}
}
