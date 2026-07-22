package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Agende una nueva cita para un cliente.
     */
    public Cita agendarCita(LocalDate fecha, LocalTime hora, Cliente cliente) {

        // 1. Validaciones básicas de nulidad
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser nula");
        }
        if (hora == null) {
            throw new IllegalArgumentException("La hora de la cita no puede ser nula");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("La cita debe estar asociada a un cliente");
        }

        // 2. Validar que la cita no sea en el pasado
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden agendar citas en fechas pasadas");
        }

        // 3. Validar disponibilidad de horario usando tu método 'existsByFechaAndHora'
        if (citaRepository.existsByFechaAndHora(fecha, hora)) {
            throw new IllegalArgumentException("Ya existe una cita agendada para la fecha y hora seleccionadas");
        }

        // 4. Crear y guardar la entidad
        Cita cita = new Cita();
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setCliente(cliente);

        Cita citaGuardada = citaRepository.save(cita);

        return citaGuardada;
    }
    /**
     * Recupera las citas asociadas al nombre de un cliente.
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

}
