package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeneradorTest {

	/**
	 * Comprueba que el generador produce árboles de la profundidad correcta,
	 * utilizando símbolos correctos.
	 */
	@Test
	public void testArbol() {
		Generador generador = new Generador();
		ExpresionRegular expresion = generador.arbol(8, 2, true);

		assertEquals("Generado árbol de profundidad erronea", 8,
				profundidad(expresion, 0));

		expresion = generador.arbol(12, 5, false);

		assertEquals("Generado árbol de profundidad erronea", 12,
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
