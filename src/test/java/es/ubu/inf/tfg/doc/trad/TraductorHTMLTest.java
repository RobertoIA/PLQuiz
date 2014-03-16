package es.ubu.inf.tfg.doc.trad;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorHTML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

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
		String esperado = "<html><head><meta content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body></body></html>";

		assertEquals("Generación incorrecta de documento HTML.", esperado,
				traductor.documento(new ArrayList<String>()));
	}

	/**
	 * Comprueba la correcta traducción de un problema de tipo Aho-Sethi-Ullman.
	 */
	@Test
	public void testTraduceAhoSethiUllman() {
		AhoSethiUllman problema = new AhoSethiUllman("((a|b*)a*c)*");
		String esperado = "Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</b></p><p>Expresión aumentada: ((((a|b*)\u2027a*)\u2027c)*\u2027$)</p><p><table border=\"1\"><tr><th>n</th><th>stePos(n)</th></tr><tr><td>1</td><td>3, 4</td></tr><tr><td>2</td><td>2, 3, 4</td></tr><tr><td>3</td><td>3, 4</td></tr><tr><td>4</td><td>1, 2, 3, 4, 5</td></tr><tr><td>5</td><td>-</td></tr></table></p><p><table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>B</td><td>C</td><td>A</td><td>1 2 3 4 5 </td></tr><tr><td>B</td><td>B</td><td>D</td><td>A</td><td>3 4 </td></tr><tr><td>C</td><td>B</td><td>C</td><td>A</td><td>2 3 4 </td></tr><tr><td>D</td><td>D</td><td>D</td><td>D</td><td></td></tr></table></p>";

		assertEquals("Traducción HTML incorrecta de problema AhoSethiUllman.",
				esperado, traductor.traduce(problema));
	}

}
