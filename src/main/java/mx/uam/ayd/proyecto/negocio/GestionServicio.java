package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.ServicioRepository;
import mx.uam.ayd.proyecto.datos.VehiculoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Servicio;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Servicio de negocio para el historial de servicios de un vehículo (HU-29).
 *
 * Se llama "GestionServicio" (en vez de "ServicioServicio") para no chocar
 * con el nombre de la entidad de negocio {@link Servicio}.
 */
@Service
public class GestionServicio {

    private static final Logger log = LoggerFactory.getLogger(GestionServicio.class);

    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;

    @Autowired
    public GestionServicio(VehiculoRepository vehiculoRepository, ServicioRepository servicioRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.servicioRepository = servicioRepository;
    }

    /**
     * Busca un vehículo por sus placas, para consultar su historial de servicio
     *
     * @param placas placas del vehículo
     * @return el vehículo encontrado, o null si no existe ninguno con esas placas
     */
    public Vehiculo buscaVehiculoPorPlacas(String placas) {

        if (placas == null || placas.trim().isEmpty()) {
            return null;
        }

        return vehiculoRepository.findByPlacas(placas.trim());
    }

    /**
     * Recupera el historial de servicios (reparaciones y mantenimientos) de un vehículo
     *
     * @param vehiculo el vehículo del que se desea consultar el historial
     * @return una lista con los servicios registrados (del más reciente al más antiguo), o lista vacía
     */
    public List<Servicio> recuperaHistorial(Vehiculo vehiculo) {

        if (vehiculo == null) {
            return new ArrayList<>();
        }

        return servicioRepository.findByVehiculoOrderByFechaDesc(vehiculo);
    }

    /**
     *
     * Registra una reparación o mantenimiento realizado a un vehículo, y lo
     * asocia a su historial. El registro queda ligado al cliente de forma
     * transitiva, a través del vehículo.
     *
     * @param vehiculo vehículo al que se le realizó el servicio
     * @param fecha fecha en que se realizó el servicio
     * @param descripcion descripción del trabajo realizado
     * @param piezasUtilizadas piezas utilizadas durante el servicio (texto libre)
     * @param costoManoObra costo de la mano de obra
     * @param observaciones observaciones adicionales sobre el servicio
     * @return el servicio registrado
     * @throws IllegalArgumentException si el vehículo es nulo, o si falta algún dato obligatorio
     */
    public Servicio registraServicio(Vehiculo vehiculo, LocalDate fecha, String descripcion,
            String piezasUtilizadas, double costoManoObra, String observaciones) {

        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe seleccionarse un vehículo para registrar el servicio");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del servicio no puede ser nula");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del trabajo realizado no puede ser nula o vacía");
        }

        if (costoManoObra < 0) {
            throw new IllegalArgumentException("El costo de mano de obra no puede ser negativo");
        }

        log.info("Registrando servicio para vehículo placas: " + vehiculo.getPlacas());

        Servicio servicio = new Servicio();
        servicio.setVehiculo(vehiculo);
        servicio.setFecha(fecha);
        servicio.setDescripcion(descripcion);
        servicio.setPiezasUtilizadas(piezasUtilizadas);
        servicio.setCostoManoObra(costoManoObra);
        servicio.setObservaciones(observaciones);
        servicio.setFechaHoraRegistro(LocalDateTime.now());

        return servicioRepository.save(servicio);
    }

}
