package mx.uam.ayd.proyecto.presentacion.registrarServicio;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.GestionServicio;
import mx.uam.ayd.proyecto.negocio.modelo.Servicio;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 *
 * Módulo de control para la historia de usuario HU-29: Historial de servicio
 *
 */
@Component
public class ControlRegistrarServicio {

	private final GestionServicio gestionServicio;
	private final VentanaRegistrarServicio ventana;

	// Vehículo actualmente cargado en la ventana, tras una búsqueda exitosa por placas
	private Vehiculo vehiculoActual;

	@Autowired
	public ControlRegistrarServicio(GestionServicio gestionServicio, VentanaRegistrarServicio ventana) {
		this.gestionServicio = gestionServicio;
		this.ventana = ventana;
	}

	/**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
	@PostConstruct
	public void init() {
		ventana.setControlRegistrarServicio(this);
	}

	/**
	 * Inicia la historia de usuario, con la ventana en blanco a la espera
	 * de que el mecánico busque un vehículo por placas
	 */
	public void inicia() {
		vehiculoActual = null;
		ventana.muestra();
	}

	/**
	 * Busca un vehículo por sus placas y, si existe, muestra su información
	 * junto con su historial de servicios previos
	 *
	 * @param placas placas del vehículo a buscar
	 */
	public void buscaVehiculo(String placas) {

		Vehiculo vehiculo = gestionServicio.buscaVehiculoPorPlacas(placas);

		if (vehiculo == null) {
			vehiculoActual = null;
			ventana.muestraVehiculoNoEncontrado();
			return;
		}

		vehiculoActual = vehiculo;
		List<Servicio> historial = gestionServicio.recuperaHistorial(vehiculo);
		ventana.muestraVehiculo(vehiculo, historial);
	}

	/**
	 * Registra un nuevo servicio (reparación o mantenimiento) para el
	 * vehículo actualmente cargado. La ventana permanece abierta para poder
	 * seguir consultando el historial actualizado.
	 *
	 * @param fecha fecha del servicio
	 * @param descripcion descripción del trabajo realizado
	 * @param piezasUtilizadas piezas utilizadas durante el servicio
	 * @param costoManoObra costo de la mano de obra
	 * @param observaciones observaciones adicionales
	 */
	public void registraServicio(LocalDate fecha, String descripcion, String piezasUtilizadas,
			double costoManoObra, String observaciones) {

		try {
			gestionServicio.registraServicio(vehiculoActual, fecha, descripcion, piezasUtilizadas, costoManoObra,
					observaciones);
			ventana.muestraDialogoConMensaje("Servicio registrado exitosamente");
			ventana.limpiaFormulario();

		} catch (Exception ex) {
			ventana.muestraDialogoConMensaje("Error al registrar el servicio: " + ex.getMessage());
		}

		List<Servicio> historial = gestionServicio.recuperaHistorial(vehiculoActual);
		ventana.actualizaHistorial(historial);
	}

	/**
	 * Termina la historia de usuario
	 */
	public void termina() {
		vehiculoActual = null;
		ventana.setVisible(false);
	}

}
