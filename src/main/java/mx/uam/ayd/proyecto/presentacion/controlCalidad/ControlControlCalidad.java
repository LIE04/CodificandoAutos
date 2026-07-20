package mx.uam.ayd.proyecto.presentacion.controlcalidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Controlador para el flujo de Verificación de Escáner (HU-40)
 * 
 * @author Erik LIE04
 */
@Component
public class ControlControlCalidad {

    @Autowired
    private ServicioReparacion servicioReparacion;

    // Aquí inyectarías tu vista cuando la tengas creada
    // @Autowired
    // private VistaControlCalidad vista;

    /**
     * Inicia el módulo de control de calidad para una reparación específica.
     */
    public void inicia(int idReparacion) {
        try {
            Reparacion reparacion = servicioReparacion.recuperarReparacion(idReparacion);
            // vista.muestra(this, reparacion);
        } catch (Exception e) {
            // Manejo de error si no existe la reparación
            System.out.println(e.getMessage());
        }
    }

    /**
     * Llamado por la vista cuando el mecánico hace clic en "Escaneo Limpio"
     */
    public void registrarEscaneoLimpio(int idReparacion) {
        try {
            servicioReparacion.procesarEscaneoLimpio(idReparacion);
            // vista.muestraMensajeExito("El vehículo está listo para entrega.");
            // vista.cerrar();
        } catch (Exception e) {
            // vista.muestraError(e.getMessage());
        }
    }

    /**
     * Llamado por la vista cuando el mecánico hace clic en "Aún presenta fallas"
     * y llena el campo de texto obligatorio.
     */
    public void registrarFallasPersistentes(int idReparacion, String codigos) {
        try {
            servicioReparacion.procesarFallasPersistentes(idReparacion, codigos);
            // vista.muestraMensajeAdvertencia("El vehículo regresó a estado de revisión.");
            // vista.cerrar();
        } catch (IllegalArgumentException e) {
            // vista.muestraError("Error: " + e.getMessage()); // Muestra error si los códigos están vacíos
        }
    }
}