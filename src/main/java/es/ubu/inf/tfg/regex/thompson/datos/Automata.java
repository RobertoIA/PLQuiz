package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

/**
 * Automata implementa el modelo lógico de un automata finito no determinista,
 * compuesto por una serie de nodos y de transiciones entre dichos nodos.
 * Permite obtener los nodos obtenidos a partir de uno dado utilizando un tipo
 * de transición determinado, y consumiendo o no un caracter dado.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;
	private Set<Character> simbolos;

	/**
	 * Constructor. Define un automata finito no determinista a partir de un
	 * árbol de expresion regular dado, de manera recursiva.
	 * 
	 * @param expresion
	 *            Árbol de expresion regular a partir del cual generar el
	 *            autómata.
	 */
	public Automata(ExpresionRegular expresion) {
		// TODO completamente provisional
		Nodo nodo0 = new Nodo(0, false);
		Nodo nodo1 = new Nodo(1, false);
		Nodo nodo2 = new Nodo(2, false);
		Nodo nodo3 = new Nodo(3, false);
		Nodo nodo4 = new Nodo(4, false);
		Nodo nodo5 = new Nodo(5, false);
		Nodo nodo6 = new Nodo(6, false);
		Nodo nodo7 = new Nodo(7, false);
		Nodo nodo8 = new Nodo(8, false);
		Nodo nodo9 = new Nodo(9, false);
		Nodo nodo10 = new Nodo(10, true);

		nodo0.añadeTransicionVacia(nodo1);
		nodo0.añadeTransicionVacia(nodo7);

		nodo1.añadeTransicionVacia(nodo2);
		nodo1.añadeTransicionVacia(nodo4);

		nodo2.añadeTransicion('a', nodo3);

		nodo3.añadeTransicionVacia(nodo6);

		nodo4.añadeTransicion('b', nodo5);

		nodo5.añadeTransicionVacia(nodo6);

		nodo6.añadeTransicionVacia(nodo7);
		nodo6.añadeTransicionVacia(nodo1);

		nodo7.añadeTransicion('a', nodo8);

		nodo8.añadeTransicion('b', nodo9);

		nodo9.añadeTransicion('b', nodo10);

		this.nodoInicial = nodo0;
		this.nodoFinal = nodo10;

		this.simbolos = new TreeSet<>();
		this.simbolos.add('a');
		this.simbolos.add('b');
	}

	/**
	 * Nodo de entrada del autómata.
	 * 
	 * @return Nodo inicial.
	 */
	public Nodo nodoInicial() {
		return this.nodoInicial;
	}

	/**
	 * Nodo final del autómata.
	 * 
	 * @return Nodo final.
	 */
	public Nodo nodoFinal() {
		return this.nodoFinal;
	}

	/**
	 * Devuelve el conjunto de símbolos que el automata utiliza en sus
	 * transiciones. No se incluye epsilon ni cualquier otro indicador de
	 * transición vacía.
	 * 
	 * @return Conjunto de símbolos del autómata.
	 */
	public Set<Character> simbolos() {
		return new TreeSet<>(this.simbolos);
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y tras consumir un símbolo determinado. Solo se cuentan las transiciones
	 * vacías efectuadas tras consumir la entrada.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @param simbolo
	 *            Símbolo de entrada.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicion(Nodo inicio, char simbolo) {
		// Nodos a los que llegamos desde el inicio sin consumir
		Set<Nodo> iniciales = transicionVacia(inicio);
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionConsumiendo = new TreeSet<>();
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionNoConsumiendo = new TreeSet<>();
		Nodo actual;

		for (Nodo nodo : iniciales) {
			actual = nodo.transicion(simbolo);
			if (actual != null)
				transicionConsumiendo.add(actual);
		}

		for (Nodo nodo : transicionConsumiendo)
			transicionNoConsumiendo.addAll(transicionVacia(nodo));

		transicionConsumiendo.addAll(transicionNoConsumiendo);

		return transicionConsumiendo;
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y sin consumir ningún símbolo.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicionVacia(Nodo inicio) {
		// Nodos a los que llegamos sin consumir entrada
		Set<Nodo> actuales = new TreeSet<>();
		Set<Nodo> visitados = new TreeSet<>();
		Nodo actual;

		actuales.add(inicio);
		while (!actuales.isEmpty()) {
			actual = actuales.iterator().next();
			actuales.addAll(actual.transicionVacia());

			visitados.add(actual);
			actuales.remove(actual);
		}

		return visitados;
	}
}
