package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.DistribuidorRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Distribuidor;

@ExtendWith(MockitoExtension.class)
class ServicioDistribuidorTest {

	@Mock
	private DistribuidorRepository distribuidorRepository;

	@InjectMocks
	private ServicioDistribuidor servicioDistribuidor;

	@Test
	void testRecuperaDistribuidoresSinRegistros() {
		// Caso: no hay distribuidores guardados, regresa lista vacía
		List<Distribuidor> distribuidores = servicioDistribuidor.recuperaDistribuidores();
		assertEquals(0, distribuidores.size());
	}

	@Test
	void testRecuperaDistribuidoresConRegistros() {
		// Caso: hay distribuidores guardados, regresa la lista completa
		ArrayList<Distribuidor> lista = new ArrayList<>();

		Distribuidor distribuidor1 = new Distribuidor();
		distribuidor1.setNombre("Refaccionaria Central");

		Distribuidor distribuidor2 = new Distribuidor();
		distribuidor2.setNombre("AutoPartes del Valle");

		lista.add(distribuidor1);
		lista.add(distribuidor2);

		when(distribuidorRepository.findAll()).thenReturn(lista);

		List<Distribuidor> distribuidores = servicioDistribuidor.recuperaDistribuidores();
		assertEquals(2, distribuidores.size());
	}

	@Test
	void testBuscaDistribuidoresCriterioVacioRegresaTodos() {
		// Caso: criterio nulo o vacío, se comporta igual que recuperaDistribuidores()
		ArrayList<Distribuidor> lista = new ArrayList<>();
		lista.add(new Distribuidor());

		when(distribuidorRepository.findAll()).thenReturn(lista);

		List<Distribuidor> resultado = servicioDistribuidor.buscaDistribuidores(null);
		assertEquals(1, resultado.size());

		resultado = servicioDistribuidor.buscaDistribuidores("   ");
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscaDistribuidoresPorNombreOTipo() {
		// Caso: hay coincidencias con el criterio de búsqueda
		String criterio = "frenos";

		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setNombre("Frenos y Clutch SA");
		distribuidor.setTipoRefaccion("frenos");

		when(distribuidorRepository.findByNombreContainingIgnoreCaseOrTipoRefaccionContainingIgnoreCase(criterio,
				criterio)).thenReturn(List.of(distribuidor));

		List<Distribuidor> resultado = servicioDistribuidor.buscaDistribuidores(criterio);
		assertEquals(1, resultado.size());
		assertEquals("Frenos y Clutch SA", resultado.get(0).getNombre());
	}

	@Test
	void testBuscaDistribuidoresSinCoincidencias() {
		// Caso: la búsqueda no encuentra coincidencias
		String criterio = "inexistente";

		when(distribuidorRepository.findByNombreContainingIgnoreCaseOrTipoRefaccionContainingIgnoreCase(criterio,
				criterio)).thenReturn(Collections.emptyList());

		List<Distribuidor> resultado = servicioDistribuidor.buscaDistribuidores(criterio);
		assertTrue(resultado.isEmpty());
	}
}
