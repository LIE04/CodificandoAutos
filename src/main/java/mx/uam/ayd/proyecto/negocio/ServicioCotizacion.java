package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.CotizacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.CotizacionConcepto;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;


@Service
public class ServicioCotizacion {

    private static final Logger log = LoggerFactory.getLogger(ServicioCotizacion.class);

    private final CotizacionRepository cotizacionRepository;

    @Autowired
    public ServicioCotizacion(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    /**
     * Crea un borrador de Cotización y lo vincula con la Cita origen.
     */
    public Cotizacion crearCotizacionBorrador(Cita citaOrigen) {
        Cotizacion nuevaCotizacion = new Cotizacion();
        nuevaCotizacion.setEstadoAprobacion("PENDIENTE");
        nuevaCotizacion.setCita(citaOrigen); // Aquí hacemos el enlace
        return nuevaCotizacion;
    }

    /**
     * 2. NUEVO: El servicio crea el concepto y lo vincula al borrador
     */
    public void agregarRefaccionACotizacionBorrador(Cotizacion borrador, Refaccion refaccion, int cantidad) {
        // La validación de negocio se hace en el servicio
        if (refaccion.getExistencia() < cantidad) {
            throw new IllegalArgumentException("No hay piezas suficientes en inventario.");
        }

        // EL SERVICIO HACE LA INSTANCIACIÓN
        CotizacionConcepto nuevoConcepto = new CotizacionConcepto(refaccion, cantidad, borrador);

        // Vinculamos el concepto creado a la lista de la cotización
        borrador.getConceptos().add(nuevoConcepto);
    }

    /**
     * Guarda la cotización y TODOS sus conceptos automáticamente
     */
    public void guardarCotizacion(Cotizacion cotizacionTerminada) {
        
        // ¡Magia! Al guardar la cotización, Hibernate va a la tabla de Cotizacion, 
        // la inserta, obtiene su ID, y luego va a la tabla de CotizacionConcepto
        // e inserta cada una de las refacciones vinculándolas al ID correcto.
        cotizacionRepository.save(cotizacionTerminada);
    }

}
