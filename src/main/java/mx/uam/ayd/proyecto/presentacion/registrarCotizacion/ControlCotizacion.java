package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mx.uam.ayd.proyecto.negocio.ServicioCotizacion;
//import mx.uam.ayd.proyecto.presentacion.consultarEntregas.VistaControl;



@Component
public class ControlCotizacion{

    @Autowired
    private ServicioCotizacion servicioCotizacion;

 //   @Autowired
 //   private VistaVehiculosEntrega vistaVehiculoEntrega;


    public ControlCotizacion(ServicioCotizacion servicioCotizacion) {
		this.servicioCotizacion =  servicioCotizacion;
	}


}