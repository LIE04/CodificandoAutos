package mx.uam.ayd.proyecto.presentacion.principal;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.presentacion.agregarUsuario.ControlAgregarUsuario;
import mx.uam.ayd.proyecto.presentacion.listarUsuarios.ControlListarUsuarios;
import mx.uam.ayd.proyecto.presentacion.listarGrupos.ControlListarGrupos;
import mx.uam.ayd.proyecto.presentacion.registrarPieza.ControlRegistrarPieza;
import mx.uam.ayd.proyecto.presentacion.consultarDistribuidores.ControlConsultarDistribuidores;
import mx.uam.ayd.proyecto.presentacion.registrarServicio.ControlRegistrarServicio;

/**
 * Esta clase lleva el flujo de control de la ventana principal
 * 
 * @author humbertocervantes
 *
 */
@Component
public class ControlPrincipal {

	private final ControlAgregarUsuario controlAgregarUsuario;
	private final ControlListarUsuarios controlListarUsuarios;
	private final ControlListarGrupos controlListarGrupos;
	private final ControlRegistrarPieza controlRegistrarPieza;
	private final ControlConsultarDistribuidores controlConsultarDistribuidores;
	private final ControlRegistrarServicio controlRegistrarServicio;
	private final VentanaPrincipal ventana;

	@Autowired
	public ControlPrincipal(
			ControlAgregarUsuario controlAgregarUsuario,
			ControlListarUsuarios controlListarUsuarios,
			ControlListarGrupos controlListarGrupos,
			ControlRegistrarPieza controlRegistrarPieza,
			ControlConsultarDistribuidores controlConsultarDistribuidores,
			ControlRegistrarServicio controlRegistrarServicio,
			VentanaPrincipal ventana) {
		this.controlAgregarUsuario = controlAgregarUsuario;
		this.controlListarUsuarios = controlListarUsuarios;
		this.controlListarGrupos = controlListarGrupos;
		this.controlRegistrarPieza = controlRegistrarPieza;
		this.controlConsultarDistribuidores = controlConsultarDistribuidores;
		this.controlRegistrarServicio = controlRegistrarServicio;
		this.ventana = ventana;
	}
	
	/**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control principal y la ventana principal
	 */
	@PostConstruct
	public void init() {
		ventana.setControlPrincipal(this);
	}
	
	/**
	 * Inicia el flujo de control de la ventana principal
	 * 
	 */
	public void inicia() {
		ventana.muestra();
	}

	/**
	 * Método que arranca la historia de usuario "agregar usuario"
	 * 
	 */
	public void agregarUsuario() {
		controlAgregarUsuario.inicia();
	}
	
	/**
	 * Método que arranca la historia de usuario "listar usuarios"
	 * 
	 */
	public void listarUsuarios() {
		controlListarUsuarios.inicia();
	}

	/**
	 * Método que arranca la historia de usuario "listar grupos"
	 * 
	 */
	public void listarGrupos() {
		controlListarGrupos.inicia();
	}

	/**
	 * Método que arranca la historia de usuario "registrar pieza" (HU-31)
	 *
	 */
	public void registrarPieza() {
		controlRegistrarPieza.inicia();
	}

	/**
	 * Método que arranca la historia de usuario "consultar distribuidores" (HU-25)
	 *
	 */
	public void consultarDistribuidores() {
		controlConsultarDistribuidores.inicia();
	}

	/**
	 * Método que arranca la historia de usuario "historial de servicio" (HU-29)
	 *
	 */
	public void registrarServicio() {
		controlRegistrarServicio.inicia();
	}
}
