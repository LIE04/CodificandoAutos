package mx.uam.ayd.proyecto.presentacion.controlCalidad;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioReparacion;

/**
 * Controlador para el flujo de Verificación de Escáner (HU-40)
 * NOTA: Contiene un mock de datos temporal para probar la interfaz visual.
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
     * Inicia el módulo de control de calidad para una reparación específica.
     */
    public void inicia(int idReparacion) {
        try {
            // =================================================================
            // INICIO DEL MOCK DE DATOS (ELIMINAR CUANDO DetallesFalla ESTÉ LISTO)
            // =================================================================
            
            // Simulamos la lista de fallas que tu compañero traerá de la base de datos
            List<String> fallasSimuladas = Arrays.asList(
                "Cambio de balatas delanteras",
                "Afinación mayor (Filtros y Bujías)",
                "Corrección de fuga de aceite en cárter",
                "Revisión de niveles de fluidos"
            );

            // Le pasamos el ID y la lista de prueba a la vista
            ventana.muestraConMock(this, idReparacion, fallasSimuladas);
            
            // =================================================================
            // FIN DEL MOCK DE DATOS
            // =================================================================

            // TODO: Cuando la base de datos esté lista, descomentar esta línea:
            // Reparacion reparacion = servicioReparacion.recuperarReparacion(idReparacion);
            // ventana.muestra(this, reparacion);
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el módulo: " + e.getMessage());
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