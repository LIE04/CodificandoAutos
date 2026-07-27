package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio que representa un Vehículo
 *
 * @author AngelYael
 */
@Entity
public class Vehiculo {
    
    /** 
     * Identificador único del vehículo 
     * Autogenerado por la base de datos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVehiculo;

    /** Marca del vehículo */
    private String marca;
    
    /** Modelo del vehículo */
    private String modelo;
    
    /** Placas registradas del vehículo */
    private String placas;

    /** Año de fabricación del vehículo */
    private int anio;

    /** Kilometraje actual del vehículo */
    private double kilometraje;

    /** 
     * Cliente dueño del vehículo 
     * Relación muchos a uno
     */
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    /**
     * Obtiene el identificador del vehículo
     * 
     * @return El ID del vehículo
     */
    public long getIdVehiculo() {
        return idVehiculo;
    }

    /**
     * Asigna el identificador del vehículo
     * 
     * @param idVehiculo El nuevo ID para el vehículo
     */
    public void setIdVehiculo(long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    /**
     * Obtiene la marca del vehículo
     * 
     * @return La marca del vehículo
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Asigna la marca del vehículo
     * 
     * @param marca La nueva marca para el vehículo
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el modelo del vehículo
     * 
     * @return El modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Asigna el modelo del vehículo
     * 
     * @param modelo El nuevo modelo para el vehículo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene las placas del vehículo
     * 
     * @return Las placas del vehículo
     */
    public String getPlacas() {
        return placas;
    }

    /**
     * Asigna las placas del vehículo
     * 
     * @param placas Las nuevas placas para el vehículo
     */
    public void setPlacas(String placas) {
        this.placas = placas;
    }

    /**
     * Obtiene el año de fabricación del vehículo
     * 
     * @return El año del vehículo
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Asigna el año de fabricación del vehículo
     * 
     * @param anio El nuevo año para el vehículo
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Obtiene el kilometraje del vehículo
     * 
     * @return El kilometraje actual del vehículo
     */
    public double getKilometraje() {
        return kilometraje;
    }

    /**
     * Asigna el kilometraje del vehículo
     * 
     * @param kilometraje El nuevo kilometraje para el vehículo
     */
    public void setKilometraje(double kilometraje) {
        this.kilometraje = kilometraje;
    }

    /**
     * Obtiene el cliente dueño del vehículo
     * 
     * @return El objeto Cliente asociado
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Asigna un cliente como dueño del vehículo
     * 
     * @param cliente El cliente a asociar
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Compara este vehículo con otro objeto para determinar si son iguales
     * La igualdad se basa exclusivamente en el identificador (idVehiculo)
     * 
     * @param obj El objeto con el cual comparar
     * @return true si los objetos tienen el mismo ID, false en caso contrario
     */
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

    /**
     * Genera un código hash para la entidad Vehiculo
     * Utilizado para estructuras de datos que requieren hash (como HashMap o HashSet)
     * 
     * @return El código hash generado basado en el idVehiculo
     */
    @Override
    public int hashCode() {
        return (int) (31 * idVehiculo);
    }

    /**
     * Devuelve una representación en formato de texto del vehículo
     * Útil para depuración y registros (logs)
     * 
     * @return Cadena de texto con los detalles principales del vehículo
     */
    @Override
    public String toString() {
        return "Vehiculo [idVehiculo=" + idVehiculo + ", marca=" + marca + ", modelo=" + modelo
                + ", placas=" + placas + ", anio=" + anio + ", kilometraje=" + kilometraje
                + ", cliente=" + (cliente == null ? null : cliente.getNombre()) + "]";
    }
}