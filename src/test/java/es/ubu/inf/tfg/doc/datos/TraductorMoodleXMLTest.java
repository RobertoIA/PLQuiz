package es.ubu.inf.tfg.doc.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorMoodleXML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

@SuppressWarnings("unused")
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
		String esperado = toString("TraductorVacio.xml"); //$NON-NLS-1$

		assertEquals("Generación incorrecta de documento Moodle XML.", //$NON-NLS-1$
				esperado, traductor.documento(new ArrayList<Plantilla>()));
	}
	
	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo construcción.
	 */
	@Test
	public void testTraduceAhoSethiUllmanConstruccion() { // throws IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUConstruccion.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUConstruccion(problema)
				.toString();

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />", //$NON-NLS-1$
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>", //$NON-NLS-1$
				"<file name=</file>"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$

		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorASUConstruccion_encontrado.kk"), "UTF8"))) { //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorASUConstruccion_esperado.kk"), "UTF8"))) { //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(esperado);
		}
		*/  //CGO
		
		

		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman subtipo construcción.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo árbol.
	 */
	@Test
	public void testTraduceAhoSethiUllmanEtiquetado() throws IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUEtiquetado.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUEtiquetado(problema).toString();

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />", //$NON-NLS-1$
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>", //$NON-NLS-1$
				"<file name=</file>"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$
		
		
		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorASUEtiquetado_encontrado.kk"), "UTF8"))) { //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorASUEtiquetado_esperado.kk"), "UTF8"))) { //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(esperado);
		}
		*/  //CGO
		
		
		

		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman subtipo etiquetado.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo tablas.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testTraduceAhoSethiUllmanTablas() { //throws IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUTablas.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUTablas(problema).toString();

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$

		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO
		
		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman subtipo tablas.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo construcción.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosConstruccion() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorCSConstruccion.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSConstruccion(problema)
				.toString();
		
		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />", //$NON-NLS-1$
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>", //$NON-NLS-1$
				"<file name=</file>"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$

		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos subtipo construcción.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo expresión.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosExpresion() { //throws IOException {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorCSExpresion.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSExpresion(problema).toString();

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:.*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$

		
		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorCSExpresion_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorMoodle_TraductorCSExpresion_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO
		

		
		
		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos subtipo expresión.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo autómata.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosAutomata() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorCSAutomata.xml"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSAutomata(problema).toString();

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />", //$NON-NLS-1$
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>", //$NON-NLS-1$
				"<file name=</file>"); //$NON-NLS-1$
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}", //$NON-NLS-1$
				"{1:MULTICHOICE:}"); //$NON-NLS-1$

		assertEquals(
				"Traducción Moodle XML incorrecta de problema de construcción de subconjuntos subtipo autómata.", //$NON-NLS-1$
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
		
		String languageFolder = Messages.getString("TraductorMoodleXMLTest.lang");  //$NON-NLS-1$
		fichero = languageFolder + fichero;

		try (InputStream entrada = getClass().getResourceAsStream(fichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) { //$NON-NLS-1$

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n"); //$NON-NLS-1$
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			fail("Error al abrir el archivo " + fichero); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}
}
