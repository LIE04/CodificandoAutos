package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        Cita resultado = servicioCita.agendarCita(fechaValida, horaValida, clientePrueba);

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
            servicioCita.agendarCita(null, horaValida, clientePrueba);
        }, "Debe lanzar excepción si la fecha es nula");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiHoraEsNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, null, clientePrueba);
        }, "Debe lanzar excepción si la hora es nula");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiClienteEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, null);
        }, "Debe lanzar excepción si el cliente es nulo");
    }

    @Test
    void agendarCita_LanzaExcepcion_SiFechaEsPasada() {
        LocalDate fechaPasada = LocalDate.now().minusDays(1); // Ayer

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaPasada, horaValida, clientePrueba);
        }, "Debe lanzar excepción si la fecha es en el pasado");

        verifyNoInteractions(citaRepository);
    }

    @Test
    void agendarCita_LanzaExcepcion_SiCitaYaExisteEnFechaYHora() {
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, clientePrueba);
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
        
        when(citaRepository.findByVehiculoAndEstado(vehiculoPrueba, "PENDIENTE")).thenReturn(citaActivaSimulada);

        Cita resultado = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoPrueba);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(citaRepository, times(1)).findByVehiculoAndEstado(vehiculoPrueba, "PENDIENTE");
    }
}