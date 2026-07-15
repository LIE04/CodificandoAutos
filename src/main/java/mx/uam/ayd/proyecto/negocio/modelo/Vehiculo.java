package mx.uam.ayd.proyecto.negocio.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad de negocio Vehiculo
 * 
 * @author AngelYael
 *
 */
@Entity // Esto le dice a Spring que esta es una entidad persistente
public class Vehiculo {
	@Id // Esto le dice a Spring que este es el identificador
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Le dice a Spring que genere el id
	private long idVehiculo;

	private String Marca, Modelo,Placas;
	
	private int Año;

    private double Kilometraje;
	
	/**
	 * @return el idVehiculo
	 */
	public long getIdVehiculo() {
		return idVehiculo;
	}

	/**
	 * @param idVehiculo the idVehiculo to set
	 */
	public void setIdVehiculo(long idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

    /**
     * @return la Marca
     */
    public String getMarca() {
        return Marca;
    }

    /**
     * @param Marca the Marca to set
     */
    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    /**
     * @return el Modelo
     */
    public String getModelo() {
        return Modelo;
    }

    /**
     * @param Modelo the Modelo to set
     */
    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    /**
     * @return las Placas
     */
    public String getPlacas() {
        return Placas;
    }

    /**
     * @param Placas the Placas to set
     */
    public void setPlacas(String Placas) {
        this.Placas = Placas;
    }

    /**
     * @return el Año
     */
    public int getAño() {
        return Año;
    }

    /**
     * @param Año the Año to set
     */
    public void setAño(int Año) {
        this.Año = Año;
    }

    /**
     * @return el Kilometraje
     */
    public double getKilometraje() {
        return Kilometraje;
    }

    /**
     * @param Kilometraje the Kilometraje to set
     */
    public void setKilometraje(double Kilometraje) {
        this.Kilometraje = Kilometraje;
    }

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vehiculo other = (Vehiculo) obj;
        return idVehiculo == other.idVehiculo;
    }
    
    @Override
    public int hashCode() {
        return (int) (31 * idVehiculo);
    }
    
    @Override
    public String toString() {
        return "Vehiculo [idVehiculo=" + idVehiculo + ", Marca=" + Marca + ", Modelo=" + Modelo 
                + ", Placas=" + Placas + ", Año=" + Año + ", Kilometraje=" + Kilometraje + "]";
    }
}