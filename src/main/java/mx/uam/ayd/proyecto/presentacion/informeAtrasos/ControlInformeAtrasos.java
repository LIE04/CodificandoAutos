package mx.uam.ayd.proyecto.presentacion.informeAtrasos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Controlador para el flujo de Notificación de Atrasos (HU-34)
 * Orquesta la comunicación entre la vista y los servicios de negocio.
 * 
 * @author Erik LIE04
 */
@Component
public class ControlInformeAtrasos {

    @Autowired
    private ServicioReparacion servicioReparacion;

    @Autowired
    private ServicioNotificacion servicioNotificacion;

    @Autowired
    private VentanaInformeAtrasos ventana;

    /**
     * INICIO Modificación realizada por Erik para la HU-34
     * Se elimina el parámetro (int idReparacion). Ahora el controlador recupera 
     * todos los vehículos en proceso y se los envía a la ventana para el ComboBox.
     */
    public void inicia() {
        try {
            // Recuperamos la lista de vehículos susceptibles a atraso
            List<Reparacion> reparacionesDisponibles = servicioReparacion.obtenerReparacionesParaAtraso();
            
            // Obtenemos las plantillas de atraso del servicio
            List<String> motivosDisponibles = servicioNotificacion.obtenerPlantillasDeAtraso();

            // Mandamos ambas listas a la ventana 
            ventana.muestra(this, reparacionesDisponibles, motivosDisponibles);
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el módulo de atrasos: " + e.getMessage());
            ventana.muestraError("No se pudo cargar la información del sistema: " + e.getMessage());
        }
    }
    // FIN Modificación Erik HU-34

    /**
     * Llamado por la ventana cuando el mecánico confirma el envío del aviso.
     */
    public void enviarAviso(int idReparacion, String motivoSeleccionado) {
        try {
            boolean exito = servicioNotificacion.enviarAvisoRetraso(idReparacion, motivoSeleccionado);
            
            if (exito) {
                ventana.muestraMensajeExito("El aviso de retraso se ha registrado y enviado al cliente correctamente.");
                ventana.cerrar();
            } else {
                ventana.muestraError("Ocurrió un problema al intentar enviar el aviso. Verifique el ID de la reparación.");
            }
        } catch (IllegalArgumentException e) {
            ventana.muestraError("Error de validación: " + e.getMessage()); 
        } catch (Exception e) {
            ventana.muestraError("Error inesperado del sistema: " + e.getMessage());
        }
    }
}