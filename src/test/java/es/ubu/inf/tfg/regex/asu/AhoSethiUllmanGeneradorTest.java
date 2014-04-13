package es.ubu.inf.tfg.regex.asu;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AhoSethiUllmanGeneradorTest {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanGeneradorTest.class);

	private final int MIN_CORRECTOS = 99; // Mínimo porcentaje de correctos
	private final int N_ITERACIONES = 100; // Total de problemas generados por
											// test

	private static final Random random = new Random(new Date().getTime());
	AhoSethiUllmanGenerador generador;

	@Before
	public void setUp() throws Exception {
		generador = new AhoSethiUllmanGenerador();
	}

	@After
	public void tearDown() throws Exception {
		generador = null;
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
		int estados, simbolos;
		boolean estadosCorrectos;
		boolean simbolosCorrectos;

		for (int i = 0; i < N_ITERACIONES; i++) {
//			 estados = random.nextInt(10) + 1;
//			 simbolos = random.nextInt(19) + 3;
			simbolos = random.nextInt(5) + 6;
			estados = random.nextInt(5) + 4;
			
			log.warn("buscando {} {}", simbolos, estados);

			problema = generador.nuevo(simbolos, estados, false);

//			 log.warn(problema.problema());

			simbolosCorrectos = problema.simbolos().size() == simbolos + 1;
			estadosCorrectos = problema.estados().size() == estados;

//			 log.warn("simbolos: {}/{} estados: {}/{}", problema.simbolos().size(), simbolos,
//					 problema.estados().size(), estados);

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
//			 estados = random.nextInt(10) + 1;
//			 simbolos = random.nextInt(19) + 3;
			simbolos = random.nextInt(5) + 6;
			estados = random.nextInt(5) + 4;
			
			log.warn("buscando {} {}", simbolos, estados);

			problema = generador.nuevo(simbolos, estados, true);

//			log.warn(problema.problema());

			simbolosCorrectos = problema.simbolos().size() == simbolos + 1;
			estadosCorrectos = problema.estados().size() == estados;

//			 log.warn("simbolos: {}/{} estados: {}/{}", problema.simbolos().size(), simbolos,
//					 problema.estados().size(), estados);

			if (estadosCorrectos && simbolosCorrectos)
				correctos++;
		}

		assertTrue("Probabilidad insuficiente de generar problemas válidos: "
				+ (correctos * 100 / N_ITERACIONES) + "%",
				(correctos * 100 / N_ITERACIONES) >= MIN_CORRECTOS);
	}
}
