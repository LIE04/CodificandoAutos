package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.ClienteRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import java.util.List;


@Service
public class ServicioCliente {

    private static final Logger log = LoggerFactory.getLogger(ServicioCliente.class);

    private final ClienteRepository clienteRepository;

    @Autowired
    public ServicioCliente(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    /**
	 * 
	 * Permite agregar un usuario
	 * 
	 * @param nombre nombre del usuario
	 * @param apellido apellido del usuario
	 * @param grupo nombre grupo al que debe pertencer
	 * @return el usuario que se agregó
	 * @throws IllegalArgumentException si el usuario ya existe, no existe el grupo,
	 *         o si alguno de los parámetros es nulo o vacío
	 * 
	 */
	public Cliente agregaCliente(String nombre, String telefono) {
		
		// Validar que ningún parámetro sea nulo o vacío
		if(nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		
		if(telefono == null || telefono.trim().isEmpty()) {
			throw new IllegalArgumentException("El teléfono no puede ser nulo o vacío");
		}
		
		// Regla de negocio: No se permite agregar dos usuarios con el mismo nombre y apellido
		
		Cliente cliente = clienteRepository.findByNombre(nombre);
		
		if(cliente != null) {
			throw new IllegalArgumentException("Ese cliente ya existe");
		}
		// Se validaron correctamente las reglas de negocio
		
		log.info("Agregando cliente "+nombre+" con telefono:"+telefono);

		// Crea el usuario
		
		cliente = new Cliente();
		cliente.setNombre(nombre);
		cliente.setTelefono(telefono);
		clienteRepository.save(cliente); //Actualizacion del Cliente en la base de datos
		return cliente;
	}

        public List<Cliente> getClientes() {
        return (List<Cliente>) clienteRepository.findAll();
    }

}
