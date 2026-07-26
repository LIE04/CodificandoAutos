package mx.uam.ayd.proyecto.presentacion.controlCalidad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
// Edite esto en tu codigo: Puse mi servicio detalles falla
import mx.uam.ayd.proyecto.negocio.ServicioDetallesFalla; 
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Controlador para el flujo de Verificación de Escáner (HU-40)
 * Conectado a la base de datos para recuperar el checklist de reparaciones.
 * 
 * @author Erik LIE04
 */
@Component
public class ControlControlCalidad {
    //Callbakc que se ejecuta al terminar el proceso(Para refrescar la ventana en HU-42)
    private Runnable alTerminarActualizar;

    @Autowired
    private ServicioReparacion servicioReparacion;

    // Edite esto en tu codigo: Inyecte mi servicio
    @Autowired
    private ServicioDetallesFalla servicioDetallesFalla; 

    @Autowired
    private VentanaControlCalidad ventana;

    /**
     * Inicia el módulo de control de calidad para una reparación específica,
     * obteniendo los datos reales de la base de datos.
     */
    public void inicia(int idReparacion, Runnable alTerminarActualizar) {
        //Guardamos la referncia del callback para ejecutarla despues (HU-42)
        this.alTerminarActualizar = alTerminarActualizar;

        try {
            Reparacion reparacion = servicioReparacion.recuperarReparacion(idReparacion);
            
            // Recuperamos las fallas asociadas DIRECTAMENTE a esta reparación.
            // Como usamos FetchType.EAGER en la entidad, la lista ya viene cargada.
            List<DetallesFalla> fallasReales = reparacion.getFallas();
            
            // Extraemos solo las descripciones (Strings) para mantener la compatibilidad con la vista
            List<String> descripcionesFallas = new ArrayList<>();
            for (DetallesFalla falla : fallasReales) {
                descripcionesFallas.add(falla.getDescripcionFalla());
            }

            // Mandamos los datos a la ventana 
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

            // Si la vista solicitante definió una acción de refresco, la notificamos (HU-42)
            if (alTerminarActualizar != null) {
                alTerminarActualizar.run();
            }
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

            // Si la vista solicitante definió una acción de refresco, la notificamos (HU-42)
            if (alTerminarActualizar != null) {
                alTerminarActualizar.run();
            }
        } catch (IllegalArgumentException e) {
            ventana.muestraError("Error: " + e.getMessage()); 
        }
    }
}