package mx.uam.ayd.proyecto.presentacion.registrarCita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ControlRegistrarCita {

    private final ServicioCita servicioCita;
    private final VentanaRegistrarCita ventana;

    @Autowired
    public ControlRegistrarCita(ServicioCita servicioCita, VentanaRegistrarCita ventana) {
        this.servicioCita = servicioCita;
        this.ventana = ventana;
    }

    public void inicia() {
        ventana.muestra(this);
    }

    public void registrarCitaCompleta(String nombre, String telefono, String marca, 
                                      String modelo, int anio, String placas, 
                                      double kilometraje, LocalDate fecha, LocalTime hora) {
        try {
            //Validar que todos los datos sean los que se necesiten
            servicioCita.agendarCitaCompleta(nombre, telefono, marca, modelo, anio, placas, kilometraje, fecha, hora);
            
            ventana.mostrarMensajeExito("Cita registrada exitosamente");
            ventana.cerrar();
            
        } catch (IllegalArgumentException e) {
            //Si algun dato falla, no se guarda y manda un error
            ventana.mostrarMensajeError(e.getMessage());
        } catch (Exception e) {
            ventana.mostrarMensajeError("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}