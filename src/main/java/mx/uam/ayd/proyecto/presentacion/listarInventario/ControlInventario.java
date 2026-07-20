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
        // 1. La validación inicial se queda igual
    if (parametro == null || parametro.trim().isEmpty()) {
        return inventarioActual;
    }

    String p = parametro.toLowerCase();
    
    // 2. Creamos una nueva lista vacía para ir guardando las piezas que coincidan
    List<Refaccion> coincidencias = new ArrayList<>();

    // 3. Recorremos el inventario pieza por pieza usando un ciclo for-each
    for (Refaccion r : inventarioActual) {
        
        String nombreRefaccion = r.getNombre().toLowerCase();
        
        // Usamos String.valueOf() por si al final decidiste que tu ID sea un Integer. 
        // Si es String, funciona igual de bien.
        String idRefaccion = String.valueOf(r.getIdRefaccion()).toLowerCase(); 
        
        // 4. Comprobamos si el nombre o el ID contienen lo que escribió el usuario
        if (nombreRefaccion.contains(p) || idRefaccion.contains(p)) {
            
            // 5. Si la pieza cumple la condición, la metemos a la lista de resultados
            coincidencias.add(r);
        }
    }

    // 6. Al terminar de revisar todas las piezas, devolvemos la lista con los resultados
    return coincidencias;
    }

    public void solicitarEdicion() {
        vistaInventario.mostrarEdicion();
    }

    public void verificarEdicion(int id, String nombre, float precio, int existencias) {
        // Se envían los datos al servicio
        boolean exito = servicioRefaccion.enviarDatos(id, nombre, precio, existencias);
        
        if (exito) {
            vistaInventario.actualizarLista();
        }
    }


}
