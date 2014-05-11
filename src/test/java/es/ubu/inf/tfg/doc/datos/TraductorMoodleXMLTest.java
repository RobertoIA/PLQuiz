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
import es.ubu.inf.tfg.doc.datos.TraductorMoodleXML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class TraductorMoodleXMLTest {

	Traductor traductor;

	@Before
	public void setUp() throws Exception {
		traductor = new TraductorMoodleXML();
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
		String esperado = toString("TraductorVacio.xml");

		assertEquals("Generación incorrecta de documento Moodle XML.",
				esperado, traductor.documento(new ArrayList<String>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo completo.
	 */
	@Test
	public void testTraduceAhoSethiUllmanCompleto() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUCompleto.xml");
		String encontrado = traductor.traduceASUCompleto(problema);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman subtipo completo.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo árbol.
	 */
	@Test
	public void testTraduceAhoSethiUllmanArbol() {
		// TODO
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo expresión.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosExpresion() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSExpresion.xml");
		String encontrado = traductor.traduceCSExpresion(problema);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos subtipo expresión.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo autómata.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosAutomata() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSAutomata.xml");
		String encontrado = traductor.traduceCSAutomata(problema);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos subtipo autómata.",
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
