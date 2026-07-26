package mx.uam.ayd.proyecto.negocio.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.time.LocalDateTime;

class RefaccionTest {

    private Refaccion refaccionPrueba;

    @BeforeEach
    void setUp() {
        // Inicializamos una nueva instancia antes de cada prueba
        refaccionPrueba = new Refaccion();
    }

    @Test
    @DisplayName("Debería asignar y recuperar los valores básicos correctamente")
    void testSetAndGetValoresBasicos() {
        // When: Asignamos valores usando los setters
        refaccionPrueba.setIdRefaccion(1);
        refaccionPrueba.setNombre("Amortiguador");
        refaccionPrueba.setPrecio(1200.50f);
        refaccionPrueba.setExistencia(5);
        refaccionPrueba.setProveedor("AutoPartes MX");

        // Then: Verificamos que los getters devuelvan exactamente lo que asignamos
        assertEquals(1, refaccionPrueba.getIdRefaccion(), "El ID debería ser 1");
        assertEquals("Amortiguador", refaccionPrueba.getNombre(), "El nombre debería coincidir");
        assertEquals(1200.50f, refaccionPrueba.getPrecio(), "El precio debería coincidir");
        assertEquals(5, refaccionPrueba.getExistencia(), "La existencia debería coincidir");
        assertEquals("AutoPartes MX", refaccionPrueba.getProveedor(), "El proveedor debería coincidir");
    }

    @Test
    @DisplayName("Debería asignar y recuperar las fechas correctamente")
    void testSetAndGetFechas() {
        // Given: Preparamos fechas específicas
        LocalDate fechaRecepcion = LocalDate.of(2023, 10, 25);
        LocalDateTime fechaRegistro = LocalDateTime.of(2023, 10, 25, 14, 30);

        // When: Las asignamos a la entidad
        refaccionPrueba.setFechaRecepcion(fechaRecepcion);
        refaccionPrueba.setFechaHoraRegistro(fechaRegistro);

        // Then: Verificamos que se guarden correctamente
        assertEquals(fechaRecepcion, refaccionPrueba.getFechaRecepcion(), "La fecha de recepción debería coincidir");
        assertEquals(fechaRegistro, refaccionPrueba.getFechaHoraRegistro(), "La fecha y hora de registro debería coincidir");
    }

    @Test
    @DisplayName("Debería retornar la cadena con el formato correcto en toString")
    void testToString() {
        // Given: Configuramos los datos que usa el método toString()
        refaccionPrueba.setIdRefaccion(10);
        refaccionPrueba.setNombre("Filtro de Aceite");
        refaccionPrueba.setPrecio(250.0f);

        // When: Ejecutamos el método
        String resultado = refaccionPrueba.toString();

        // Then: Validamos que la concatenación sea exacta a la programada
        assertEquals("10 - Filtro de Aceite ($250.0)", resultado, "El formato del toString no es el esperado");
    }
}