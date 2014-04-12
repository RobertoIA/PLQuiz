package es.ubu.inf.tfg.regex.datos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ExpresionRegular implementa un nodo de una expresión regular en forma de
 * árbol. Únicamente mantiene información acerca del tipo de nodo y referencias
 * a los nodos que cuelgan de él. Cualquier sub-árbol podría transplantarse de
 * expresión o utilizarse por separado sin encontrar inconsistencias. Admite los
 * siguientes tipos de nodo:
 * <ul>
 * <li>Nodo símbolo
 * <li>Nodo vacío
 * <li>Nodo concatenación
 * <li>Nodo unión
 * <li>Nodo cierre
 * </ul>
 * <p>
 * ExpresionRegular proporciona métodos estáticos builder que se encargan de la
 * creación de cada tipo de nodo. Incluye un método extra para construir el nodo
 * con el que se aumenta una expresión, que se trata de un nodo símbolo '$'.
 * <p>
 * A excepción de las referencias a sus hijos, las propiedades de un nodo son
 * inmutables.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ExpresionRegular {

	private static enum Tipo {
		SIMBOLO, VACIO, CONCAT, UNION, CIERRE
	}

	private final Tipo tipo;
	private final int posicion;
	private final char simbolo;

	private ExpresionRegular hijoIzquierdo;
	private ExpresionRegular hijoDerecho;

	private ExpresionRegular(Tipo tipo, int posicion, char simbolo,
			ExpresionRegular hijoDerecho, ExpresionRegular hijoIzquierdo) {
		this.tipo = tipo;
		this.posicion = posicion;
		this.simbolo = simbolo;

		this.hijoIzquierdo = hijoIzquierdo;
		this.hijoDerecho = hijoDerecho;
	}

	/**
	 * Construye y devuelve un nodo hoja de tipo símbolo, en la posición dada y
	 * conteniendo el caracter especificado.
	 * 
	 * @param posicion
	 *            Posición del nodo.
	 * @param simbolo
	 *            Símbolo contenido.
	 * @return Nodo símbolo.
	 */
	public static ExpresionRegular nodoSimbolo(int posicion, char simbolo) {
		return new ExpresionRegular(Tipo.SIMBOLO, posicion, simbolo, null, null);
	}

	/**
	 * Construye y devuelve un nodo hoja vacío.
	 * 
	 * @return Nodo vacío.
	 */
	public static ExpresionRegular nodoVacio() {
		return new ExpresionRegular(Tipo.VACIO, Integer.MIN_VALUE, '\u0000',
				null, null);
	}

	/**
	 * Construye y devuelve un nodo hoja de tipo símbolo en la posición dada,
	 * con el símbolo '$', y que se usa para aumentar una expresión. Esta
	 * implementación referencia al método {@link #nodoSimbolo(int, char)
	 * nodoSimbolo} para la creación del nodo.
	 * 
	 * @param posicion
	 *            Posicion del nodo.
	 * @return Nodo símbolo.
	 */
	public static ExpresionRegular nodoAumentado(int posicion) {
		return nodoSimbolo(posicion, '$');
	}

	/**
	 * Construye y devuelve un nodo concatenación del cual cuelgan los dos nodos
	 * hijo especificados.
	 * 
	 * @param hijoDerecho
	 *            Operando derecho en la concatenación.
	 * @param hijoIzquierdo
	 *            Operando izquierdo en la concatenación.
	 * @return Nodo concatenación.
	 */
	public static ExpresionRegular nodoConcat(ExpresionRegular hijoDerecho,
			ExpresionRegular hijoIzquierdo) {
		return new ExpresionRegular(Tipo.CONCAT, Integer.MIN_VALUE, '\u0000',
				hijoDerecho, hijoIzquierdo);
	}

	/**
	 * Construye y devuelve un nodo concatenación del cual cuelgan los dos nodos
	 * hijo especificados.
	 * 
	 * @param hijoDerecho
	 *            Operando derecho en la concatenación.
	 * @param hijoIzquierdo
	 *            Operando izquierdo en la concatenación.
	 * @return Nodo unión.
	 */
	public static ExpresionRegular nodoUnion(ExpresionRegular hijoDerecho,
			ExpresionRegular hijoIzquierdo) {
		return new ExpresionRegular(Tipo.UNION, Integer.MIN_VALUE, '\u0000',
				hijoDerecho, hijoIzquierdo);
	}

	/**
	 * Construye y devuelve un nodo cierre del cual cuelga el nodo hijo
	 * especificado.
	 * 
	 * @param hijo
	 *            Operando del cierre.
	 * @return Nodo cierre.
	 */
	public static ExpresionRegular nodoCierre(ExpresionRegular hijo) {
		return new ExpresionRegular(Tipo.CIERRE, Integer.MIN_VALUE, '\u0000',
				null, hijo);
	}

	/**
	 * Comprueba si el nodo es de tipo símbolo.
	 * 
	 * @return <code>true</code> si el nodo es de tipo símbolo,
	 *         <code>false</code> si no.
	 */
	public boolean esSimbolo() {
		return this.tipo == Tipo.SIMBOLO;
	}

	/**
	 * Comprueba si el nodo es de tipo vacío.
	 * 
	 * @return <code>true</code> si el nodo es de tipo vacío, <code>false</code>
	 *         si no.
	 */
	public boolean esVacio() {
		return this.tipo == Tipo.VACIO;
	}

	/**
	 * Comprueba si el nodo es de tipo concatenación.
	 * 
	 * @return <code>true</code> si el nodo es de tipo concatenación,
	 *         <code>false</code> si no.
	 */
	public boolean esConcat() {
		return this.tipo == Tipo.CONCAT;
	}

	/**
	 * Comprueba si el nodo es de tipo unión.
	 * 
	 * @return <code>true</code> si el nodo es de tipo unión, <code>false</code>
	 *         si no.
	 */
	public boolean esUnion() {
		return this.tipo == Tipo.UNION;
	}

	/**
	 * Comprueba si el nodo es de tipo cierre.
	 * 
	 * @return <code>true</code> si el nodo es de tipo cierre,
	 *         <code>false</code> si no.
	 */
	public boolean esCierre() {
		return this.tipo == Tipo.CIERRE;
	}

	/**
	 * Devuelve el símbolo asociado al nodo si el nodo es de tipo símbolo, lanza
	 * <code>UnsupportedOperationException</code> en caso contrario.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo no sea de tipo símbolo.
	 * 
	 * @return Símbolo asociado al nodo.
	 */
	public char simbolo() {
		if (!esSimbolo())
			throw new UnsupportedOperationException(
					"Solo los nodos símbolo tienen símbolo.");
		return this.simbolo;
	}

	/**
	 * Devuelve la posición del nodo si el nodo es de tipo símbolo, lanza
	 * <code>UnsupportedOperationException</code> en caso contrario.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo no sea de tipo símbolo.
	 * 
	 * @return Posición del nodo.
	 */
	public int posicion() {
		if (!esSimbolo())
			throw new UnsupportedOperationException(
					"Solo los nodos simbolo tienen posición");
		return this.posicion;
	}

	/**
	 * Devuelve el hijo izquierdo del nodo en caso de que lo tenga. Los nodos de
	 * tipo símbolo o vacío no tienen hijo izquierdo, y lanzan
	 * <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo sea de tipo símbolo o vacío.
	 * @return Referencia al operando izquierdo del nodo.
	 */
	public ExpresionRegular hijoIzquierdo() {
		if (esSimbolo() || esVacio())
			throw new UnsupportedOperationException("Los nodos " + this.tipo
					+ " no tienen hijo izquierdo.");
		return this.hijoIzquierdo;
	}

	/**
	 * Devuelve el hijo derecho del nodo en caso de que lo tenga. Los nodos de
	 * tipo símbolo, vacío o cierre no tienen hijo derecho, y lanzan
	 * <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo sea de tipo símbolo, vacío o cierre.
	 * @return Referencia al operando derecho del nodo.
	 */
	public ExpresionRegular hijoDerecho() {
		if (esSimbolo() || esVacio() || esCierre())
			throw new UnsupportedOperationException("Los nodos " + this.tipo
					+ " no tienen hijo derecho.");
		return this.hijoDerecho;
	}

	/**
	 * Devuelve la lista de nodos que constituyen la expresión.
	 * 
	 * @return Nodos que forman la expresión.
	 */
	public List<ExpresionRegular> nodos() {
		List<ExpresionRegular> nodos = new ArrayList<>();
		Stack<ExpresionRegular> pila = new Stack<>();
		pila.add(this);
		ExpresionRegular actual = this;

		while (!pila.isEmpty()) {
			nodos.add(actual);

			if (!actual.esSimbolo() && !actual.esVacio()) {
				pila.push(actual.hijoIzquierdo());

				if (!actual.esCierre())
					pila.push(actual.hijoDerecho());
			}

			actual = pila.pop();
		}

		return nodos;
	}

	/**
	 * Calcula la profundidad de un nodo dado, siendo 0 para los nodos hoja y
	 * creciendo hacia la raíz.
	 * 
	 * @return Profundidad del nodo.
	 */
	public int profundidad() {
		if (esSimbolo() || esVacio())
			return 0;
		else if (esCierre())
			return this.hijoIzquierdo.profundidad() + 1;
		else
			return Math.max(this.hijoIzquierdo.profundidad() + 1,
					this.hijoDerecho.profundidad() + 1);
	}

	/**
	 * Construye una representación de la expresión regular, utilizando
	 * caracteres especiales para representar las concatenaciones y los nodos
	 * vacíos.
	 * <p>
	 * Formato UTF-8.
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();

		switch (this.tipo) {
		case VACIO:
			string.append('\u03B5');
			break;
		case SIMBOLO:
			string.append(this.simbolo);
			break;
		case UNION:
			string.append('(');
			string.append(this.hijoIzquierdo);
			string.append('|');
			string.append(this.hijoDerecho);
			string.append(')');
			break;
		case CONCAT:
			string.append('(');
			string.append(this.hijoIzquierdo);
			string.append('\u2027');
			string.append(this.hijoDerecho);
			string.append(')');
			break;
		case CIERRE:
			string.append(this.hijoIzquierdo);
			string.append('*');
			break;
		default:
			break;
		}

		return string.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof ExpresionRegular))
			return false;

		// Consideramos dos expresiones iguales si las escribimos igual.
		if (!(o.toString().equals(toString())))
			return false;

		return true;
	}
}
