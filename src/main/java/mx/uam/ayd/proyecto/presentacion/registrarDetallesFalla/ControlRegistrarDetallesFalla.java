package mx.uam.ayd.proyecto.presentacion.registrarDetallesFalla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioDetallesFalla;

@Component
public class ControlRegistrarDetallesFalla {

    private final ServicioDetallesFalla servicioDetallesFalla;
    private final VentanaRegistrarDetallesFalla ventana;

    @Autowired
    public ControlRegistrarDetallesFalla(ServicioDetallesFalla servicioDetallesFalla, VentanaRegistrarDetallesFalla ventana) {
        this.servicioDetallesFalla = servicioDetallesFalla;
        this.ventana = ventana;
    }

    /**
     * Inicia la historia de usuario
     */
    public void inicia() {
        // Aquí podrías recibir un Vehículo si lo necesitas para la vista, 
        // por ahora iniciamos la ventana y le pasamos este controlador.
        ventana.muestra(this);
    }

    /**
     * Método que la ventana invoca al dar clic en "Agregar falla"
     */
    public void agregarFalla(String descripcion, String estado) {
        try {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estado);
            ventana.muestraMensaje("Éxito", "Los detalles de la falla se registraron correctamente.");
            ventana.cierra();
        } catch (IllegalArgumentException e) {
            ventana.muestraMensaje("Error de Validación", e.getMessage());
        } catch (Exception e) {
            ventana.muestraMensaje("Error", "Ocurrió un error al registrar los detalles de la falla.");
        }
    }
}