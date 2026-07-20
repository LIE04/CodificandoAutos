package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import java.util.List;


/**
 * Repositorio para vehículos
 */
public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {

    /**
     * Busca un vehículo por sus placas
     */
    public Vehiculo findByPlacas(String placas);
    
    /**
     * Busca un vehículo por sus id
     */
    public Vehiculo findbyiVehiculo(long idVehiculo);
}
