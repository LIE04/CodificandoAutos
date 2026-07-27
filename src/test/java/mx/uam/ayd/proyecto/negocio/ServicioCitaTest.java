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
import org.junit.jupiter.api.DisplayName;
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

    //Prueba para agendar cita

    @Test
    @DisplayName("Debería agendar la cita exitosamente si el horario está disponible")
    void agendarCita_Exito() {
        // Dado: Simulamos que el horario no está ocupado y el repositorio guarda la cita
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(false);
        
        Cita citaSimulada = new Cita();
        citaSimulada.setFecha(fechaValida);
        citaSimulada.setHora(horaValida);
        citaSimulada.setCliente(clientePrueba);

        when(citaRepository.save((Cita) any())).thenReturn(citaSimulada);

        // Cuando: Ejecutamos el método para agendar la cita
        Cita resultado = servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, vehiculoPrueba);

        // Entonces: Verificamos que devuelva la cita y se haya llamado al método save()
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
    @DisplayName("Debería lanzar excepción si la fecha enviada es nula")
    void agendarCita_LanzaExcepcion_SiFechaEsNula() {
        // Cuando: Intentamos agendar pasando null en el campo fecha
        // Entonces: Validamos que se lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(null, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la fecha es nula");
    }

    @Test
    @DisplayName("Debería lanzar excepción si la hora enviada es nula")
    void agendarCita_LanzaExcepcion_SiHoraEsNula() {
        // Cuando: Intentamos agendar pasando null en el campo hora
        // Entonces: Validamos que se lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, null, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la hora es nula");
    }

    @Test
    @DisplayName("Debería lanzar excepción si el cliente enviado es nulo")
    void agendarCita_LanzaExcepcion_SiClienteEsNulo() {
        // Cuando: Intentamos agendar pasando null en el cliente
        // Entonces: Validamos que se lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, null, vehiculoPrueba);
        }, "Debe lanzar excepción si el cliente es nulo");
    }

    @Test
    @DisplayName("Debería lanzar excepción si el vehículo enviado es nulo")
    void agendarCita_LanzaExcepcion_SiVehiculoEsNulo() {
        // Cuando: Intentamos agendar pasando null en el vehículo
        // Entonces: Validamos que se lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, null);
        }, "Debe lanzar excepción si el vehículo es nulo");
    }

    @Test
    @DisplayName("Debería lanzar excepción si la fecha es en el pasado")
    void agendarCita_LanzaExcepcion_SiFechaEsPasada() {
        // Dado: Preparamos una fecha de ayer
        LocalDate fechaPasada = LocalDate.now().minusDays(1);

        // Cuando: Intentamos agendar la cita
        // Entonces: Validamos que falle por fecha pasada y NO interactúe con la BD
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaPasada, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la fecha es en el pasado");

        verifyNoInteractions(citaRepository);
    }

    @Test
    @DisplayName("Debería lanzar excepción si es hoy pero la hora ya pasó")
    void agendarCita_LanzaExcepcion_SiEsHoyYHoraPasada() {
        // Dado: Simulamos una cita para hoy, pero con una hora que ya transcurrió
        LocalDate hoy = LocalDate.now();
        LocalTime horaPasada = LocalTime.now().minusMinutes(5); 

        // Cuando: Intentamos agendar la cita
        // Entonces: Validamos que se rechace y NO interactúe con la BD
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(hoy, horaPasada, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es para hoy pero en una hora que ya pasó");
        
        verifyNoInteractions(citaRepository);
    }

    @Test
    @DisplayName("No debería agendar si ya existe una cita en esa fecha y hora")
    void agendarCita_LanzaExcepcion_SiCitaYaExisteEnFechaYHora() {
        // Dado: Simulamos que el repositorio confirma que el horario está ocupado
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(true);

        // Cuando: Intentamos agendar en ese mismo horario
        // Entonces: Debe fallar y asegurarse de que el método save() nunca sea llamado
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaValida, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si ya hay una cita en ese horario");

        verify(citaRepository, times(1)).existsByFechaAndHora(fechaValida, horaValida);
        verifyNoMoreInteractions(citaRepository);
    }

    @Test
    @DisplayName("Debería lanzar excepción si la hora es antes de apertura")
    void agendarCita_LanzaExcepcion_SiHoraEsAntesDeApertura() {
        // Dado: Establecemos un horario antes de las 9:00 AM
        LocalTime horaTemprano = LocalTime.of(8, 30);
        
        // Cuando: Intentamos agendar la cita
        // Entonces: Validamos que falle por estar fuera de horario
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaTemprano, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es antes de las 9:00 hrs");
        
        verifyNoInteractions(citaRepository);
    }

    @Test
    @DisplayName("Debería lanzar excepción si la hora es después de cierre")
    void agendarCita_LanzaExcepcion_SiHoraEsDespuesDeCierre() {
        // Dado: Establecemos un horario después de las 6:00 PM
        LocalTime horaTarde = LocalTime.of(18, 30);
        
        // Cuando: Intentamos agendar la cita
        // Entonces: Validamos que falle por estar fuera de horario
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCita(fechaValida, horaTarde, clientePrueba, vehiculoPrueba);
        }, "Debe lanzar excepción si la cita es después de las 18:00 hrs");
        
        verifyNoInteractions(citaRepository);
    }

    //Pruebas para agendarCitaCompleta

    @Test
    @DisplayName("Debería registrar cliente, vehículo y agendar la cita exitosamente")
    void agendarCitaCompleta_Exito() {
        // Dado: Simulamos la creación exitosa del cliente, vehículo y que el horario esté libre
        when(servicioCliente.agregaCliente("Ana", "5551234567")).thenReturn(clientePrueba);
        when(servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2020, 15000.0, clientePrueba))
        .thenReturn(vehiculoPrueba);
        when(citaRepository.existsByFechaAndHora(fechaValida, horaValida)).thenReturn(false);
    
        Cita citaGuardada = new Cita();
        citaGuardada.setFecha(fechaValida);
        citaGuardada.setHora(horaValida);
        when(citaRepository.save(any(Cita.class))).thenReturn(citaGuardada);

        // Cuando: Ejecutamos el flujo de agendamiento completo
        Cita resultado = servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                                          2020, "ABC-123", 15000.0, fechaValida, horaValida);

        // Entonces: Verificamos que retorne la cita y se hayan llamado los servicios correspondientes
        assertNotNull(resultado);
        verify(servicioCliente, times(1)).agregaCliente("Ana", "5551234567");
        verify(servicioVehiculo, times(1)).agregaVehiculo(eq("Toyota"), eq("Corolla"), eq("ABC-123"), 
                                                          eq(2020), eq(15000.0), eq(clientePrueba));
    }

    @Test
    @DisplayName("Debería abortar el agendamiento completo si falla la creación del cliente")
    void agendarCitaCompleta_LanzaExcepcion_SiFallaAlCrearCliente() {
        // Dado: Simulamos que el ServicioCliente falla al intentar crear el registro
        when(servicioCliente.agregaCliente(anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("Error al crear cliente"));

        // Cuando: Ejecutamos el agendamiento completo
        // Entonces: Debe abortar y NO interactuar con vehículo ni repositorio de citas
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                              2020, "ABC-123", 15000.0, fechaValida, horaValida);
        }, "Debe abortar si el cliente falla");

        verifyNoInteractions(servicioVehiculo);
        verifyNoInteractions(citaRepository);
    }

    @Test
    @DisplayName("Debería abortar el agendamiento completo si falla la creación del vehículo")
    void agendarCitaCompleta_LanzaExcepcion_SiFallaAlCrearVehiculo() {
        // Dado: Simulamos que el cliente se crea, pero la creación del vehículo falla
        when(servicioCliente.agregaCliente(anyString(), anyString())).thenReturn(clientePrueba);
        when(servicioVehiculo.agregaVehiculo(anyString(), anyString(), anyString(), anyInt(), anyDouble(), any(Cliente.class)))
            .thenThrow(new IllegalArgumentException("Error al crear vehículo"));

        // Cuando: Ejecutamos el agendamiento completo
        // Entonces: Se intenta crear el cliente, pero la cita NO debe guardarse
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.agendarCitaCompleta("Ana", "5551234567", "Toyota", "Corolla", 
                                              2020, "ABC-123", 15000.0, fechaValida, horaValida);
        }, "Debe abortar si el vehículo falla");

        verify(servicioCliente, times(1)).agregaCliente(anyString(), anyString());
        verifyNoInteractions(citaRepository);
    }

    //Pruebas para consultarCitas

    @Test
    @DisplayName("Debería retornar la lista de citas del cliente por su nombre")
    void consultarCitasPorNombreCliente_Exito() {
        // Dado: Simulamos la respuesta de la BD con dos citas para ese nombre
        String nombreBuscado = "  Juan Perez  "; 
        List<Cita> listaSimulada = Arrays.asList(new Cita(), new Cita());
        when(citaRepository.findByClienteNombre("Juan Perez")).thenReturn(listaSimulada);

        // Cuando: Ejecutamos la búsqueda de citas
        List<Cita> resultado = servicioCita.consultarCitasPorNombreCliente(nombreBuscado);

        // Entonces: Verificamos que devuelva los 2 resultados limpiando espacios del nombre
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepository, times(1)).findByClienteNombre("Juan Perez"); 
    }

    @Test
    @DisplayName("Debería retornar excepción si el nombre a buscar es nulo o vacío")
    void consultarCitasPorNombreCliente_LanzaExcepcion_SiNombreEsNuloOVacio() {
        // Cuando: Buscamos usando null y luego una cadena vacía
        // Entonces: Validamos que se lancen excepciones y NO se llame al repositorio
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.consultarCitasPorNombreCliente(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.consultarCitasPorNombreCliente("   ");
        });
        
        verifyNoInteractions(citaRepository);
    }

    //Pruebas para obtenerCitaPendientePorVehiculo

    @Test
    @DisplayName("Debería encontrar la cita pendiente asociada a un vehículo")
    void obtenerCitaPendientePorVehiculo_Exito() {
        // Dado: Simulamos que existe una cita activa (PENDIENTE) para el vehículo
        Cita citaActivaSimulada = new Cita();
        citaActivaSimulada.setEstado("PENDIENTE");
        when(citaRepository.findByVehiculo(vehiculoPrueba)).thenReturn(citaActivaSimulada);

        // Cuando: Consultamos la cita pendiente
        Cita resultado = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoPrueba);

        // Entonces: Verificamos que se obtenga correctamente
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(citaRepository, times(1)).findByVehiculo(vehiculoPrueba);
    }

    @Test
    @DisplayName("Debería lanzar excepción si el vehículo es nulo al consultar cita pendiente")
    void obtenerCitaPendientePorVehiculo_LanzaExcepcion_SiVehiculoEsNulo() {
        // Cuando: Ejecutamos pasando un null directamente
        // Entonces: Validamos que falle y no llame a BD
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.obtenerCitaPendientePorVehiculo(null);
        }, "Debe lanzar excepción si el vehículo es nulo");
        
        verifyNoInteractions(citaRepository);
    }

    @Test
    @DisplayName("Debería retornar null si el vehículo no tiene citas pendientes")
    void obtenerCitaPendientePorVehiculo_RetornaNull_SiNoHayCitasPendientes() {
        // Dado: Simulamos que el repositorio no encuentra ninguna cita pendiente
        when(citaRepository.findByVehiculo(vehiculoPrueba)).thenReturn(null);

        // Cuando: Buscamos la cita del vehículo
        Cita resultado = servicioCita.obtenerCitaPendientePorVehiculo(vehiculoPrueba);

        // Entonces: El resultado debe ser nulo sin provocar errores
        assertNull(resultado, "Debe retornar null si no encuentra ninguna cita pendiente");
        verify(citaRepository, times(1)).findByVehiculo(vehiculoPrueba);
    }
}