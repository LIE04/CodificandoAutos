package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad de negocio Cita
 * @author Angel Yael
 */
@Entity
public class Cita {

    /** 
     * Identificador único de la cita. 
     * Autogenerado por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCita;

    /** Fecha de la cita en formato Año-Mes-Día */
    private LocalDate fecha;
    
    /** Hora de la cita en formato Hora:Minutos */
    private LocalTime hora;
    
    /** Estado actual de la cita (ej. Pendiente, Confirmada, Cancelada) */
    private String estado;

    /** 
     * Cliente asociado a la cita
     * Relación muchos a uno
     */
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    /**
     * Lista de cotizaciones generadas en esta cita
     * Relación uno a muchos, gestionada en cascada
     */
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cotizacion> cotizaciones = new ArrayList<>();

    /** 
     * Vehículo a ser atendido en la cita
     * Relación muchos a uno
     */
    @ManyToOne(targetEntity = Vehiculo.class)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
    
    /**
     * Constructor por defecto (Requerido por JPA)
     */
    public Cita() {
    }

    /**
     * Obtiene el identificador de la cita
     * 
     * @return El ID de la cita
     */
    public long getIdCita() {
        return idCita;
    }

    /**
     * Asigna un identificador a la cita
     * 
     * @param idCita El nuevo ID de la cita
     */
    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    /**
     * Obtiene la fecha de la cita
     * 
     * @return La fecha de la cita
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha de la cita
     * 
     * @param fecha La nueva fecha para la cita
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la hora de la cita
     * 
     * @return La hora de la cita
     */
    public LocalTime getHora() {
        return hora;
    }

    /**
     * Asigna la hora de la cita
     * 
     * @param hora La nueva hora para la cita
     */
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    /**
     * Obtiene el estado actual de la cita
     * 
     * @return El estado de la cita
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna un estado a la cita
     * 
     * @param estado El nuevo estado de la cita
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el cliente asociado a la cita
     * 
     * @return El objeto Cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Asigna un cliente a la cita
     * 
     * @param cliente El cliente a asociar
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el vehículo asociado a la cita
     * 
     * @return El objeto Vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * Asigna un vehículo a la cita
     * 
     * @param vehiculo El vehículo a asociar
     */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * Compara esta cita con otro objeto para determinar si son iguales
     * La igualdad se basa exclusivamente en el identificador (idCita)
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
        Cita other = (Cita) obj;
        return idCita == other.idCita;
    }

    /**
     * Genera un código hash para la entidad Cita
     * Utilizado para estructuras de datos que requieren hash (como HashMap o HashSet)
     * 
     * @return El código hash generado basado en el idCita
     */
    @Override
    public int hashCode() {
        return (int) (31 * idCita);
    }

    /**
     * Devuelve una representación en formato de texto de la cita
     * Útil para depuración y registros
     * 
     * @return Cadena de texto con los detalles principales de la cita
     */
    @Override
    public String toString() {
        return "Cita [idCita=" + idCita + ", fecha=" + fecha + ", hora=" + hora + ", cliente=" + cliente + "]";
    }
}