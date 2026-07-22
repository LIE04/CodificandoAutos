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

        when(clienteRepository.findByNombre(nombre)).thenReturn(null);

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

        when(clienteRepository.findByNombre(nombre)).thenReturn(clienteExistente);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioCliente.agregaCliente(nombre, telefono);
        });

        assertEquals("Ese cliente ya existe", exception.getMessage());
    }
}
