package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
//@author AngelYael

@Entity
public class Cliente {

    /**
	 * Generar un id único para cada cliente, el cual será autoincrementable
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCliente;

    private String nombre;

    private String telefono;
	
	/**
	 * Un cliente puede tener varios vehículos; el dueño de la relación es Vehiculo (FK cliente)
	 * la parte de FechtRype.LAZY indica que los vehiculos de un cliente no se cargaran hasta que se soliciten, 
	 * para no saturar el sistema
	 */
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private final List<Vehiculo> vehiculos = new ArrayList<>();

    /**
     * @return El ID del cliente
     */
    public long getIdCliente() {
        return idCliente;
    }

    /**
     * Asignacion de un nuevo ID al cliente
     * 
     * @param idCliente El ID que tendrá este cliente
     */
    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return Texto con el nombre actual.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre El nombre que le queremos poner al cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Texto con el número de teléfono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono El nuevo número de teléfono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return Una lista (List) con objetos de tipo Vehiculo.
     */
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    /**
     * @param obj El otro objeto con el que queremos comparar a nuestro cliente.
     * @return true si son el mismo cliente, false si son diferentes o si el otro es nulo.
     */
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

    /**
     * @return Un número entero único para identificar el objeto en memoria.
     */
    @Override
    public int hashCode() {
        return (int) (31 * idCliente);
    }

    /**
     * @return Una cadena de texto con los datos legibles del cliente.
     */
    @Override
    public String toString() {
        return "Cliente [idCliente=" + idCliente + ", nombre=" + nombre + ", telefono=" + telefono + "]";
    }
}