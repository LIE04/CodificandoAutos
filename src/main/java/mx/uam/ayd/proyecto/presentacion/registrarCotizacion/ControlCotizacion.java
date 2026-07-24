package mx.uam.ayd.proyecto.presentacion.registrarCotizacion;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.CotizacionConcepto;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioVehiculo;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioRefaccion;
import mx.uam.ayd.proyecto.negocio.ServicioCotizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ControlCotizacion {

    @Autowired private ServicioCliente servicioCliente;
    @Autowired private ServicioVehiculo servicioVehiculo;
    @Autowired private ServicioCita servicioCita;
    @Autowired private ServicioRefaccion servicioRefaccion;
    @Autowired private ServicioCotizacion servicioCotizacion;
    private float totalRefacciones = 0.0f;
    private float costoManoObra = 0.0f;
    
    @Autowired private VistaCotizacion vista;

    public void iniciar() {
        vista.iniciar(this);
        List<Cliente> clientesDisponibles = servicioCliente.getClientes();
        vista.mostrarClientes(clientesDisponibles);
    }

    public void onClienteSeleccionado(Cliente cliente) {
        if (cliente != null) {
            List<Vehiculo> vehiculosDelCliente = servicioVehiculo.getVehiculosCliente(cliente.getIdCliente());
            vista.mostrarVehiculos(vehiculosDelCliente);
        }
    }

    public void onVehiculoSeleccionado(Vehiculo vehiculoSeleccionado) {
        if (vehiculoSeleccionado != null) {
            Cita cita = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoSeleccionado);
            
            if (cita != null) {
                boolean exito = servicioCotizacion.crearCotizacionBorrador(cita);
                if (exito) {
                    vista.permitirEdicion();
                }
            } else {
                vista.mostrarMensajeError("El vehículo no tiene una cita pendiente.");
                vista.bloquearEdicion();
                
            }
        }
    }

    public void onBuscarRefaccionClick(Integer idPieza) {
        List<Refaccion> encontrada = servicioRefaccion.buscarRefaccion(idPieza);
        vista.mostrarRefaccion(encontrada);
    }

    public float calcularSubtotal() {
        return totalRefacciones + costoManoObra;
    }

    public float calcularIva() {
        return calcularSubtotal() * 0.16f; 
    }

    public float calcularTotal() {
        return calcularSubtotal() + calcularIva();
    }

    public void onAgregarRefaccion(Refaccion seleccionada, int cantidad) {
        if (seleccionada != null) {
            boolean exito = servicioCotizacion.agregarRefaccionACotizacionBorrador(seleccionada, cantidad);
            if (exito) {
                float costoDeEstaPieza = seleccionada.getPrecio() * cantidad;
                this.totalRefacciones = this.totalRefacciones + costoDeEstaPieza;

                vista.recalcularTotales();

            } 
        }else {
            vista.mostrarMensajeError("Seleccione una refaccion");
        }
    }

    public void onActualizarServicio(String fallas, String manoObra, float costoManoObra) {
        boolean exito = servicioCotizacion.capturarDatosServicio(fallas, manoObra, costoManoObra);
        if (exito) {

            this.costoManoObra = costoManoObra;
            vista.recalcularTotales();
        }
    }

    public void onGuardarClick() {
        boolean exito = servicioCotizacion.finalizarCotizacion();
        if (exito) {
            vista.mostrarMensajeExito();
        } else {
            vista.mostrarMensajeError("No se pudo guardar la cotización.");
        }
    }
}