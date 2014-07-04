package es.ubu.inf.tfg.doc.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorLatex;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class TraductorLatexTest {

	Traductor traductor;

	@Before
	public void setUp() throws Exception {
		traductor = new TraductorLatex();
	}

	@After
	public void tearDown() throws Exception {
		traductor = null;
	}

	/**
	 * Comprueba la correcta generación de un documento que contenga los datos
	 * dados.
	 */
	@Test
	public void testDocumento() {
		String esperado = toString("TraductorVacio.tex");

		assertEquals("Generación incorrecta de documento Latex.", esperado,
				traductor.documento(new ArrayList<Plantilla>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo construcción.
	 */
	@Test
	public void testTraduceAhoSethiUllmanConstruccion() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUConstruccion.tex");
		String encontrado = traductor.traduceASUConstruccion(problema)
				.toString();

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");

		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo construccion.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo etiquetado.
	 */
	@Test
	public void testTraduceAhoSethiUllmanEtiquetado() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUEtiquetado.tex");
		String encontrado = traductor.traduceASUEtiquetado(problema).toString();

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");

		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo etiquetado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo tablas.
	 */
	@Test
	public void testTraduceAhoSethiUllmanTablas() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUTablas.tex");

		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo tablas.",
				esperado, traductor.traduceASUTablas(problema).toString());
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo construcción.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosConstruccion() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSConstruccion.tex");
		String encontrado = traductor.traduceCSConstruccion(problema).toString();
		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");

		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo construcción.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo expresión.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosExpresion() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSExpresion.tex");

		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo expresión.",
				esperado, traductor.traduceCSExpresion(problema).toString());
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo autómata.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosAutomata() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSAutomata.tex");
		String encontrado = traductor.traduceCSAutomata(problema).toString();

		encontrado = encontrado.replaceAll("\\{.*.jpg\\}", "{.jpg}");

		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo autómata.",
				esperado, encontrado);
	}

	/**
	 * Lee un recurso como una cadena de caracteres.
	 * 
	 * @param fichero
	 *            Recurso a leer.
	 * @return Contenido del recurso.
	 */
	private String toString(String fichero) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = getClass().getResourceAsStream(fichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo " + fichero);
			return "";
		}
	}
}
