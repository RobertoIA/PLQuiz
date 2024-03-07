package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ExpresionRegularTest {

	/**
	 * Comprueba que el tipo de un nodo símbolo únicamente pueda ser símbolo.
	 */
	@Test
	public void testNodoSimboloTipo() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		assertTrue("Nodo símbolo no reconocido como tal.", nodo.esSimbolo());
		assertFalse("Nodo símbolo reconocido como otro tipo.", nodo.esVacio()
				|| nodo.esConcat() || nodo.esUnion() || nodo.esCierre());
	}

	/**
	 * Comprueba que el símbolo de un nodo símbolo es el correcto.
	 */
	@Test
	public void testNodoSimboloSimbolo() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		assertEquals("Símbolo incorrecto en nodo símbolo.", 'a', nodo.simbolo());
	}

	/**
	 * Comprueba que la posición de un nodo símbolo es la correcta.
	 */
	@Test
	public void testNodoSimboloPosicion() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		assertEquals("Posición incorrecta en nodo símbolo", 1, nodo.posicion());
	}

	/**
	 * Comprueba que un nodo símbolo no tenga hijo izquierdo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoSimboloHijoIzquierdo() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		nodo.hijoIzquierdo();
	}

	/**
	 * Comprueba que un nodo símbolo no tenga hijo derecho.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoSimboloHijoDerecho() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		nodo.hijoDerecho();
	}

	/**
	 * Comprueba la correcta impresión de un nodo símbolo.
	 */
	@Test
	public void testNodoSimboloString() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		assertEquals("Incorrecta impresión de nodo símbolo.", "a",
				nodo.toString());
	}

	/**
	 * Comprueba que el tipo de un nodo vacío únicamente pueda ser vacío.
	 */
	@Test
	public void testNodoVacioTipo() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		assertTrue("Nodo vacío no reconocido como tal.", nodo.esVacio());
		assertFalse("Nodo vacío reconocido como otro tipo.", nodo.esSimbolo()
				|| nodo.esConcat() || nodo.esUnion() || nodo.esCierre());
	}

	/**
	 * Comprueba que los nodos vacíos no tengan símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoVacioSimbolo() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.simbolo();
	}

	/**
	 * Comprueba que los nodos vacíos no tengan posición.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoVacioPosicion() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.posicion();
	}

	/**
	 * Comprueba que los nodos vacíos no tengan hijo izquierdo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoVacioHijoIzquierdo() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.hijoIzquierdo();
	}

	/**
	 * Comprueba que los nodos vacíos no tengan hijo derecho.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoVacioHijoDerecho() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.hijoDerecho();
	}

	/**
	 * Comprueba la correcta impresión de un nodo vacío.
	 */
	@Test
	public void testNodoVacioString() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();
		
		/*
		System.out.println(nodo.toString());
		*/
		
		assertEquals("Incorrecta impresión de nodo vacío.", "\u03B5",
				nodo.toString());
	}

	/**
	 * Comprueba que el tipo de un nodo aumentado únicamente pueda ser símbolo.
	 */
	@Test
	public void testNodoAumentadoTipo() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		assertTrue("Nodo aumentado no reconocido como símbolo.",
				nodo.esSimbolo());
		assertFalse(
				"Nodo aumentado reconocido como otro tipo distinto a símbolo.",
				nodo.esVacio() || nodo.esConcat() || nodo.esUnion()
						|| nodo.esCierre());
	}

	/**
	 * Comprueba que el símbolo de un nodo aumentado sea '$'.
	 */
	@Test
	public void testNodoAumentadoSimbolo() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		assertEquals("Símbolo incorrecto en nodo aumentado.", '$',
				nodo.simbolo());
	}

	/**
	 * Comprueba que la posición de un nodo aumentado sea la correcta.
	 */
	@Test
	public void testNodoAumentadoPosicion() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		assertEquals("Posición incorrecta en nodo aumentado", 1,
				nodo.posicion());
	}

	/**
	 * Comprueba que un nodo aumentado no tenga hijo izquierdo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoAumentadoHijoIzquierdo() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		nodo.hijoIzquierdo();
	}

	/**
	 * Comprueba que un nodo aumentado no tenga hijo izquierdo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoAumentadoHijoDerecho() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		nodo.hijoDerecho();
	}

	/**
	 * Comprueba la correcta impresión de un nodo aumentado.
	 */
	@Test
	public void testNodoAumentadoString() {
		ExpresionRegular nodo = ExpresionRegular.nodoAumentado(1);

		assertEquals("Incorrecta impresión de nodo aumentado.", "$",
				nodo.toString());
	}

	/**
	 * Comprueba que el tipo de un nodo concatenación únicamente pueda ser
	 * concatenación.
	 */
	@Test
	public void testNodoConcatTipo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		assertTrue("Nodo concatenación no reconocido como tal.",
				nodo.esConcat());
		assertFalse(
				"Nodo concatenación reconocido como otro tipo.",
				nodo.esSimbolo() || nodo.esVacio() || nodo.esUnion()
						|| nodo.esCierre());
	}

	/**
	 * Comprueba que los nodos concatenación no tengan símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoConcatSimbolo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		nodo.simbolo();
	}

	/**
	 * Comprueba que los nodos concatenación no tengan posición.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoConcatPosicion() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		nodo.posicion();
	}

	/**
	 * Comprueba que los nodos concatenación tengan el hijo izquierdo correcto.
	 */
	@Test
	public void testNodoConcatHijoIzquierdo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Hijo izquierdo incorrecto en nodo concatenación",
				hijoIzquierdo, nodo.hijoIzquierdo());
	}

	/**
	 * Comprueba que los nodos concatenación tengan el hijo derecho correcto.
	 */
	@Test
	public void testNodoConcatHijoDerecho() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Hijo derecho incorrecto en nodo concatenación",
				hijoDerecho, nodo.hijoDerecho());
	}

	/**
	 * Comprueba la correcta impresión de un nodo concatenación.
	 */
	@Test
	public void testNodoConcatString() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoSimbolo(2, 'b');
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoIzquierdo, hijoDerecho);
		
		/*
		System.out.println(nodo.toString());
		*/
		

		assertEquals("Incorrecta impresión de nodo concatenación.",
				"a·b", nodo.toString());
	}

	/**
	 * Comprueba que el tipo de un nodo unión únicamente pueda ser unión.
	 */
	@Test
	public void testNodoUnionTipo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		assertTrue("Nodo unión no reconocido como tal.", nodo.esUnion());
		assertFalse("Nodo unión reconocido como otro tipo.", nodo.esSimbolo()
				|| nodo.esVacio() || nodo.esConcat() || nodo.esCierre());
	}

	/**
	 * Comprueba que los nodos unión no tengan símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoUnionSimbolo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		nodo.simbolo();
	}

	/**
	 * Comprueba que los nodos unión no tengan posición.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoUnionPosicion() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		nodo.posicion();
	}

	/**
	 * Comprueba que los nodos unión tengan el hijo izquierdo correcto.
	 */
	@Test
	public void testNodoUnionHijoIzquierdo() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Hijo izquierdo incorrecto en nodo unión", hijoIzquierdo,
				nodo.hijoIzquierdo());
	}

	/**
	 * Comprueba que los nodos unión tengan el hijo derecho correcto.
	 */
	@Test
	public void testNodoUnionHijoDerecho() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoVacio();
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Hijo derecho incorrecto en nodo unión", hijoDerecho,
				nodo.hijoDerecho());
	}

	/**
	 * Comprueba la correcta impresión de un nodo unión.
	 */
	@Test
	public void testNodoUnionString() {
		ExpresionRegular hijoIzquierdo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular hijoDerecho = ExpresionRegular.nodoSimbolo(2, 'b');
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoIzquierdo, hijoDerecho);
		
		/*
		System.out.println(nodo.toString());
		*/
		
		assertEquals("Incorrecta impresión de nodo unión.", "a|b",
				nodo.toString());
	}

	/**
	 * Comprueba que el tipo de un nodo cierre únicamente pueda ser cierre.
	 */
	@Test
	public void testNodoCierreTipo() {
		ExpresionRegular hijo = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		assertTrue("Nodo cierre no reconocido como tal.", nodo.esCierre());
		assertFalse("Nodo cierre reconocido como otro tipo.", nodo.esSimbolo()
				|| nodo.esVacio() || nodo.esConcat() || nodo.esUnion());
	}

	/**
	 * Comprueba que los nodos cierre no tengan símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoCierreSimbolo() {
		ExpresionRegular hijo = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		nodo.simbolo();
	}

	/**
	 * Comprueba que los nodos cierre no tengan posición.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoCierrePosicion() {
		ExpresionRegular hijo = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		nodo.posicion();
	}

	/**
	 * Comprueba que los nodos cierre tengan el hijo correcto.
	 */
	@Test
	public void testNodoCierreHijoIzquierdo() {
		ExpresionRegular hijo = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		assertEquals("Hijo incorrecto en nodo cierre", hijo,
				nodo.hijoIzquierdo());
	}

	/**
	 * Comprueba que los nodos cierre solo tengan un hijo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNodoCierreHijoDerecho() {
		ExpresionRegular hijo = ExpresionRegular.nodoVacio();
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		nodo.hijoDerecho();
	}

	/**
	 * Comprueba la correcta impresión de un nodo cierre.
	 */
	@Test
	public void testNodoCierreString() {
		ExpresionRegular hijo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(hijo);

		assertEquals("Incorrecta impresión de nodo cierre.", "a*",
				nodo.toString());
	}

	/**
	 * Comprueba que los nodos se lista correctamente.
	 */
	@Test
	public void testNodos() {
		ExpresionRegular nodoA = ExpresionRegular.nodoSimbolo(1, 'a');      // a
		ExpresionRegular nodoB = ExpresionRegular.nodoCierre(nodoA);        // a*
		ExpresionRegular nodoC = ExpresionRegular.nodoSimbolo(2, 'b');      // b
		ExpresionRegular nodoD = ExpresionRegular.nodoConcat(nodoB, nodoC); // a*.b
		ExpresionRegular nodoE = ExpresionRegular.nodoVacio();              // E
		ExpresionRegular nodoF = ExpresionRegular.nodoUnion(nodoD, nodoE);  // a*.b|E

		List<ExpresionRegular> nodos = new ArrayList<>();
		nodos.add(nodoF);
		nodos.add(nodoE);
		nodos.add(nodoD);
		nodos.add(nodoC);
		nodos.add(nodoB);
		nodos.add(nodoA);
		
		/*
		System.out.println(nodos);
		System.out.println(nodoF.nodos());
		*/

		assertEquals("Listado de nodos incorrecto.", nodos, nodoF.nodos());
	}

	/**
	 * Comprueba el correcto cálculo de la profundidad de un nodo.
	 */
	@Test
	public void testProfundidad() {
		ExpresionRegular hijo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular padre = ExpresionRegular.nodoCierre(hijo);
		ExpresionRegular abuelo = ExpresionRegular.nodoConcat(padre,
				ExpresionRegular.nodoVacio());

		assertEquals("Error calculando la profundidad del árbol", 0,
				hijo.profundidad());
		assertEquals("Error calculando la profundidad del árbol", 1,
				padre.profundidad());
		assertEquals("Error calculando la profundidad del árbol", 2,
				abuelo.profundidad());
	}

	//CGO added these	
	/**
	 * Comprueba que la expresión regular se imprime solo con los paréntesis indispensables
	 */
	@Test
	public void testParentheses1() {
		ExpresionRegular nodoA1 = ExpresionRegular.nodoSimbolo(1, 'a');                 // a

		ExpresionRegular nodoA1estrella = ExpresionRegular.nodoCierre(nodoA1);          // a*
		ExpresionRegular nodoB = ExpresionRegular.nodoSimbolo(2, 'b');                  // b
		ExpresionRegular nodoBandA1estrella = ExpresionRegular.nodoConcat(
				nodoA1estrella, nodoB);                                                 // a*.b
		ExpresionRegular nodoE = ExpresionRegular.nodoVacio();
		ExpresionRegular nodoEorBandA1estrella = ExpresionRegular.nodoCierre(
				ExpresionRegular.nodoUnion(nodoE, nodoBandA1estrella));                 // E|a*.b
		ExpresionRegular nodoC = ExpresionRegular.nodoSimbolo(3, 'c');                  // c
		ExpresionRegular nodoA4 = ExpresionRegular.nodoSimbolo(4, 'a');                 // a
		ExpresionRegular nodoDollar = ExpresionRegular.nodoAumentado(5);                // $		
		ExpresionRegular nodoCandIEorBandA1estrellaD = ExpresionRegular.nodoConcat(
				nodoEorBandA1estrella, nodoC);                                          // (E|a*.b).c
		ExpresionRegular nodoCandIEorBandA1estrellaDorA4 = ExpresionRegular.nodoUnion(
				nodoCandIEorBandA1estrellaD, nodoA4);                                   // (E|a*.b).c|a
		ExpresionRegular nodoER = ExpresionRegular.nodoConcat(
				nodoCandIEorBandA1estrellaDorA4, nodoDollar);                           // ((E|a*.b).c|a).$
		

		/*
		System.out.print("nodoER: ");
		System.out.println(nodoER);
		System.out.println(nodoER.toString2());
		*/
		

		assertEquals("Problemas en la parentización de la expresión regular.", nodoER.toString(), "((\u03B5|a*·b)*·c|a)·$");
	}

	//CGO added these	
	/**
	 * Comprueba que la expresión regular se imprime solo con los paréntesis indispensables
	 */
	@Test
	public void testParentheses2() {
		ExpresionRegular nodoA = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular nodoB = ExpresionRegular.nodoSimbolo(2, 'b');
		ExpresionRegular nodoC = ExpresionRegular.nodoSimbolo(3, 'c');
		ExpresionRegular nodoD = ExpresionRegular.nodoSimbolo(4, 'd');
		ExpresionRegular nodoE = ExpresionRegular.nodoSimbolo(5, 'e');

		ExpresionRegular nodoBC = ExpresionRegular.nodoConcat(nodoB, nodoC);
		ExpresionRegular nodoABC = ExpresionRegular.nodoUnion(nodoA, nodoBC);
		ExpresionRegular nodoDE = ExpresionRegular.nodoUnion(nodoD, nodoE);

		ExpresionRegular nodoABCDE = ExpresionRegular.nodoConcat(nodoABC, nodoDE);
		
		/*
		System.out.print("nodoABCDE: ");
		System.out.println(nodoABCDE);
		System.out.println(nodoABCDE.toString2());	
		*/

		// Ahora es "(a|b·c)·(d|e)", en vez de "((a|(b·c))·(d|e))"
		
		assertEquals("Problemas en la parentización de la expresión regular.", nodoABCDE.toString2(), "(a|b·c)·(d|e)");
	}

	//CGO added these	
	/**
	 * Comprueba que la expresión regular se imprime solo con los paréntesis indispensables
	 */
	@Test
	public void testParentheses3() {
		ExpresionRegular nodoA = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular nodoB = ExpresionRegular.nodoSimbolo(2, 'b');
		ExpresionRegular nodoC = ExpresionRegular.nodoSimbolo(3, 'c');
		ExpresionRegular nodoD = ExpresionRegular.nodoSimbolo(4, 'd');
		ExpresionRegular nodoE = ExpresionRegular.nodoSimbolo(5, 'e');

		ExpresionRegular nodoBC = ExpresionRegular.nodoConcat(nodoB, nodoC);
		ExpresionRegular nodoABC = ExpresionRegular.nodoConcat(nodoA, nodoBC);
		ExpresionRegular nodoDE = ExpresionRegular.nodoUnion(nodoD, nodoE);
		ExpresionRegular nodoDEa = ExpresionRegular.nodoCierre(nodoDE);
		

		ExpresionRegular nodoABCDE = ExpresionRegular.nodoConcat(nodoABC, nodoDEa);
		
		/*
		System.out.print("nodoABCDE: ");
		System.out.println(nodoABCDE);
		System.out.println(nodoABCDE.toString2());	
		*/

		// Ahora es "a·(b·c)·(d|e)*", en vez de "((a·(b·c))·(d|e)*)"
		assertEquals("Problemas en la parentización de la expresión regular.", nodoABCDE.toString2(), "a·(b·c)·(d|e)*");
	}

}
