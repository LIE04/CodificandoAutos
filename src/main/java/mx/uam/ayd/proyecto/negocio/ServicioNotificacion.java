package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.ReparacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.HistorialNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejar la lógica de notificaciones a clientes.
 * Implementa la lógica de la HU-34 (Informe de atrasos).
 * 
 * @author Erik LIE04
 */
@Service
public class ServicioNotificacion {

    private static final Logger log = LoggerFactory.getLogger(ServicioNotificacion.class);
    
    private final ReparacionRepository reparacionRepository;
    
    // Inyección de dependencias por constructor
    @Autowired
    public ServicioNotificacion(ReparacionRepository reparacionRepository) {
        this.reparacionRepository = reparacionRepository;
    }

    /**
     * Obtiene la lista de motivos o plantillas predefinidas para informar un atraso.
     * (HU-34)
     * @return Lista de motivos en formato String
     */
    public List<String> obtenerPlantillasDeAtraso() {
        // Regla de Negocio: Proveer opciones predefinidas para estandarizar la comunicación 
        // y facilitar la selección en la interfaz de usuario.
        return Arrays.asList(
            "Falta de refacciones en inventario",
            "Complicaciones técnicas imprevistas",
            "Ausencia de personal asignado",
            "Retraso de proveedores externos",
            "Carga de trabajo excesiva en el taller"
        );
    }

    /**
     * Registra y "envía" el aviso de retraso al cliente (HU-34).
     * 
     * @param idReparacion Identificador de la reparación afectada.
     * @param motivo El motivo seleccionado en la ventana modal.
     * @return true si se procesó correctamente, false en caso contrario.
     */
    public boolean enviarAvisoRetraso(int idReparacion, String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un motivo para el atraso.");
        }

        Optional<Reparacion> opcional = reparacionRepository.findById(idReparacion);
        
        if (opcional.isPresent()) {
            Reparacion reparacion = opcional.get();
            
            // Explicación: Se crea el registro del historial con la hora actual exacta
            // devuelta por el sistema operativo en el momento del click.
            HistorialNotificacion nuevaNotificacion = new HistorialNotificacion();
            nuevaNotificacion.setFechaHora(LocalDateTime.now());
            nuevaNotificacion.setMotivo(motivo);
            
            // Usamos el método addHistorialNotificacion que hicimos en Reparacion.java 
            // para vincular ambos objetos en memoria antes de guardarlos.
            reparacion.addHistorialNotificacion(nuevaNotificacion);
            
            // Simulación de conexión con la API de Mensajería Externa
            // Aquí iría el código real de Twilio, WhatsApp API o JavaMail en el futuro.
            log.info("Simulando envío de mensaje (API Externa) para Reparacion ID {}. Motivo: {}", idReparacion, motivo);
            
            // Explicación: Al guardar la reparación, gracias a que configuramos CascadeType.ALL 
            // en Reparacion.java, Spring Boot/Hibernate inserta automáticamente el nuevo 
            // 'HistorialNotificacion' en su respectiva tabla hija en la base de datos.
            reparacionRepository.save(reparacion);
            
            return true;
        } else {
            log.warn("No se encontró la reparación con ID: {}", idReparacion);
            return false;
        }
    }
}