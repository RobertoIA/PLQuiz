package es.ubu.inf.tfg.doc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public class DocumentoMoodleTest {

	Documento documento;

	@Before
	public void setUp() throws Exception {
		documento = new DocumentoMoodle();

	}

	@After
	public void tearDown() throws Exception {
		documento = null;
	}

	/**
	 * Comprueba que los problemas de tipo AhoSethiUllman se añaden
	 * correctamente al documento.
	 */
	@Test
	public void testAñadirProblemaASU() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);

		String esperado = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz><!-- question: 1  --><question type=\"cloze\"><name><text>PLQuiz</text></name><questiontext><text><![CDATA[<p>Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</p><p>Función de transición:<table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=1 2 3 4 5 #CORRECT~0#Falso}</td></tr><tr><td>B</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=3 4 #CORRECT~0#Falso}</td></tr><tr><td>C</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=2 3 4 #CORRECT~0#Falso}</td></tr><tr><td>D</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=Cjto. vacio#CORRECT~0#Falso}</td></tr></table></p>\nLos estados finales son:  {:MULTICHOICE:=A#Correct~X, Y, Z#Falso}]]></text></questiontext><generalfeedback><text></text></generalfeedback><shuffleanswers>0</shuffleanswers></question></quiz>";

		assertEquals("Genera problemas Aho-Sethi-Ullman erroneos.", esperado,
				documento.toString());
	}

	/**
	 * Comprueba que los problemas de tipo AhoSethiUllman se eliminan
	 * correctamente del documento.
	 */
	@Test
	public void testEliminarProblemaASU() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		documento.añadirProblema(problema);
		documento.eliminarProblema(problema);

		String esperado = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz></quiz>";

		assertEquals("Elimina problemas Aho-Sethi-Ullman incorrectamente",
				esperado, documento.toString());
	}
}
