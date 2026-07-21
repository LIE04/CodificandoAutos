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
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;


@Service
public class ServicioCotizacion {

    private static final Logger log = LoggerFactory.getLogger(ServicioCotizacion.class);

    private final CotizacionRepository cotizacionRepository;

    @Autowired
    public ServicioCotizacion(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }


}
