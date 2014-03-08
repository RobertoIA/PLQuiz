package es.ubu.inf.tfg.asu.datos;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapaPosicionesTest {

	private MapaPosiciones<Integer> mapaInteger;
	private MapaPosiciones<Character> mapaCharacter;

	@Before
	public void setUp() throws Exception {
		mapaInteger = new MapaPosiciones<>();
		mapaCharacter = new MapaPosiciones<>();
	}

	@After
	public void tearDown() throws Exception {
		mapaInteger = null;
		mapaCharacter = null;
	}

	/**
	 * Comprueba que las claves sueltas se añaden y recuperan correctamente.
	 */
	@Test
	public void testAddClave() {
		// MapaPosiciones<Integer>
		mapaInteger.add(1);
		assertEquals("Error recuperando contenido de clave suelta.", set(),
				mapaInteger.get(1));

		// MapaPosiciones<Character>
		mapaCharacter.add('a');
		assertEquals("Error recuperando contenido de clave suelta.", set(),
				mapaCharacter.get('a'));
	}

	/**
	 * Comprueba que las claves sueltas a las que añadimos una posicion suelta
	 * se añaden y recuperan correctamente, existieran ya o no.
	 */
	@Test
	public void testAddClavePosicion() {
		// MapaPosiciones<Integer>
		mapaInteger.add(1, 1);
		assertEquals(
				"Error recuperando el contenido de clave nueva con posicion suelta.",
				set(1), mapaInteger.get(1));
		mapaInteger.add(1, 2);
		assertEquals(
				"Error recuperando el contenido de clave existente con posicion suelta.",
				set(1, 2), mapaInteger.get(1));

		// MapaPosiciones<Character>
		mapaCharacter.add('a', 1);
		assertEquals(
				"Error recuperando el contenido de clave nueva con posicion suelta.",
				set(1), mapaCharacter.get('a'));
		mapaCharacter.add('a', 2);
		assertEquals(
				"Error recuperando el contenido de clave existente con posicion suelta.",
				set(1, 2), mapaCharacter.get('a'));
	}

	/**
	 * Comprueba que las claves sueltas a las que añadimos un conjunto de
	 * posiciones se añaden y recuperan correctamente, existieran ya o no.
	 */
	@Test
	public void testAddClavePosiciones() {
		// MapaPosiciones<Integer>
		mapaInteger.add(1, set(1, 2));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaInteger.get(1));
		mapaInteger.add(1, set(3, 4));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaInteger.get(1));

		// MapaPosiciones<Character>
		mapaCharacter.add('a', set(1, 2));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaCharacter.get('a'));
		mapaCharacter.add('a', set(3, 4));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaCharacter.get('a'));
	}

	/**
	 * Comprueba que los conjuntos de claves a las que añadimos un conjunto de
	 * posiciones se añaden y recuperan correctamente, existieran ya o no.
	 */
	@Test
	public void testAddClavesPosiciones() {
		// MapaPosiciones<Integer>
		mapaInteger.add(set(1, 2), set(1, 2));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaInteger.get(1));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaInteger.get(2));
		mapaInteger.add(set(1, 2), set(3, 4));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaInteger.get(1));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaInteger.get(2));

		// MapaPosiciones<Character>
		mapaCharacter.add(set('a', 'b'), set(1, 2));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaCharacter.get('a'));
		assertEquals(
				"Error recuperando el contenido de clave nueva con conjunto posiciones.",
				set(1, 2), mapaCharacter.get('b'));
		mapaCharacter.add(set('a', 'b'), set(3, 4));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaCharacter.get('a'));
		assertEquals(
				"Error recuperando el contenido de clave existente con conjunto posiciones.",
				set(1, 2, 3, 4), mapaCharacter.get('b'));
	}

	/**
	 * Comprueba que el mapa devuelve un conjunto de claves correcto e
	 * inmutable.
	 */
	@Test
	public void testKeys() {
		// MapaPosiciones<Integer>
		mapaInteger.add(set(1, 2, 3, 4), set(1, 2));
		Set<Integer> iKeys = mapaInteger.keys();
		assertEquals("Error recuperando el conjunto de claves.",
				set(1, 2, 3, 4), iKeys);
		iKeys.add(5);
		assertEquals("Error modificando el conjunto de claves.",
				set(1, 2, 3, 4), mapaInteger.keys());

		// MapaPosiciones<Character>
		mapaCharacter.add(set('a', 'b', 'c', 'd'), set(1, 2));
		Set<Character> cKeys = mapaCharacter.keys();
		assertEquals("Error recuperando el conjunto de claves.",
				set('a', 'b', 'c', 'd'), cKeys);
		cKeys.add('e');
		assertEquals("Error modificando el conjunto de claves.",
				set('a', 'b', 'c', 'd'), mapaCharacter.keys());
	}

	/**
	 * Comprueba que la unión de dos mapas tiene los contenidos correctos.
	 */
	@Test
	public void testUnion() {
		// MapaPosiciones<Integer>
		mapaInteger.add(1, set(1, 2, 3));
		mapaInteger.add(2, set(1, 2, 3));
		MapaPosiciones<Integer> mapaIntegerB = new MapaPosiciones<>();
		mapaIntegerB.add(1, set(4, 5, 6));
		mapaIntegerB.add(2, set(1, 2, 3));
		mapaIntegerB.add(3, set(1, 2, 3));
		MapaPosiciones<Integer> unionInteger = MapaPosiciones.union(
				mapaInteger, mapaIntegerB);

		assertEquals("La union no contiene todas las claves.", set(1, 2, 3),
				unionInteger.keys());
		assertEquals("La union no une claves existentes en ambas aprtes.",
				set(1, 2, 3, 4, 5, 6), unionInteger.get(1));
		assertEquals(
				"La union no trata correctamente claves y posiciones duplicadas.",
				set(1, 2, 3), unionInteger.get(2));
		assertEquals(
				"La union no añade correctamente claves existentes en una sola parte.",
				set(1, 2, 3), unionInteger.get(3));

		// MapaPosiciones<Character>
		mapaCharacter.add('a', set(1, 2, 3));
		mapaCharacter.add('b', set(1, 2, 3));
		MapaPosiciones<Character> mapaCharacterB = new MapaPosiciones<>();
		mapaCharacterB.add('a', set(4, 5, 6));
		mapaCharacterB.add('b', set(1, 2, 3));
		mapaCharacterB.add('c', set(1, 2, 3));
		MapaPosiciones<Character> unionCharacter = MapaPosiciones.union(
				mapaCharacter, mapaCharacterB);

		assertEquals("La union no contiene todas las claves.",
				set('a', 'b', 'c'), unionCharacter.keys());
		assertEquals("La union no une claves existentes en ambas aprtes.",
				set(1, 2, 3, 4, 5, 6), unionCharacter.get('a'));
		assertEquals(
				"La union no trata correctamente claves y posiciones duplicadas.",
				set(1, 2, 3), unionCharacter.get('b'));
		assertEquals(
				"La union no añade correctamente claves existentes en una sola parte.",
				set(1, 2, 3), unionCharacter.get('c'));
	}

	/**
	 * Comprueba que la copia de un mapa es igual al original, pero no la misma
	 * instancia.
	 */
	@Test
	public void testCopia() {
		// MapaPosiciones<Integer>
		MapaPosiciones<Integer> mapaIntegerB = MapaPosiciones
				.copia(mapaInteger);
		assertFalse("La copia del mapa es la propia instancia de entrada.",
				mapaInteger == mapaIntegerB);
		assertEquals("La copia del mapa no es igual al original.", mapaInteger,
				mapaIntegerB);

		// MapaPosiciones<Character>
		MapaPosiciones<Character> mapaCharacterB = MapaPosiciones
				.copia(mapaCharacter);
		assertFalse("La copia del mapa es la propia instancia de entrada.",
				mapaCharacter == mapaCharacterB);
		assertEquals("La copia del mapa no es igual al original.",
				mapaCharacter, mapaCharacterB);
	}

	/**
	 * Genera un set a partir de una lista de enteros de longitud variable.
	 * 
	 * @param ns
	 *            Lista de enteros de longitud variable.
	 * @return Set de enteros.
	 */
	private static Set<Integer> set(int... ns) {
		Set<Integer> set = new TreeSet<>();
		for (int n : ns)
			set.add(n);
		return set;
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
