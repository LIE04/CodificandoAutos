package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * Entidad que representa la bitácora de notificaciones de atraso enviadas al cliente.
 * 
 * @author Erik
 */
@Entity
public class HistorialNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistorial;

    /** Fecha y hora exacta en la que el sistema procesó el aviso */
    private LocalDateTime fechaHora;

    /** El motivo seleccionado en el menú desplegable */
    private String motivo;

    /**
     * Relación con la reparación que sufrió el retraso.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reparacion")
    private Reparacion reparacion;

    /** Constructor vacío requerido por Spring JPA */
    public HistorialNotificacion() {
    }

    public HistorialNotificacion(LocalDateTime fechaHora, String motivo) {
        this.fechaHora = fechaHora;
        this.motivo = motivo;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Reparacion getReparacion() {
        return reparacion;
    }

    public void setReparacion(Reparacion reparacion) {
        this.reparacion = reparacion;
    }
} {
    
}
