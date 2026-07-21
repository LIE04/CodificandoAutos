package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.RefaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 * Servicio de negocio para el inventario de piezas (HU-31: Registro de piezas).
 * Usa la entidad Refaccion (la misma que HU-30/HU-12), en vez de tener una
 * entidad aparte para lo mismo.
 */
@Service
public class ServicioInventario {

    private static final Logger log = LoggerFactory.getLogger(ServicioInventario.class);

    private final RefaccionRepository refaccionRepository;

    @Autowired
    public ServicioInventario(RefaccionRepository refaccionRepository) {
        this.refaccionRepository = refaccionRepository;
    }

    /**
     *
     * Registra la llegada de piezas al taller. Si ya existe una refacción con el
     * mismo nombre y proveedor, se incrementa su existencia; si no
     * existe, se da de alta un nuevo registro.
     *
     * @param nombre nombre de la pieza
     * @param cantidad cantidad recibida, debe ser mayor a cero
     * @param proveedor proveedor que suministró la pieza
     * @param fechaRecepcion fecha en la que se recibió la pieza
     * @param costoUnitario costo unitario de la pieza
     * @return la refacción registrada o actualizada
     * @throws IllegalArgumentException si algún dato obligatorio falta o la cantidad no es mayor a cero
     */
    public Refaccion registrarPieza(String nombre, int cantidad, String proveedor,
            LocalDate fechaRecepcion, double costoUnitario) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la pieza no puede ser nulo o vacío");
        }

        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo o vacío");
        }

        if (fechaRecepcion == null) {
            throw new IllegalArgumentException("La fecha de recepción no puede ser nula");
        }

        // Regla de negocio: la cantidad recibida debe ser mayor a cero
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad recibida debe ser mayor a cero");
        }

        LocalDateTime ahora = LocalDateTime.now();

        // Regla de negocio: si la refaccion ya existe (mismo nombre y proveedor),
        // se incrementa la existencia en vez de duplicar el registro
        Refaccion refaccion = refaccionRepository.findByNombreAndProveedor(nombre, proveedor);

        if (refaccion != null) {
            log.info("Incrementando existencia de refaccion existente: " + nombre + " proveedor: " + proveedor);
            refaccion.setExistencia(refaccion.getExistencia() + cantidad);
            refaccion.setPrecio((float) costoUnitario);
            refaccion.setFechaRecepcion(fechaRecepcion);
            refaccion.setFechaHoraRegistro(ahora);
        } else {
            log.info("Registrando nueva refaccion: " + nombre + " proveedor: " + proveedor);
            refaccion = new Refaccion();
            refaccion.setNombre(nombre);
            refaccion.setExistencia(cantidad);
            refaccion.setProveedor(proveedor);
            refaccion.setPrecio((float) costoUnitario);
            refaccion.setFechaRecepcion(fechaRecepcion);
            refaccion.setFechaHoraRegistro(ahora);
        }

        return refaccionRepository.save(refaccion);
    }

    /**
     * Recupera todas las piezas registradas en el inventario
     *
     * @return una lista con las refacciones (o lista vacía)
     */
    public List<Refaccion> recuperaPiezas() {

        List<Refaccion> piezas = new ArrayList<>();

        for (Refaccion refaccion : refaccionRepository.findAll()) {
            piezas.add(refaccion);
        }

        return piezas;
    }

}
