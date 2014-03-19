package es.ubu.inf.tfg.regex.thompson;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThompsonTest {

	private Thompson th;

	@Before
	public void setUp() throws Exception {
		th = new Thompson("(a|b)*abb");
	}

	@After
	public void tearDown() throws Exception {
		th = null;
	}

	/**
	 * Comprueba que se recupera correctamente el problema original.
	 */
	@Test
	public void testProblema() {
		assertEquals("Error recuperando el problema original.", "(a|b)*abb",
				th.problema());
	}

	/**
	 * Comprueba que los simbolos del autómata se calculan correctamente.
	 */
	@Test
	public void testSimbolos() {
		assertEquals("Conjunto de símbolos incorrecto.", set('a', 'b'),
				th.simbolos());
	}

	/**
	 * Comprueba que el ejercicio es capaz de obtener una lista de estados
	 * correcta.
	 */
	@Test
	public void testEstados() {
		assertEquals("Conjunto de estados incorrecto.",
				set('A', 'B', 'C', 'D', 'E'), th.estados());
	}

	/**
	 * Comprueba que el ejercicio es capaz de obtener el conjunto de posiciones
	 * correcto para un estado dado.
	 */
	@Test
	public void testPosicionesEstado() {
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(0, 1, 2, 4, 7), th.posiciones('A'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(1, 2, 3, 4, 6, 7, 8), th.posiciones('B'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(1, 2, 4, 5, 6, 7), th.posiciones('C'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(1, 2, 4, 5, 6, 7, 9), th.posiciones('D'));
		assertEquals("Incorrecto conjunto de posiciones para un estado dado.",
				set(1, 2, 4, 5, 6, 7, 10), th.posiciones('E'));
	}

	/**
	 * Comprueba que el ejercicio es capaz de calcular correctamente una función
	 * de transición.
	 */
	@Test
	public void testMueve() {
		assertEquals("Función de transición incorrecta.", 'B',
				th.mueve('A', 'a'));
		assertEquals("Función de transición incorrecta.", 'C',
				th.mueve('A', 'b'));
		assertEquals("Función de transición incorrecta.", 'B',
				th.mueve('B', 'a'));
		assertEquals("Función de transición incorrecta.", 'D',
				th.mueve('B', 'b'));
		assertEquals("Función de transición incorrecta.", 'B',
				th.mueve('C', 'a'));
		assertEquals("Función de transición incorrecta.", 'C',
				th.mueve('C', 'b'));
		assertEquals("Función de transición incorrecta.", 'B',
				th.mueve('D', 'a'));
		assertEquals("Función de transición incorrecta.", 'E',
				th.mueve('D', 'b'));
		assertEquals("Función de transición incorrecta.", 'B',
				th.mueve('E', 'a'));
		assertEquals("Función de transición incorrecta.", 'C',
				th.mueve('E', 'b'));
	}

	/**
	 * Comprueba que el ejercicio es capaz de identificar correctamente los
	 * estados finales.
	 */
	@Test
	public void testEsFinal() {
		assertFalse("Estado no final identificado como final.", th.esFinal('A'));
		assertFalse("Estado no final identificado como final.", th.esFinal('B'));
		assertFalse("Estado no final identificado como final.", th.esFinal('C'));
		assertFalse("Estado no final identificado como final.", th.esFinal('D'));
		assertTrue("Estado final identificado como no final.", th.esFinal('E'));
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
