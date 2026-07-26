package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    void testAgregaClienteExitoso() {
        String nombre = "Juan Perez";
        String telefono = "1234567890";

        when(clienteRepository.findByNombreAndTelefono(nombre, telefono)).thenReturn(null); 

        Cliente clienteAgregado = servicioCliente.agregaCliente(nombre, telefono);

        assertNotNull(clienteAgregado);
        assertEquals(nombre, clienteAgregado.getNombre());
        assertEquals(telefono, clienteAgregado.getTelefono());
    }

    @Test
    void testAgregaClienteNombreNulo() {
        String nombre = null;
        String telefono = "1234567890";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    void testAgregaClienteTelefonoNulo() {
        String nombre = "Juan Perez";
        String telefono = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        assertEquals("El teléfono no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    void testAgregaClienteExistente() {
        String nombre = "Juan Perez";
        String telefono = "1234567890";

        Cliente clienteExistente = new Cliente();
        clienteExistente.setNombre(nombre);
        clienteExistente.setTelefono(telefono);

        when(clienteRepository.findByNombreAndTelefono(nombre, telefono)).thenReturn(clienteExistente);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        assertEquals("Ese cliente ya existe", exception.getMessage());
    }
    // --- NUEVAS PRUEBAS PARA COMPLEMENTAR agregaCliente ---

    @Test
    void testAgregaClienteNombreVacio() {
        // Probamos con espacios en blanco para asegurar que el .trim().isEmpty() funciona
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("   ", "1234567890");
        });

        assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    void testAgregaClienteTelefonoVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "   ");
        });

        assertEquals("El teléfono no puede ser nulo o vacío", exception.getMessage());
    }

    @Test
    void testAgregaClienteTelefonoConLetras() {
        // Intentamos pasar un teléfono con letras y caracteres especiales
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "123abc456-");
        });

        assertEquals("El teléfono debe contener únicamente números", exception.getMessage());
    }

    @Test
    void testGetClientesExitoso() {
        // 1. Preparamos el escenario simulando una lista de clientes en la base de datos
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Ana");
        
        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Beto");
        
        java.util.List<Cliente> listaSimulada = java.util.Arrays.asList(cliente1, cliente2);
        
        when(clienteRepository.findAll()).thenReturn(listaSimulada);

        // 2. Ejecutamos el método
        java.util.List<Cliente> resultado = servicioCliente.getClientes();

        // 3. Validamos que nos regrese la lista correctamente
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        
        // Verificamos que sí se haya llamado al repositorio
        org.mockito.Mockito.verify(clienteRepository, org.mockito.Mockito.times(1)).findAll();
    }
    @Test
    void testAgregaClienteTelefonoLongitudInvalida() {
        // Intentamr pasar un teléfono con menos de 10 dígitos
        Exception exceptionCorta = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "12345");
        });
        assertEquals("El teléfono debe tener exactamente 10 dígitos", exceptionCorta.getMessage());

        //Intentar pasar un telefono con menos de 10 digitos
        Exception exceptionLarga = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente("Juan Perez", "12345678901");
        });
        assertEquals("El teléfono debe tener exactamente 10 dígitos", exceptionLarga.getMessage());
        
        //Confirmar que la base de datos no guardo nada
        org.mockito.Mockito.verifyNoInteractions(clienteRepository);
    }
}
