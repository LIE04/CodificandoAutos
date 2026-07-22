package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.DetallesFallaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;


@Service
public class ServicioDetallesFalla {
    @Autowired
    private DetallesFallaRepository detallesFallaRepository;

    /**
     * Agrega un nuevo detalle de falla a una reparación existente
     */
    public DetallesFalla agregarDetallesFalla(String descripcionFalla, String estatus) {
        // Validaciones básicas de nulidad
        if (descripcionFalla == null || descripcionFalla.isEmpty()) {
            throw new IllegalArgumentException("La descripción de la falla no puede ser nula o vacía");
        }
        if (estatus == null || estatus.isEmpty()) {
            throw new IllegalArgumentException("El estatus de la falla no puede ser nulo o vacío");
        }

        // Crear y guardar la entidad DetallesFalla
        DetallesFalla detallesFalla = new DetallesFalla();
        detallesFalla.setDescripcionFalla(descripcionFalla);
        detallesFalla.setEstatus(estatus);
        DetallesFalla detallesFallaGuardado = detallesFallaRepository.save(detallesFalla);

        return detallesFallaGuardado;

    }
}