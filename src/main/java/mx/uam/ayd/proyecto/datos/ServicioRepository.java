package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Servicio;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Repositorio para el historial de servicios (HU-29)
 */
public interface ServicioRepository extends CrudRepository<Servicio, Long> {

    /**
     * Recupera el historial de servicios de un vehículo, del más reciente al más antiguo
     */
    public List<Servicio> findByVehiculoOrderByFechaDesc(Vehiculo vehiculo);

}
