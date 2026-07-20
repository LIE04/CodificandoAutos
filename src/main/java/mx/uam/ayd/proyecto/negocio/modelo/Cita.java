package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate; // Para manejar solo la fecha (año-mes-día)
import java.time.LocalTime; // Para manejar solo la hora (hora:minutos)

/**
 * Entidad de negocio Cita
 */
@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCita;

    private LocalDate fecha;
    private LocalTime hora;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallesFalla> detallesFalla = new ArrayList<>();
    
    public Cita() {
    }

    public long getIdCita() {
        return idCita;
    }

    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<DetallesFalla> getDetallesFalla() {
        return detallesFalla;
    }

    //uso de una logica diferente para equals
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
    //Genera un numero entero para la clase Cita, basado en el idCita, para ser usado en estructuras de datos como HashMap o HashSet
    @Override
    public int hashCode() {
        return (int) (31 * idCita);
    }
//Imprime los valores de cada variable de la clase Cita, para poder ser usado en la consola o en un log
    @Override
    public String toString() {
        return "Cita [idCita=" + idCita + ", fecha=" + fecha + ", hora=" + hora + ", cliente=" + cliente + "]";
    }
}