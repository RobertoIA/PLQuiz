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
import es.ubu.inf.tfg.doc.datos.TraductorLatex;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

@SuppressWarnings("unused")
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
		String esperado = toString("TraductorVacio.tex"); //$NON-NLS-1$
		String encontrado = traductor.documento(new ArrayList<Plantilla>());

		assertEquals("Generación incorrecta de documento Latex.", esperado, encontrado); //$NON-NLS-1$
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo construcción.
	 */
	@Test
	public void testTraduceAhoSethiUllmanConstruccion() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUConstruccion.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUConstruccion(problema).toString();

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}"); //$NON-NLS-1$ //$NON-NLS-2$
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo construcción.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo etiquetado.
	 */
	@Test
	public void testTraduceAhoSethiUllmanEtiquetado() { //throws IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUEtiquetado.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUEtiquetado(problema).toString();
		
		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}"); //$NON-NLS-1$ //$NON-NLS-2$
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}"); //$NON-NLS-1$ //$NON-NLS-2$

		
		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorASUEtiquetado_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorASUEtiquetado_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO
		
		

		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo etiquetado.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman
	 * subtipo tablas.
	 */
	@Test
	public void testTraduceAhoSethiUllmanTablas() { //throws IOException {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorASUTablas.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceASUTablas(problema).toString();
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}"); //$NON-NLS-1$ //$NON-NLS-2$

		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorASUTablas_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorASUTablas_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO


		assertEquals(
				"Traducción Latex incorrecta de problema AhoSethiUllman subtipo tablas.", //$NON-NLS-1$
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
		String esperado = toString("TraductorCSConstruccion.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSConstruccion(problema).toString();
		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}"); //$NON-NLS-1$ //$NON-NLS-2$
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo construcción.", //$NON-NLS-1$
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
		String esperado = toString("TraductorCSExpresion.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSExpresion(problema).toString();
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}"); //$NON-NLS-1$ //$NON-NLS-2$

		
		
		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorCSExpresion_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorCSExpresion_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO
		
		

		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo expresión.", //$NON-NLS-1$
				esperado, encontrado);
	}

	/**
	 * Comprueba la correcta traducción de un problema de construcción de
	 * subconjuntos subtipo autómata.
	 */
	@Test
	public void testTraduceConstruccionSubconjuntosAutomata() { //throws IOException {
		ConstruccionSubconjuntos problema = new ConstruccionSubconjuntos("((a|b*)a*c)*"); //$NON-NLS-1$
		String esperado = toString("TraductorCSAutomata.tex"); //$NON-NLS-1$
		String encontrado = traductor.traduceCSAutomata(problema).toString();

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}"); //$NON-NLS-1$ //$NON-NLS-2$
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}"); //$NON-NLS-1$

		
		
		/* CGO
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorCSAutomata_encontrado.kk"), "UTF8"))) {
			writer.write(encontrado);
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("TraductorLatex_TraductorCSAutomata_esperado.kk"), "UTF8"))) {
			writer.write(esperado);
		}
		*/  //CGO
		
		

		assertEquals(
				"Traducción Latex incorrecta de problema de construcción de subconjuntos subtipo autómata.", //$NON-NLS-1$
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
		
		String languageFolder = Messages.getString("TraductorLatexTest.lang"); //$NON-NLS-1$
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
