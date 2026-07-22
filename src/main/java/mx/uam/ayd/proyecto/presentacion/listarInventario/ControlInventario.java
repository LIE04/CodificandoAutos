package mx.uam.ayd.proyecto.presentacion.listarInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import mx.uam.ayd.proyecto.negocio.ServicioRefaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;


@Component
public class ControlInventario {

    @Autowired
    private ServicioRefaccion servicioRefaccion;

    @Autowired
    private VistaInventario vistaInventario;

    @Autowired
private VistaEditarRefaccion vistaEditarRefaccion;

    private List<Refaccion> inventarioActual;

    public ControlInventario(ServicioRefaccion servicioRefaccion, VistaInventario vistaInventario) {
		this.servicioRefaccion = servicioRefaccion;
		this.vistaInventario = vistaInventario;
	}

    public void inicia() {
        // 1. Le decimos a la vista quién es su control (¡Muy importante para que los botones de la vista funcionen!)
        vistaInventario.setControlInventario(this);
        
        // 2. Ejecutamos el método que pide los datos, tal como dicta tu diagrama de secuencia
        SolicitarInventario();
    }

    public void SolicitarInventario() {
        inventarioActual = servicioRefaccion.getRefaccion();

        vistaInventario.mostrarInventario(inventarioActual);
    }

    public void buscarRefaccion(String parametroBusqueda) {

        List<Refaccion> coincidencias = filtrarCoincidencias(parametroBusqueda);
        vistaInventario.retornarCoincidencias(coincidencias);
    }

    private List<Refaccion> filtrarCoincidencias(String parametro) {
        
    if (parametro == null || parametro.trim().isEmpty()) {
        return inventarioActual;
    }

    String p = parametro.toLowerCase();
    
    List<Refaccion> coincidencias = new ArrayList<>();

    for (Refaccion r : inventarioActual) {
        
        String nombreRefaccion = r.getNombre().toLowerCase();
        
        String idRefaccion = String.valueOf(r.getIdRefaccion()).toLowerCase(); 
        
        if (nombreRefaccion.contains(p) || idRefaccion.contains(p)) {
            
            coincidencias.add(r);
        }
    }

    return coincidencias;
    }

    public void solicitarEdicion(Refaccion seleccionada) {
        vistaEditarRefaccion.inicia(this, seleccionada);
    }

    public void verificarEdicion(int id, String nombre, float precio, int existencias) {
        // Se envían los datos al servicio
        boolean exito = servicioRefaccion.enviarDatos(id, nombre, precio, existencias);
        
        if (exito) {
            vistaInventario.actualizarLista();
        }
    }


}
