package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ServicioCita {

    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private ServicioCliente servicioCliente;

    @Autowired
    private ServicioVehiculo servicioVehiculo;

    /**
     * Agendar cita para el cliente en la fecha y hora seleccionada
     */
    public Cita agendarCita(LocalDate fecha, LocalTime hora, Cliente cliente, Vehiculo vehiculo) {

        // Verificar que los datos ingresados no sean nulos
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser nula");
        }
        if (hora == null) {
            throw new IllegalArgumentException("La hora de la cita no puede ser nula");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("La cita debe estar asociada a un cliente");
        }

        // Verificar que la cita no sea registrada en una fecha pasada
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en fechas pasadas");
        }

        // Verificar que no exista una cita agendada en el mismo dia y hora
        if (citaRepository.existsByFechaAndHora(fecha, hora)) {
            throw new IllegalArgumentException("Ya existe una cita agendada para la fecha y hora seleccionadas");
        }

        // Creacion de la cita y guardado en la base de datos
        Cita cita = new Cita();
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setCliente(cliente);
        cita.setVehiculo(vehiculo);

        Cita citaGuardada = citaRepository.save(cita);

        return citaGuardada;
    }
    /**
    * Recupera las citas asociadas al nombre de un cliente
    */
    public List<Cita> consultarCitasPorNombreCliente(String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }
        return citaRepository.findByClienteNombre(nombreCliente.trim());
    }

    public Cita obtenerCitaPendientePorVehiculo(Vehiculo vehiculoSeleccionado) {
        
        // Buscamos la cita que le pertenece a ese vehículo y que su estado sea "PENDIENTE"
        Cita citaActiva = citaRepository.findByVehiculoAndEstado(vehiculoSeleccionado, "PENDIENTE");
        
        return citaActiva;
    }

    @Transactional
    public Cita agendarCitaCompleta(String nombre, String telefono, String marca, 
                                    String modelo, int anio, String placas, 
                                    double kilometraje, LocalDate fecha, LocalTime hora) {
        
        //Validaciones para guardar el cliente y vehiculo
        Cliente cliente = servicioCliente.agregaCliente(nombre, telefono);

        Vehiculo vehiculo = servicioVehiculo.agregaVehiculo(marca, modelo, placas, anio, kilometraje, cliente);
        
        //Agendar la cita
        return agendarCita(fecha, hora, cliente, vehiculo); 
    }

}
