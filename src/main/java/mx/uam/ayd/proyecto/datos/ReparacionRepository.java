package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

import java.util.List;

/**
 * Repositorio para la entidad Reparacion
 * Maneja las operaciones de persistencia en la base de datos
 * @author Erik LIE04
 */
public interface ReparacionRepository extends CrudRepository<Reparacion, Integer> {

    //public List<Reparacion> findByEstatusServicio(String estatusServicio);

}