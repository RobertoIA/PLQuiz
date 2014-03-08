package es.ubu.inf.tfg.asu.parser;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;

public class ExpresionRegularParserTest {

	/**
	 * Comprueba que las cadenas que no terminan en salto de linea son no
	 * validas, aunque por lo demás sean correctas.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test(expected = ParseException.class)
	public void testNoNewline() throws ParseException {
		String problema = "ab*";
		Reader input = new StringReader(problema);

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		parser.expresion();
	}

	/**
	 * Comprueba que las expresiones no validas producen un error en el parser.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test(expected = ParseException.class)
	public void testParseException() throws ParseException {
		String problema = "a**\n";
		Reader input = new StringReader(problema);

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		parser.expresion();
	}

	/**
	 * Comprueba que las expresiones con caracteres no validas producen un error
	 * en el token manager.
	 * 
	 * @throws Exception
	 *             Error del token manager o error del parser.
	 */
	@Test(expected = TokenMgrError.class)
	public void testTokenMgrError() throws Exception {
		String problema = "ab*;\n";
		Reader input = new StringReader(problema);

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		parser.expresion();
	}

	/**
	 * Comprueba que el parser produce la expresion regular correcta,
	 * comparándola con una construida a mano.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testExpresionParse() throws ParseException {
		ExpresionRegular expresion; // ((a|b*)a*c)*
		expresion = ExpresionRegular.nodoSimbolo(1, 'a');
		expresion = ExpresionRegular.nodoUnion(ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(2, 'b')), expresion);
		expresion = ExpresionRegular.nodoConcat(ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(3, 'a')), expresion);
		expresion = ExpresionRegular.nodoConcat(
				ExpresionRegular.nodoSimbolo(4, 'c'), expresion);
		expresion = ExpresionRegular.nodoCierre(expresion);
		expresion = ExpresionRegular.nodoConcat(
				ExpresionRegular.nodoAumentado(5), expresion);

		String problema = "((a|b*)a*c)*\n";
		Reader input = new StringReader(problema);

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		ExpresionRegular resultado = parser.expresion();

		assertEquals(expresion, resultado);
	}
}
