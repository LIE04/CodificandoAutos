package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad de negocio Cliente
 * 
 * @author AngelYael
 *
 */
@Entity // Esto le dice a Spring que esta es una entidad persistente
public class Cliente {
	@Id // Esto le dice a Spring que este es el identificador
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Le dice a Spring que genere el id
	private long idCliente;

	private String nombre;
	
	private int numero;
	
	/**
	 * @return el idCliente
	 */
	public long getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return el nombre nombre del cliente
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return el numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return idCliente == other.idCliente;
	}
	
	@Override
	public int hashCode() {
		return (int) (31 * idCliente);
	}
	
	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", numero=" + numero + "]";
	}
}
