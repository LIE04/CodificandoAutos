package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.ReparacionRepository;
import mx.uam.ayd.proyecto.datos.ReparacionRepository.VehiculosPendientesDTO;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.time.LocalDateTime;
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
    }

    /**
     * Recupera una reparación por su ID.
     * 
     */
    public Reparacion recuperarReparacion(int idReparacion) {
        Optional<Reparacion> opcional = reparacionRepository.findById(idReparacion);
        if(opcional.isPresent()) {
            return opcional.get();
        }
        throw new IllegalArgumentException("No se encontró la reparación con ID: " + idReparacion);
    }


    /*
    HU-14 Crear Reparacion con su fallas O
    @author Oscar Hinojosa
    */
    @Transactional
    public Reparacion crearNuevaReparacionConFallas(String codigosFalla, Vehiculo vehiculo) {
        
        if (codigosFalla == null || codigosFalla.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar los códigos de falla detectados.");
        }
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe seleccionar un vehículo.");
        }

        // Definimos los estatus que se consideran "Reparación Cerrada/Finalizada"
        List<String> estatusFinalizados = List.of("Entregado", "En espera", "Listo paara entrega");

        // Validamos si el carro ya tiene un proceso activo en el taller
        boolean tieneReparacionActiva = reparacionRepository.existsByVehiculoAndEstatusServicioIn(vehiculo, estatusFinalizados);

        if (tieneReparacionActiva) {
            throw new IllegalArgumentException("El vehículo con placas " + vehiculo.getPlacas() + 
                " ya tiene un proceso de reparación activo en el taller. Debe finalizar la reparación actual antes de abrir una nueva.");
        }

        // Crear la nueva entidad Reparacion
        Reparacion nuevaReparacion = new Reparacion();
        nuevaReparacion.setEstatusServicio("En espera");
        nuevaReparacion.setVehiculo(vehiculo);
        nuevaReparacion.setFechaInicio(LocalDateTime.now());
        

        // Procesar las múltiples fallas separadas por coma
        String[] listaFallas = codigosFalla.split(",");
        for (String descripcion : listaFallas) {
            String fallaLimpia = descripcion.trim();
            
            if (!fallaLimpia.isEmpty()) {
                DetallesFalla nuevaFalla = new DetallesFalla();
                nuevaFalla.setDescripcionFalla(fallaLimpia);
                nuevaFalla.setEstatus("En espera");
                nuevaFalla.setVehiculo(vehiculo); // Relación directa
                
                // Vincular bidireccionalmente agregando a la lista
                nuevaReparacion.addFalla(nuevaFalla);
            }
        }

        // 4. Guardar en la base de datos
        // Hibernate automáticamente hace el INSERT de la Reparacion y luego los INSERT de cada DetallesFalla
        return reparacionRepository.save(nuevaReparacion);
    }

    /**
     * Escenario 1 (HU-40): Registra un escaneo limpio y avanza el estado.
     */
    public Reparacion procesarEscaneoLimpio(int idReparacion) {
        Reparacion reparacion = recuperarReparacion(idReparacion);
        
        // Modificación realizada por Erik para la HU-40 (Control de Calidad)
        // Regla de Negocio: Pasa a estado "Listo para entrega" al verificar escaneo limpio, conectando con la HU-42
        reparacion.setEstatusServicio("Listo para entrega");
        
        // Se añade nota a las observaciones para mantener un historial en la BD
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

        // Modificación realizada por Erik para la HU-40 (Control de Calidad)
        // Regla de Negocio: Procesar múltiples fallas separadas por coma y agregarlas a la base de datos.
        String[] nuevasFallas = codigosFalla.split(",");
        for (String descripcion : nuevasFallas) {
            String fallaLimpia = descripcion.trim(); 
            
            if (!fallaLimpia.isEmpty()) {
                DetallesFalla nuevaFalla = new DetallesFalla();
                nuevaFalla.setDescripcionFalla(fallaLimpia);
                nuevaFalla.setEstatus("En espera"); 
                
                reparacion.addFalla(nuevaFalla);
            }
        }

        // Modificación realizada por Erik para la HU-40 (Control de Calidad)
        // Regla de Negocio: Bloqueo de entrega, regresa a revisión para que el mecánico lo atienda.
        reparacion.setEstatusServicio("En espera"); 
        
        // Guardamos un pequeño registro en las observaciones técnicas (solo como historial en texto)
        String notasActuales = reparacion.getObservacionesTecnicas() != null ? reparacion.getObservacionesTecnicas() : "";
        reparacion.setObservacionesTecnicas(notasActuales + " | [Control de Calidad Fallido - Nuevas fallas registradas]");
        
        return reparacionRepository.save(reparacion);
    }


    /*  
    Obtener vehiculos pendientes para la lista (HU-42)
     */
    public List<VehiculosPendientesDTO> obtenerVehiculosParaEntrega() {
        return reparacionRepository.findVehiculosActivos();
    }
    
    // Cambiar Estatus servicio de los vehiculos con 'Listo para entrega' a 'Entregado' (HU-42)
    public boolean marcarEntregado(Integer idReparacion){
        Optional<Reparacion> reparacionOpt = reparacionRepository.findById(idReparacion);

        if(reparacionOpt.isPresent()) {
            Reparacion reparacion = reparacionOpt.get();
            
            // Verificamos que ya haya pasado por el control de calidad
            if(reparacion.getEstatusServicio().equalsIgnoreCase("Listo para entrega")){
               reparacion.setEstatusServicio("Entregado");
               reparacionRepository.save(reparacion);
               return true;
            }
        }
        return false;
    }

    // INICIO Modificación realizada por Erik para la HU-34 (Notificación de Atraso)
    /**
     * Obtiene todos los vehículos en proceso que son susceptibles de sufrir un atraso.
     */
    public List<Reparacion> obtenerReparacionesParaAtraso() {
        return reparacionRepository.findReparacionesParaAtraso();
    }
    // FIN Modificación Erik HU-34
}