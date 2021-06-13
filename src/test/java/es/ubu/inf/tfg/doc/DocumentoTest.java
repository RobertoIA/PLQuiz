package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

@SuppressWarnings("unused")
public class DocumentoTest {

	@Rule
	public TemporaryFolder directorioTemporal = new TemporaryFolder();

	Documento documento;

	String problemaA = "((a|b*)a*c)*";
	String problemaB = "(a|b*)c*a";
	String problemaC = "(a|b)*a(a|b)(a|b)";

	AhoSethiUllman asuProblemaA;
	AhoSethiUllman asuProblemaB;
	AhoSethiUllman asuProblemaC;

	ConstruccionSubconjuntos csProblemaA;
	ConstruccionSubconjuntos csProblemaB;
	ConstruccionSubconjuntos csProblemaC;

	@Before
	public void setUp() throws Exception {
		documento = new Documento();

		asuProblemaA = new AhoSethiUllman(problemaA);
		asuProblemaB = new AhoSethiUllman(problemaB);
		asuProblemaC = new AhoSethiUllman(problemaC);

		csProblemaA = new ConstruccionSubconjuntos(problemaA);
		csProblemaB = new ConstruccionSubconjuntos(problemaB);
		csProblemaC = new ConstruccionSubconjuntos(problemaC);
	}

	@After
	public void tearDown() throws Exception {
		documento = null;

		asuProblemaA = null;
		asuProblemaB = null;
		asuProblemaC = null;
	}

	/**
	 * Comprueba que se generan correctamente documentos vacíos.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testVacio() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		esperado = toString("vacio.html");
		encontrado = documento.vistaPrevia();

		assertEquals("Vista previa de documento vacío errónea.", esperado,
				encontrado);

		// Fichero XML
		esperado = toString("vacio.xml");
		ficheroTemporal = ficheroTemporal("vacio.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		assertEquals("Exportación de documento XML vacío errónea.", esperado,
				encontrado);

		// Fichero Latex
		esperado = toString("vacio.tex");
		ficheroTemporal = ficheroTemporal("vacio.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		assertEquals("Exportación de documento Latex vacío errónea.", esperado,
				encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas Aho-Sethi-Ullman subtipo
	 * construcción correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarASUConstruccion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.asuConstruccion(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarASUConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo construcción a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarASUConstruccion.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo construcción a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarASUConstruccion.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo construcción a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas Aho-Sethi-Ullman subtipo
	 * etiquetado correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarASUEtiquetado() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarASUEtiquetado.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarASUEtiquetado.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarASUEtiquetado.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas Aho-Sethi-Ullman subtipo tablas
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarASUTablas() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.asuTablas(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuTablas(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuTablas(asuProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarASUTablas.html");
		encontrado = documento.vistaPrevia();
		
		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo tablas a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarASUTablas.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo tablas a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarASUTablas.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		
		assertEquals(
				"Añadido erróneo de problemas Aho-Sethi-Ullman subtipo tablas a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman subtipo construcción
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASUConstruccion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaC, 3));
		documento.eliminarProblema(Problema.asuConstruccion(asuProblemaC, 3));

		esperado = toString("eliminarASUConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo construcción en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASUConstruccion.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo construcción en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASUConstruccion.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");
		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo construcción en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman subtipo etiquetado
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASUEtiquetado() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaC, 3));
		documento.eliminarProblema(Problema.asuEtiquetado(asuProblemaC, 3));

		esperado = toString("eliminarASUEtiquetado.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASUEtiquetado.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASUEtiquetado.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo etiquetado en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas Aho-Sethi-Ullman subtipo tablas
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarASUTablas() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuTablas(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuTablas(asuProblemaB, 2));
		documento.añadirProblema(Problema.asuTablas(asuProblemaC, 3));
		documento.eliminarProblema(Problema.asuTablas(asuProblemaC, 3));

		esperado = toString("eliminarASUTablas.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo tablas en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarASUTablas.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo tablas en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarASUTablas.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");
		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");

		assertEquals(
				"Borrado erróneo de problemas Aho-Sethi-Ullman subtipo tablas en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman subtipo
	 * construcción correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASUConstrucción() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuConstruccion(asuProblemaB, 2));
		documento.sustituirProblema(Problema.asuConstruccion(asuProblemaB, 2),
				Problema.asuConstruccion(asuProblemaC, 2));

		esperado = toString("sustituirASUConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo construcción en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASUConstruccion.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo construcción en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASUConstruccion.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo construcción en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman subtipo etiquetado
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASUEtiquetado() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuEtiquetado(asuProblemaB, 2));
		documento.sustituirProblema(Problema.asuEtiquetado(asuProblemaB, 2),
				Problema.asuEtiquetado(asuProblemaC, 2));

		esperado = toString("sustituirASUEtiquetado.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo etiquetado en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASUEtiquetado.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo etiquetado en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASUEtiquetado.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo etiquetado en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas Aho-Sethi-Ullman subtipo tablas
	 * correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirASUTablas() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.asuTablas(asuProblemaA, 1));
		documento.añadirProblema(Problema.asuTablas(asuProblemaB, 2));
		documento.sustituirProblema(Problema.asuTablas(asuProblemaB, 2),
				Problema.asuTablas(asuProblemaC, 2));

		esperado = toString("sustituirASUTablas.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo tablas en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirASUTablas.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo tablas en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirASUTablas.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");
		
		assertEquals(
				"Sustitución errónea de problemas Aho-Sethi-Ullman subtipo tablas en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas de construcción de subconjuntos
	 * subtipo construcción correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarCSConstruccion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.CSConstruccion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSConstruccion(csProblemaB, 2));
		documento.añadirProblema(Problema.CSConstruccion(csProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarCSConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo construcción a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarCSConstruccion.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo construcción a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarCSConstruccion.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}");
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");
		
		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo construcción a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcción de subconjuntos
	 * subtipo construcción correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCSConstruccion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSConstruccion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSConstruccion(csProblemaB, 2));
		documento.añadirProblema(Problema.CSConstruccion(csProblemaC, 3));
		documento.eliminarProblema(Problema.CSConstruccion(csProblemaC, 3));

		esperado = toString("eliminarCSConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo construcción en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCSConstruccion.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo construcción en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCSConstruccion.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo construcción en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcción de subconjuntos
	 * subtipo construcción correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCSConstruccion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSConstruccion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSConstruccion(csProblemaB, 2));
		documento.sustituirProblema(Problema.CSConstruccion(csProblemaB, 2),
				Problema.CSConstruccion(csProblemaC, 2));

		esperado = toString("sustituirCSConstruccion.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");
		encontrado = encontrado.replaceAll("<p>Solución:[^<]*</p>",
				"<p>Solución:</p>");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo construcción en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCSConstruccion.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo construcción en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCSConstruccion.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");
		
		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo construcción en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas de construcción de subconjuntos
	 * subtipo expresión correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.CSExpresion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSExpresion(csProblemaB, 2));
		documento.añadirProblema(Problema.CSExpresion(csProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo expresión a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo expresión a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo expresión a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcción de subconjuntos
	 * subtipo expresión correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSExpresion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSExpresion(csProblemaB, 2));
		documento.añadirProblema(Problema.CSExpresion(csProblemaC, 3));
		documento.eliminarProblema(Problema.CSExpresion(csProblemaC, 3));

		esperado = toString("eliminarCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo expresión en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo expresión en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo expresión en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcción de subconjuntos
	 * subtipo expresión correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCSExpresion() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSExpresion(csProblemaA, 1));
		documento.añadirProblema(Problema.CSExpresion(csProblemaB, 2));
		documento.sustituirProblema(Problema.CSExpresion(csProblemaB, 2),
				Problema.CSExpresion(csProblemaC, 2));

		esperado = toString("sustituirCSExpresion.html");
		encontrado = documento.vistaPrevia();

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo expresión en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCSExpresion.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo expresión en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCSExpresion.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);
		encontrado = encontrado.replaceAll("myincludegraphicssol\\{[0-9]+\\}", "myincludegraphicssol{}");
		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo expresión en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se incorporan problemas de construcción de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testIncorporarCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		documento.añadirProblema(Problema.CSAutomata(csProblemaA, 1));
		documento.añadirProblema(Problema.CSAutomata(csProblemaB, 2));
		documento.añadirProblema(Problema.CSAutomata(csProblemaC, 3));

		// Vista previa
		esperado = toString("incorporarCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo automata a vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("incorporarCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("incorporar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo automata a documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("incorporarCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("incorporar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}");

		assertEquals(
				"Añadido erróneo de problemas de construcción de subconjuntos subtipo automata a documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se eliminan problemas de construcción de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testEliminarCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSAutomata(csProblemaA, 1));
		documento.añadirProblema(Problema.CSAutomata(csProblemaB, 2));
		documento.añadirProblema(Problema.CSAutomata(csProblemaC, 3));
		documento.eliminarProblema(Problema.CSAutomata(csProblemaC, 3));

		esperado = toString("eliminarCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo automata en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("eliminarCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("eliminar.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("eliminarCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("eliminar.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		
		assertEquals(
				"Borrado erróneo de problemas de construcción de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);
	}

	/**
	 * Comprueba que se sustituyen problemas de construcción de subconjuntos
	 * subtipo automata correctamente.
	 * 
	 * @throws IOException
	 *             Error operando con archivos.
	 */
	@Test
	public void testSustituirCSAutomata() throws IOException {
		File ficheroTemporal;

		String esperado;
		String encontrado;

		// Vista previa
		documento.añadirProblema(Problema.CSAutomata(csProblemaA, 1));
		documento.añadirProblema(Problema.CSAutomata(csProblemaB, 2));
		documento.sustituirProblema(Problema.CSAutomata(csProblemaB, 2),
				Problema.CSAutomata(csProblemaC, 2));

		esperado = toString("sustituirCSAutomata.html");
		encontrado = documento.vistaPrevia();

		encontrado = encontrado
				.replaceAll("<img src=\".*\">", "<img src=\"\">");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo autómata en vista previa.",
				esperado, encontrado);

		// Fichero XML
		esperado = toString("sustituirCSAutomata.xml");
		ficheroTemporal = ficheroTemporal("sustituir.xml");

		documento.exportaXML(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll(
				"<img src=\"@@PLUGINFILE@@/[^.]*.jpg\" alt=\"\" />",
				"<img src=\"@@PLUGINFILE@@/.jpg\" alt=\"\" />");
		encontrado = encontrado.replaceAll("<file name=[^<]*</file>",
				"<file name=</file>");
		encontrado = encontrado.replaceAll("\\{1:MULTICHOICE:[^}]*\\}",
				"{1:MULTICHOICE:}");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo automata en documento XML exportado.",
				esperado, encontrado);

		// Fichero Latex
		esperado = toString("sustituirCSAutomata.tex");
		ficheroTemporal = ficheroTemporal("sustituir.tex");

		documento.exportaLatex(ficheroTemporal);
		encontrado = toString(ficheroTemporal);

		encontrado = encontrado.replaceAll("\\[width=90mm\\]\\{[^\\}]*\\}", "[width=90mm]{}");
		encontrado = encontrado.replaceAll("myincludegraphics\\{[0-9]+\\}", "myincludegraphics{}");

		assertEquals(
				"Sustitución errónea de problemas de construcción de subconjuntos subtipo automata en documento Latex exportado.",
				esperado, encontrado);
	}

	/**
	 * Genera un fichero temporal con el nombre dado. Los ficheros temporales
	 * desaparecen al finalizar los test.
	 * 
	 * @param nombre
	 *            Nombre del fichero a crear.
	 * @return Fichero temporal.
	 */
	private File ficheroTemporal(String nombre) {
		File fichero = null;
		try {
			fichero = directorioTemporal.newFile(nombre);
		} catch (IOException e) {
			fail("Error al crear el fichero temporal " + nombre);
		}
		return fichero;
	}

	/**
	 * Lee un fichero como una cadena de caracteres. Usado con ficheros
	 * temporales.
	 * 
	 * @param fichero
	 *            Fichero temporal a leer.
	 * @return Contenido del fichero.
	 */
	private String toString(File fichero) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = new FileInputStream(fichero);
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
			fail("Error al abrir el archivo temporal " + fichero.getName());
			return "";
		}
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
