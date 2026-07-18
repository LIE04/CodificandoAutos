package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.DistribuidorRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;

/**
 * Servicio de negocio para la consulta de distribuidores (HU-25)
 */
@Service
public class ServicioDistribuidor {

    private static final Logger log = LoggerFactory.getLogger(ServicioDistribuidor.class);

    private final DistribuidorRepository distribuidorRepository;

    @Autowired
    public ServicioDistribuidor(DistribuidorRepository distribuidorRepository) {
        this.distribuidorRepository = distribuidorRepository;
    }

    /**
     * Recupera todos los distribuidores registrados en el sistema
     *
     * @return una lista con los distribuidores (o lista vacía)
     */
    public List<Distribuidor> recuperaDistribuidores() {

        List<Distribuidor> distribuidores = new ArrayList<>();

        for (Distribuidor distribuidor : distribuidorRepository.findAll()) {
            distribuidores.add(distribuidor);
        }

        return distribuidores;
    }

    /**
     *
     * Busca distribuidores por nombre o por tipo de refacción. Si el
     * criterio de búsqueda es nulo o vacío, se regresan todos los
     * distribuidores registrados.
     *
     * @param criterio texto a buscar en el nombre o el tipo de refacción
     * @return una lista con los distribuidores que coinciden (o lista vacía)
     */
    public List<Distribuidor> buscaDistribuidores(String criterio) {

        if (criterio == null || criterio.trim().isEmpty()) {
            return recuperaDistribuidores();
        }

        log.info("Buscando distribuidores con criterio: " + criterio);

        return distribuidorRepository.findByNombreContainingIgnoreCaseOrTipoRefaccionContainingIgnoreCase(criterio,
                criterio);
    }

}
