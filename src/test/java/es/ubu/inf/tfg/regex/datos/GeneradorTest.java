package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneradorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Comprueba que el generador toma las propiedades correctas en cuanto al
	 * uso de vacíos.
	 */
	@Test
	public void testUsaVacio() {
		Generador generador = new Generador(0, true);
		assertTrue("Generador cree no usar vacíos incorrectamente.",
				generador.usaVacio());

		generador = new Generador(0, false);
		assertFalse("Generador cree usar vacíos incorrectamente.",
				generador.usaVacio());
	}

	/**
	 * Comprueba que el generador toma las propiedades correctas en cuanto al
	 * número de símbolos.
	 */
	@Test
	public void testSimbolos() {
		Generador generador = new Generador(5, true);
		assertEquals(
				"Generador con vacíos contiene número erroneo de símbolos.", 6,
				generador.simbolos());

		generador = new Generador(5, false);
		assertEquals(
				"Generador sin vacíos contiene número erroneo de símbolos.", 5,
				generador.simbolos());
	}

	/**
	 * Comprueba que el generador produce árboles de la profundidad correcta,
	 * utilizando símbolos correctos.
	 */
	@Test
	public void testSubArbol() {
		Generador generador = new Generador(2, true);
		ExpresionRegular expresion = generador.subArbol(8, null);

		assertEquals("Generado árbol de profundidad erronea", 8,
				profundidad(expresion, 0));
	}

	/**
	 * Calcula de forma recursiva la profundidad de un árbol de expresión
	 * regular.
	 * 
	 * @param expresion
	 *            Expresión regular en forma de árbol.
	 * @param profundidad
	 *            Profundidad alcanzada hasta el momento.
	 * @return Profundidad alcanzada tras analizar el nodo actual.
	 */
	private int profundidad(ExpresionRegular expresion, int profundidad) {
		if (expresion.esSimbolo() || expresion.esVacio()) {
			return profundidad;
		} else if (expresion.esCierre()) {
			return profundidad(expresion.hijoIzquierdo(), profundidad + 1);
		} else {
			int profIzquierda = profundidad(expresion.hijoIzquierdo(),
					profundidad + 1);
			int profDerecha = profundidad(expresion.hijoDerecho(),
					profundidad + 1);
			return Math.max(profIzquierda, profDerecha);
		}
	}
}
