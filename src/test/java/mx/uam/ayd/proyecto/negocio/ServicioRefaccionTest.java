package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mx.uam.ayd.proyecto.datos.RefaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

class ServicioRefaccionTest {

    // 1. Zona de Mocks (Simulacros)
    @Mock
    private RefaccionRepository refaccionRepository;

    @InjectMocks
    private ServicioRefaccion servicioRefaccion;

    private Refaccion refaccionPrueba;

    @BeforeEach
    void setUp() {
        // Inicializamos los mocks de Mockito
        MockitoAnnotations.openMocks(this);
        
        // Preparamos nuestro objeto de prueba base
        refaccionPrueba = new Refaccion();
        
        refaccionPrueba.setNombre("Bujía");
        refaccionPrueba.setPrecio(150.0f);
        refaccionPrueba.setExistencia(10);
    }

    

    @Test
    @DisplayName("Debería recuperar la lista completa de refacciones (HU-12)")
    void testGetRefaccion() {
        // Given: Le decimos al Mock qué responder cuando el servicio llame a findAll()
        List<Refaccion> listaEsperada = Arrays.asList(refaccionPrueba);
        when(refaccionRepository.findAll()).thenReturn(listaEsperada);

        // When: Ejecutamos el método real
        List<Refaccion> resultado = servicioRefaccion.getRefaccion();

        // Then: Verificamos que el servicio nos devuelva lo que el Mock entregó
        assertFalse(resultado.isEmpty(), "La lista de refacciones no debería estar vacía");
        assertEquals(1, resultado.size(), "La lista debería contener un elemento");
        assertEquals("Bujía", resultado.get(0).getNombre(), "El nombre de la refacción debe coincidir");
    }

    // -- Pruebas para enviarDatos() --

    @Test
    @DisplayName("Debería actualizar y guardar datos cuando la refacción existe (HU-12)")
    void testEnviarDatosExito() {
        // Given: Simulamos que la pieza existe en la BD
        when(refaccionRepository.findById(1)).thenReturn(Optional.of(refaccionPrueba));
        
        // When: Intentamos editarla
        boolean resultado = servicioRefaccion.enviarDatos(1, "Bujía Iridium", 200.0f, 15);

        // Then: Verificamos que la respuesta fue true y que se llamó al método save()
        assertTrue(resultado, "Debería retornar true si la refacción fue editada exitosamente");
        verify(refaccionRepository, times(1)).save(refaccionPrueba); 
    }

    @Test
    @DisplayName("No debería actualizar ni guardar cuando la refacción no existe (HU-12)")
    void testEnviarDatosFallo() {
        // Given: Simulamos que el repositorio no encuentra la pieza (Optional vacío)
        when(refaccionRepository.findById(99)).thenReturn(Optional.empty());

        // When: Intentamos editar un ID inexistente
        boolean resultado = servicioRefaccion.enviarDatos(99, "Llanta", 800.0f, 2);

        // Then: Debe dar false y NO debe llamar al método save()
        assertFalse(resultado, "Debería retornar false si el ID no existe en el repositorio");
        verify(refaccionRepository, never()).save(any(Refaccion.class)); 
    }

    // -- Pruebas para buscarRefaccion() --

    @Test
    @DisplayName("Debería retornar lista vacía si el ID de la pieza es nulo (HU-14)")
    void testBuscarRefaccionIdNulo() {
        // When: Ejecutamos pasando un null directamente
        List<Refaccion> resultado = servicioRefaccion.buscarRefaccion(null);

        // Then: Validamos la regla del IF en tu código
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía si el ID enviado es nulo");
    }

    @Test
    @DisplayName("Debería encontrar la refacción y retornar una lista con ella (HU-14)")
    void testBuscarRefaccionExito() {
        // Given: Simulamos que el ID 1 sí existe
        when(refaccionRepository.findById(1)).thenReturn(Optional.of(refaccionPrueba));

        // When: Ejecutamos la búsqueda
        List<Refaccion> resultado = servicioRefaccion.buscarRefaccion(1);

        // Then: Comprobamos que la pieza se agregó a la lista
        assertFalse(resultado.isEmpty(), "La lista no debe estar vacía si se encontró la pieza");
        assertEquals(1, resultado.size(), "La lista debe tener exactamente 1 elemento");
    }
}