package es.ubu.inf.tfg.asu.datos;

import java.util.Set;
import java.util.TreeSet;

/**
 * Nodo implementa la información calculada sobre un nodo ExpresionRegular. El
 * cálculo se realiza de forma recursiva recorriendo el árbol hacia abajo. El
 * valor de los atributos de un nodo depende directamente de los valores de los
 * atributos de los nodos de sus descendientes.
 * <p>
 * Nodo forma una estructura de árbol paralela a la de ExpresionRegular,
 * pudiendo accederse desde el nodo a la expresión correspondiente.
 * <p>
 * Nodo es una estructura de datos inmutable. Aunque la estructura de la
 * expresión regular sobre la que se calculó cambie, los datos y la estructura
 * de Nodo continuarán siendo válidos ya que mantiene una referencia nodo Nodo a
 * nodo ExpresionRegular, sin depender de la estructura de estos últimos.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Nodo {

	private final ExpresionRegular expresion;
	private final Nodo hijoIzquierdo;
	private final Nodo hijoDerecho;

	private boolean esAnulable;
	private Set<Integer> primeraPos;
	private Set<Integer> ultimaPos;

	private MapaPosiciones<Character> simbolos;
	private MapaPosiciones<Integer> siguientePos;

	/**
	 * Calcula los atributos de un nodo ExpresionRegular a partir de los de sus
	 * descendientes. La forma de realizar estos cálculos dependerá del tipo de
	 * nodo ExpresionRegular.
	 * 
	 * @param expresion
	 *            ExpresionRegular sobre la que realizar los cálculos.
	 * @throws IllegalArgumentException
	 *             En caso de que el nodo no perteneza a ningún tipo conocido.
	 */
	public Nodo(ExpresionRegular expresion) {
		this.expresion = expresion;

		if (expresion.esVacio()) { // nodo vacio
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = new TreeSet<Integer>();
			this.ultimaPos = new TreeSet<Integer>();
			this.simbolos = new MapaPosiciones<>();
			this.siguientePos = new MapaPosiciones<>();

		} else if (expresion.esSimbolo()) { // nodo símbolo / aumentado
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = false;
			this.primeraPos = new TreeSet<Integer>();
			this.primeraPos.add(expresion.posicion());
			this.ultimaPos = new TreeSet<Integer>(this.primeraPos);
			this.simbolos = new MapaPosiciones<>();
			this.simbolos.add(expresion.simbolo(), expresion.posicion());
			this.siguientePos = new MapaPosiciones<>();
			this.siguientePos.add(expresion.posicion());

		} else if (expresion.esCierre()) { // nodo cierre
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			this.ultimaPos = this.hijoIzquierdo.ultimaPos();
			this.simbolos = this.hijoIzquierdo.simbolos();
			this.siguientePos = this.hijoIzquierdo.siguientePos();
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(),
					this.hijoIzquierdo.primeraPos());

		} else if (expresion.esConcat()) { // nodo concat
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					&& this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			if (this.hijoIzquierdo.esAnulable())
				this.primeraPos.addAll(this.hijoDerecho.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			if (this.hijoDerecho.esAnulable())
				this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(),
					this.hijoDerecho.primeraPos());

		} else if (expresion.esUnion()) { // nodo union
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					|| this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoDerecho.primeraPos();
			this.primeraPos.addAll(this.hijoIzquierdo.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());

		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresion regular de tipo desconocido.");
		}
	}

	/**
	 * Devuelve una referencia a la expresión regular sobre la que se calculó
	 * este nodo. La manera correcta de recorrer el árbol es a través del nodo,
	 * no de la expresión regular obtenida mediante este método, ya que su
	 * estructura se considera mutable.
	 * 
	 * @return ExpresionRegular de la que se obtuvo este nodo.
	 */
	public ExpresionRegular expresion() {
		return this.expresion;
	}

	/**
	 * Devuelve una referencia al nodo hijo izquierdo de este nodo. Los nodos
	 * cuya expresion regular asociada sean tipo símbolo o vacío no tienen hijo
	 * izquierdo, y lanzan <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo de la expresión asociada sea de tipo
	 *             símbolo o vacío.
	 * @return Referencia al operando izquierdo del nodo.
	 */
	public Nodo hijoIzquierdo() {
		if (this.expresion.esSimbolo() || this.expresion.esVacio())
			throw new UnsupportedOperationException(
					"Los nodos simbolo y vacío no tienen hijo izquierdo.");
		return this.hijoIzquierdo;
	}

	/**
	 * Devuelve una referencia al nodo hijo izquierdo de este nodo. Los nodos
	 * cuya expresion regular asociada sean símbolo, vacío o cierre no tienen
	 * hijo derecho, y lanzan <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo de la expresión asociada sea de tipo
	 *             símbolo, vacío o cierre.
	 * @return Referencia al operando derecho del nodo.
	 */
	public Nodo hijoDerecho() {
		if (this.expresion.esSimbolo() || this.expresion.esVacio()
				|| this.expresion.esCierre())
			throw new UnsupportedOperationException(
					"Los nodos símbolo, vacío y cierre no tienen hijo derecho.");
		return this.hijoDerecho;
	}

	/**
	 * Comprueba si el nodo se define como anulable.
	 * 
	 * @return <code>true</code> si el nodo es anulable, <code>false</code> si
	 *         no.
	 */
	public boolean esAnulable() {
		return this.esAnulable;
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la primera-pos del nodo.
	 * 
	 * @return primera-pos del nodo.
	 */
	public Set<Integer> primeraPos() {
		return new TreeSet<>(this.primeraPos);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la última-pos del nodo.
	 * 
	 * @return última-pos del nodo.
	 */
	public Set<Integer> ultimaPos() {
		return new TreeSet<>(this.ultimaPos);
	}

	/**
	 * Devuelve un diccionario de los símbolos encontrados en la expresión, y
	 * sus posiciones.
	 * 
	 * @return Diccionario de símbolos.
	 */
	public MapaPosiciones<Character> simbolos() {
		return MapaPosiciones.copia(this.simbolos);
	}

	/**
	 * Obtiene el conjunto siguiente-pos del nodo.
	 * 
	 * @return siguiente-pos del nodo.
	 */
	public MapaPosiciones<Integer> siguientePos() {
		return MapaPosiciones.copia(this.siguientePos);
	}
}
