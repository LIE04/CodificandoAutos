package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DetallesFalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetalleFalla;
    private String tipoFalla;
    private String estatus;

    public DetallesFalla() {
    }

    public long getIdDetalleFalla() {
        return idDetalleFalla;
    }

    public void setIdDetalleFalla(long idDetalleFalla) {
        this.idDetalleFalla = idDetalleFalla;
    }

    public String getTipoFalla() {
        return tipoFalla;
    }

    public void setTipoFalla(String tipoFalla) {
        this.tipoFalla = tipoFalla;
    }
    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}