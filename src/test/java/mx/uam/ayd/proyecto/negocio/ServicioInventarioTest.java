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

import mx.uam.ayd.proyecto.datos.RefaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

@ExtendWith(MockitoExtension.class)
class ServicioInventarioTest {

	@Mock
	private RefaccionRepository refaccionRepository;

	@InjectMocks
	private ServicioInventario servicioInventario;

	@Test
	void testRegistrarPiezaNueva() {
		// Caso 1: la pieza no existe, se debe crear un registro nuevo con la cantidad recibida
		String nombre = "Balata delantera";
		String proveedor = "Refaccionaria Central";
		LocalDate fecha = LocalDate.now();

		when(refaccionRepository.findByNombreAndProveedor(nombre, proveedor)).thenReturn(null);
		when(refaccionRepository.save(ArgumentMatchers.any(Refaccion.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Refaccion resultado = servicioInventario.registrarPieza(nombre, 5, proveedor, fecha, 350.0);

		assertNotNull(resultado);
		assertEquals(nombre, resultado.getNombre());
		assertEquals(proveedor, resultado.getProveedor());
		assertEquals(5, resultado.getExistencia());
		assertNotNull(resultado.getFechaHoraRegistro());
	}

	@Test
	void testRegistrarPiezaExistenteIncrementaCantidad() {
		// Caso 2: la pieza ya existe, se debe incrementar la existencia disponible
		String nombre = "Filtro de aceite";
		String proveedor = "AutoPartes del Valle";

		Refaccion existente = new Refaccion();
		existente.setNombre(nombre);
		existente.setProveedor(proveedor);
		existente.setExistencia(10);

		when(refaccionRepository.findByNombreAndProveedor(nombre, proveedor)).thenReturn(existente);
		when(refaccionRepository.save(ArgumentMatchers.any(Refaccion.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Refaccion resultado = servicioInventario.registrarPieza(nombre, 4, proveedor, LocalDate.now(), 120.0);

		assertEquals(14, resultado.getExistencia());
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
		List<Refaccion> piezas = servicioInventario.recuperaPiezas();
		assertEquals(0, piezas.size());

		// Caso 2: hay piezas registradas, regresa la lista completa
		ArrayList<Refaccion> lista = new ArrayList<>();

		Refaccion pieza1 = new Refaccion();
		pieza1.setNombre("Balata delantera");

		Refaccion pieza2 = new Refaccion();
		pieza2.setNombre("Filtro de aceite");

		lista.add(pieza1);
		lista.add(pieza2);

		when(refaccionRepository.findAll()).thenReturn(lista);

		piezas = servicioInventario.recuperaPiezas();
		assertEquals(2, piezas.size());
	}
}
