package es.ubu.inf.tfg.doc.trad;

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
		String esperado = toString("XMLTraductorVacio.xml");

		assertEquals("Generación incorrecta de documento Moodle XML.",
				esperado, traductor.documento(new ArrayList<String>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman.
	 */
	@Test
	public void testTraduceAhoSethiUllman() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("XMLTraductorASU.xml");

		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman.",
				esperado, traductor.traduce(problema));
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntos() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos("((a|b*)a*c)*");
		String esperado = toString("XMLTraductorCS.xml");

		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos.",
				esperado, traductor.traduce(problema));
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
						new InputStreamReader(entrada, "UTF16"))) {

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
