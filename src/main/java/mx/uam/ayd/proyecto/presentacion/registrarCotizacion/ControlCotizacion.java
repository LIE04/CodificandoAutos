package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioCotizacion;
import mx.uam.ayd.proyecto.negocio.ServicioRefaccion;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioVehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.CotizacionConcepto;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;




@Component
public class ControlCotizacion{

   @Autowired
   private ServicioCliente servicioCliente;
   
   @Autowired
   private ServicioVehiculo servicioVehiculo;

   @Autowired
   private ServicioRefaccion servicioRefaccion;

   @Autowired
   private ServicioCotizacion servicioCotizacion;

   //@Autowired
   //private ServicioCita servicioCita;

   private VistaCotizacion vista;

   private Cliente clienteSeleccionado;

   // Este es tu "borrador" en memoria. Aún no se guarda en la BD.
   private Cotizacion cotizacionBorrador = new Cotizacion();


   // PASO 1: Esto se llama justo cuando abres la ventana de "Generar Cotización"
    public void iniciarVistaCotizacion(VistaCotizacion vista) {
        this.vista = vista;
        List<Cliente> clientesDisponibles = servicioCliente.getClientes();
        this.vista.mostrarClientes(clientesDisponibles); // Llena el ComboBox
    }

    // PASO 2: Esto se llama cuando el usuario hace clic/selecciona un cliente en la vista
    public void clienteSeleccionado(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        List<Vehiculo> vehiculosDelCliente = servicioVehiculo.getVehiculosCliente(cliente.getIdCliente());
        vista.mostrarVehiculos(vehiculosDelCliente);
    }

// PASO 3 MODIFICADO: Ahora buscamos la Cita y delegamos la creación al Servicio
   /* public void iniciarNuevaCotizacion(Vehiculo vehiculoSeleccionado) {
        
        // 1. Buscamos la cita activa/pendiente de este vehículo
        // (Asegúrate de tener un método así en tu ServicioCita)
       // Cita citaDelVehiculo = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoSeleccionado);
        
        if (citaDelVehiculo == null) {
            // Manejo de error: Si el vehículo no tiene cita, no se puede cotizar
            throw new IllegalStateException("El vehículo seleccionado no tiene una cita pendiente.");
        }

        // 2. El servicio de cotización fabrica la entidad y le enlaza la cita
        this.cotizacionBorrador = servicioCotizacion.crearCotizacionBorrador(citaDelVehiculo);
    }

    // PASO 4a: Buscar refacción por nombre
    public Refaccion buscarRefaccion(String nombrePieza) {
        return servicioRefaccion.buscarPorNombre(nombrePieza); 
    }*/

// PASO 4b: Ingresar la pieza a la cotización a través del servicio
    public void agregarRefaccionACotizacion(Refaccion refaccionSeleccionada, int cantidad) {
        
        // ¡Magia! Le pasamos el problema al servicio. 
        // Él crea el CotizacionConcepto y lo mete al borrador.
        servicioCotizacion.agregarRefaccionACotizacionBorrador(this.cotizacionBorrador, refaccionSeleccionada, cantidad);
        
        // El controlador solo se encarga de actualizar los cálculos para la vista
        recalcularTotales();
    }

    // PASO 5: La vista llamará a este método pasándole el texto de los TextFields
    public void capturarDatosServicio(String fallas, String descripcionManoObra, float costoManoObra) {
        this.cotizacionBorrador.setDescripcionFallas(fallas);
        this.cotizacionBorrador.setManoObra(descripcionManoObra);
        this.cotizacionBorrador.setManoObraCosto(costoManoObra);
        
        recalcularTotales(); 
    }
    
    // PASO 6: Recálculo automático (Uso interno)
    private void recalcularTotales() {
        float costoRefacciones = 0;
        
        for (CotizacionConcepto concepto : cotizacionBorrador.getConceptos()) {
            costoRefacciones += concepto.getSubtotal();
        }
        
        this.cotizacionBorrador.setRefaccionesCosto(costoRefacciones);
        
        float total = costoRefacciones + this.cotizacionBorrador.getManoObraCosto();
        this.cotizacionBorrador.setCostoTotal(total);
        
       this.vista.actualizarEtiquetasTotales(costoRefacciones, total);
    }

    // PASO 7: ¡MÉTODO AGREGADO! Guardado final
    public void finalizarCotizacion() {
        // Le pasamos el borrador ya lleno a la capa de servicio para que lo guarde en BD
        servicioCotizacion.guardarCotizacion(this.cotizacionBorrador);
        
        // Aquí podrías decirle a la vista que cierre la ventana o muestre un mensaje de éxito
    }
}