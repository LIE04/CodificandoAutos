package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Agregar un vehículo exitosamente con datos válidos")
    void agregaVehiculo_Exito() {
        // Dado: que no existe un vehículo con las placas dadas en el sistema
        when(vehiculoRepository.findByPlacas("ABC-123")).thenReturn(null);
        
        // Cuando: se registra el vehículo con todos los datos correctos
        Vehiculo resultado = servicioVehiculo.agregaVehiculo(
                "Toyota", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba
        );
        
        // Entonces: el vehículo devuelto tiene los atributos correctos y es guardado en el repositorio
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
    @DisplayName("Fallo al intentar agregar un vehículo con marca nula o vacía")
    void agregaVehiculo_LanzaExcepcion_SiMarcaEsNulaOVacia() {
        // Dado: una petición para agregar un vehículo con la marca nula
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo(null, "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si la marca es nula");

        // Dado: una petición para agregar un vehículo con la marca vacía o en blanco
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("   ", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si la marca está vacía");
    }
    
    @Test
    @DisplayName("Fallo al intentar agregar un vehículo con modelo nulo o vacío")
    void agregaVehiculo_LanzaExcepcion_SiModeloEsNuloOVacio() {
        // Dado: una petición para agregar un vehículo con el modelo nulo
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", null, "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el modelo es nulo");

        // Dado: una petición para agregar un vehículo con el modelo vacío o en blanco
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "   ", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el modelo está vacío");
    }

    @Test
    @DisplayName("Fallo al intentar agregar un vehículo con placas nulas o vacías")
    void agregaVehiculo_LanzaExcepcion_SiPlacasSonNulasOVacias() {
        // Dado: una petición para agregar un vehículo con las placas nulas
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", null, 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas son nulas");

        // Dado: una petición para agregar un vehículo con las placas vacías o en blanco
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "   ", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas están vacías");
    }

    @Test
    @DisplayName("Fallo al intentar agregar un vehículo con año inválido")
    void agregaVehiculo_LanzaExcepcion_SiAnioInvalido() {
        // Dado: una petición para agregar un vehículo con un año igual a cero o menor
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 0, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si el año es 0 o menor");
    }

    @Test
    @DisplayName("Fallo al intentar agregar un vehículo con kilometraje negativo")
    void agregaVehiculo_LanzaExcepcion_SiKilometrajeInvalido() {
        // Dado: una petición para agregar un vehículo con un kilometraje menor a cero
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, -1.0, clientePrueba);
        }, "Debe lanzar excepción si el kilometraje es negativo");
    }

    @Test
    @DisplayName("Fallo al intentar agregar un vehículo sin cliente asociado")
    void agregaVehiculo_LanzaExcepcion_SiClienteEsNulo() {
        // Dado: una petición para agregar un vehículo donde el cliente es nulo
        // Cuando: se intenta agregar el vehículo
        // Entonces: se lanza una excepción de argumento ilegal
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, 15000.5, null);
        });
    }
    
    @Test
    @DisplayName("Fallo al intentar agregar un vehículo con placas ya registradas en el sistema")
    void agregaVehiculo_LanzaExcepcion_SiPlacasYaExisten() {
        // Dado: que ya existe un vehículo registrado previamente con las mismas placas
        when(vehiculoRepository.findByPlacas("ABC-123")).thenReturn(new Vehiculo());

        // Cuando: se intenta agregar un nuevo vehículo con esas placas
        // Entonces: se lanza una excepción indicando que las placas ya existen
        assertThrows(IllegalArgumentException.class, () -> {
            servicioVehiculo.agregaVehiculo("Toyota", "Corolla", "ABC-123", 2022, 15000.5, clientePrueba);
        }, "Debe lanzar excepción si las placas ya existen en el sistema");
    }

    @Test
    @DisplayName("Obtener la lista de vehículos asociados a un cliente específico")
    void getVehiculosCliente_Exito() {
        // Dado: el identificador de un cliente y una lista de vehículos registrados a su nombre
        long idCliente = 1L;
        java.util.List<Vehiculo> listaSimulada = java.util.Arrays.asList(new Vehiculo(), new Vehiculo());
        when(vehiculoRepository.findByClienteIdCliente(idCliente)).thenReturn(listaSimulada);

        // Cuando: se solicitan los vehículos correspondientes a ese cliente
        java.util.List<Vehiculo> resultado = servicioVehiculo.getVehiculosCliente(idCliente);

        // Entonces: se retorna la lista correcta y se verifica la interacción con el repositorio
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe retornar los 2 vehículos del cliente");
        verify(vehiculoRepository, times(1)).findByClienteIdCliente(idCliente);
    }

    @Test
    @DisplayName("Obtener la lista completa de vehículos registrados")
    void recuperaTodos_Exito() {
        // Dado: una base de datos que contiene una lista de vehículos registrados
        java.util.List<Vehiculo> listaSimulada = java.util.Arrays.asList(new Vehiculo(), new Vehiculo(), new Vehiculo());
        when(vehiculoRepository.findAll()).thenReturn(listaSimulada);

        // Cuando: se solicita recuperar todos los vehículos
        java.util.List<Vehiculo> resultado = servicioVehiculo.recuperaTodos();

        // Entonces: se retorna la lista total de vehículos y se verifica la consulta al repositorio
        assertNotNull(resultado);
        assertEquals(3, resultado.size(), "Debe retornar todos los vehículos registrados");
        verify(vehiculoRepository, times(1)).findAll();
    }

}