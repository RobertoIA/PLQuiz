package es.ubu.inf.tfg.regex.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.StringReader;

import org.junit.Test;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

public class ExpresionRegularParserTest {

	/**
	 * Comprueba que un simple símbolo es considerado una expresión regular
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseSymbol() throws ParseException {
		String out;
		CharStream input = new JavaCharStream(new StringReader("a\n"));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		out = parser.expresion().toString();
		
		//System.out.println(out);
		
		assertEquals("Parser produce expresión incorrecta.", out, "a·$");
	}

	/**
	 * Comprueba que épsilon es considerado una expresión regular
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseEmpty() throws ParseException {
		String out;
		CharStream input = new JavaCharStream(new StringReader("E\n"));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		out = parser.expresion().toString();
		
		//System.out.println(out);
		
		assertEquals("Parser produce expresión incorrecta.", out, "\u03B5·$");
	}
	
	/**
	 * Comprueba que las cadenas que no terminan en salto de linea son no
	 * válidas, aunque por lo demás sean correctas.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test(expected = ParseException.class)
	public void testNoNewline() throws ParseException {
		String problema = "a.b*";
		CharStream input = new JavaCharStream(new StringReader(problema));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		parser.expresion();
	}

	/**
	 * Comprueba que se aplica correctamente la idempotencia de la cerradura
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseKleene() throws ParseException {
		CharStream input1 = new JavaCharStream(new StringReader("a***\n"));
		CharStream input2 = new JavaCharStream(new StringReader("a*\n"));

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		assertEquals("Parser produce expresión incorrecta.", parser1.expresion(),
				parser2.expresion());
	}

	/**
	 * Comprueba que se la selección preserva el orden de los operandos
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseSelect() throws ParseException {
		String out;
		CharStream input = new JavaCharStream(new StringReader("a|b\n"));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		out = parser.expresion().toString();
		
		//System.out.println(out);
		
		assertEquals("Parser produce expresión incorrecta.", out, "(a|b)·$");
	}

	/**
	 * Comprueba que se la concatenación preserva el orden de los operandos
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseConcat() throws ParseException {
		String out;
		CharStream input = new JavaCharStream(new StringReader("a.b\n"));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		out = parser.expresion().toString();
		
		//System.out.println(out);
		
		assertEquals("Parser produce expresión incorrecta.", out, "a·b·$");
	}

	/**
	 * Comprueba que se la concatenación sea opcional
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseOptionalConcat() throws ParseException {
		CharStream input1 = new JavaCharStream(new StringReader("a.b.c\n"));
		CharStream input2 = new JavaCharStream(new StringReader("(a(b))c\n"));
		String out1, out2;

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		out1 = parser1.expresion().toString();
		out2 = parser2.expresion().toString();
		
		assertEquals("Parser produce expresión incorrecta.", out1, out2);
	}
	
	/**
	 * Comprueba las precedencias de la selección y la concatenación
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseOptionalSelectConcat() throws ParseException {
		CharStream input1 = new JavaCharStream(new StringReader("a|b.c\n"));
		CharStream input2 = new JavaCharStream(new StringReader("a|(b.c)\n"));
		String out1, out2;

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		out1 = parser1.expresion().toString();
		out2 = parser2.expresion().toString();
		
		assertEquals("Parser produce expresión incorrecta.", out1, out2);
	}
	
	/**
	 * Comprueba las precedencias de la selección y el cierre
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseOptionalSelectKleene() throws ParseException {
		CharStream input1 = new JavaCharStream(new StringReader("a|b*\n"));
		CharStream input2 = new JavaCharStream(new StringReader("a|(b*)\n"));
		String out1, out2;

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		out1 = parser1.expresion().toString();
		out2 = parser2.expresion().toString();
		
		//System.out.println(out1);
		//System.out.println(out2);
		
		assertEquals("Parser produce expresión incorrecta.", out1, out2);
	}

	/**
	 * Comprueba las precedencias de la concatenación y el cierre
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseOptionalConcatKleene1() throws ParseException {
		CharStream input1 = new JavaCharStream(new StringReader("a.b*\n"));
		CharStream input2 = new JavaCharStream(new StringReader("a.(b)*\n"));
		String out1, out2;

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		out1 = parser1.expresion().toString();
		out2 = parser2.expresion().toString();
		
		//System.out.println(out1);
		//System.out.println(out2);
		
		assertEquals("Parser produce expresión incorrecta.", out1, out2);
	}

	/**
	 * Comprueba las precedencias de la concatenación y el cierre
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testParseOptionalConcatKleene2() throws ParseException {
		ExpresionRegular expr1, expr2;
		CharStream input1 = new JavaCharStream(new StringReader("a.b*\n"));
		CharStream input2 = new JavaCharStream(new StringReader("(a.b)*\n"));
		String out1, out2;

		ExpresionRegularParser parser1 = new ExpresionRegularParser(input1);
		ExpresionRegularParser parser2 = new ExpresionRegularParser(input2);
		
		expr1 = parser1.expresion();
		expr2 = parser2.expresion();
		
		out1 = expr1.toString();
		out2 = expr2.toString();

		//System.out.println(out1);
		//System.out.println(out2);
				
		assertEquals("Parser produce expresión incorrecta.", "(a·b)*·$", out2);
		assertFalse("Parser produce expresión incorrecta.", out1.equals(out2));
	}

	/**
	 * Comprueba que el parser produce la expresión regular correcta,
	 * comparándola con una construida a mano.
	 * 
	 * @throws ParseException
	 *             Error del parser.
	 */
	@Test
	public void testExpresionParse() throws ParseException {
		ExpresionRegular expresion; // ((a|b*)a*c)*
		expresion = ExpresionRegular.nodoSimbolo(1, 'a');                    // a
		expresion = ExpresionRegular.nodoUnion(expresion, ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(2, 'b')));          // a|b*
		expresion = ExpresionRegular.nodoConcat(expresion, ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(3, 'a')));          // (a|b*)a*
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoSimbolo(4, 'c'));                       // (a|b*)a*c
		expresion = ExpresionRegular.nodoCierre(expresion);                  // ((a|b*)a*c)*
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoAumentado(5));                          // ((a|b*)a*c)*$

		String problema = "((a|b*)a*c)*\n";
		CharStream input = new JavaCharStream(new StringReader(problema));

		ExpresionRegularParser parser = new ExpresionRegularParser(input);
		ExpresionRegular resultado = parser.expresion();
		
		//System.out.println(resultado.toString());
		//System.out.println(expresion.toString());

		assertEquals("Parser produce expresión incorrecta.", expresion, resultado);
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
		expresion = ExpresionRegular.nodoSimbolo(1, 'a');                      // a
		expresion = ExpresionRegular.nodoUnion(expresion, ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(2, 'b')));            // a|b*
		expresion = ExpresionRegular.nodoConcat(expresion, ExpresionRegular
				.nodoCierre(ExpresionRegular.nodoSimbolo(3, 'a')));            // (a|b*)a*
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoSimbolo(4, 'c'));                         // (a|b*)a*c
		expresion = ExpresionRegular.nodoCierre(expresion);
		expresion = ExpresionRegular.nodoConcat(expresion,                     // ((a|b*)a*c)*
				ExpresionRegular.nodoAumentado(5));                            // ((a|b*)a*c)*$

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