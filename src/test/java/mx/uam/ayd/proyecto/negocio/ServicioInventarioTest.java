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

import mx.uam.ayd.proyecto.datos.PiezaInventarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.PiezaInventario;

@ExtendWith(MockitoExtension.class)
class ServicioInventarioTest {

	@Mock
	private PiezaInventarioRepository piezaInventarioRepository;

	@InjectMocks
	private ServicioInventario servicioInventario;

	@Test
	void testRegistrarPiezaNueva() {
		// Caso 1: la pieza no existe, se debe crear un registro nuevo con la cantidad recibida
		String nombre = "Balata delantera";
		String proveedor = "Refaccionaria Central";
		LocalDate fecha = LocalDate.now();

		when(piezaInventarioRepository.findByNombreAndProveedor(nombre, proveedor)).thenReturn(null);
		when(piezaInventarioRepository.save(ArgumentMatchers.any(PiezaInventario.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		PiezaInventario resultado = servicioInventario.registrarPieza(nombre, 5, proveedor, fecha, 350.0);

		assertNotNull(resultado);
		assertEquals(nombre, resultado.getNombre());
		assertEquals(proveedor, resultado.getProveedor());
		assertEquals(5, resultado.getCantidad());
		assertNotNull(resultado.getFechaHoraRegistro());
	}

	@Test
	void testRegistrarPiezaExistenteIncrementaCantidad() {
		// Caso 2: la pieza ya existe, se debe incrementar la cantidad disponible
		String nombre = "Filtro de aceite";
		String proveedor = "AutoPartes del Valle";

		PiezaInventario existente = new PiezaInventario();
		existente.setNombre(nombre);
		existente.setProveedor(proveedor);
		existente.setCantidad(10);

		when(piezaInventarioRepository.findByNombreAndProveedor(nombre, proveedor)).thenReturn(existente);
		when(piezaInventarioRepository.save(ArgumentMatchers.any(PiezaInventario.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		PiezaInventario resultado = servicioInventario.registrarPieza(nombre, 4, proveedor, LocalDate.now(), 120.0);

		assertEquals(14, resultado.getCantidad());
	}

	@Test
	void testRegistrarPiezaCantidadInvalida() {
		// Caso 3: la cantidad recibida debe ser mayor a cero
		assertThrows(IllegalArgumentException.class, () -> {
			servicioInventario.registrarPieza("Bujía", 0, "Proveedor X", LocalDate.now(), 50.0);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			servicioInventario.registrarPieza("Bujía", -3, "Proveedor X", LocalDate.now(), 50.0);
		});
	}

	@Test
	void testRegistrarPiezaDatosObligatoriosFaltantes() {
		// Caso 4: nombre, proveedor y fecha son obligatorios
		assertThrows(IllegalArgumentException.class, () -> {
			servicioInventario.registrarPieza(null, 5, "Proveedor X", LocalDate.now(), 50.0);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			servicioInventario.registrarPieza("Bujía", 5, "", LocalDate.now(), 50.0);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			servicioInventario.registrarPieza("Bujía", 5, "Proveedor X", null, 50.0);
		});
	}

	@Test
	void testRecuperaPiezas() {
		// Caso 1: no hay piezas registradas, regresa lista vacía
		List<PiezaInventario> piezas = servicioInventario.recuperaPiezas();
		assertEquals(0, piezas.size());

		// Caso 2: hay piezas registradas, regresa la lista completa
		ArrayList<PiezaInventario> lista = new ArrayList<>();

		PiezaInventario pieza1 = new PiezaInventario();
		pieza1.setNombre("Balata delantera");

		PiezaInventario pieza2 = new PiezaInventario();
		pieza2.setNombre("Filtro de aceite");

		lista.add(pieza1);
		lista.add(pieza2);

		when(piezaInventarioRepository.findAll()).thenReturn(lista);

		piezas = servicioInventario.recuperaPiezas();
		assertEquals(2, piezas.size());
	}
}
