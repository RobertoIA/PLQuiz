package es.ubu.inf.tfg.regex.asu;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

public class AhoSethiUllmanGeneradorTest {

	private final int MIN_CORRECTOS = 99; // Mínimo porcentaje de correctos
	private final int N_ITERACIONES = 10; // Total de problemas generados por
											// test

	private static final Random random = new Random(new Date().getTime());
	AhoSethiUllmanGenerador generador = AhoSethiUllmanGenerador.getInstance();

	/**
	 * Comprueba que la petición de múltiples instancias de
	 * AhoSethiUllmanGenerador da como resultado múltiples referencias a la
	 * misma instancia. Es decir, implementa correctamente el patrón singleton.
	 */
	@Test
	public void testGetInstance() {
		AhoSethiUllmanGenerador generadorB = AhoSethiUllmanGenerador
				.getInstance();

		assertSame("Comportamiento de singleton erroneo.", generador,
				generadorB);
	}

	/**
	 * Comprueba que la clase genera un problema sin incluir nodos vacíos y con
	 * los parámetros pedidos. Debe generar el problema pedido en al menos un
	 * 99% de los casos.
	 */
	@Test
	public void testNuevoNoVacio() {
		AhoSethiUllman problema;
		int correctos = 0;
		boolean estadosCorrectos;
		boolean simbolosCorrectos;

		for (int i = 0; i < N_ITERACIONES; i++) {
			problema = generador.nuevo(3, 8, false);

			estadosCorrectos = problema.simbolos().size() == 4;
			simbolosCorrectos = problema.estados().size() == 8;

			if (estadosCorrectos && simbolosCorrectos)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas válidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}

	/**
	 * Comprueba que la clase genera un problema incluyendo nodos vacíos y con
	 * los parámetros pedidos o similares. Debe generar el problema pedido en al
	 * menos un 99% de los casos.
	 */
	@Test
	public void testNuevoVacio() {
		AhoSethiUllman problema;
		int correctos = 0;
		int estados, simbolos;
		boolean estadosCorrectos;
		boolean simbolosCorrectos;

		for (int i = 0; i < N_ITERACIONES; i++) {
			estados = random.nextInt(10) + 1;
			simbolos = random.nextInt(19) + 3;

			problema = generador.nuevo(estados, simbolos, true);

			estadosCorrectos = problema.simbolos().size() == estados + 1;
			simbolosCorrectos = problema.estados().size() == simbolos;

			if (estadosCorrectos && simbolosCorrectos)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas válidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}
}
