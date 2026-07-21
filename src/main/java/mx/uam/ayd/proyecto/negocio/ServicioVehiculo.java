package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.VehiculoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;



@Service
public class ServicioVehiculo {

    private static final Logger log = LoggerFactory.getLogger(ServicioVehiculo.class);

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public ServicioVehiculo(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository =  vehiculoRepository;
    }


}