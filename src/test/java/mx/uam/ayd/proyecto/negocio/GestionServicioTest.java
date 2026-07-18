package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.ServicioRepository;
import mx.uam.ayd.proyecto.datos.VehiculoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Servicio;
import mx.uam.ayd.proyecto.negocio.modelo.Vehiculo;

@ExtendWith(MockitoExtension.class)
class GestionServicioTest {

	@Mock
	private VehiculoRepository vehiculoRepository;

	@Mock
	private ServicioRepository servicioRepository;

	@InjectMocks
	private GestionServicio gestionServicio;

	@Test
	void testBuscaVehiculoPorPlacasEncontrado() {
		String placas = "ABC-123";

		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlacas(placas);

		when(vehiculoRepository.findByPlacas(placas)).thenReturn(vehiculo);

		Vehiculo resultado = gestionServicio.buscaVehiculoPorPlacas(placas);
		assertNotNull(resultado);
		assertEquals(placas, resultado.getPlacas());
	}

	@Test
	void testBuscaVehiculoPorPlacasNoEncontrado() {
		when(vehiculoRepository.findByPlacas("XYZ-999")).thenReturn(null);

		Vehiculo resultado = gestionServicio.buscaVehiculoPorPlacas("XYZ-999");
		assertNull(resultado);
	}

	@Test
	void testBuscaVehiculoPorPlacasCriterioVacio() {
		// Caso: placas nulas o vacías, no se debe consultar el repositorio
		assertNull(gestionServicio.buscaVehiculoPorPlacas(null));
		assertNull(gestionServicio.buscaVehiculoPorPlacas("   "));
	}

	@Test
	void testRecuperaHistorialVehiculoNulo() {
		// Caso: sin vehículo, regresa lista vacía sin tocar el repositorio
		List<Servicio> historial = gestionServicio.recuperaHistorial(null);
		assertEquals(0, historial.size());
	}

	@Test
	void testRecuperaHistorialConServicios() {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlacas("ABC-123");

		ArrayList<Servicio> lista = new ArrayList<>();
		lista.add(new Servicio());
		lista.add(new Servicio());

		when(servicioRepository.findByVehiculoOrderByFechaDesc(vehiculo)).thenReturn(lista);

		List<Servicio> historial = gestionServicio.recuperaHistorial(vehiculo);
		assertEquals(2, historial.size());
	}

	@Test
	void testRegistraServicioExitoso() {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlacas("ABC-123");

		when(servicioRepository.save(ArgumentMatchers.any(Servicio.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Servicio resultado = gestionServicio.registraServicio(vehiculo, LocalDate.now(), "Cambio de balatas",
				"Balatas delanteras", 450.0, "Sin observaciones");

		assertNotNull(resultado);
		assertEquals("Cambio de balatas", resultado.getDescripcion());
		assertEquals(450.0, resultado.getCostoManoObra());
		assertNotNull(resultado.getFechaHoraRegistro());
		assertEquals(vehiculo, resultado.getVehiculo());
	}

	@Test
	void testRegistraServicioSinVehiculo() {
		assertThrows(IllegalArgumentException.class, () -> {
			gestionServicio.registraServicio(null, LocalDate.now(), "Cambio de aceite", "Filtro", 200.0, "");
		});
	}

	@Test
	void testRegistraServicioDatosObligatoriosFaltantes() {
		Vehiculo vehiculo = new Vehiculo();

		assertThrows(IllegalArgumentException.class, () -> {
			gestionServicio.registraServicio(vehiculo, null, "Descripción", "Piezas", 100.0, "");
		});

		assertThrows(IllegalArgumentException.class, () -> {
			gestionServicio.registraServicio(vehiculo, LocalDate.now(), "", "Piezas", 100.0, "");
		});

		assertThrows(IllegalArgumentException.class, () -> {
			gestionServicio.registraServicio(vehiculo, LocalDate.now(), "Descripción", "Piezas", -50.0, "");
		});
	}
}
