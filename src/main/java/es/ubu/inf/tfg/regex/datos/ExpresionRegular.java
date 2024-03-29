package es.ubu.inf.tfg.regex.datos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.SwingConstants;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

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
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ExpresionRegular {

	private static enum Tipo {
		SIMBOLO, VACIO, CONCAT, UNION, CIERRE
	}

	public final Tipo tipo;
	private final int posicion;
	private final char simbolo;

	public ExpresionRegular hijoIzquierdo;
	public ExpresionRegular hijoDerecho;

	private BufferedImage imagen;

	private ExpresionRegular(Tipo tipo, int posicion, char simbolo,
			ExpresionRegular hijoIzquierdo, ExpresionRegular hijoDerecho) {
		this.tipo = tipo;
		this.posicion = posicion;
		this.simbolo = simbolo;

		this.hijoIzquierdo = hijoIzquierdo;
		this.hijoDerecho = hijoDerecho;
	}

	/**
	 * Construye y devuelve un nodo hoja de tipo símbolo, en la posición dada y
	 * conteniendo el carácter especificado.
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
	 *            Posición del nodo.
	 * @return Nodo símbolo.
	 */
	public static ExpresionRegular nodoAumentado(int posicion) {
		return nodoSimbolo(posicion, '$');
	}

	/**
	 * Construye y devuelve un nodo concatenación del cual cuelgan los dos nodos
	 * hijo especificados.
	 * 
	 * @param hijoIzquierdo
	 *            Operando izquierdo en la concatenación.
	 * @param hijoDerecho
	 *            Operando derecho en la concatenación.
	 * @return Nodo concatenación.
	 */
	public static ExpresionRegular nodoConcat(ExpresionRegular hijoIzquierdo,
			ExpresionRegular hijoDerecho) {
		return new ExpresionRegular(Tipo.CONCAT, Integer.MIN_VALUE, '\u0000',
				hijoIzquierdo, hijoDerecho);
	}

	/**
	 * Construye y devuelve un nodo concatenación del cual cuelgan los dos nodos
	 * hijo especificados.
	 * 
	 * @param hijoIzquierdo
	 *            Operando izquierdo en la concatenación.
	 * @param hijoDerecho
	 *            Operando derecho en la concatenación.
	 * @return Nodo unión.
	 */
	public static ExpresionRegular nodoUnion(ExpresionRegular hijoIzquierdo,
			ExpresionRegular hijoDerecho) {
		return new ExpresionRegular(Tipo.UNION, Integer.MIN_VALUE, '\u0000',
				hijoIzquierdo, hijoDerecho);
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
				hijo, null);
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
					"Solo los nodos símbolo tienen posición");
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
	 * Dibuja el árbol de la expresión regular como grafo.
	 * 
	 * @return Imagen representando el árbol de la expresión regular.
	 */
	public BufferedImage imagen() {
		if (this.imagen == null) {
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<ExpresionRegular, Object> gNodos = new HashMap<>();
			ExpresionRegular actual;
			Object gNodo, gActual;
			List<ExpresionRegular> siguientes = new ArrayList<>();
			boolean tieneHijoIzquierdo, tieneHijoDerecho;

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;";
			String estiloEdge = "strokeColor=black;fontColor=black;labelBackgroundColor=white;endArrow=open;";

			graph.getModel().beginUpdate();
			try {
				siguientes.add(this);

				while (!siguientes.isEmpty()) {
					actual = siguientes.get(0);

					if (!gNodos.containsKey(actual)) {
						gActual = graph.insertVertex(parent, null,
								actual.tipo(), 0, 0, 30, 30, estiloVertex);
						gNodos.put(actual, gActual);
					} else {
						gActual = gNodos.get(actual);
					}

					tieneHijoIzquierdo = !actual.esSimbolo()
							&& !actual.esVacio();
					tieneHijoDerecho = tieneHijoIzquierdo && !actual.esCierre();

					if (tieneHijoIzquierdo) {
						siguientes.add(actual.hijoIzquierdo());
						gNodo = graph.insertVertex(parent, null, actual
								.hijoIzquierdo().tipo(), 0, 0, 30, 30,
								estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoIzquierdo(), gNodo);
					}

					if (tieneHijoDerecho) {
						siguientes.add(actual.hijoDerecho());
						gNodo = graph.insertVertex(parent, null, actual
								.hijoDerecho().tipo(), 0, 0, 30, 30,
								estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoDerecho(), gNodo);
					}

					siguientes.remove(actual);
				}
			} finally {
				graph.getModel().endUpdate();

				mxGraphComponent graphComponent = new mxGraphComponent(graph);

				new mxHierarchicalLayout(graph, SwingConstants.NORTH)
						.execute(parent);
				new mxParallelEdgeLayout(graph).execute(parent);

				this.imagen = mxCellRenderer.createBufferedImage(graph, null,
						1, Color.WHITE, graphComponent.isAntiAlias(), null,
						graphComponent.getCanvas());
			}
		}

		return this.imagen;
	}

	/**
	 * Devuelve el tipo del nodo como una cadena de caracteres.
	 * 
	 * @return Representación del nodo en caracteres.
	 */
	private String tipo() {
		switch (this.tipo) {
		case SIMBOLO:
			return simbolo() + "";
		case CIERRE:
			return "*";
		case CONCAT:
			// CGO changed this
			//return "\u2027";
			return "·"; // Esto hace más fácil el depurado, «a·b» vs «a\u2027b»
		case UNION:
			return "|";
		case VACIO:
			// CGO changed this
			return "\u03B5";
			//return "E"; // Esto podría hacer más fácil el depurado, «a|E» vs «a|\u03B5»
		default:
			return "";
		}
	}

	/**
	 * Construye una representación de la expresión regular, utilizando
	 * caracteres especiales para representar las concatenaciones y los nodos
	 * vacíos.
	 * <p>
	 * Formato UTF-8.
	 */
	public String toStringOLD() {
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
	public String toString() {
		return this.toString2();
	}
	
	/**
	 * Comprueba si el nodo es un operando.
	 * 
	 * @return <code>true</code> si el nodo representa un operando,
	 *         <code>false</code> si no.
	 */
	public boolean esOperando() {
		return this.esSimbolo() || this.esVacio();
	}	

	/**
	 * Comprueba si el nodo es un operador binario.
	 * 
	 * @return <code>true</code> si el nodo representa un operador binario,
	 *         <code>false</code> si no.
	 */
	public boolean esBinario() {
		return this.esConcat() || this.esUnion();
	}	

	/**
	 * Devuelve la precedencia del del nodo como un valor entero.
	 * 
	 * @return Precedencia del operador como valor numérico.
	 */
	public int precedence() {
		switch (this.tipo) {
		case UNION:
			return 0;
		case CONCAT:
			return 1;
		case CIERRE:
			return 2;
		default:
			return -1;
		}
	}

	/**
	 * Construye una representación de la expresión regular, utilizando
	 * caracteres especiales para representar las concatenaciones y los nodos
	 * vacíos y eliminando los paréntesis cuando estos sean redundantes debido
	 * a que con las precedencias de los operadores que combinan las sub-expresiones
	 * es suficiente para que no haya ambigüedades en la interpretación de la expresión regular.
	 * <p>
	 * Formato UTF-8.
	 */
	public String toString2() {
		ExpresionRegular left, right;
		String lop, lcp, rop, rcp; // left and right, open and closing parentheses
		StringBuilder string = new StringBuilder();
		
		switch (this.tipo) {
		case VACIO:
			// CGO changed this
			string.append('\u03B5');
			//string.append('E'); // Esto podría hacer más fácil el depurado
			break;
		case SIMBOLO:
			string.append(this.simbolo);
			break;
		case UNION:
		case CONCAT:
			left = this.hijoIzquierdo;
			right = this.hijoDerecho;
			if (!left.esOperando() && this.precedence() > left.precedence()) {
				lop = "(";
				lcp = ")";
			} else {
				lop = "";
				lcp = "";
			}
			if (!right.esOperando() && this.precedence() >= right.precedence()) {
				rop = "(";
				rcp = ")";
			} else {
				rop = "";
				rcp = "";
			}
			string.append(lop);
			string.append(this.hijoIzquierdo.toString2());
			string.append(lcp);
			string.append(this.tipo());
			string.append(rop);
			string.append(this.hijoDerecho.toString2());
			string.append(rcp);
			break;
		case CIERRE:
			if (this.hijoIzquierdo.esBinario()) {
				lop = "(";
				lcp = ")";
			} else {
				lop = "";
				lcp = "";
			}
			string.append(lop);
			string.append(this.hijoIzquierdo.toString2());
			string.append(lcp);
			string.append("*");
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

	@Override
	public int hashCode() {
		int result = 17;

		switch (tipo) {
		case CIERRE:
			result = result * 31;
			break;
		case CONCAT:
			result = result * 31 + 1;
			break;
		case SIMBOLO:
			result = result * 31 + 2;
			break;
		case UNION:
			result = result * 31 + 3;
			break;
		case VACIO:
			result = result * 31 + 4;
			break;
		}

//		result = result * 31 + posicion;
		result = result * 31 + simbolo;

		result = result * 31
				+ (hijoIzquierdo != null ? hijoIzquierdo.hashCode() : 0);
		result = result * 31
				+ (hijoDerecho != null ? hijoDerecho.hashCode() : 0);

		return result;
	}
}
