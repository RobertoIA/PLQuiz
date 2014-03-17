package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.regex.datos.MapaEstados;

public class MapaEstadosTest {

	private MapaEstados mapaEstados;

	@Before
	public void setUp() throws Exception {
		mapaEstados = new MapaEstados();
	}

	@After
	public void tearDown() throws Exception {
		mapaEstados = null;
	}

	/**
	 * Comprueba que las transiciones se añaden correctamente al mapa, y que se
	 * obtiene el destino correcto para un par estado-símbolo dado.
	 */
	@Test
	public void testAddTransicionGetTransicion() {
		mapaEstados.add('A', 'a', 'B');
		mapaEstados.add('A', 'b', 'A');
		mapaEstados.add('B', 'a', 'A');
		mapaEstados.add('B', 'b', 'B');

		assertEquals("Obtenida transición incorrecta.", 'B',
				mapaEstados.get('A', 'a'));
		assertEquals("Obtenida transición incorrecta.", 'A',
				mapaEstados.get('A', 'b'));
		assertEquals("Obtenida transición incorrecta.", 'A',
				mapaEstados.get('B', 'a'));
		assertEquals("Obtenida transición incorrecta.", 'B',
				mapaEstados.get('B', 'b'));
	}

	/**
	 * Comprueba que se obtiene la lista correcta de estados existentes.
	 */
	@Test
	public void testEstados() {
		mapaEstados.add('A', 'a', 'B');
		mapaEstados.add('A', 'b', 'A');
		mapaEstados.add('B', 'a', 'A');
		mapaEstados.add('B', 'b', 'B');

		assertEquals("Obtenida lista de estados incorrecta.", set('A', 'B'),
				mapaEstados.estados());
	}

	/**
	 * Genera un set a partir de una lista de caracteres de longitud variable.
	 * 
	 * @param ns
	 *            Lista de caracteres de longitud variable.
	 * @return Set de caracteres.
	 */
	private static Set<Character> set(char... ns) {
		Set<Character> set = new TreeSet<>();
		for (char n : ns)
			set.add(n);
		return set;
	}
}
