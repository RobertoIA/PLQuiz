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
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorHTML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

@SuppressWarnings("unused")
public class TraductorHTMLTest {

	Traductor traductor;

	@Before
	public void setUp() throws Exception {
		traductor = new TraductorHTML();
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
		String esperado = toString("TraductorVacio.html"); //$NON-NLS-1$

		assertEquals("Generación incorrecta de documento HTML.", esperado, //$NON-NLS-1$
				traductor.documento(new ArrayList<Plantilla>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo construcción.
	 */
	@Test
	public void testTraduceAhoSethiUllmanConstruccion() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUConstruccion.html"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUConstruccion(problema).toString();

		encontrado = encontrado.replaceAll("<img src=\".*\">", "<img src=\"\">"); //$NON-NLS-1$ //$NON-NLS-2$
		encontrado = encontrado.replaceAll("<p>" + Messages.getString("TraductorHTMLTest.solution") + "[^<]*</p>",	"<p>" + Messages.getString("TraductorHTMLTest.solution") + "</p>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-5$ //$NON-NLS-6$
		
		assertEquals(
				"Traducción HTML incorrecta de problema AhoSethiUllman subtipo construcción.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo etiquetado.
	 */
	@Test
	public void testTraduceAhoSethiUllmanEtiquetado() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUEtiquetado.html");
		String encontrado = traductor.traduceASUEtiquetado(problema).toString();

		encontrado = encontrado.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Traducción HTML incorrecta de problema AhoSethiUllman subtipo etiquetado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo tablas.
	 */
	@Test
	public void testTraduceAhoSethiUllmanTablas() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = toString("TraductorASUTablas.html");
		String encontrado = traductor.traduceASUTablas(problema).toString();

		
		
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorHTML_TraductorASUTablas_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorHTML_TraductorASUTablas_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		

		
		
		assertEquals(
				"Traducción HTML incorrecta de problema AhoSethiUllman subtipo tablas.",
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo construcción.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosConstrucción() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*");
		String esperado = toString("TraductorCSConstruccion.html");
		String encontrado = traductor.traduceCSConstruccion(problema).toString();

		encontrado = encontrado.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>" + Messages.getString("TraductorHTMLTest.solution") + "[^<]*</p>",	"<p>" + Messages.getString("TraductorHTMLTest.solution") + "</p>"); //$NON-NLS-2$ //$NON-NLS-5$ //$NON-NLS-6$

		assertEquals(
				"Traducción HTML incorrecta de problema de construcción de subconjuntos subtipo construcción.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo expresión.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosExpresion() {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos(
				"((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorCSExpresion.html"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSExpresion(problema).toString();

		assertEquals(
				"Traducción HTML incorrecta de problema de construcción de subconjuntos subtipo expresión.", //$NON-NLS-1$
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
		String esperado = toString("TraductorCSAutomata.html"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSAutomata(problema).toString();

		encontrado = encontrado.replaceAll("<img src=\".*\">", "<img src=\"\">"); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(
				"Traducción HTML incorrecta de problema de construcción de subconjuntos subtipo autómata.", //$NON-NLS-1$
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
		
		String languageFolder = Messages.getString("TraductorHTMLTest.lang");  //$NON-NLS-1$
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
