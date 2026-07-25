package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetallesFalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetalleFalla;
    
    private String descripcionFalla;
    private String estatus; // En espera, Listo para entregar, Entregado

    @ManyToOne
    @JoinColumn(name = "id_reparacion")
    private Reparacion reparacion;
    
    // --- NUEVO: Relación directa con Vehiculo ---
    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    public DetallesFalla() {
    }

    // Getters y Setters existentes
    public long getIdDetalleFalla() {
        return idDetalleFalla;
    }

    public void setIdDetalleFalla(long idDetalleFalla) {
        this.idDetalleFalla = idDetalleFalla;
    }

    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        this.descripcionFalla = descripcionFalla;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}