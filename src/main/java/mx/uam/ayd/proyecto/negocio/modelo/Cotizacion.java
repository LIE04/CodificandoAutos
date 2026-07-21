package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


/*
Entidad de negocio que representa una Cotizacion
*/
@Entity
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCotizacion;

    private String descripcionFallas;
    private String manoObra;
    private float manoObraCosto;
    private float refaccionesCosto;
    private float costoTotal;
    private String estadoAprobacion;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita")
    private Cita cita;

    // 2. Relación con Reparación 
    @OneToOne(mappedBy = "cotizacion", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Reparacion reparacion;

  
    @OneToMany(mappedBy = "cotizacion", fetch = FetchType.LAZY)
    private final List<Refaccion> refacciones = new ArrayList<>();

    public Cotizacion(){  
    }
    
 
	public long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}


    public String getDescripcionFallas() {
		return descripcionFallas;
	}

	public void setDescripcionFallas(String descripcionFallas) {
		this.descripcionFallas = descripcionFallas;
	}


	public String getManoObra() {
		return manoObra;
	}

	public void setManoObra(String manoObra) {
		this.manoObra = manoObra;
	}


	public float getManoObraCosto() {
		return manoObraCosto;
	}

	public void setManoObraCosto(float manoObraCosto) {
		this.manoObraCosto = manoObraCosto;
	}


	public float getRefaccionesCosto() {
		return refaccionesCosto;
	}

	public void setRefaccionesCosto(float refaccionesCosto) {
		this.refaccionesCosto = refaccionesCosto;
	}


    public float getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(float costoTotal) {
		this.costoTotal = costoTotal;
	}


    public String getEstadoAprobacion() {
		return estadoAprobacion;
	}

	public void setEstadoAprobacion(String estadoAprobacion) {
		this.estadoAprobacion = estadoAprobacion;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cotizacion other = (Cotizacion) obj;
		return idCotizacion == other.idCotizacion;
	}
	
	@Override
	public int hashCode() {
		return (int) (31 * idCotizacion);
	}
	
	@Override
	public String toString() {
		return "Cotizacion [idCotizacion=" + idCotizacion + ", descripcion fallas=" + descripcionFallas + 
        ", costo de mano de obra=" + manoObraCosto + ", costo de refacciones=" + refaccionesCosto + 
        ", costo total=" + costoTotal + ", estado de aprobacion=" + estadoAprobacion + "]";
	}

}
