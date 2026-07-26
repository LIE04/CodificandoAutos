package mx.uam.ayd.proyecto.presentacion.consultarEntregas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import mx.uam.ayd.proyecto.datos.ReparacionRepository.VehiculosPendientesDTO;
import mx.uam.ayd.proyecto.negocio.ServicioReparacion;
// Modificación realizada por Erik para la HU-40 (Control de Calidad)
// Limpié los import duplicados que había de ControlControlCalidad para mantener limpio el código
import mx.uam.ayd.proyecto.presentacion.controlCalidad.ControlControlCalidad;

@Component
public class ControlVehiculosEntrega {

    @Autowired
    private ServicioReparacion servicioReparacion;

    @Autowired
    private VistaVehiculosEntrega vistaVehiculoEntrega;

    // Modificación realizada por Erik para la HU-40 (Control de Calidad)
    // Inyectamos mi controlador del escáner para poder llamarlo desde esta vista
    @Autowired
    private ControlControlCalidad controlControlCalidad;

    private List<VehiculosPendientesDTO> inventarioActual;

    public ControlVehiculosEntrega(ServicioReparacion servicioReparacion, VistaVehiculosEntrega vistaVehiculoEntrega, ControlControlCalidad controlControlCalidad) {
        this.servicioReparacion = servicioReparacion;
        this.vistaVehiculoEntrega = vistaVehiculoEntrega;
        // Modificación realizada por Erik para la HU-40 (Control de Calidad)
        this.controlControlCalidad = controlControlCalidad; 
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
        List<VehiculosPendientesDTO> coincidencias = filtarCoincidencias(parametroBusqueda);
        vistaVehiculoEntrega.retornarCoincidencias(coincidencias);
    }

    public List<VehiculosPendientesDTO> filtarCoincidencias(String parametro) {
        if (parametro == null || parametro.trim().isEmpty()) {
            return inventarioActual;
        }

        String p = parametro.toLowerCase();
        List<VehiculosPendientesDTO> coincidencias = new ArrayList<>();

        for (VehiculosPendientesDTO r : inventarioActual) {
            String nombreRefaccion = r.getNombre().toLowerCase();
            if (nombreRefaccion.contains(p)) {
                coincidencias.add(r);
            }
        }
        return coincidencias;
    }

    // Modificación realizada por Erik para la HU-40 (Control de Calidad)
    // Método puente para conectar el botón de la tabla con la ventana del escáner
    public void solicitarEscaner(int idReparacion) {
        // Arrancamos el flujo de control de calidad pasándole el ID de la reparación
        controlControlCalidad.inicia(idReparacion);

        // Actualizamos la lista de inventario. Ojo: si la ventana del escáner no es modal (showAndWait),
        // tal vez haya que poner un botón de "Refrescar" en la vista.
        SolicitarInventario();
    }

    public boolean terminarEntrega(Integer idReparacion) {
        boolean exito = servicioReparacion.marcarEntregado(idReparacion);

        if(exito){
            SolicitarInventario();
            vistaVehiculoEntrega.muestraDialogoConMensaje("Vehiculo entregado con exito");
        } else {
            vistaVehiculoEntrega.muestraDialogoConMensaje("Error: No puedes entregar este vehículo porque sigue 'En espera'");
        }
        return exito;
    }
}