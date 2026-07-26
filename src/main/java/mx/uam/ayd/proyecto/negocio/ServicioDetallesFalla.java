package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.DetallesFallaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@Service
public class ServicioDetallesFalla {

    @Autowired
    private DetallesFallaRepository detallesFallaRepository;

    public DetallesFalla agregarDetallesFalla(String descripcionFalla, Vehiculo vehiculo) {
        
        // Validaciones de seguridad
        if (descripcionFalla == null || descripcionFalla.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la falla no puede estar vacía.");
        }
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }

        // Creación y guardado
        DetallesFalla detallesFalla = new DetallesFalla();
        detallesFalla.setDescripcionFalla(descripcionFalla);
        detallesFalla.setVehiculo(vehiculo); // Aquí ocurre el enlace
        detallesFalla.setEstatus("En espera");

        return detallesFallaRepository.save(detallesFalla);
    }
    public List<DetallesFalla> recuperarFallasPorVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }
        return detallesFallaRepository.findByVehiculo(vehiculo);
    }
}