package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa la reparacion de un vehiculo en el taller
 * 
 * @author Erik LIE04
 */
@Entity
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReparacion;

    /** Cuando el mecanico le empieza a meter mano al carro */
    private LocalDateTime fechaInicio;
    
    /** Cuando queda listo y reparado */
    private LocalDateTime fechaFin;
    
    /** Para saber si esta en proceso pausado o terminado */
    private String estatusServicio;
    
    /** Notas que deja el mecanico sobre lo que le hizo al auto */
    private String observacionesTecnicas;
    
    /** Cuantos meses de garantia tiene el cliente por este trabajo */
    private int garantiaMeses;
    
    /** Las letras chiquitas de lo que cubre la garantia */
    private String condicionesGarantia;

    /** 
     * Lista de fallas detectadas asociadas a esta reparacion.
     * mappedBy indica que la entidad DetallesFalla es la dueña de la relación.
     */
    @OneToMany(mappedBy = "reparacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallesFalla> fallas = new ArrayList<>();

    /** CONSTRUCTORES
     * Constructor vacio que nos pide Spring para armar el objeto
     */
    public Reparacion() {
    }

    /** GETTERS Y SETTERS
     * Obtiene el ID de la reparacion
     * @return idReparacion
     */
    public int getIdReparacion() {
        return idReparacion;
    }

    /**
     * Establece el ID de la reparacion
     * @param idReparacion el nuevo identificador
     */
    public void setIdReparacion(int idReparacion) {
        this.idReparacion = idReparacion;
    }

    /**
     * Obtiene la fecha en la que inicio el trabajo
     * @return fechaInicio
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Asigna la fecha de inicio
     * @param fechaInicio momento en que arranco
     */
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha en la que se termino todo
     * @return fechaFin
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    /**
     * Asigna la fecha de termino
     * @param fechaFin momento en que quedo listo
     */
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene en que estado va la reparacion
     * @return estatusServicio
     */
    public String getEstatusServicio() {
        return estatusServicio;
    }

    /**
     * Actualiza el estado de la reparacion
     * @param estatusServicio el nuevo estado
     */
    public void setEstatusServicio(String estatusServicio) {
        this.estatusServicio = estatusServicio;
    }

    /**
     * Obtiene las notas del mecanico
     * @return observacionesTecnicas
     */
    public String getObservacionesTecnicas() {
        return observacionesTecnicas;
    }

    /**
     * Guarda las notas del mecanico
     * @param observacionesTecnicas el texto con los detalles
     */
    public void setObservacionesTecnicas(String observacionesTecnicas) {
        this.observacionesTecnicas = observacionesTecnicas;
    }

    /**
     * Obtiene el tiempo de garantia
     * @return garantiaMeses
     */
    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    /**
     * Asigna los meses que dura la garantia
     * @param garantiaMeses cantidad de meses
     */
    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    /**
     * Obtiene las reglas de la garantia
     * @return condicionesGarantia
     */
    public String getCondicionesGarantia() {
        return condicionesGarantia;
    }

    /**
     * Asigna las reglas para que aplique la garantia
     * @param condicionesGarantia texto explicativo
     */
    public void setCondicionesGarantia(String condicionesGarantia) {
        this.condicionesGarantia = condicionesGarantia;
    }

    /**
     * Obtiene la lista de fallas de la reparacion
     * @return fallas
     */
    public List<DetallesFalla> getFallas() {
        return fallas;
    }

    /**
     * Asigna la lista de fallas a la reparacion
     * @param fallas lista de DetallesFalla
     */
    public void setFallas(List<DetallesFalla> fallas) {
        this.fallas = fallas;
    }

    /**
     * Metodo de ayuda para agregar una falla individual y mantener
     * la consistencia bidireccional de los objetos en memoria.
     * @param falla el detalle de la falla a agregar
     */
    public void addFalla(DetallesFalla falla) {
        this.fallas.add(falla);
        falla.setReparacion(this);
    }
}