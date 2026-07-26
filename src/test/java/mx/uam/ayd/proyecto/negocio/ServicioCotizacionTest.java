package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mx.uam.ayd.proyecto.datos.CotizacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

class ServicioCotizacionTest {

    @Mock
    private CotizacionRepository cotizacionRepository;

    @InjectMocks
    private ServicioCotizacion servicioCotizacion;

    private Cita citaPrueba;
    private Refaccion refaccionPrueba;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Preparamos objetos básicos para no enviar nulos
        citaPrueba = new Cita();
        
        refaccionPrueba = new Refaccion();
        refaccionPrueba.setPrecio(100.0f); 
    }

    // --- Pruebas para crearCotizacionBorrador ---

    @Test
    @DisplayName("Debería retornar false si se intenta crear un borrador con cita nula")
    void testCrearCotizacionBorradorCitaNula() {
        boolean resultado = servicioCotizacion.crearCotizacionBorrador(null);
        assertFalse(resultado, "No debe permitir crear un borrador sin una cita válida");
    }

    @Test
    @DisplayName("Debería crear el borrador en memoria exitosamente")
    void testCrearCotizacionBorradorExito() {
        boolean resultado = servicioCotizacion.crearCotizacionBorrador(citaPrueba);
        assertTrue(resultado, "Debería retornar true al crear el borrador exitosamente");
    }

    // --- Pruebas para agregarRefaccionACotizacionBorrador ---

    @Test
    @DisplayName("No debería agregar refacción si no hay un borrador activo")
    void testAgregarRefaccionSinBorrador() {
        // When: Intentamos agregar sin haber llamado a crearCotizacionBorrador() antes
        boolean resultado = servicioCotizacion.agregarRefaccionACotizacionBorrador(refaccionPrueba, 2);
        
        // Then
        assertFalse(resultado, "Debe retornar false porque this.cotizacion es nulo");
    }

    @Test
    @DisplayName("No debería agregar refacción con datos inválidos (Nulo o cantidad 0)")
    void testAgregarRefaccionDatosInvalidos() {
        // Given: Inicializamos el borrador
        servicioCotizacion.crearCotizacionBorrador(citaPrueba);
        
        // When/Then: Probamos los dos casos del IF de validación
        assertFalse(servicioCotizacion.agregarRefaccionACotizacionBorrador(null, 2), "No debe aceptar refacción nula");
        assertFalse(servicioCotizacion.agregarRefaccionACotizacionBorrador(refaccionPrueba, 0), "No debe aceptar cantidades menores o iguales a 0");
    }

    @Test
    @DisplayName("Debería agregar la refacción al concepto del borrador exitosamente")
    void testAgregarRefaccionExito() {
        // Given: Inicializamos el borrador
        servicioCotizacion.crearCotizacionBorrador(citaPrueba);
        
        // When: Agregamos una pieza válida
        boolean resultado = servicioCotizacion.agregarRefaccionACotizacionBorrador(refaccionPrueba, 2);
        
        // Then
        assertTrue(resultado, "Debería agregar el concepto y retornar true");
    }

    // --- Pruebas para capturarDatosServicio ---

    @Test
    @DisplayName("No debería capturar datos si no hay borrador activo")
    void testCapturarDatosSinBorrador() {
        boolean resultado = servicioCotizacion.capturarDatosServicio("Cambio balatas", 500.0f);
        assertFalse(resultado, "Debe retornar false porque this.cotizacion es nulo");
    }

    @Test
    @DisplayName("Debería capturar datos exitosamente y manejar costos negativos")
    void testCapturarDatosExitoYValidacion() {
        // Given: Inicializamos el borrador
        servicioCotizacion.crearCotizacionBorrador(citaPrueba);
        
        // When: Enviamos un costo negativo para forzar el ELSE de tu método
        boolean resultado = servicioCotizacion.capturarDatosServicio("Revisión general", -150.0f);
        
        // Then
        assertTrue(resultado, "Debería retornar true tras guardar los datos");
        // Nota: En una prueba más profunda con un 'Getter' para la cotización, aquí verificaríamos que el costo se guardó como 0.0f
    }

    // --- Pruebas para finalizarCotizacion ---

    @Test
    @DisplayName("No debería finalizar la cotización si no hay un borrador activo")
    void testFinalizarCotizacionSinBorrador() {
        boolean resultado = servicioCotizacion.finalizarCotizacion();
        assertFalse(resultado, "Debe retornar false si intenta finalizar sin haber creado el borrador");
    }

    @Test
    @DisplayName("Debería calcular totales correctamente, guardar y limpiar el borrador")
    void testFinalizarCotizacionExitoCalculosYGuardado() {
        // Given: Preparamos un escenario completo
        servicioCotizacion.crearCotizacionBorrador(citaPrueba);
        servicioCotizacion.agregarRefaccionACotizacionBorrador(refaccionPrueba, 2); // 2 piezas de $100 = $200
        servicioCotizacion.capturarDatosServicio("Cambio", 500.0f); // Mano de obra = $500
        
        // When: Finalizamos
        boolean resultado = servicioCotizacion.finalizarCotizacion();
        
        // Then: Verificamos que finalizó con éxito
        assertTrue(resultado, "Debe retornar true al finalizar exitosamente");
        
        // CAPTURADOR DE ARGUMENTOS (Para revisar las matemáticas antes de que se vuelva nulo)
        ArgumentCaptor<Cotizacion> captor = ArgumentCaptor.forClass(Cotizacion.class);
        verify(cotizacionRepository, times(1)).save(captor.capture());
        
        Cotizacion cotizacionGuardada = captor.getValue();
        
        // Verificamos las matemáticas: Subtotal (200 + 500 = 700). IVA (700 * 0.16 = 112). Total = 812.0
        assertEquals(812.0f, cotizacionGuardada.getCostoTotal(), 0.01, "El cálculo del IVA y total no es correcto");
        
        // Verificamos la limpieza en memoria: Si intentamos capturar datos ahora, debe dar false porque es nulo
        assertFalse(servicioCotizacion.capturarDatosServicio( "Prueba", 100f), "El borrador debió ser destruido (null)");
    }
}