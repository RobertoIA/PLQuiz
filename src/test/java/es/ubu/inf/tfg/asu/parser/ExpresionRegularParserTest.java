package es.ubu.inf.tfg.asu.parser;

import static org.junit.Assert.assertEquals;

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
		CharStream input = new JavaCharStream(new StringReader(problema));

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
		CharStream input = new JavaCharStream(new StringReader(problema));

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
		CharStream input = new JavaCharStream(new StringReader(problema));

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
		CharStream input = new JavaCharStream(new StringReader(problema));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		ExpresionRegular resultado = parser.expresion();

		assertEquals("Parser produce expresion incorrecta.", expresion,
				resultado);
	}

	/**
	 * Comprueba que el parser es capaz de procesar correctamente la salida de
	 * una expresión existente.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testUnicode() throws ParseException {
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

		// Utilizamos el hijo izquierdo porque nos interesa la expresión
		// original, sin aumentar.
		CharStream input = new JavaCharStream(new StringReader(expresion
				.hijoIzquierdo().toString() + '\n'));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		ExpresionRegular resultado = parser.expresion();

		assertEquals("" + expresion.toString() + " " + resultado.toString(),
				expresion.toString(), resultado.toString());
	}
}
