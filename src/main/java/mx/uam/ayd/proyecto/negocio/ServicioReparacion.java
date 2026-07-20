package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.ReparacionRepository;
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
    //private final ServicioVehiculo servicioVehiculo;
    
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

    public static class VehiculoEntregaDTO {
        private Long idReparacion;
        private String marca;
        private String modelo;
        private String placas;
        private String nombreCliente;
        private String estatusServicio;

        // Getters y Setters
        public Long getIdReparacion() { return idReparacion; }
        public void setIdReparacion(Long idReparacion) { this.idReparacion = idReparacion; }
        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }
        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }
        public String getPlacas() { return placas; }
        public void setPlacas(String placas) { this.placas = placas; }
        public String getNombreCliente() { return nombreCliente; }
        public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
        public String getEstatusServicio() { return estatusServicio; }
        public void setEstatusServicio(String estatusServicio) { this.estatusServicio = estatusServicio; }
    }

    /**
     * Recupera un servicio con statusservicio "En espera".
     */

   /*  public List<VehiculoEntregaDTO> solicitarListaPendientes(){
        List<VehiculoEntregaDTO> listaFinal = new ArrayList<>();

        // 1. Usamos tu findBy para obtener las órdenes con el estatus que necesitas
        List<Reparacion> reparaciones = reparacionRepository.findByEstatusServicio("pendiente");

        // 2. Iteramos sobre los resultados para rellenar los DTOs
        for (Reparacion rep : reparaciones) {
            
            // Instanciamos el DTO que declaraste dentro de la interfaz
            VehiculoEntregaDTO dto = new VehiculoEntregaDTO();
            
            // Rellenamos con los datos que ya tenemos de la reparación
            //dto.setIdReparacion(rep.getIdReparacion());
           // dto.setEstatusServicio(rep.getEstatusServicio());

            // 3. Consultamos al servicio de vehículos para obtener lo que falta
            // (Asumiendo que devuelve un objeto con marca, modelo, placas y nombre)
            //var infoExtra = servicioVehiculo.solicitaDatosVehiculo(rep.getIdVehiculo());

            // Terminamos de rellenar el DTO
        //dto.setMarca(infoExtra.getMarca());
           // dto.setModelo(infoExtra.getModelo());
           // dto.setPlacas(infoExtra.getPlacas());
           // dto.setNombreCliente(infoExtra.getNombreCliente());

            // 4. Lo agregamos a la lista final
            listaFinal.add(dto);
        }

        return listaFinal;
    }
     */
}