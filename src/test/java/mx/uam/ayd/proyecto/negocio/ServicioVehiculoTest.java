package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.VehiculoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@ExtendWith(MockitoExtension.class)
class ServicioVehiculoTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ServicioVehiculo servicioVehiculo;

    private Cliente clientePrueba;

    @BeforeEach
    void setUp() {
        // Inicializamos un cliente cualquiera para usarlo en los tests
        clientePrueba = new Cliente();

    }

    // Test para el método agregaVehiculo
    @Test
    void agregaVehiculo_Exito() {
        //Simular que no existe un vehículo con las placas dadas
        when(vehiculoRepository.findByPlacas("ABC-123")).thenReturn(null);
        Vehiculo resultado = servicioVehiculo.agregaVehiculo(
                "Toyota", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba
        );
        //Verificar que el resultado no sea nulo y que los atributos sean correctos
        assertNotNull(resultado, "El vehículo devuelto no debe ser nulo");
        assertEquals("Toyota", resultado.getMarca());
        assertEquals("Corolla", resultado.getModelo());
        assertEquals("ABC-123", resultado.getPlacas());
        assertEquals(2022, resultado.getAnio());
        assertEquals(15000.5, resultado.getKilometraje());
        assertEquals(clientePrueba, resultado.getCliente());

        // Verificamos que se haya llamado al repositorio para guardar el vehículo
        verify(vehiculoRepository, times(1)).save(resultado);
    }

    // Verificar nulos y vacíos para marca, modelo, placas, año, kilometraje y cliente
    @Test
    void agregaVehiculo_LanzaExcepcion_SiMarcaEsNulaOVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo(null, "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si la marca es nula");

        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("   ", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si la marca está vacía");
    }
    
    @Test
    void agregaVehiculo_LanzaExcepcion_SiModeloEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", null, "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el modelo es nulo");

        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "   ", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el modelo está vacío");
    }

@Test
    void agregaVehiculo_LanzaExcepcion_SiPlacasSonNulasOVacias() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", null, 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas son nulas");

        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "   ", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas están vacías");
    }
    @Test
    void agregaVehiculo_LanzaExcepcion_SiAnioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 0, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el año es 0 o menor");
    }

    @Test
    void agregaVehiculo_LanzaExcepcion_SiKilometrajeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, -1.0, clientePrueba);
        }, "Debe lanzar excepción si el kilometraje es negativo");
    }

    @Test
    void agregaVehiculo_LanzaExcepcion_SiClienteEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, 15000.5, null);
        });
    }
    
    @Test
    void agregaVehiculo_LanzaExcepcion_SiPlacasYaExisten() {
        when(vehiculoRepository.findByPlacas("ABC-123")).thenReturn(new Vehiculo());

        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas ya existen en el sistema");
    }

    @Test
    void getVehiculosCliente_Exito() {
        long idCliente = 1L;
        java.util.List<Vehiculo> listaSimulada = java.util.Arrays.asList(new Vehiculo(), new Vehiculo());
        
        when(vehiculoRepository.findByClienteIdCliente(idCliente)).thenReturn(listaSimulada);

        java.util.List<Vehiculo> resultado = servicioVehiculo.getVehiculosCliente(idCliente);

        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe retornar los 2 vehículos del cliente");
        verify(vehiculoRepository, times(1)).findByClienteIdCliente(idCliente);
    }

    @Test
    void recuperaTodos_Exito() {
        java.util.List<Vehiculo> listaSimulada = java.util.Arrays.asList(new Vehiculo(), new Vehiculo(), new Vehiculo());
        
        when(vehiculoRepository.findAll()).thenReturn(listaSimulada);

        java.util.List<Vehiculo> resultado = servicioVehiculo.recuperaTodos();

        assertNotNull(resultado);
        assertEquals(3, resultado.size(), "Debe retornar todos los vehículos registrados");
        verify(vehiculoRepository, times(1)).findAll();
    }

}