package mx.uam.ayd.proyecto.presentacion.consultarEntregas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mx.uam.ayd.proyecto.datos.ReparacionRepository.VehiculosPendientesDTO;
import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.presentacion.consultarEntregas.VistaVehiculosEntrega;



@Component
public class ControlVehiculosEntrega {

    @Autowired
    private ServicioReparacion servicioReparacion;

    @Autowired
    private VistaVehiculosEntrega vistaVehiculoEntrega;

    private List<VehiculosPendientesDTO> inventarioActual;

    public ControlVehiculosEntrega(ServicioReparacion servicioReparacion, VistaVehiculosEntrega vistaVehiculoEntrega) {
		this.servicioReparacion = servicioReparacion;
		this.vistaVehiculoEntrega = vistaVehiculoEntrega;
	}

    public void inicia() {
        
        vistaVehiculoEntrega.setControlVehiculosEntrega(this);
        
        SolicitarInventario();
    }
    
    public void SolicitarInventario() {
        inventarioActual = servicioReparacion.obtenerVehiculosParaEntrega();

        vistaVehiculoEntrega.mostrarListaVehiculos(inventarioActual);
    }

    public void buscarRefaccion(String parametroBusqueda) {

        List<VehiculosPendientesDTO> coincidencias = retornarCoincidencias(parametroBusqueda);
        vistaVehiculoEntrega.retornarCoincidencias(coincidencias);
    }

    public List<VehiculosPendientesDTO> retornarCoincidencias(String parametro) {
        
    if (parametro == null || parametro.trim().isEmpty()) {
        return inventarioActual;
    }

    String p = parametro.toLowerCase();
    
    List<VehiculosPendientesDTO> coincidencias = new ArrayList<>();

    for (VehiculosPendientesDTO r : inventarioActual) {
        
        String nombreRefaccion = r.getNombre().toLowerCase();
        
        //String idRefaccion = String.valueOf(r.getIdRefaccion()).toLowerCase(); 
        
        if (nombreRefaccion.contains(p)) {
            
            coincidencias.add(r);
        }
    }

    return coincidencias;
    }

    public boolean finalizarEntrega(Integer idReparacion) {

        boolean exito = servicioReparacion.marcarEntregado(idReparacion);

        if(exito){
            
            SolicitarInventario();

        }

        return exito;

    }



}
