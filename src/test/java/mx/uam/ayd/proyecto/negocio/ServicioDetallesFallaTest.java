package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.DetallesFallaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.DetallesFalla;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo; // NUEVO IMPORT

@ExtendWith(MockitoExtension.class)
class ServicioDetallesFallaTest {

    @Mock
    private DetallesFallaRepository detallesFallaRepository;

    @InjectMocks
    private ServicioDetallesFalla servicioDetallesFalla;

    @Test
    void testAgregarDetallesFallaExito() {
        String descripcion = "Falla en los frenos";
        String estatus = "En espera";
        Vehiculo vehiculoMock = new Vehiculo(); // Creamos un vehículo válido para pasar la prueba

        when(detallesFallaRepository.save(any(DetallesFalla.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Se agrega el vehículo como tercer parámetro
        DetallesFalla resultado = servicioDetallesFalla.agregarDetallesFalla(descripcion, vehiculoMock);

        assertNotNull(resultado, "El objeto devuelto no debe ser nulo");
        assertEquals(descripcion, resultado.getDescripcionFalla(), "La descripción debe coincidir");
        assertEquals(estatus, resultado.getEstatus(), "El estatus debe coincidir");
        assertEquals(vehiculoMock, resultado.getVehiculo(), "El vehículo asignado debe coincidir"); // Nueva comprobación
        
        verify(detallesFallaRepository, times(1)).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaDescripcionNula() {
        String descripcion = null;
        Vehiculo vehiculoMock = new Vehiculo();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, vehiculoMock);
        });

        assertEquals("La descripción de la falla no puede estar vacía.", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaDescripcionVacia() {
        String descripcion = "";
        Vehiculo vehiculoMock = new Vehiculo();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, vehiculoMock);
        });

        assertEquals("La descripción de la falla no puede estar vacía.", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }

    //Validar vehiculo nulo
    @Test
    void testAgregarDetallesFallaVehiculoNulo() {
        String descripcion = "Bujías desgastadas";
        Vehiculo vehiculoNulo = null; // Simulamos que se olvidó mandar el auto

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, vehiculoNulo);
        });

        assertEquals("El vehículo no puede ser nulo.", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }
}