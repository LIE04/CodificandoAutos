package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de negocio PiezaInventario (HU-31: Registro de piezas)
 *
 * Representa una refacción disponible en el almacén del taller. La cantidad
 * se incrementa cuando llega una nueva remesa de una pieza que ya existe
 * (mismo nombre y proveedor), en vez de crear un registro duplicado.
 */
@Entity
public class PiezaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPieza;

    private String nombre;
    private int cantidad;
    private String proveedor;
    private double costoUnitario;
    private LocalDate fechaRecepcion;

    // Fecha y hora del último movimiento (entrada) de esta pieza, para auditoría
    private LocalDateTime fechaHoraRegistro;

    public PiezaInventario() {
    }

    public long getIdPieza() {
        return idPieza;
    }

    public void setIdPieza(long idPieza) {
        this.idPieza = idPieza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PiezaInventario other = (PiezaInventario) obj;
        return idPieza == other.idPieza;
    }

    @Override
    public int hashCode() {
        return (int) (31 * idPieza);
    }

    @Override
    public String toString() {
        return "PiezaInventario [idPieza=" + idPieza + ", nombre=" + nombre + ", cantidad=" + cantidad
                + ", proveedor=" + proveedor + ", costoUnitario=" + costoUnitario + ", fechaRecepcion="
                + fechaRecepcion + ", fechaHoraRegistro=" + fechaHoraRegistro + "]";
    }
}
