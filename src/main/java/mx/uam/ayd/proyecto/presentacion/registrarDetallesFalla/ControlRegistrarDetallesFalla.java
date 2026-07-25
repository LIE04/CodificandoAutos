package mx.uam.ayd.proyecto.presentacion.registrarDetallesFalla;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioDetallesFalla;
import mx.uam.ayd.proyecto.negocio.ServicioVehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@Component
public class ControlRegistrarDetallesFalla {

    private final ServicioDetallesFalla servicioDetallesFalla;
    private final ServicioVehiculo servicioVehiculo;
    private final VentanaRegistrarDetallesFalla ventana;

    @Autowired
    public ControlRegistrarDetallesFalla(ServicioDetallesFalla servicioDetallesFalla, 
                                         ServicioVehiculo servicioVehiculo, 
                                         VentanaRegistrarDetallesFalla ventana) {
        this.servicioDetallesFalla = servicioDetallesFalla;
        this.servicioVehiculo = servicioVehiculo;
        this.ventana = ventana;
    }

    public void inicia() {
        // Pedimos la lista al servicio y abrimos la ventana
        List<Vehiculo> vehiculos = servicioVehiculo.recuperaTodos();
        ventana.muestra(this, vehiculos);
    }

    public void agregarFalla(String descripcion, String estado, Vehiculo vehiculoSeleccionado) {
        try {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estado, vehiculoSeleccionado);
            ventana.muestraMensaje("Éxito", "Los detalles de la falla se registraron correctamente.");
            ventana.cierra();
        } catch (IllegalArgumentException e) {
            ventana.muestraMensaje("Error de Validación", e.getMessage());
        } catch (Exception e) {
            ventana.muestraMensaje("Error", "Ocurrió un error inesperado al guardar en la base de datos.");
        }
    }
}