package mx.uam.ayd.proyecto.presentacion.listarInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import mx.uam.ayd.proyecto.negocio.ServicioRefaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 * Control para la gestión y despliegue del inventario
 */
@Component
public class ControlInventario {

    /** Servicio para la gestión de refacciones */
    @Autowired
    private ServicioRefaccion servicioRefaccion;

    /** Vista principal del inventario */
    @Autowired
    private VistaInventario vistaInventario;

    /** Vista para la edición de una refacción */
    @Autowired
    private VistaEditarRefaccion vistaEditarRefaccion;

    /** Lista que almacena el inventario cargado actualmente */
    private List<Refaccion> inventarioActual;

    /**
     * Constructor para la inyección de dependencias del control
     * 
     * @param servicioRefaccion Servicio para operaciones de refacciones
     * @param vistaInventario Vista principal del inventario
     * @param vistaEditarRefaccion Vista para la edición de refacciones
     */
    public ControlInventario(ServicioRefaccion servicioRefaccion, VistaInventario vistaInventario, VistaEditarRefaccion vistaEditarRefaccion) {
        this.servicioRefaccion = servicioRefaccion;
        this.vistaInventario = vistaInventario;
        this.vistaEditarRefaccion = vistaEditarRefaccion;
    }

    /**
     * Inicia la interacción asociando el control a la vista y solicitando los datos del inventario
     */
    public void inicia() {
        // 1. Le decimos a la vista quién es su control (¡Muy importante para que los botones de la vista funcionen!)
        vistaInventario.setControlInventario(this);
        
        // 2. Ejecutamos el método que pide los datos, tal como dicta tu diagrama de secuencia
        SolicitarInventario();
    }

    /**
     * Obtiene la lista completa de refacciones desde el servicio y actualiza la vista
     */
    public void SolicitarInventario() {
        inventarioActual = servicioRefaccion.getRefaccion();

        vistaInventario.mostrarInventario(inventarioActual);
    }

    /**
     * Busca refacciones que coincidan con el parámetro recibido y actualiza la vista
     * 
     * @param parametroBusqueda Texto o ID a buscar en el inventario
     */
    public void buscarRefaccion(String parametroBusqueda) {

        List<Refaccion> coincidencias = filtrarCoincidencias(parametroBusqueda);
        vistaInventario.retornarCoincidencias(coincidencias);
    }

    /**
     * Filtra el inventario local por nombre o identificador
     * 
     * @param parametro Criterio de búsqueda ingresado
     * @return Lista de refacciones que coinciden con el parámetro
     */
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

    /**
     * Solicita el despliegue de la ventana de edición para una refacción
     * 
     * @param seleccionada Refacción elegida para editar
     */
    public void solicitarEdicion(Refaccion seleccionada) {
        vistaEditarRefaccion.inicia(this, seleccionada);
    }

    /**
     * Valida los datos modificados de una refacción y solicita su actualización al servicio
     * 
     * @param id Identificador de la refacción
     * @param nombre Nuevo nombre de la refacción
     * @param precio Nuevo precio de la refacción
     * @param existencias Cantidad actualizada de existencias
     */
    public void verificarEdicion(int id, String nombre, float precio, int existencias) {

        if(nombre == null || nombre.trim().isEmpty()) {
           vistaEditarRefaccion.muestraDialogoConMensaje("El nombre no puede estar vacio");
           return;
        }

        if (precio <= 0) {
            vistaEditarRefaccion.muestraDialogoConMensaje("El precio no puede ser igual o menor a 0.");
            return; 
        }

        if (existencias < 0) {
            vistaEditarRefaccion.muestraDialogoConMensaje("La existencia no puede ser menor a 0.");
            return; 
        }
   
        // Se envían los datos al servicio
        boolean exito = servicioRefaccion.enviarDatos(id, nombre, precio, existencias);
        
        if (exito) {
            vistaInventario.actualizarLista();
        }
    }

}