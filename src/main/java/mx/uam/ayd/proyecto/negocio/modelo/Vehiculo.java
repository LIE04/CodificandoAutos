package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

	private String marca, modelo, placas;

	private int anio;

    private double kilometraje;

    // Cliente dueño del vehículo (un vehículo pertenece a un solo cliente)
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

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
     * @return la marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return el modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return las placas
     */
    public String getPlacas() {
        return placas;
    }

    /**
     * @param placas the placas to set
     */
    public void setPlacas(String placas) {
        this.placas = placas;
    }

    /**
     * @return el año del vehículo
     */
    public int getAnio() {
        return anio;
    }

    /**
     * @param anio the anio to set
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * @return el kilometraje
     */
    public double getKilometraje() {
        return kilometraje;
    }

    /**
     * @param kilometraje the kilometraje to set
     */
    public void setKilometraje(double kilometraje) {
        this.kilometraje = kilometraje;
    }

    /**
     * @return el cliente dueño del vehículo
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        return "Vehiculo [idVehiculo=" + idVehiculo + ", marca=" + marca + ", modelo=" + modelo
                + ", placas=" + placas + ", anio=" + anio + ", kilometraje=" + kilometraje
                + ", cliente=" + (cliente == null ? null : cliente.getNombre()) + "]";
    }
}
