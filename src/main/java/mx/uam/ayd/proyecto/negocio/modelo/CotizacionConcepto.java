package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;

@Entity
public class CotizacionConcepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idConcepto;

    // Relación hacia la Refaccion (¡Justo lo que mencionaste!)
    @ManyToOne
    @JoinColumn(name = "id_refaccion")
    private Refaccion refaccion;

    // Relación hacia la Cotizacion padre
    @ManyToOne
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion; 

    private int cantidad;
    private float subtotal;

    public CotizacionConcepto() {}

    public CotizacionConcepto(Refaccion refaccion, int cantidad, Cotizacion cotizacion) {
        this.refaccion = refaccion;
        this.cantidad = cantidad;
        this.cotizacion = cotizacion;
        this.subtotal = refaccion.getPrecio() * cantidad;
    }

    // Getters y Setters...
    public Refaccion getRefaccion() { return refaccion; }
    public Cotizacion getCotizacion() { return cotizacion; }
    public int getCantidad() { return cantidad; }
    public float getSubtotal() { return subtotal; }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.refaccion.getPrecio() * cantidad;
    }

    // Este método sirve como puente para que JavaFX lea el nombre directamente
    public String getNombreRefaccion() {
        if (this.refaccion != null) {
            return this.refaccion.getNombre();
        }
        return "";
    }
}
