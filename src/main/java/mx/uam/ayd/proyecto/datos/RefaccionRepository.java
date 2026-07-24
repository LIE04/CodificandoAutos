package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

import java.util.List;

/**
 * Repositorio para acceder a los datos de las Refacciones en la base de datos
 * Utilizado por el ServicioPedido (HU-30) para buscar las piezas solicitadas
 * @author Erik LIE04
 */
public interface RefaccionRepository extends CrudRepository<Refaccion, Integer> {
    
    /**
     * Busca una refacción en la base de datos a partir de su nombre exacto.
     * Spring Data JPA construye la consulta SQL automáticamente basándose en el nombre de este método.
     * 
     * @param nombre El nombre de la refacción a buscar
     * @return La entidad Refaccion correspondiente, o null si no se encuentra
     */

    public Refaccion findByNombre(String nombre);

    /**
     * Busca una refacción por nombre y proveedor, para saber si una remesa
     * nueva debe incrementar la existencia o si hay que dar de alta la pieza (HU-31).
     *
     * @param nombre nombre de la refacción
     * @param proveedor proveedor que la surtió
     * @return la refacción encontrada, o null si no existe
     */
    public Refaccion findByNombreAndProveedor(String nombre, String proveedor);

    public List<Refaccion> findAll();

    
    public List<Refaccion> findByNombreContainingIgnoreCase(String nombre);


}