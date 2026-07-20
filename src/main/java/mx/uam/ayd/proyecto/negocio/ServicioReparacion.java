package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.ReparacionRepository;
import mx.uam.ayd.proyecto.datos.ReparacionRepository.VehiculosPendientesDTO;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
//import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

/**
 * Servicio para la entidad Reparacion
 * Maneja la lógica de negocio, incluyendo la validación de Control de Calidad (HU-40)
 * 
 * @author Erik LIE04
 */
@Service
public class ServicioReparacion {

    private static final Logger log = LoggerFactory.getLogger(ServicioReparacion.class);
    
    private final ReparacionRepository reparacionRepository;
    
    // Inyección de dependencias por constructor (Combina ambas versiones)
    @Autowired
    public ServicioReparacion(ReparacionRepository reparacionRepository) {
        this.reparacionRepository = reparacionRepository;
        //this.servicioVehiculo = servicioVehiculo;
    }

    /**
     * Recupera una reparación por su ID.
     */
    public Reparacion recuperarReparacion(int idReparacion) {
        Optional<Reparacion> opcional = reparacionRepository.findById(idReparacion);
        if(opcional.isPresent()) {
            return opcional.get();
        }
        throw new IllegalArgumentException("No se encontró la reparación con ID: " + idReparacion);
    }

    /**
     * Escenario 1 (HU-40): Registra un escaneo limpio y avanza el estado.
     */
    public Reparacion procesarEscaneoLimpio(int idReparacion) {
        Reparacion reparacion = recuperarReparacion(idReparacion);
        
        // Regla de Negocio: Pasa a estado de entrega al verificar escaneo limpio
        reparacion.setEstatusServicio("Listo para entrega");
        
        // Se añade nota a las observaciones para mantener un historial
        String notasActuales = reparacion.getObservacionesTecnicas() != null ? reparacion.getObservacionesTecnicas() : "";
        reparacion.setObservacionesTecnicas(notasActuales + " | [Control de Calidad: Escaneo Limpio Exitoso]");
        
        return reparacionRepository.save(reparacion);
    }

    /**
     * Escenario 2 y 3 (HU-40): Registra fallas persistentes y retrocede el estado.
     */
    public Reparacion procesarFallasPersistentes(int idReparacion, String codigosFalla) {
        Reparacion reparacion = recuperarReparacion(idReparacion);
        
        if (codigosFalla == null || codigosFalla.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar los códigos de falla detectados.");
        }

        // Regla de Negocio: Bloqueo de entrega, regresa a revisión
        reparacion.setEstatusServicio("En revisión / Diagnóstico");
        
        // Guardamos los códigos en las observaciones técnicas
        String notasActuales = reparacion.getObservacionesTecnicas() != null ? reparacion.getObservacionesTecnicas() : "";
        reparacion.setObservacionesTecnicas(notasActuales + " | [Control de Calidad Fallido - Códigos persistentes: " + codigosFalla + "]");
        
        return reparacionRepository.save(reparacion);
    }

    public List<VehiculosPendientesDTO> obtenerVehiculosParaEntrega() {

        return reparacionRepository.findVehiculosListosParaEntrega();
    }
}