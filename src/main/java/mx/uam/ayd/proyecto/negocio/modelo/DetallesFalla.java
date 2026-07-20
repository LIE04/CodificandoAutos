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
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    public DetallesFalla() {
    }

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
    public Cita getCita() {
        return cita;
    }
    public void setCita(Cita cita) {
        this.cita = cita;
    }
    public String getEstatus() {
        return estatus;
    }
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}