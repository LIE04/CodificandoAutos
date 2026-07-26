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
        if (vehiculo == null) {
            throw new IllegalArgumentException("La cita debe estar asociada a un vehículo");
        }

        // Verificar que la cita esté dentro del horario de atención (9:00 a 18:00)
        LocalTime horaApertura = LocalTime.of(9, 0);
        LocalTime horaCierre = LocalTime.of(17, 30);

        if (hora.isBefore(horaApertura) || hora.isAfter(horaCierre)) {
            throw new IllegalArgumentException("Las citas solo pueden agendarse en horario de 9:00 a 18:00 hrs");
        }

        // Verificar que la cita no sea registrada en una fecha pasada
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en fechas pasadas");
        }
        
        // Verificar que si la cita es HOY, la hora no haya pasado ya
        if (fecha.isEqual(LocalDate.now()) && hora.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en horas pasadas para el día de hoy");
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

        return citaRepository.save(cita); // Simplificamos un poco el guardado y retorno
    }

    public Cita obtenerCitaPendientePorVehiculo(Vehiculo vehiculoSeleccionado) {
        if (vehiculoSeleccionado == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo al buscar una cita");
        }
        
        // Buscamos la cita que le pertenece a ese vehículo y que su estado sea "PENDIENTE"
        return citaRepository.findByVehiculoAndEstado(vehiculoSeleccionado, "PENDIENTE");
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
