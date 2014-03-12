package es.ubu.inf.tfg.doc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public class DocumentoHTMLTest {

	Documento documento;

	@Before
	public void setUp() throws Exception {
		documento = new DocumentoHTML();
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

		String esperado = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body><p><b>1.- Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ((a|b*)a*c)*</b></p><p>Expresión aumentada: ((((a|b*)\u2027a*)\u2027c)*\u2027$)</p><p><table border=\"1\"><tr><th>n</th><th>stePos(n)</th></tr><tr><td>1</td><td>3, 4</td></tr><tr><td>2</td><td>2, 3, 4</td></tr><tr><td>3</td><td>3, 4</td></tr><tr><td>4</td><td>1, 2, 3, 4, 5</td></tr><tr><td>5</td><td>-</td></tr></table></p><p><table border=\"1\"><tr><th></th><th>a</th><th>b</th><th>c</th><th></th></tr><tr><td>(A)</td><td>B</td><td>C</td><td>A</td><td>1 2 3 4 5 </td></tr><tr><td>B</td><td>B</td><td>D</td><td>A</td><td>3 4 </td></tr><tr><td>C</td><td>B</td><td>C</td><td>A</td><td>2 3 4 </td></tr><tr><td>D</td><td>D</td><td>D</td><td>D</td><td></td></tr></table></p></html></body>";

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

		String esperado = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body></html></body>";

		assertEquals("Elimina problemas Aho-Sethi-Ullman incorrectamente",
				esperado, documento.toString());
	}
}
