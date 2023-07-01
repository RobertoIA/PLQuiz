package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class GeneradorTest {

	private static final Logger log = LoggerFactory
			.getLogger(GeneradorTest.class);

	private final int ITERACIONES = 10_000;
	private final int MAX_PROFUNDIDAD = 8;

	/**
	 * Comprueba que el generador produce árboles de la profundidad correcta,
	 * utilizando símbolos correctos.
	 */
	@Test
	public void testArbol() {
		Generador generador = new Generador(2, true, false);
		ExpresionRegular expresion = generador.arbol(8);

		assertEquals("Generado árbol de profundidad errónea", 8,
				expresion.profundidad());

		generador = new Generador(2, true, true);
		expresion = generador.arbol(8);

		assertEquals("Generado árbol de profundidad errónea", 9,
				expresion.profundidad());

		generador = new Generador(5, false, false);
		expresion = generador.arbol(12);

		assertEquals("Generado árbol de profundidad errónea", 12,
				expresion.profundidad());

		generador = new Generador(5, false, true);
		expresion = generador.arbol(12);

		assertEquals("Generado árbol de profundidad errónea", 13,
				expresion.profundidad());
	}

	/**
	 * Comprueba que las mutaciones sobre una expresión devuelven expresiones
	 * correctas (profundidad dada +/- 1).
	 */
	@Test
	public void testMutacion() {  // FIXME once in a while this fails
		Generador generador = new Generador(2, true, false);
		ExpresionRegular expresion = generador.arbol(8);
		ExpresionRegular mutante = generador.mutacion(expresion);

		assertFalse("La expresión mutada es igual a la original.",
				expresion.equals(mutante));
		assertTrue("La expresión mutada tiene una profundidad errónea.",
				Math.abs(8 - mutante.profundidad()) <= 1);
	}

	/**
	 * Genera una serie de problemas Aho-Sethi-Ullman a partir de expresiones
	 * regulares aleatorias y almacena sus características.
	 * <p>
	 * No incluye nodos vacíos.
	 */
	@Test
	@Ignore
	public void testGeneracionASUNoVacio() {
		genera(ITERACIONES, false, "ASU", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas de Construcción de Subconjuntos a partir de
	 * expresiones regulares aleatorias y almacena sus características.
	 * <p>
	 * No incluye nodos vacíos.
	 */
	@Test
	@Ignore
	public void testGeneracionCSNoVacio() {
		genera(ITERACIONES, false, "CS", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas Aho-Sethi-Ullman a partir de expresiones
	 * regulares aleatorias y almacena sus características.
	 * <p>
	 * Incluye nodos vacíos.
	 */
	@Test
	@Ignore
	public void testGeneracionASUVacio() {
		genera(ITERACIONES, true, "ASU", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de problemas de Construcción de Subconjuntos a partir de
	 * expresiones regulares aleatorias y almacena sus características.
	 * <p>
	 * Incluye nodos vacíos.
	 */
	@Test
	@Ignore
	public void testGeneracionCSVacio() {
		genera(ITERACIONES, true, "CS", MAX_PROFUNDIDAD);
	}

	/**
	 * Genera una serie de instancias de expresiones regulares, con o sin nodos
	 * vacíos, y analiza sus características de acuerdo con un tipo de problema
	 * dado.
	 * <p>
	 * Los resultados se almacenan en el log.
	 */
	private void genera(int n, boolean vacio, String tipo, int profundidad) {
		Generador generador;
		ExpresionRegular expresion;
		int it;

		switch (tipo) {
		case "ASU":
			AhoSethiUllman asu;
			generador = new Generador(6, vacio, true);

			while (profundidad > 0) {
				it = n;
				log.info("profundidad {}", profundidad);
				while (it > 0) {
					expresion = generador.arbol(profundidad);
					asu = new AhoSethiUllman(expresion);

					log.info("{} {}", asu.simbolos().size(), asu.estados()
							.size());

					it--;
				}
				profundidad--;
			}
			break;
		case "CS":
			ConstruccionSubconjuntos cs;
			generador = new Generador(6, vacio, false);

			while (profundidad > 0) {
				it = n;
				log.info("profundidad {}", profundidad);
				while (it > 0) {
					expresion = generador.arbol(profundidad);
					cs = new ConstruccionSubconjuntos(expresion);

					log.info("{} {}", cs.simbolos().size(), cs.estados().size());

					it--;
				}
				profundidad--;
			}
			break;
		}
	}

	/**
	 * Realiza un muestrario de tiempos de generación de expresiones, tomando la
	 * media de una serie de expresiones aleatorias para cada profundidad.
	 */
	@Test
	@Ignore
	public void testGeneracionTiempos() {
		long t, t0;
		Generador generador = new Generador(28, false, false);
		ExpresionRegular exp = null;

		for (int p = 0; p <= MAX_PROFUNDIDAD; p++) {
			t = 0;
			for (int i = 0; i < ITERACIONES; i++) {
				t0 = System.nanoTime();
				exp = generador.arbol(p);
				
				if(exp.profundidad() != p) fail();
				
				t += System.nanoTime() - t0;
			}
			t /= ITERACIONES;
			log.info("{}", exp);
			log.info("profundidad {}, {}", p, t);
		}
	}
}
