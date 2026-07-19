package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad de negocio Distribuidor (HU-25: Consultar números de distribuidores)
 *
 * Representa a un proveedor externo de refacciones al que el mecánico puede
 * contactar para pedir piezas que faltan en el taller.
 */
@Entity
public class Distribuidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDistribuidor;

    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;

    // Categoría de refacciones que provee este distribuidor (ej. "frenos", "motor"),
    // usada para poder buscarlo cuando no se recuerda su nombre exacto
    private String tipoRefaccion;

    public Distribuidor() {
    }

    public long getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(long idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoRefaccion() {
        return tipoRefaccion;
    }

    public void setTipoRefaccion(String tipoRefaccion) {
        this.tipoRefaccion = tipoRefaccion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Distribuidor other = (Distribuidor) obj;
        return idDistribuidor == other.idDistribuidor;
    }

    @Override
    public int hashCode() {
        return (int) (31 * idDistribuidor);
    }

    @Override
    public String toString() {
        return "Distribuidor [idDistribuidor=" + idDistribuidor + ", nombre=" + nombre + ", telefono=" + telefono
                + ", correo=" + correo + ", direccion=" + direccion + ", tipoRefaccion=" + tipoRefaccion + "]";
    }
}
