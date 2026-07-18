package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;

/**
 * Repositorio para distribuidores (HU-25: Consultar números de distribuidores)
 */
public interface DistribuidorRepository extends CrudRepository<Distribuidor, Long> {

    /**
     * Busca distribuidores cuyo nombre o tipo de refacción contenga el
     * criterio dado (sin distinguir mayúsculas/minúsculas)
     */
    public List<Distribuidor> findByNombreContainingIgnoreCaseOrTipoRefaccionContainingIgnoreCase(String nombre,
            String tipoRefaccion);

}
