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

    // Spring Boot traduce esto a: SELECT * FROM Vehiculo WHERE id_cliente = ?
    List <Vehiculo> findByClienteIdCliente(long idCliente);
    

}
