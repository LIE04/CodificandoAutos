package mx.uam.ayd.proyecto.presentacion.consultarDistribuidores;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioDistribuidor;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;

/**
 *
 * Módulo de control para la historia de usuario HU-25: Consultar números de
 * distribuidores
 *
 */
@Component
public class ControlConsultarDistribuidores {

	private final ServicioDistribuidor servicioDistribuidor;
	private final VentanaConsultarDistribuidores ventana;

	@Autowired
	public ControlConsultarDistribuidores(ServicioDistribuidor servicioDistribuidor,
			VentanaConsultarDistribuidores ventana) {
		this.servicioDistribuidor = servicioDistribuidor;
		this.ventana = ventana;
	}

	/**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
	@PostConstruct
	public void init() {
		ventana.setControlConsultarDistribuidores(this);
	}

	/**
	 * Inicia la historia de usuario, mostrando todos los distribuidores registrados
	 */
	public void inicia() {
		List<Distribuidor> distribuidores = servicioDistribuidor.recuperaDistribuidores();

		if (distribuidores.isEmpty()) {
			ventana.muestra(distribuidores, "No hay distribuidores registrados");
		} else {
			ventana.muestra(distribuidores, null);
		}
	}

	/**
	 * Procesa una búsqueda de distribuidores por nombre o tipo de refacción.
	 * Si el criterio está vacío, se muestran todos los distribuidores.
	 *
	 * @param criterio texto ingresado por el mecánico en el buscador
	 */
	public void buscaDistribuidores(String criterio) {

		List<Distribuidor> resultados = servicioDistribuidor.buscaDistribuidores(criterio);

		if (resultados.isEmpty()) {
			String mensaje = (criterio == null || criterio.trim().isEmpty()) ? "No hay distribuidores registrados"
					: "No se encontraron distribuidores que coincidan con la búsqueda";
			ventana.actualizaTabla(resultados, mensaje);
		} else {
			ventana.actualizaTabla(resultados, null);
		}
	}

	/**
	 * Termina la historia de usuario
	 */
	public void termina() {
		ventana.setVisible(false);
	}

}
