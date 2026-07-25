package mx.uam.ayd.proyecto.presentacion.registrarCita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioVehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ControlRegistrarCita {

    private final ServicioCliente servicioCliente;
    private final ServicioVehiculo servicioVehiculo;
    private final ServicioCita servicioCita;
    private final VentanaRegistrarCita ventana;

    @Autowired
    public ControlRegistrarCita(ServicioCliente servicioCliente, 
                                ServicioVehiculo servicioVehiculo, 
                                ServicioCita servicioCita, 
                                VentanaRegistrarCita ventana) {
        this.servicioCliente = servicioCliente;
        this.servicioVehiculo = servicioVehiculo;
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
            // 1. Registrar o recuperar cliente
            Cliente cliente = servicioCliente.agregaCliente(nombre, telefono);
            
            // 2. Registrar o recuperar vehículo
            Vehiculo vehiculo = servicioVehiculo.agregaVehiculo(marca, modelo, placas, anio, kilometraje, cliente);
            
            // 3. Agendar la cita
            servicioCita.agendarCita(fecha, hora, cliente, vehiculo);
            
            ventana.mostrarMensajeExito("Cita registrada exitosamente");
            ventana.cerrar();
            
        } catch (IllegalArgumentException e) {
            ventana.mostrarMensajeError(e.getMessage());
        } catch (Exception e) {
            ventana.mostrarMensajeError("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}