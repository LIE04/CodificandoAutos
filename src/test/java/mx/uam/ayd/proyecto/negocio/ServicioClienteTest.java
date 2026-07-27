package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.datos.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ServicioClienteTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ServicioCliente servicioCliente;

    @Test
    @DisplayName("Agregar un cliente exitosamente con datos válidos")
    void testAgregaClienteExitoso() {
        // Dado: un nombre y un teléfono válidos que no existen en el repositorio
        String nombre = "Juan Perez";
        String telefono = "1234567890";
        when(clienteRepository.findByNombreAndTelefono(nombre, telefono)).thenReturn(null); 

        // Cuando: se intenta agregar al cliente en el sistema
        Cliente clienteAgregado = servicioCliente.agregaCliente(nombre, telefono);

        // Entonces: el cliente es agregado correctamente y los datos coinciden
        assertNotNull(clienteAgregado);
        assertEquals(nombre, clienteAgregado.getNombre());
        assertEquals(telefono, clienteAgregado.getTelefono());
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con nombre nulo")
    void testAgregaClienteNombreNulo() {
        // Dado: un nombre nulo y un teléfono válido
        String nombre = null;
        String telefono = "1234567890";

        // Cuando: se intenta agregar al cliente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        // Entonces: se lanza una excepción indicando que el nombre no puede ser nulo o vacío
        assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con teléfono nulo")
    void testAgregaClienteTelefonoNulo() {
        // Dado: un nombre válido y un teléfono nulo
        String nombre = "Juan Perez";
        String telefono = null;

        // Cuando: se intenta agregar al cliente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        // Entonces: se lanza una excepción indicando que el teléfono no puede ser nulo o vacío
        assertEquals("El teléfono no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente que ya está registrado")
    void testAgregaClienteExistente() {
        // Dado: un cliente que ya existe en el repositorio con el mismo nombre y teléfono
        String nombre = "Juan Perez";
        String telefono = "1234567890";

        Cliente clienteExistente = new Cliente();
        clienteExistente.setNombre(nombre);
        clienteExistente.setTelefono(telefono);

        when(clienteRepository.findByNombreAndTelefono(nombre, telefono)).thenReturn(clienteExistente);

        // Cuando: se intenta agregar al cliente nuevamente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        // Entonces: se lanza una excepción advirtiendo que el cliente ya existe
        assertEquals("Ese cliente ya existe", exception.getMessage());
    }

    // --- NUEVAS PRUEBAS PARA COMPLEMENTAR agregaCliente ---

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con nombre vacío o en blanco")
    void testAgregaClienteNombreVacio() {
        // Dado: un nombre con solo espacios en blanco y un teléfono válido
        // Probamos con espacios en blanco para asegurar que el .trim().isEmpty() funciona
        
        // Cuando: se intenta agregar al cliente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("   ", "1234567890");
        });

        // Entonces: se lanza una excepción indicando que el nombre no puede ser nulo o vacío
        assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con teléfono vacío o en blanco")
    void testAgregaClienteTelefonoVacio() {
        // Dado: un nombre válido y un teléfono con solo espacios en blanco
        
        // Cuando: se intenta agregar al cliente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "   ");
        });

        // Entonces: se lanza una excepción indicando que el teléfono no puede ser nulo o vacío
        assertEquals("El teléfono no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con letras en el teléfono")
    void testAgregaClienteTelefonoConLetras() {
        // Dado: un nombre válido y un teléfono que contiene letras o caracteres especiales
        // Intentamos pasar un teléfono con letras y caracteres especiales
        
        // Cuando: se intenta agregar al cliente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "123abc456-");
        });

        // Entonces: se lanza una excepción indicando que el teléfono debe contener únicamente números
        assertEquals("El teléfono debe contener únicamente números", exception.getMessage());
    }

    @Test
    @DisplayName("Obtener la lista completa de clientes registrados exitosamente")
    void testGetClientesExitoso() {
        // Dado: un repositorio que contiene una lista simulada de clientes en la base de datos
        // 1. Preparamos el escenario simulando una lista de clientes en la base de datos
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Ana");
        
        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Beto");
        
        java.util.List<Cliente> listaSimulada = java.util.Arrays.asList(cliente1, cliente2);
        
        when(clienteRepository.findAll()).thenReturn(listaSimulada);

        // Cuando: se solicita la lista de todos los clientes
        // 2. Ejecutamos el método
        java.util.List<Cliente> resultado = servicioCliente.getClientes();

        // Entonces: se devuelve la lista correctamente con todos los registros esperados
        // 3. Validamos que nos regrese la lista correctamente
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        
        // Verificamos que sí se haya llamado al repositorio
        org.mockito.Mockito.verify(clienteRepository, org.mockito.Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Fallo al intentar agregar un cliente con teléfono de longitud incorrecta")
    void testAgregaClienteTelefonoLongitudInvalida() {
        // Dado: un nombre válido y un teléfono con menos de 10 dígitos
        // Intentamr pasar un teléfono con menos de 10 dígitos
        
        // Cuando: se intenta agregar al cliente con el teléfono corto
        Exception exceptionCorta = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "12345");
        });
        
        // Entonces: se lanza una excepción por la longitud incorrecta
        assertEquals("El teléfono debe tener exactamente 10 dígitos", exceptionCorta.getMessage());

        // Dado: un teléfono con más de 10 dígitos
        //Intentar pasar un telefono con menos de 10 digitos (Nota: en realidad son más de 10)
        
        // Cuando: se intenta agregar al cliente con el teléfono largo
        Exception exceptionLarga = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "12345678901");
        });
        
        // Entonces: se lanza una excepción y no se realiza ninguna operación en la base de datos
        assertEquals("El teléfono debe tener exactamente 10 dígitos", exceptionLarga.getMessage());
        
        //Confirmar que la base de datos no guardo nada
        org.mockito.Mockito.verifyNoInteractions(clienteRepository);
    }
}