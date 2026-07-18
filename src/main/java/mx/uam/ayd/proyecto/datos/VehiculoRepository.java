package mx.uam.ayd.proyecto.datos;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

public interface VehiculoRepository extends CrudRepository <Vehiculo, Long> {
    
    // Buscar por numero de placa del vehiculo, el optional sirve para que si no encuentra la placa no se rompa la aplicacion
    public Optional<Vehiculo> findByPlaca(String placa);

}