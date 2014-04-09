package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneradorTest {

	/**
	 * Comprueba que el generador produce árboles de la profundidad correcta,
	 * utilizando símbolos correctos.
	 */
	@Test
	public void testArbol() {
		Generador generador = new Generador(2, true);
		ExpresionRegular expresion = generador.arbol(8);

		assertEquals("Generado árbol de profundidad erronea", 8,
				expresion.profundidad());

		generador = new Generador(5, false);
		expresion = generador.arbol(12);

		assertEquals("Generado árbol de profundidad erronea", 12,
				expresion.profundidad());
	}

	/**
	 * Comprueba que las mutaciones sobre una expresión devuelven expresiones
	 * correctas.
	 */
	@Test
	public void testMutacion() {
		Generador generador = new Generador(2, true);
		ExpresionRegular expresion = generador.arbol(8);
		ExpresionRegular mutante = generador.mutacion(expresion);

		assertFalse("La expresión mutada es igual a la original.",
				expresion.equals(mutante));
		assertEquals("La expresión mutada tiene una profundidad erronea.", 8,
				mutante.profundidad());
	}
}
