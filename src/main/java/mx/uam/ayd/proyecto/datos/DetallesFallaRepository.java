package mx.uam.ayd.proyecto.datos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;
public interface DetallesFallaRepository extends CrudRepository<DetallesFalla, Long> {

    public List<DetallesFalla> findByReparacion(Reparacion reparacion);
    public List<DetallesFalla> findByEstatus(String estatus);
    List<DetallesFalla> findByVehiculo(Vehiculo vehiculo);
}