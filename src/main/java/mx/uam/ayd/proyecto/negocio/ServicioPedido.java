package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.DistribuidorRepository;
import mx.uam.ayd.proyecto.datos.PedidoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;
import mx.uam.ayd.proyecto.negocio.modelo.Pedido;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejar la logica de negocio de los Pedidos
 * Centraliza las reglas antes de tocar la base de datos
 * 
 * @author Erik LIE04
 */
@Service
public class ServicioPedido {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Repositorio integrado del compañero para consultar la base
    @Autowired
    private DistribuidorRepository distribuidorRepository;

    /**
     * Recupera todos los distribuidores para mostrarlos en la vista (ComboBox)
     * @return Lista de distribuidores disponibles
     */
    public List<Distribuidor> obtenerDistribuidores() {
        return (List<Distribuidor>) distribuidorRepository.findAll();
    }

    /**
     * Crea un nuevo pedido con sus validaciones correspondientes
     * @param distribuidor El proveedor de la pieza (entidad)
     * @param refaccion La pieza del catalogo solicitada
     * @param cantidad Numero de piezas a pedir
     * @param reparacion El auto destino (puede ser null si es para inventario local)
     * @return el pedido guardado en la base de datos
     */
    public Pedido crearPedido(Distribuidor distribuidor, Refaccion refaccion, int cantidad, Reparacion reparacion) {
        
        // Validaciones de negocio
        if (distribuidor == null) {
            throw new IllegalArgumentException("Se debe seleccionar un distribuidor valido");
        }
        if (refaccion == null) {
            throw new IllegalArgumentException("La refaccion no puede ser nula");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad pedida debe ser mayor a cero");
        }

        // Armamos el objeto a mano
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFechaPedido(LocalDate.now());
        nuevoPedido.setEstadoPedido("Pendiente"); // Estado inicial por defecto
        nuevoPedido.setDistribuidor(distribuidor);
        nuevoPedido.setRefaccion(refaccion);
        nuevoPedido.setCantidad(cantidad);
        nuevoPedido.setReparacion(reparacion); 

        return pedidoRepository.save(nuevoPedido);
    }

    /**
     * Recupera la lista completa de pedidos para llenar la tabla de seguimiento
     * @return lista de todos los pedidos registrados
     */
    public List<Pedido> recuperarPedidos() {
        return (List<Pedido>) pedidoRepository.findAll();
    }

    /**
     * Actualiza el estado de un pedido (ej. Entregado, Cancelado)
     * @param idPedido id del pedido a modificar
     * @param nuevoEstado el estado al que va a cambiar
     * @return el pedido actualizado
     * @throws IllegalArgumentException si el pedido no existe
     */
    public Pedido actualizarEstado(int idPedido, String nuevoEstado) {
        
        // Buscamos si el pedido realmente existe en la BD
        Optional<Pedido> optPedido = pedidoRepository.findById(idPedido);
        
        if(optPedido.isPresent()) {
            Pedido pedido = optPedido.get();
            pedido.setEstadoPedido(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        
        throw new IllegalArgumentException("No se encontro el pedido con ID: " + idPedido);
    }
    /**
     * Actualiza el estado de un pedido existente en la base de datos.
     * 
     * @param pedido El pedido seleccionado desde la vista.
     * @param nuevoEstado El nuevo estado ("Cancelado" o "Entregado").
     */
    public void actualizarEstadoPedido(Pedido pedido, String nuevoEstado) {
        // Validamos que el pedido no sea nulo por seguridad
        if (pedido == null || nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El pedido y el estado no pueden estar vacíos");
        }
        
        pedido.setEstadoPedido(nuevoEstado);
        
        // Al usar save() con una entidad que ya tiene un ID asignado, 
        // Spring Data JPA hace un UPDATE en lugar de un INSERT.
        pedidoRepository.save(pedido);
    }
}