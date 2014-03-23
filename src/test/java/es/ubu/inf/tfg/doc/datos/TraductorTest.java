package es.ubu.inf.tfg.doc.datos;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TraductorTest {

	Traductor traductor;

	/**
	 * Los métodos formatoIntermedio y formatoFinal son identicos en todos los
	 * traductos, ya que heredan de la clase abstracta. Probamos con uno
	 * cualquiera.
	 */
	@Before
	public void setUp() throws Exception {
		traductor = new TraductorHTML();

	}

	@After
	public void tearDown() throws Exception {
		traductor = null;
	}

	/**
	 * Comprueba que la conversión a forma intermedia escapa los caracteres
	 * correctos.
	 */
	@Test
	public void testFormatoIntermedio() {
		String original = "{ contenido entre llaves } { <%0%> }";
		String intermedio = "\\'{\\' contenido entre llaves \\'}\\' \\'{\\' {0} \\'}\\'";

		assertEquals("Error convirtiendo plantilla a formato intermedio.",
				intermedio, traductor.formatoIntermedio(original));
	}

	/**
	 * Comprueba que la conversion desde forma intermedia da el resultado
	 * esperado. Asumimos que la forma intermedia ha sufrido ya una sustitución,
	 * lo cual modifica los caracteres escapados.
	 */
	@Test
	public void testFormatoFinal() {
		String intermedioSustituido = "\\{\\ contenido entre llaves \\}\\ \\{\\ {0} \\}\\";
		String resultado = "{ contenido entre llaves } { {0} }";

		assertEquals("Error convirtiendo plantilla a formato final.",
				resultado, traductor.formatoFinal(intermedioSustituido));
	}

}
