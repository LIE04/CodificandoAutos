package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.PiezaInventario;

/**
 * Repositorio para piezas de inventario (HU-31: Registro de piezas)
 */
public interface PiezaInventarioRepository extends CrudRepository<PiezaInventario, Long> {

    /**
     * Busca una pieza existente por nombre y proveedor, para decidir si una
     * nueva remesa debe incrementar la cantidad o crear un registro nuevo.
     */
    public PiezaInventario findByNombreAndProveedor(String nombre, String proveedor);

}
