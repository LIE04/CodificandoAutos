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

@ExtendWith(MockitoExtension.class)
class ServicioDetallesFallaTest {

    @Mock
    private DetallesFallaRepository detallesFallaRepository;

    @InjectMocks
    private ServicioDetallesFalla servicioDetallesFalla;

    @Test
    void testAgregarDetallesFallaExito() {
        String descripcion = "Pantalla rota";
        String estatus = "En espera";

        when(detallesFallaRepository.save(any(DetallesFalla.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DetallesFalla resultado = servicioDetallesFalla.agregarDetallesFalla(descripcion, estatus);

        assertNotNull(resultado, "El objeto devuelto no debe ser nulo");
        assertEquals(descripcion, resultado.getDescripcionFalla(), "La descripción debe coincidir");
        assertEquals(estatus, resultado.getEstatus(), "El estatus debe coincidir");
        
        verify(detallesFallaRepository, times(1)).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaDescripcionNula() {
        String descripcion = null;
        String estatus = "En espera";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estatus);
        });

        assertEquals("La descripción de la falla no puede ser nula o vacía", exception.getMessage());

        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaDescripcionVacia() {
        String descripcion = "";
        String estatus = "En espera";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estatus);
        });

        assertEquals("La descripción de la falla no puede ser nula o vacía", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaEstatusNulo() {
        String descripcion = "Batería inflada";
        String estatus = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estatus);
        });

        assertEquals("El estatus de la falla no puede ser nulo o vacío", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }

    @Test
    void testAgregarDetallesFallaEstatusVacio() {
        String descripcion = "Batería inflada";
        String estatus = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDetallesFalla.agregarDetallesFalla(descripcion, estatus);
        });

        assertEquals("El estatus de la falla no puede ser nulo o vacío", exception.getMessage());
        verify(detallesFallaRepository, never()).save(any(DetallesFalla.class));
    }
}