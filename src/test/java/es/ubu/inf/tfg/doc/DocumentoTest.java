package es.ubu.inf.tfg.doc;

import static org.junit.Assert.*;

import org.junit.Test;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

public class DocumentoTest {

	/**
	 * Comprueba que los documentos HTML vacios se crean correctamente.
	 */
	@Test
	public void testDocumentoHTML() {
		Documento documento = Documento.DocumentoHTML();
		String contenido = "<html><head><meta content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body></body></html>";

		assertEquals("Creación incorrecta de documento HTML vacío.", contenido,
				documento.toString());
	}

	/**
	 * Comprueba que los documentos Moodle XML vacios se crean correctamente.
	 */
	@Test
	public void testDocumentoMoodleXML() {
		Documento documento = Documento.DocumentoMoodleXML();
		String contenido = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz></quiz>";

		assertEquals("Creación incorrecta de documento Moodle XML vacío.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos HTML con un problema AhoSethiUllman añadido
	 * se crean correctamente.
	 */
	@Test
	public void testHTMLAñadirProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoHTML();
		String contenido = "<html><head><meta content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body><p><b>1.- Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</b></p><p>Expresión aumentada: ((((a|b*)\u2027a*)\u2027c)*\u2027$)</p><p><table border=\"1\"><tr><th>n</th><th>stePos(n)</th></tr><tr><td>1</td><td>3, 4</td></tr><tr><td>2</td><td>2, 3, 4</td></tr><tr><td>3</td><td>3, 4</td></tr><tr><td>4</td><td>1, 2, 3, 4, 5</td></tr><tr><td>5</td><td>-</td></tr></table></p><p><table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>B</td><td>C</td><td>A</td><td>1 2 3 4 5 </td></tr><tr><td>B</td><td>B</td><td>D</td><td>A</td><td>3 4 </td></tr><tr><td>C</td><td>B</td><td>C</td><td>A</td><td>2 3 4 </td></tr><tr><td>D</td><td>D</td><td>D</td><td>D</td><td></td></tr></table></p></body></html>";

		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);

		assertEquals(
				"Añadido incorrecto de problema AhoSethiUllman a documento HTML.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos HTML permiten eliminar problemas
	 * AhoSethiUllman correctamente.
	 */
	@Test
	public void testHTMLEliminarProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoHTML();
		String contenido = "<html><head><meta content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body></body></html>";

		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);
		documento.eliminarProblema(problema);

		assertEquals(
				"Eliminado incorrecto de problema AhoSethiUllman a documento HTML.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos Moodle XML con un problema AhoSethiUllman
	 * añadido se crean correctamente.
	 */
	@Test
	public void testMoodleXMLAñadirProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoMoodleXML();
		String contenido = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz><!-- question: 1  --><question type=\"cloze\"><name><text>PLQuiz</text></name><questiontext><text><![CDATA[<p>Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</p><p>Función de transición:<table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=1 2 3 4 5 #CORRECT~0#Falso}</td></tr><tr><td>B</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=3 4 #CORRECT~0#Falso}</td></tr><tr><td>C</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=2 3 4 #CORRECT~0#Falso}</td></tr><tr><td>D</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=Cjto. vacio#CORRECT~0#Falso}</td></tr></table></p>\nLos estados finales son:  {:MULTICHOICE:=A#Correct~X, Y, Z#Falso}]]></text></questiontext><generalfeedback><text></text></generalfeedback><shuffleanswers>0</shuffleanswers></question></quiz>";

		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);

		assertEquals(
				"Añadido incorrecto de problema AhoSethiUllman a documento Moodle XML.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos Moodle XML permiten eliminar problemas
	 * AhoSethiUllman correctamente.
	 */
	@Test
	public void testMoodleXMLEliminarProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoMoodleXML();
		String contenido = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz></quiz>";

		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);
		documento.eliminarProblema(problema);

		assertEquals(
				"Eliminado incorrecto de problema AhoSethiUllman a documento Moodle XML.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos HTML sustituyen problemas correctamente.
	 */
	@Test
	public void testHTMLSustituirProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoHTML();
		String contenido = "<html><head><meta content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body><p><b>1.- Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</b></p><p>Expresión aumentada: ((((a|b*)\u2027a*)\u2027c)*\u2027$)</p><p><table border=\"1\"><tr><th>n</th><th>stePos(n)</th></tr><tr><td>1</td><td>3, 4</td></tr><tr><td>2</td><td>2, 3, 4</td></tr><tr><td>3</td><td>3, 4</td></tr><tr><td>4</td><td>1, 2, 3, 4, 5</td></tr><tr><td>5</td><td>-</td></tr></table></p><p><table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>B</td><td>C</td><td>A</td><td>1 2 3 4 5 </td></tr><tr><td>B</td><td>B</td><td>D</td><td>A</td><td>3 4 </td></tr><tr><td>C</td><td>B</td><td>C</td><td>A</td><td>2 3 4 </td></tr><tr><td>D</td><td>D</td><td>D</td><td>D</td><td></td></tr></table></p></body></html>";

		AhoSethiUllman problema = new AhoSethiUllman("(a|b*)c*a");
		documento.añadirProblema(problema);

		AhoSethiUllman nuevo = new AhoSethiUllman("((a|b*)a*c)*");
		documento.sustituirProblema(problema, nuevo);

		assertEquals(
				"Sustitucíon incorrecta de problema AhoSethiUllman en documento HTML.",
				contenido, documento.toString());
	}

	/**
	 * Comprueba que los documentos Moodle XML sustituyen problemas
	 * correctamente.
	 */
	@Test
	public void testMoodleXMLSustituirProblemaAhoSethiUllman() {
		Documento documento = Documento.DocumentoMoodleXML();
		String contenido = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz><!-- question: 1  --><question type=\"cloze\"><name><text>PLQuiz</text></name><questiontext><text><![CDATA[<p>Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</p><p>Función de transición:<table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=1 2 3 4 5 #CORRECT~0#Falso}</td></tr><tr><td>B</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=3 4 #CORRECT~0#Falso}</td></tr><tr><td>C</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=2 3 4 #CORRECT~0#Falso}</td></tr><tr><td>D</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=Cjto. vacio#CORRECT~0#Falso}</td></tr></table></p>\nLos estados finales son:  {:MULTICHOICE:=A#Correct~X, Y, Z#Falso}]]></text></questiontext><generalfeedback><text></text></generalfeedback><shuffleanswers>0</shuffleanswers></question></quiz>";

		AhoSethiUllman problema = new AhoSethiUllman("(a|b*)c*a");
		documento.añadirProblema(problema);

		AhoSethiUllman nuevo = new AhoSethiUllman("((a|b*)a*c)*");
		documento.sustituirProblema(problema, nuevo);

		assertEquals(
				"Sustitucíon incorrecta de problema AhoSethiUllman en documento Moodle XML.",
				contenido, documento.toString());
	}
}
