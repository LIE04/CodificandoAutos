package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de negocio que representa una Refaccion en la base de datos.
 * Esta clase mapea los datos de las piezas que se almacenan en el sistema,
 * de acuerdo al modelo de dominio de la HU-30
 *
 * @author Erik LIE04
 */
@Entity // Esto le dice a Spring que esta es una entidad persistente
public class Refaccion {

    @Id // indica que es la llave primaria en la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue hace que se incremente automáticamente
    private int idRefaccion;

    /** Nombre o descripción de la refacción */
    private String nombre;

    /** Precio unitario de la refacción. */
    private float precio;

    /** Cantidad de piezas disponibles en inventario. */
    private int existencia;

    /** Proveedor que surtió la última remesa de esta refacción (HU-31). */
    private String proveedor;

    /** Fecha en la que se recibió la última remesa (HU-31). */
    private LocalDate fechaRecepcion;

    /** Fecha y hora del último movimiento de entrada, para auditoría (HU-31). */
    private LocalDateTime fechaHoraRegistro;


    /** CONSTRUCTORES
     * Constructor vacío
     * para que puedan crear objetos de esta clase al consultar la base de datos
     */
    public Refaccion() {
    }

    /** GETTERS Y SETTERS
     * Obtiene el ID de la refacción
     * @return idRefaccion
     */
    public int getIdRefaccion() {
        return idRefaccion;
    }

    /**
     * Establece el ID de la refacción
     * @param idRefaccion el nuevo identificador
     */
    public void setIdRefaccion(int idRefaccion) {
        this.idRefaccion = idRefaccion;
    }

    /**
     * Obtiene el nombre de la pieza
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna o cambia el nombre de la pieza
     * @param nombre la nueva descripción
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el precio de la pieza
     * @return precio
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Asigna un nuevo precio a la pieza
     * @param precio el costo a registrar
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad en existencia
     * @return existencia
     */
    public int getExistencia() {
        return existencia;
    }

    /**
     * Actualiza la cantidad de piezas disponibles
     * @param existencia nuevo valor de inventario
     */
    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    /**
     * Obtiene el proveedor de la última remesa recibida
     * @return proveedor
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Asigna el proveedor de la última remesa recibida
     * @param proveedor el nuevo proveedor
     */
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Obtiene la fecha en que se recibió la última remesa
     * @return fechaRecepcion
     */
    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * Asigna la fecha de recepción de la última remesa
     * @param fechaRecepcion la nueva fecha
     */
    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    /**
     * Obtiene la fecha y hora del último movimiento de entrada
     * @return fechaHoraRegistro
     */
    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    /**
     * Sella la fecha y hora del último movimiento de entrada
     * @param fechaHoraRegistro la nueva fecha y hora
     */
    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    @Override
    public String toString() {
       
        return idRefaccion + " - " + nombre + " ($" + precio + ")"; 
    }


}