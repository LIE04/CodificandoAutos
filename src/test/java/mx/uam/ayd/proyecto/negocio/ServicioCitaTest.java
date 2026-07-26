package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@ExtendWith(MockitoExtension.class)
class ServicioCitaTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private ServicioCita servicioCita;

    @Mock
    private ServicioCliente servicioCliente;

    @Mock
    private ServicioVehiculo servicioVehiculo;

    private Cliente clientePrueba;
    private Vehiculo vehiculoPrueba;
    private LocalDate fechaValida;
    private LocalTime horaValida;

    @BeforeEach
    void setUp() {
        clientePrueba = new Cliente();
        vehiculoPrueba = new Vehiculo();
        
        // Usamos fechas futuras para evitar que la validación de fecha pasada falle
        fechaValida = LocalDate.now().plusDays(1); // Mañana
        horaValida = LocalTime.of(14, 30); // 14:30 hrs
    }


    @Test
    void agendarCita_Exito() {
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(false);
        
        Cita citaSimulada = new Cita();
        citaSimulada.setFecha(fechaValida);
        citaSimulada.setHora(horaValida);
        citaSimulada.setCliente(clientePrueba);

        when(citaRepository.save((Cita) any())).thenReturn(citaSimulada);

        Cita resultado = servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, vehiculoPrueba);

        assertNotNull(resultado, "La cita no debe ser nula");
        assertEquals(fechaValida, resultado.getFecha());
        assertEquals(horaValida, resultado.getHora());
        assertEquals(clientePrueba, resultado.getCliente());

        ArgumentCaptor<Cita> captor = ArgumentCaptor.forClass(Cita.class);
        verify(citaRepository, times(1)).save((Cita) captor.capture());
        
        Cita citaGuardada = captor.getValue();
        assertEquals(fechaValida, citaGuardada.getFecha());
    }

    @Test
    void agendarCita_LanzaExcepcion_SiFechaEsNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(null, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la fecha es nula");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiHoraEsNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, null, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la hora es nula");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiClienteEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, null, vehiculoPrueba);
        }, "Debe lanzar excepción si el cliente es nulo");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiFechaEsPasada() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1); // Ayer

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaPasada, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la fecha es en el pasado");

        verifyNoInteractions(citaRepository);
    }

    @Test
    void agendarCita_LanzaExcepcion_SiCitaYaExisteEnFechaYHora() {
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si ya hay una cita en ese horario");

        // Verificamos que se llamó a la comprobación, y que NO se llamó a nada más (como el save)
        verify(citaRepository, times(1)).existsByFechaAndHora(fechaValida, horaValida);
        verifyNoMoreInteractions(citaRepository);
    }

    @Test
    void consultarCitasPorNombreCliente_Exito() {
        String nombreBuscado = "  Juan Perez  "; 
        List<Cita> listaSimulada = Arrays.asList(new Cita(), new Cita());
        
        when(citaRepository.findByClienteNombre("Juan Perez")).thenReturn(listaSimulada);

        List<Cita> resultado = servicioCita.consultarCitasPorNombreCliente(nombreBuscado);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepository, times(1)).findByClienteNombre("Juan Perez"); 
    }

    @Test
    void consultarCitasPorNombreCliente_LanzaExcepcion_SiNombreEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.consultarCitasPorNombreCliente(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.consultarCitasPorNombreCliente("   ");
        });
        
        // Si el nombre es inválido, no debe llamar a la base de datos
        verifyNoInteractions(citaRepository);
    }

    @Test
    void obtenerCitaPendientePorVehiculo_Exito() {
        Cita citaActivaSimulada = new Cita();
        citaActivaSimulada.setEstado("PENDIENTE");
        
        when(citaRepository.findByVehiculo(vehiculoPrueba)).thenReturn(citaActivaSimulada);

        Cita resultado = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoPrueba);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(citaRepository, times(1)).findByVehiculo(vehiculoPrueba);
    }
    @Test
    void agendarCitaCompleta_Exito() {
        // 1. Preparar el escenario (Mocks)
        when(servicioCliente.agregaCliente("Ana", "5551234567")).thenReturn(clientePrueba);
        when(servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2020, 15000.0, clientePrueba))
        .thenReturn(vehiculoPrueba);
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(false);
    
        Cita citaGuardada = new Cita();
        citaGuardada.setFecha(fechaValida);
        citaGuardada.setHora(horaValida);
        when(citaRepository.save(any(Cita.class))).thenReturn(citaGuardada);

        // 2. Ejecutar
        Cita resultado = servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                                      2020, "ABC-123", 15000.0, fechaValida, horaValida);

        // 3. Validar
        assertNotNull(resultado);
        verify(servicioCliente, times(1)).agregaCliente("Ana", "5551234567");
        verify(servicioVehiculo, times(1)).agregaVehiculo(eq("Toyota"), eq("Corolla"), eq("ABC-123"), 
                                                      eq(2020), eq(15000.0), eq(clientePrueba));
    }
    // --- NUEVAS PRUEBAS PARA agendarCita ---

    @Test
    void agendarCita_LanzaExcepcion_SiVehiculoEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, null);
        }, "Debe lanzar excepción si el vehículo es nulo");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiEsHoyYHoraPasada() {
        LocalDate hoy = LocalDate.now();
        // Le restamos unos minutos a la hora actual para simular que ya pasó
        LocalTime horaPasada = LocalTime.now().minusMinutes(5); 

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(hoy, horaPasada, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es para hoy pero en una hora que ya pasó");
        
        verifyNoInteractions(citaRepository);
    }

    // --- NUEVAS PRUEBAS PARA obtenerCitaPendientePorVehiculo ---

    @Test
    void obtenerCitaPendientePorVehiculo_LanzaExcepcion_SiVehiculoEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.obtenerCitaPendientePorVehiculo(null);
        }, "Debe lanzar excepción si el vehículo es nulo");
        
        verifyNoInteractions(citaRepository);
    }

    @Test
    void obtenerCitaPendientePorVehiculo_RetornaNull_SiNoHayCitasPendientes() {
        // Simulamos el escenario donde el repositorio busca pero no encuentra nada y devuelve null
        when(citaRepository.findByVehiculo(vehiculoPrueba)).thenReturn(null);

        Cita resultado = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoPrueba);

        assertNull(resultado, "Debe retornar null si no encuentra ninguna cita pendiente");
        verify(citaRepository, times(1)).findByVehiculo(vehiculoPrueba);
    }
    @Test
    void agendarCita_LanzaExcepcion_SiHoraEsAntesDeApertura() {
        LocalTime horaTemprano = LocalTime.of(8, 30); // 8:30 AM
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaTemprano, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es antes de las 9:00 hrs");
        
        verifyNoInteractions(citaRepository);
    }

    @Test
    void agendarCita_LanzaExcepcion_SiHoraEsDespuesDeCierre() {
        LocalTime horaTarde = LocalTime.of(18, 30); // 18:30 hrs
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaTarde, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es después de las 18:00 hrs");
        
        verifyNoInteractions(citaRepository);
    }
    @Test
    void agendarCitaCompleta_LanzaExcepcion_SiFallaAlCrearCliente() {
        // Simulamos que el ServicioCliente falla (ej. teléfono inválido o cliente duplicado)
        when(servicioCliente.agregaCliente(anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("Error al crear cliente"));

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                              2020, "ABC-123", 15000.0, fechaValida, horaValida);
        }, "Debe abortar si el cliente falla");

        //Si falló el cliente, NO debe intentar crear el vehículo ni la cita
        verifyNoInteractions(servicioVehiculo);
        verifyNoInteractions(citaRepository);
    }

    @Test
    void agendarCitaCompleta_LanzaExcepcion_SiFallaAlCrearVehiculo() {
        // Simulamos que el cliente sí se crea bien...
        when(servicioCliente.agregaCliente(anyString(), anyString())).thenReturn(clientePrueba);
        
        //pero el vehículo falla (ej. placas duplicadas)
        when(servicioVehiculo.agregaVehiculo(anyString(), anyString(), anyString(), anyInt(), anyDouble(), any(Cliente.class)))
            .thenThrow(new IllegalArgumentException("Error al crear vehículo"));

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                              2020, "ABC-123", 15000.0, fechaValida, horaValida);
        }, "Debe abortar si el vehículo falla");

        //El cliente sí se intentó crear, pero la cita NO debe guardarse
        verify(servicioCliente, times(1)).agregaCliente(anyString(), anyString());
        verifyNoInteractions(citaRepository);
    }
}