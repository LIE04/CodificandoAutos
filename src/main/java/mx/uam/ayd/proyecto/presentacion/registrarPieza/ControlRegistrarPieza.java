package mx.uam.ayd.proyecto.presentacion.registrarPieza;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioInventario;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 *
 * Módulo de control para la historia de usuario HU-31: Registro de piezas
 *
 */
@Component
public class ControlRegistrarPieza {

	private final ServicioInventario servicioInventario;
	private final VentanaRegistrarPieza ventana;

	@Autowired
	public ControlRegistrarPieza(ServicioInventario servicioInventario, VentanaRegistrarPieza ventana) {
		this.servicioInventario = servicioInventario;
		this.ventana = ventana;
	}

	/**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
	@PostConstruct
	public void init() {
		ventana.setControlRegistrarPieza(this);
	}

	/**
	 * Inicia la historia de usuario
	 */
	public void inicia() {
		List<Refaccion> piezas = servicioInventario.recuperaPiezas();
		ventana.muestra(piezas);
	}

	/**
	 * Procesa el registro de una pieza recibida en el taller. Si la pieza
	 * ya existía (mismo nombre y proveedor) se incrementa su cantidad; si
	 * no, se crea un nuevo registro de inventario. La ventana permanece
	 * abierta para seguir registrando piezas.
	 *
	 * @param nombre nombre de la pieza
	 * @param cantidad cantidad recibida
	 * @param proveedor proveedor que suministró la pieza
	 * @param fechaRecepcion fecha de recepción de la pieza
	 * @param costoUnitario costo unitario de la pieza
	 */
	public void registraPieza(String nombre, int cantidad, String proveedor, LocalDate fechaRecepcion,
			double costoUnitario) {

		try {
			servicioInventario.registrarPieza(nombre, cantidad, proveedor, fechaRecepcion, costoUnitario);
			ventana.muestraDialogoConMensaje("Pieza registrada exitosamente");
			ventana.limpiaFormulario();

		} catch (Exception ex) {
			ventana.muestraDialogoConMensaje("Error al registrar la pieza: " + ex.getMessage());
		}

		// Se actualiza la tabla de últimos registros, sin cerrar la ventana
		List<Refaccion> piezas = servicioInventario.recuperaPiezas();
		ventana.actualizaTabla(piezas);
	}

	/**
	 * Termina la historia de usuario
	 */
	public void termina() {
		ventana.setVisible(false);
	}

}
