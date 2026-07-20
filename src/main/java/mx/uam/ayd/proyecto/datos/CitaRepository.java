package mx.uam.ayd.proyecto.datos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;

public interface CitaRepository extends CrudRepository<Cita, Long> {
    
    // Devuelve una lista por si el cliente tiene historial de múltiples citas
    public List<Cita> findByNombre(String nombre);

    public List<Cita> findByFecha(LocalDate fecha);

    public List<Cita> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    public List<Cita> findByEstatus(String estatus);

    public boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);
}