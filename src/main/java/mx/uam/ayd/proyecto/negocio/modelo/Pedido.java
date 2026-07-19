package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;

/**
 * Representa la entidad de negocio Pedido
 * Guarda la informacion de una solicitud de piezas al distribuidor
 * 
 * @author Erik LIE04
 */
@Entity 
public class Pedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int idPedido;

    /** Fecha en la que se pidio la pieza */
    private LocalDate fechaPedido; 
    
    /** Estado del pedido por defecto En espera */
    private String estadoPedido; 

    /** 
     * Relacion con Distribuidor (Actualizado para la integracion)
     */
    @ManyToOne
    @JoinColumn(name = "id_distribuidor")
    private Distribuidor distribuidor; 
    
    /** Cuantas piezas de esta refaccion necesitamos para el auto */
    private int cantidad;

    /** 
     * Relacion con Refaccion
     * Un pedido esta ligado a una refaccion del catalogo
     */
    @ManyToOne
    @JoinColumn(name = "id_refaccion")
    private Refaccion refaccion;

    /**
     * Relacion con Reparacion
     * Para saber a que auto le urgen estas piezas (nulo si es para inventario local)
     */
    @ManyToOne
    @JoinColumn(name = "id_reparacion", nullable = true)
    private Reparacion reparacion; 


    /** CONSTRUCTORES
     * Constructor vacio
     */
    public Pedido() {
    }


    /** GETTERS Y SETTERS
     * Obtiene el ID del pedido
     * @return idPedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el ID del pedido
     * @param idPedido el nuevo identificador
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la fecha en la que se hizo el pedido
     * @return fechaPedido
     */
    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    /**
     * Asigna la fecha del pedido
     * @param fechaPedido dia en que se solicito
     */
    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * Obtiene el estado actual del pedido
     * @return estadoPedido
     */
    public String getEstadoPedido() {
        return estadoPedido;
    }

    /**
     * Cambia el estado del pedido
     * @param estadoPedido el nuevo estado
     */
    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    /**
     * Obtiene el distribuidor de la pieza
     * @return distribuidor
     */
    public Distribuidor getDistribuidor() {
        return distribuidor;
    }

    /**
     * Asigna el distribuidor
     * @param distribuidor objeto del proveedor
     */
    public void setDistribuidor(Distribuidor distribuidor) {
        this.distribuidor = distribuidor;
    }
    
    /**
     * Obtiene la cantidad de piezas pedidas
     * @return cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Asigna la cantidad de piezas que se van a pedir
     * @param cantidad numero de piezas
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene la refaccion asociada a este pedido
     * @return refaccion
     */
    public Refaccion getRefaccion() {
        return refaccion;
    }

    /**
     * Enlaza la refaccion con el pedido
     * @param refaccion el objeto refaccion
     */
    public void setRefaccion(Refaccion refaccion) {
        this.refaccion = refaccion;
    }

    /**
     * Obtiene la reparacion dueña de este pedido
     * @return reparacion
     */
    public Reparacion getReparacion() {
        return reparacion;
    }

    /**
     * Asigna la reparacion para saber de que auto es
     * @param reparacion el objeto de la reparacion
     */
    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }
}