package mx.uam.ayd.proyecto.presentacion.controlCalidad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Controlador para el flujo de Verificación de Escáner (HU-40)
 * Conectado a la base de datos para recuperar el checklist de reparaciones.
 * 
 * @author Erik LIE04
 */
@Component
public class ControlControlCalidad {

    @Autowired
    private ServicioReparacion servicioReparacion;

    @Autowired
    private VentanaControlCalidad ventana;

    /**
     * Inicia el módulo de control de calidad para una reparación específica,
     * obteniendo los datos reales de la base de datos.
     */
    public void inicia(int idReparacion) {
        try {
            // 1. Recuperamos la reparación real a través del servicio
            Reparacion reparacion = servicioReparacion.recuperarReparacion(idReparacion);
            
            // 2. Obtenemos la lista real de entidades DetallesFalla asociadas a la reparación
            // Nota: Asegúrate de que la entidad Reparacion ya tenga el método getFallas()
            List<DetallesFalla> fallasReales = reparacion.getFallas(); 
            
            // 3. Extraemos solo las descripciones (Strings) para mantener la compatibilidad con tu vista
            List<String> descripcionesFallas = new ArrayList<>();
            for (DetallesFalla falla : fallasReales) {
                descripcionesFallas.add(falla.getDescripcionFalla());
            }

            // 4. Mandamos los datos a la ventana 
            // (El método de la ventana sigue llamándose muestraConMock, puedes renombrarlo a 'muestra' después si lo deseas)
            ventana.muestraConMock(this, idReparacion, descripcionesFallas);
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el módulo: " + e.getMessage());
            // Mostramos el error en pantalla por si falla la conexión a BD o no existe el ID
            ventana.muestraError("No se pudo cargar la reparación: " + e.getMessage());
        }
    }

    /**
     * Llamado por la ventana cuando el mecánico confirma el "Escaneo Limpio"
     */
    public void registrarEscaneoLimpio(int idReparacion) {
        try {
            servicioReparacion.procesarEscaneoLimpio(idReparacion);
            ventana.muestraMensajeExito("El vehículo está listo para entrega.");
            ventana.cerrar();
        } catch (Exception e) {
            ventana.muestraError(e.getMessage());
        }
    }

    /**
     * Llamado por la ventana cuando el mecánico documenta que "Aún presenta fallas"
     */
    public void registrarFallasPersistentes(int idReparacion, String fallasExtra) {
        try {
            servicioReparacion.procesarFallasPersistentes(idReparacion, fallasExtra);
            ventana.muestraMensajeAdvertencia("El vehículo regresó a estado de revisión con las fallas adicionales registradas.");
            ventana.cerrar();
        } catch (IllegalArgumentException e) {
            ventana.muestraError("Error: " + e.getMessage()); 
        }
    }
}