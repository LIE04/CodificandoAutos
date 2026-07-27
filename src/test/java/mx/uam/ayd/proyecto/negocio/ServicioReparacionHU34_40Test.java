package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.ReparacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.HistorialNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

/**
 * Tests para el ServicioReparacion, enfocados en HU-34 (Notificación de Atraso) y HU-40 (Verificación de Escáner).
 * 
 * @author Erik LIE04
 */
@ExtendWith(MockitoExtension.class)
public class ServicioReparacionHU34_40Test {

    @Mock
    private ReparacionRepository reparacionRepository;

    @InjectMocks
    private ServicioReparacion servicioReparacion;

    @InjectMocks
    private ServicioNotificacion servicioNotificacion;

    private Reparacion reparacion;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
        vehiculo.setIdVehiculo(1);
        vehiculo.setPlacas("ABC-123");

        reparacion = new Reparacion();
        reparacion.setIdReparacion(1);
        reparacion.setEstatusServicio("En proceso");
        reparacion.setVehiculo(vehiculo);
        reparacion.setFechaInicio(LocalDateTime.now().minusDays(5));
        reparacion.setObservacionesTecnicas("Diagnóstico inicial: Falla en motor.");
        reparacion.setHistorialNotificaciones(new ArrayList<>());
    }

    /**
     * Test para HU-40: Escenario 1 - Escaneo Limpio Exitoso
     */
    @Test
    @DisplayName("HU-40: Escenario 1 - Escaneo Limpio Exitoso")
    void testProcesarEscaneoLimpio_Exito() {
        // Given
        when(reparacionRepository.findById(reparacion.getIdReparacion())).thenReturn(Optional.of(reparacion));
        when(reparacionRepository.save(any(Reparacion.class))).thenReturn(reparacion);

        // When
        Reparacion reparacionActualizada = servicioReparacion.procesarEscaneoLimpio(reparacion.getIdReparacion());

        // Then
        assertNotNull(reparacionActualizada);
        assertEquals("Listo para entrega", reparacionActualizada.getEstatusServicio());
        assertTrue(reparacionActualizada.getObservacionesTecnicas().contains("| [Control de Calidad: Escaneo Limpio Exitoso]"));
        verify(reparacionRepository, times(1)).save(reparacion);
    }

    /**
     * Test para HU-40: Escenario 2 y 3 - Fallas Persistentes Detectadas
     * Ajustado para el nuevo modelo de persistencia por String con etiquetas.
     */
    @Test
    @DisplayName("HU-40: Escenario 2 y 3 - Fallas Persistentes Detectadas")
    void testProcesarFallasPersistentes_Exito() {
        // Given
        String nuevasFallas = "P0301, P0420";
        when(reparacionRepository.findById(reparacion.getIdReparacion())).thenReturn(Optional.of(reparacion));
        when(reparacionRepository.save(any(Reparacion.class))).thenReturn(reparacion);

        // When
        Reparacion reparacionActualizada = servicioReparacion.procesarFallasPersistentes(reparacion.getIdReparacion(), nuevasFallas);

        // Then
        assertNotNull(reparacionActualizada);
        assertEquals("En espera", reparacionActualizada.getEstatusServicio());
        
        // AHORA VALIDAMOS EL NUEVO FORMATO DE STRING:
        String observaciones = reparacionActualizada.getObservacionesTecnicas();
        
        // 1. Debe conservar las observaciones originales
        assertTrue(observaciones.startsWith("Diagnóstico inicial: Falla en motor."), "Debe mantener las notas anteriores");
        
        // 2. Debe contener cada falla con su etiqueta respectiva para que la UI cree el CheckBox correcto
        assertTrue(observaciones.contains("P0301 [Falla detectada en Escáner]"), "Falta la etiqueta para la primera falla");
        assertTrue(observaciones.contains("P0420 [Falla detectada en Escáner]"), "Falta la etiqueta para la segunda falla");
        
        verify(reparacionRepository, times(1)).save(reparacion);
    }

    /**
     * Test para HU-34: Escenario 1 - Envío de Aviso de Retraso Exitoso
     */
    @Test
    @DisplayName("HU-34: Escenario 1 - Envío de Aviso de Retraso Exitoso")
    void testEnviarAvisoRetraso_Exito() {
        // Given
        String motivo = "Falta de refacciones en inventario";
        when(reparacionRepository.findById(reparacion.getIdReparacion())).thenReturn(Optional.of(reparacion));
        when(reparacionRepository.save(any(Reparacion.class))).thenReturn(reparacion);

        // When
        boolean resultado = servicioNotificacion.enviarAvisoRetraso(1, motivo);

        // Then
        assertTrue(resultado);
        assertFalse(reparacion.getHistorialNotificaciones().isEmpty());
        assertEquals(1, reparacion.getHistorialNotificaciones().size());
        assertEquals(motivo, reparacion.getHistorialNotificaciones().get(0).getMotivo());
        verify(reparacionRepository, times(1)).save(reparacion);
    }

    /**
     * Test para HU-34: Escenario 2 - Intento de Envío de Aviso de Retraso sin Motivo
     */
    @Test
    @DisplayName("HU-34: Escenario 2 - Intento de Envío de Aviso de Retraso sin Motivo")
    void testEnviarAvisoRetraso_MotivoVacio() {
        // Given
        String motivoVacio = "";
        String motivoNulo = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificacion.enviarAvisoRetraso(1, motivoVacio);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificacion.enviarAvisoRetraso(1, motivoNulo);
        });
        verify(reparacionRepository, never()).save(any(Reparacion.class));
    }

    /**
     * Test para HU-34: Escenario 3 - Intento de Envío de Aviso de Retraso a Reparación Inexistente
     */
    @Test
    @DisplayName("HU-34: Escenario 3 - Intento de Envío de Aviso de Retraso a Reparación Inexistente")
    void testEnviarAvisoRetraso_ReparacionInexistente() {
        // Given
        String motivo = "Falta de refacciones en inventario";
        when(reparacionRepository.findById(999)).thenReturn(Optional.empty());

        // When
        boolean resultado = servicioNotificacion.enviarAvisoRetraso(999, motivo);

        // Then
        assertFalse(resultado);
        verify(reparacionRepository, never()).save(any(Reparacion.class));
    }
}