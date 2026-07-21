package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import mx.uam.ayd.proyecto.datos.VehiculoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;



@Service
public class ServicioVehiculo {

    private static final Logger log = LoggerFactory.getLogger(ServicioVehiculo.class);

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public ServicioVehiculo(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository =  vehiculoRepository;
    }
    /**
	 * 
	 * Permite agregar un vehículo al sistema, siempre y cuando no exista un vehículo con las mismas placas
	 * 
	 * @param marca marca del vehículo
	 * @param modelo modelo del vehículo
	 * @param placas placas del vehículo
	 * @param anio año del vehículo
	 * @param kilometraje kilometraje del vehículo
	 * @param cliente cliente dueño del vehículo
	 * @return el vehículo que se agregó
	 * @throws IllegalArgumentException si algún parámetro es nulo o vacío, o si ya existe un vehículo con las mismas placas
     */
    
	public Vehiculo agregaVehiculo(String marca, String modelo, String placas, int anio, double kilometraje, Cliente cliente) {
		
		// Validar que ningún parámetro sea nulo o vacío
		if(marca == null || marca.trim().isEmpty()) {
			throw new IllegalArgumentException("La marca no puede ser nula o vacía");
		}
		
		if(modelo == null || modelo.trim().isEmpty()) {
			throw new IllegalArgumentException("El modelo no puede ser nulo o vacío");
		}
        if(placas==null || placas.trim().isEmpty()) {
            throw new IllegalArgumentException("Las placas no pueden ser nulas o vacías");
        }
        if(anio <= 0) {
            throw new IllegalArgumentException("El año debe ser mayor a 0");
        }
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
		
		log.info("Agregando vehículo "+marca+" "+modelo+" con placas:"+placas);

		// Crea el vehículo
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setMarca(marca);
		vehiculo.setModelo(modelo);
		vehiculo.setPlacas(placas);
		vehiculo.setAnio(anio);
		vehiculo.setKilometraje(kilometraje);
		vehiculo.setCliente(cliente);

		vehiculoRepository.save(vehiculo); // Actualización del vehículo en la base de datos
		return vehiculo;
	}
    public List<Vehiculo> getVehiculosCliente(long idCliente) {
        // Asumiendo que en Vehiculo tienes una relación @ManyToOne hacia Cliente
        return vehiculoRepository.findByClienteIdCliente(idCliente);
    }


}