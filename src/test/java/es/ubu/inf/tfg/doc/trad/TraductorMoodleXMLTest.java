package es.ubu.inf.tfg.doc.trad;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

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
		String esperado = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz></quiz>";

		assertEquals("Generación incorrecta de documento Moodle XML.",
				esperado, traductor.documento(new ArrayList<String>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman.
	 */
	@Test
	public void testTraduceAhoSethiUllman() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = "<p>Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</p><p>Función de transición:<table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=1 2 3 4 5 #CORRECT~0#Falso}</td></tr><tr><td>B</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=3 4 #CORRECT~0#Falso}</td></tr><tr><td>C</td><td>{:MULTICHOICE:=B#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=C#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=A#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=2 3 4 #CORRECT~0#Falso}</td></tr><tr><td>D</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=D#CORRECT~Z#Falso}</td><td>{:MULTICHOICE:=Cjto. vacio#CORRECT~0#Falso}</td></tr></table></p>\nLos estados finales son:  {:MULTICHOICE:=A#Correct~X, Y, Z#Falso}";

		assertEquals(
				"Traducción Moodle XML incorrecta de problema AhoSethiUllman.",
				esperado, traductor.traduce(problema));
	}

}
