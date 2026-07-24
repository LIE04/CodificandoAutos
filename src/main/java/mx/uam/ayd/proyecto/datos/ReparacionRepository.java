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
        Long getIdCotizacion();
        String getNombre();
        String getMarca();
        String getModelo();
        String getPlacas();
        String getEstatusServicio();
    }
    /*
    Metodo para hacer JOIN de las tres entidades
    */
@Query("SELECT r.idReparacion AS id, " +
           "coti.idCotizacion AS idCotizacion, " + 
           "cli.nombre AS nombre, " +
           "v.marca AS marca, " +
           "v.modelo AS modelo, " +
           "v.placas AS placas, " +
           "r.estatusServicio AS estatusServicio " +
           "FROM Reparacion r " +
           "LEFT JOIN r.cotizacion coti " +  
           "JOIN r.vehiculo v " +            
           "JOIN v.cliente cli " +      
           "WHERE r.estatusServicio IN ('En espera', 'Listo para entrega')") 

    /*
    Metodo para devolver la lista
    */
    List<VehiculosPendientesDTO> findVehiculosActivos();
    


}