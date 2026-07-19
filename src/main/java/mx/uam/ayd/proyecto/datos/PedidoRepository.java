package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Pedido;

/**
 * Repositorio para la entidad Pedido
 * Maneja las operaciones de persistencia en la base de datos
 * 
 * @author Erik LIE04
 */
public interface PedidoRepository extends CrudRepository<Pedido, Integer> {
    // Spring Boot se encarga de implementar los metodos basicos (save, findById, findAll, etc.)
    // Si despues necesitamos buscar pedidos por estatus o fecha, lo agregamos aqui
}