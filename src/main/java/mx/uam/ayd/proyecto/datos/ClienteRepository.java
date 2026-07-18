package mx.uam.ayd.proyecto.datos;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;

public interface ClienteRepository extends CrudRepository <Cliente, Long> {
    
    // Buscar por nombre del cliente
    public Cliente findByNombre(String nombre);
    
    // Buscar por numero de telefono del cliente, el optional sirve para que si no encuentra el telefono no se rompa la aplicacion
    public Optional<Cliente> findByTelefono(String telefono);

}