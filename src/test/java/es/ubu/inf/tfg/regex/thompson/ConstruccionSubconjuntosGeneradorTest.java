package es.ubu.inf.tfg.regex.thompson;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConstruccionSubconjuntosGeneradorTest {

	private final int MIN_CORRECTOS = 80; // Mínimo porcentaje de correctos
	private final int N_ITERACIONES = 10; // Total de problemas generados por
											// test

	private static final Random random = new Random(new Date().getTime());
	ConstruccionSubconjuntosGenerador generador;

	@Before
	public void setUp() throws Exception {
		generador = new ConstruccionSubconjuntosGenerador();
	}

	@After
	public void tearDown() throws Exception {
		generador = null;
	}

	/**
	 * Comprueba que la clase genera un problema sin incluir nodos vacíos y con
	 * los parámetros pedidos. Debe generar el problema pedido o diferir de el
	 * en uno en como máximo uno de los parámetros en al menos un 80% de los
	 * casos.
	 */
	@Ignore
	@Test
	public void testNuevoNoVacio() {
		ConstruccionSubconjuntos problema;
		int correctos = 0;
		int estados, simbolos;
		int dif;

		for (int i = 0; i < N_ITERACIONES; i++) {
			estados = random.nextInt(13) + 3;
			simbolos = random.nextInt(5) + 2;

			problema = generador.nuevo(simbolos, estados, false);

			dif = Math.abs(problema.simbolos().size() - simbolos)
					+ Math.abs(problema.estados().size() - estados);

			if (dif <= 1)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas válidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}

	/**
	 * Comprueba que la clase genera un problema incluyendo nodos vacíos y con
	 * los parámetros pedidos o similares. Debe generar el problema pedido o
	 * diferir de el en uno en como máximo uno de los parámetros en al menos un
	 * 80% de los casos.
	 */
	@Ignore
	@Test
	public void testNuevoVacio() {
		ConstruccionSubconjuntos problema;
		int correctos = 0;
		int estados, simbolos;
		int dif;

		for (int i = 0; i < N_ITERACIONES; i++) {
			estados = random.nextInt(13) + 3;
			simbolos = random.nextInt(5) + 2;

			problema = generador.nuevo(simbolos, estados, true);

			dif = Math.abs(problema.simbolos().size() - simbolos)
					+ Math.abs(problema.estados().size() - estados);

			if (dif <= 1)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas válidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}
}
