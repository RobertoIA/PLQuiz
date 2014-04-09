package es.ubu.inf.tfg.regex.datos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
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
		ExpresionRegular nodo = ExpresionRegular.nodoConcat(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Incorrecta impresión de nodo concatenación.",
				"(a\u2027b)", nodo.toString());
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
		ExpresionRegular nodo = ExpresionRegular.nodoUnion(hijoDerecho,
				hijoIzquierdo);

		assertEquals("Incorrecta impresión de nodo unión.", "(a|b)",
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
	 * Comprueba que se realizan copias correctas.
	 */
	@Test
	public void testCopia() {
		ExpresionRegular expresion = ExpresionRegular.nodoSimbolo(1, 'a');
		expresion = ExpresionRegular.nodoConcat(expresion,
				ExpresionRegular.nodoVacio());
		expresion = ExpresionRegular.nodoCierre(expresion);

		ExpresionRegular copia = ExpresionRegular.copia(expresion);

		assertNotSame("Copia devuelve referencia al mismo objeto.", expresion,
				copia);
		assertNotSame("Copia en profundidad incorrecta.",
				expresion.hijoIzquierdo(), copia.hijoIzquierdo());
		assertEquals("Copia diferente al original.", expresion, copia);
		assertEquals("Copia en profundidad diferente al original.",
				expresion.hijoIzquierdo(), copia.hijoIzquierdo());
	}

	/**
	 * Comprueba que los nodos se lista correctamente.
	 */
	@Test
	public void testNodos() {
		ExpresionRegular nodoA = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular nodoB = ExpresionRegular.nodoCierre(nodoA);
		ExpresionRegular nodoC = ExpresionRegular.nodoSimbolo(2, 'b');
		ExpresionRegular nodoD = ExpresionRegular.nodoConcat(nodoB, nodoC);
		ExpresionRegular nodoE = ExpresionRegular.nodoVacio();
		ExpresionRegular nodoF = ExpresionRegular.nodoUnion(nodoD, nodoE);

		List<ExpresionRegular> nodos = new ArrayList<>();
		nodos.add(nodoF);
		nodos.add(nodoD);
		nodos.add(nodoB);
		nodos.add(nodoA);
		nodos.add(nodoC);
		nodos.add(nodoE);

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

	/**
	 * Comprueba que se sustituyen adecuadamente hijos derechos cuando sus tipos
	 * son correctos.
	 */
	@Test
	public void testNuevoHijoDerecho() {
		ExpresionRegular hijo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular padre = ExpresionRegular.nodoConcat(
				ExpresionRegular.nodoVacio(), ExpresionRegular.nodoVacio());

		padre.hijoDerecho(hijo);

		assertEquals("Error sustituyendo el hijo derecho.", hijo,
				padre.hijoDerecho());
	}

	/**
	 * Comprueba que se sustituyen adecuadamente hijos izquierdos cuando sus
	 * tipos son correctos.
	 */
	@Test
	public void testNuevoHijoIzquierdo() {
		ExpresionRegular hijo = ExpresionRegular.nodoSimbolo(1, 'a');
		ExpresionRegular padre = ExpresionRegular.nodoCierre(ExpresionRegular
				.nodoVacio());

		padre.hijoIzquierdo(hijo);

		assertEquals("Error sustituyendo el hijo izquierdo.", hijo,
				padre.hijoIzquierdo());
	}

	/**
	 * Comprueba que no se puede modificar el hijo izquierdo de un nodo símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNuevoHijoIzquierdoNodoSimbolo() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		nodo.hijoIzquierdo(ExpresionRegular.nodoVacio());
	}

	/**
	 * Comprueba que no se puede modificar el hijo izquierdo de un nodo vacío.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNuevoHijoIzquierdoNodoVacio() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.hijoIzquierdo(ExpresionRegular.nodoVacio());
	}

	/**
	 * Comprueba que no se puede modificar el hijo derecho de un nodo símbolo.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNuevoHijoDerechoNodoSimbolo() {
		ExpresionRegular nodo = ExpresionRegular.nodoSimbolo(1, 'a');

		nodo.hijoDerecho(ExpresionRegular.nodoVacio());
	}

	/**
	 * Comprueba que no se puede modificar el hijo derecho de un nodo vacío.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNuevoHijoDerechoNodoVacio() {
		ExpresionRegular nodo = ExpresionRegular.nodoVacio();

		nodo.hijoDerecho(ExpresionRegular.nodoVacio());
	}

	/**
	 * Comprueba que no se puede modificar el hijo derecho de un nodo cierre.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testNuevoHijoDerechoNodoCierre() {
		ExpresionRegular nodo = ExpresionRegular.nodoCierre(ExpresionRegular
				.nodoVacio());

		nodo.hijoDerecho(ExpresionRegular.nodoVacio());
	}
}
