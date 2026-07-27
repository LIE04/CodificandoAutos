package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import mx.uam.ayd.proyecto.datos.DistribuidorRepository;
import mx.uam.ayd.proyecto.datos.PedidoRepository;
import mx.uam.ayd.proyecto.datos.RefaccionRepository;
import mx.uam.ayd.proyecto.datos.ReparacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;
import mx.uam.ayd.proyecto.negocio.modelo.Pedido;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Reparacion;

/**
 * Tests para el ServicioPedido, enfocados en HU-30 (Seguimiento de Pedidos).
 * 
 * @author Erik LIE04
 */
@ExtendWith(MockitoExtension.class)

public class ServicioPedidoHU30Test {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RefaccionRepository refaccionRepository;

    @Mock
    private ReparacionRepository reparacionRepository;

    @Mock
    private DistribuidorRepository distribuidorRepository;

    @InjectMocks
    private ServicioPedido servicioPedido;

    private Distribuidor distribuidor;
    private Refaccion refaccion;
    private Reparacion reparacion;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        distribuidor = new Distribuidor();
        distribuidor.setIdDistribuidor(1);
        distribuidor.setNombre("AutoPartes S.A.");

        refaccion = new Refaccion();
        refaccion.setIdRefaccion(1);
        refaccion.setNombre("Filtro de Aceite");
        refaccion.setExistencia(10);

        reparacion = new Reparacion();
        reparacion.setIdReparacion(1);
        reparacion.setEstatusServicio("En proceso");

        pedido = new Pedido();
        pedido.setIdPedido(1);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstadoPedido("Pendiente");
        pedido.setDistribuidor(distribuidor);
        pedido.setRefaccion(refaccion);
        pedido.setCantidad(5);
        pedido.setReparacion(reparacion);
    }

    /**
     * Test para HU-30: Escenario 1 - Crear Pedido Exitoso para Reparación
     * Dado un distribuidor, una refacción, una cantidad y una reparación válidos
     * Cuando se crea un nuevo pedido
     * Entonces el pedido debe ser guardado con estado "Pendiente" y asociado correctamente.
     */
    @Test
    @DisplayName("HU-30: Escenario 1 - Crear Pedido Exitoso para Reparación")
    void testCrearPedido_ParaReparacion_Exito() {
        // Given
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // When
        Pedido pedidoCreado = servicioPedido.crearPedido(distribuidor, refaccion, 5, reparacion);

        // Then
        assertNotNull(pedidoCreado);
        assertEquals("Pendiente", pedidoCreado.getEstadoPedido());
        assertEquals(distribuidor, pedidoCreado.getDistribuidor());
        assertEquals(refaccion, pedidoCreado.getRefaccion());
        assertEquals(5, pedidoCreado.getCantidad());
        assertEquals(reparacion, pedidoCreado.getReparacion());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    /**
     * Test para HU-30: Escenario 2 - Crear Pedido Exitoso para Inventario (sin Reparación)
     * Dado un distribuidor, una refacción y una cantidad válidos, y sin reparación asociada
     * Cuando se crea un nuevo pedido
     * Entonces el pedido debe ser guardado con estado "Pendiente" y sin reparación asociada.
     */
    @Test
    @DisplayName("HU-30: Escenario 2 - Crear Pedido Exitoso para Inventario")
    void testCrearPedido_ParaInventario_Exito() {
        // Given
        Pedido pedidoParaInventario = new Pedido();
        pedidoParaInventario.setIdPedido(2);
        pedidoParaInventario.setFechaPedido(LocalDate.now());
        pedidoParaInventario.setEstadoPedido("Pendiente");
        pedidoParaInventario.setDistribuidor(distribuidor);
        pedidoParaInventario.setRefaccion(refaccion);
        pedidoParaInventario.setCantidad(5);
        pedidoParaInventario.setReparacion(null); // Aseguramos que la reparación es nula

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoParaInventario);

        // When
        Pedido pedidoCreado = servicioPedido.crearPedido(distribuidor, refaccion, 5, null);

        // Then
        assertNotNull(pedidoCreado);
        assertEquals("Pendiente", pedidoCreado.getEstadoPedido());
        assertNull(pedidoCreado.getReparacion());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    /**
     * Test para HU-30: Escenario 3 - Crear Pedido con Distribuidor Inválido
     * Dado un distribuidor nulo
     * Cuando se intenta crear un pedido
     * Entonces se debe lanzar una IllegalArgumentException.
     */
    @Test
    @DisplayName("HU-30: Escenario 3 - Crear Pedido con Distribuidor Inválido")
    void testCrearPedido_DistribuidorInvalido() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.crearPedido(null, refaccion, 5, reparacion);
        });
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    /**
     * Test para HU-30: Escenario 4 - Crear Pedido con Refacción Inválida
     * Dada una refacción nula
     * Cuando se intenta crear un pedido
     * Entonces se debe lanzar una IllegalArgumentException.
     */
    @Test
    @DisplayName("HU-30: Escenario 4 - Crear Pedido con Refacción Inválida")
    void testCrearPedido_RefaccionInvalida() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.crearPedido(distribuidor, null, 5, reparacion);
        });
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    /**
     * Test para HU-30: Escenario 5 - Crear Pedido con Cantidad Inválida
     * Dada una cantidad menor o igual a cero
     * Cuando se intenta crear un pedido
     * Entonces se debe lanzar una IllegalArgumentException.
     */
    @Test
    @DisplayName("HU-30: Escenario 5 - Crear Pedido con Cantidad Inválida")
    void testCrearPedido_CantidadInvalida() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.crearPedido(distribuidor, refaccion, 0, reparacion);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.crearPedido(distribuidor, refaccion, -1, reparacion);
        });
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    /**
     * Test para HU-30: Escenario 6 - Recuperar Todos los Pedidos
     * Dado que existen pedidos en la base de datos
     * Cuando se solicitan todos los pedidos
     * Entonces se debe retornar una lista con todos los pedidos.
     */
    @Test
    @DisplayName("HU-30: Escenario 6 - Recuperar Todos los Pedidos")
    void testRecuperarPedidos_Exito() {
        // Given
        List<Pedido> pedidosMock = Arrays.asList(pedido, new Pedido());
        when(pedidoRepository.findAll()).thenReturn(pedidosMock);

        // When
        List<Pedido> pedidosRecuperados = servicioPedido.recuperarPedidos();

        // Then
        assertNotNull(pedidosRecuperados);
        assertEquals(2, pedidosRecuperados.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    /**
     * Test para HU-30: Escenario 7 - Actualizar Estado de Pedido a "Entregado" (Incrementa Existencia)
     * Dado un pedido con estado "Pendiente" y una refacción con existencia X
     * Cuando el estado del pedido se actualiza a "Entregado"
     * Entonces el estado del pedido debe ser "Entregado"
     * Y la existencia de la refacción debe incrementarse en la cantidad del pedido.
     */
    @Test
    @DisplayName("HU-30: Escenario 7 - Actualizar Estado de Pedido a \"Entregado\" (Incrementa Existencia)")
    void testActualizarEstadoPedido_Entregado_IncrementaExistencia() {
        // Given
        int existenciaInicial = refaccion.getExistencia(); // 10
        int cantidadPedido = pedido.getCantidad(); // 5
        pedido.setEstadoPedido("Pendiente");

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(refaccionRepository.save(any(Refaccion.class))).thenReturn(refaccion);

        // When
        servicioPedido.actualizarEstadoPedido(pedido, "Entregado");

        // Then
        assertEquals("Entregado", pedido.getEstadoPedido());
        assertEquals(existenciaInicial + cantidadPedido, refaccion.getExistencia()); // 10 + 5 = 15
        verify(pedidoRepository, times(1)).save(pedido);
        verify(refaccionRepository, times(1)).save(refaccion);
    }

    /**
     * Test para HU-30: Escenario 8 - Actualizar Estado de Pedido de "Entregado" a "Cancelado" (Decrementa Existencia)
     * Dado un pedido con estado "Entregado" y una refacción con existencia X
     * Cuando el estado del pedido se actualiza a "Cancelado"
     * Entonces el estado del pedido debe ser "Cancelado"
     * Y la existencia de la refacción debe decrementarse en la cantidad del pedido.
     */
    @Test
    @DisplayName("HU-30: Escenario 8 - Actualizar Estado de Pedido de \"Entregado\" a \"Cancelado\" (Decrementa Existencia)")
    void testActualizarEstadoPedido_Cancelado_DecrementaExistencia() {
        // Given
        int existenciaInicial = refaccion.getExistencia(); // 10
        int cantidadPedido = pedido.getCantidad(); // 5
        pedido.setEstadoPedido("Entregado"); // Simular que ya estaba entregado
        refaccion.setExistencia(existenciaInicial + cantidadPedido); // Simular existencia después de la entrega

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(refaccionRepository.save(any(Refaccion.class))).thenReturn(refaccion);

        // When
        servicioPedido.actualizarEstadoPedido(pedido, "Cancelado");

        // Then
        assertEquals("Cancelado", pedido.getEstadoPedido());
        assertEquals(existenciaInicial, refaccion.getExistencia()); // Debería volver a la existencia inicial
        verify(pedidoRepository, times(1)).save(pedido);
        verify(refaccionRepository, times(1)).save(refaccion);
    }

    /**
     * Test para HU-30: Escenario 9 - Actualizar Estado de Pedido sin Cambio de Existencia
     * Dado un pedido con estado "Pendiente"
     * Cuando el estado del pedido se actualiza a "En espera" (no es "Entregado")
     * Entonces el estado del pedido debe ser "En espera"
     * Y la existencia de la refacción no debe cambiar.
     */
    @Test
    @DisplayName("HU-30: Escenario 9 - Actualizar Estado de Pedido sin Cambio de Existencia")
    void testActualizarEstadoPedido_SinCambioExistencia() {
        // Given
        int existenciaInicial = refaccion.getExistencia(); // 10
        pedido.setEstadoPedido("Pendiente");

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // When
        servicioPedido.actualizarEstadoPedido(pedido, "En espera");

        // Then
        assertEquals("En espera", pedido.getEstadoPedido());
        assertEquals(existenciaInicial, refaccion.getExistencia());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(refaccionRepository, never()).save(any(Refaccion.class)); // No debería guardar refacción
    }

    /**
     * Test para HU-30: Escenario 10 - Actualizar Estado de Pedido con Pedido Nulo
     * Dado un pedido nulo
     * Cuando se intenta actualizar su estado
     * Entonces se debe lanzar una IllegalArgumentException.
     */
    @Test
    @DisplayName("HU-30: Escenario 10 - Actualizar Estado de Pedido con Pedido Nulo")
    void testActualizarEstadoPedido_PedidoNulo() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.actualizarEstadoPedido(null, "Entregado");
        });
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(refaccionRepository, never()).save(any(Refaccion.class));
    }

    /**
     * Test para HU-30: Escenario 11 - Actualizar Estado de Pedido con Estado Nulo o Vacío
     * Dado un estado nulo o vacío
     * Cuando se intenta actualizar el estado de un pedido
     * Entonces se debe lanzar una IllegalArgumentException.
     */
    @Test
    @DisplayName("HU-30: Escenario 11 - Actualizar Estado de Pedido con Estado Nulo o Vacío")
    void testActualizarEstadoPedido_EstadoInvalido() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.actualizarEstadoPedido(pedido, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.actualizarEstadoPedido(pedido, "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPedido.actualizarEstadoPedido(pedido, "   ");
        });
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(refaccionRepository, never()).save(any(Refaccion.class));
    }
}
