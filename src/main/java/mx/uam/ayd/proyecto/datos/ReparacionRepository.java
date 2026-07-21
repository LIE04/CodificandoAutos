package mx.uam.ayd.proyecto.datos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

import java.util.List;

/**
 * Repositorio para la entidad Reparacion
 * Maneja las operaciones de persistencia en la base de datos
 * @author Erik LIE04
 */
public interface ReparacionRepository extends CrudRepository<Reparacion, Integer> {
    
    /*
     Metodo para inicializar el Objeto de Transferencia de Datos entre las entidades Reparacion, Vehiculo y CLiente
    */
    public interface VehiculosPendientesDTO {
        Integer getId();
        String getNombre();
        String getMarca();
        String getModelo();
        String getPlacas();
        String getEstatusServicio();
    }
    /*
    Metodo para hacer JOIN de las tres entidades
    */
    @Query("SELECT r.idReparacion AS id, c.nombre AS nombre, v.marca AS marca, " +
           "v.modelo AS modelo, v.placas AS placas, r.estatusServicio AS estatusServicio " +
           "FROM Reparacion r " +
           "JOIN r.vehiculo v " +
           "JOIN v.cliente c " +
           "WHERE r.estatusServicio = 'En espera'")

    /*
    Metodo para devolver la lista
    */
    List<VehiculosPendientesDTO> findVehiculosListosParaEntrega();
    
}