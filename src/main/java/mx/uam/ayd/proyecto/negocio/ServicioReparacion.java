package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mx.uam.ayd.proyecto.datos.ReparacionRepository;
//import mx.uam.ayd.proyecto.datos.UsuarioRepository;

public class ServicioReparacion {

	// Define a static logger field
	private static final Logger log = LoggerFactory.getLogger(ServicioReparacion.class);
	
	private final ReparacionRepository reparacionRepository;
	//private final GrupoRepository grupoRepository;
	
	@Autowired
	public ServicioReparacion(ReparacionRepository reparacionRepository) {
		this.reparacionRepository = reparacionRepository;
		//this.grupoRepository = grupoRepository;
    }
}
