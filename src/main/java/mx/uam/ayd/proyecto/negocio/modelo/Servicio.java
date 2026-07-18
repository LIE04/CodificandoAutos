package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de negocio Servicio (HU-29: Historial de servicio)
 *
 * Representa una reparación o mantenimiento realizado a un vehículo. Cada
 * registro queda asociado al vehículo (y, a través de él, a su cliente) para
 * construir el historial del vehículo.
 */
@Entity
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idServicio;

    private LocalDate fecha;
    private String descripcion;
    private String piezasUtilizadas;
    private double costoManoObra;
    private String observaciones;

    // Fecha y hora en la que se capturó el registro (no la fecha del servicio en sí)
    private LocalDateTime fechaHoraRegistro;

    @ManyToOne
    private Vehiculo vehiculo;

    public Servicio() {
    }

    public long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(long idServicio) {
        this.idServicio = idServicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPiezasUtilizadas() {
        return piezasUtilizadas;
    }

    public void setPiezasUtilizadas(String piezasUtilizadas) {
        this.piezasUtilizadas = piezasUtilizadas;
    }

    public double getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(double costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Servicio other = (Servicio) obj;
        return idServicio == other.idServicio;
    }

    @Override
    public int hashCode() {
        return (int) (31 * idServicio);
    }

    @Override
    public String toString() {
        return "Servicio [idServicio=" + idServicio + ", fecha=" + fecha + ", descripcion=" + descripcion
                + ", piezasUtilizadas=" + piezasUtilizadas + ", costoManoObra=" + costoManoObra + ", observaciones="
                + observaciones + ", fechaHoraRegistro=" + fechaHoraRegistro + "]";
    }
}
