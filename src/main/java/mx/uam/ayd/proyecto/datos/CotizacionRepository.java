package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;


public interface CotizacionRepository extends CrudRepository<Cotizacion, Long> {
    // Spring Boot se encarga de implementar los metodos basicos (save, findById, findAll, etc.)
    // Si despues necesitamos buscar pedidos por estatus o fecha, lo agregamos aqui
    
}