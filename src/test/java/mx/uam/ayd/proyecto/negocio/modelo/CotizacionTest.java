package mx.uam.ayd.proyecto.negocio.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class CotizacionTest {

    private Cotizacion cotizacionPrueba;

    @BeforeEach
    void setUp() {
        // Inicializamos una nueva instancia antes de cada prueba
        cotizacionPrueba = new Cotizacion();
    }

    @Test
    @DisplayName("Debería inicializar con estado 'Pendiente' y lista de conceptos lista")
    void testConstructorInicializacion() {
        // Then: Verificamos los valores que asigna tu constructor por defecto
        assertEquals("Pendiente", cotizacionPrueba.getEstadoAprobacion(), "El estado inicial debe ser 'Pendiente'");
        assertNotNull(cotizacionPrueba.getConceptos(), "La lista de conceptos no debe ser nula");
        assertTrue(cotizacionPrueba.getConceptos().isEmpty(), "La lista de conceptos debe estar vacía al iniciar");
    }

    @Test
    @DisplayName("Debería asignar y recuperar los valores básicos correctamente")
    void testSetAndGetValoresBasicos() {
        // When: Asignamos valores
        cotizacionPrueba.setIdCotizacion(5L);
        cotizacionPrueba.setDescripcionFallas("Falla en el motor");
        cotizacionPrueba.setManoObra("Revisión general");
        cotizacionPrueba.setManoObraCosto(500.50f);
        cotizacionPrueba.setRefaccionesCosto(1200.0f);
        cotizacionPrueba.setCostoTotal(1700.50f);

        // Then: Validamos que los getters devuelvan lo correcto
        assertEquals(5L, cotizacionPrueba.getIdCotizacion(), "El ID debería ser 5");
        assertEquals("Falla en el motor", cotizacionPrueba.getDescripcionFallas(), "La descripción de falla debería coincidir");
        assertEquals("Revisión general", cotizacionPrueba.getManoObra(), "La mano de obra debería coincidir");
        assertEquals(500.50f, cotizacionPrueba.getManoObraCosto(), "El costo de mano de obra debería coincidir");
        assertEquals(1200.0f, cotizacionPrueba.getRefaccionesCosto(), "El costo de refacciones debería coincidir");
        assertEquals(1700.50f, cotizacionPrueba.getCostoTotal(), "El costo total debería coincidir");
    }

    @Test
    @DisplayName("Debería validar correctamente equals y hashCode basado en el ID")
    void testEqualsAndHashCode() {
        // Given: Creamos una segunda cotización para comparar
        Cotizacion otraCotizacion = new Cotizacion();
        
        // When: Les asignamos el mismo ID a ambas
        cotizacionPrueba.setIdCotizacion(10L);
        otraCotizacion.setIdCotizacion(10L);
        
        // Then: Como tu método equals() se basa en el ID, deben ser consideradas iguales
        assertTrue(cotizacionPrueba.equals(otraCotizacion), "Deben ser iguales si tienen el mismo ID");
        assertEquals(cotizacionPrueba.hashCode(), otraCotizacion.hashCode(), "El hashCode debe ser igual para el mismo ID");
        
        // When: Cambiamos el ID de la segunda cotización
        otraCotizacion.setIdCotizacion(15L);
        
        // Then: Ya no deben ser consideradas iguales
        assertFalse(cotizacionPrueba.equals(otraCotizacion), "No deben ser iguales si tienen distinto ID");
    }

    @Test
    @DisplayName("Debería retornar la cadena con el formato correcto en toString")
    void testToString() {
        // Given: Configuramos los datos (recuerda que el estado ya es "Pendiente")
        cotizacionPrueba.setIdCotizacion(1L);
        cotizacionPrueba.setDescripcionFallas("Frenos gastados");
        cotizacionPrueba.setManoObraCosto(200.0f);
        cotizacionPrueba.setRefaccionesCosto(300.0f);
        cotizacionPrueba.setCostoTotal(500.0f);

        // When: Ejecutamos el método
        String resultado = cotizacionPrueba.toString();
        String esperado = "Cotizacion [idCotizacion=1, descripcion fallas=Frenos gastados, costo de mano de obra=200.0, costo de refacciones=300.0, costo total=500.0, estado de aprobacion=Pendiente]";

        // Then: Comprobamos el formato exacto
        assertEquals(esperado, resultado, "El formato del toString no es el esperado");
    }
    
    @Test
    @DisplayName("Debería asignar y recuperar una Cita asociada")
    void testSetAndGetCita() {
        // Given: Una cita de prueba
        Cita citaPrueba = new Cita();
        
        // When
        cotizacionPrueba.setCita(citaPrueba);
        
        // Then
        assertEquals(citaPrueba, cotizacionPrueba.getCita(), "La cita asociada debería coincidir con la asignada");
    }
}
