package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.PiezaInventarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.PiezaInventario;

/**
 * Servicio de negocio para el inventario de piezas (HU-31: Registro de piezas)
 */
@Service
public class ServicioInventario {

    private static final Logger log = LoggerFactory.getLogger(ServicioInventario.class);

    private final PiezaInventarioRepository piezaInventarioRepository;

    @Autowired
    public ServicioInventario(PiezaInventarioRepository piezaInventarioRepository) {
        this.piezaInventarioRepository = piezaInventarioRepository;
    }

    /**
     *
     * Registra la llegada de piezas al taller. Si ya existe una pieza con el
     * mismo nombre y proveedor, se incrementa su cantidad disponible; si no
     * existe, se crea un nuevo registro de inventario.
     *
     * @param nombre nombre de la pieza
     * @param cantidad cantidad recibida, debe ser mayor a cero
     * @param proveedor proveedor que suministró la pieza
     * @param fechaRecepcion fecha en la que se recibió la pieza
     * @param costoUnitario costo unitario de la pieza
     * @return la pieza registrada o actualizada
     * @throws IllegalArgumentException si algún dato obligatorio falta o la cantidad no es mayor a cero
     */
    public PiezaInventario registrarPieza(String nombre, int cantidad, String proveedor,
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

        // Regla de negocio: si la pieza ya existe (mismo nombre y proveedor),
        // se incrementa la cantidad en vez de duplicar el registro
        PiezaInventario pieza = piezaInventarioRepository.findByNombreAndProveedor(nombre, proveedor);

        if (pieza != null) {
            log.info("Incrementando cantidad de pieza existente: " + nombre + " proveedor: " + proveedor);
            pieza.setCantidad(pieza.getCantidad() + cantidad);
            pieza.setCostoUnitario(costoUnitario);
            pieza.setFechaRecepcion(fechaRecepcion);
            pieza.setFechaHoraRegistro(ahora);
        } else {
            log.info("Registrando nueva pieza: " + nombre + " proveedor: " + proveedor);
            pieza = new PiezaInventario();
            pieza.setNombre(nombre);
            pieza.setCantidad(cantidad);
            pieza.setProveedor(proveedor);
            pieza.setCostoUnitario(costoUnitario);
            pieza.setFechaRecepcion(fechaRecepcion);
            pieza.setFechaHoraRegistro(ahora);
        }

        return piezaInventarioRepository.save(pieza);
    }

    /**
     * Recupera todas las piezas registradas en el inventario
     *
     * @return una lista con las piezas (o lista vacía)
     */
    public List<PiezaInventario> recuperaPiezas() {

        List<PiezaInventario> piezas = new ArrayList<>();

        for (PiezaInventario pieza : piezaInventarioRepository.findAll()) {
            piezas.add(pieza);
        }

        return piezas;
    }

}
