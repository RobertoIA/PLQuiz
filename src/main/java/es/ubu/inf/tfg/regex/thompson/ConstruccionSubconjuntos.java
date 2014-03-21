package es.ubu.inf.tfg.regex.thompson;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.MapaEstados;
import es.ubu.inf.tfg.regex.parser.CharStream;
import es.ubu.inf.tfg.regex.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.regex.parser.JavaCharStream;
import es.ubu.inf.tfg.regex.parser.ParseException;
import es.ubu.inf.tfg.regex.parser.TokenMgrError;
import es.ubu.inf.tfg.regex.thompson.datos.Automata;
import es.ubu.inf.tfg.regex.thompson.datos.Nodo;

/**
 * * Resuelve un problema de construcción de subconjuntos a partir de un AFND,
 * generado a partir de una expresión regular con el método de
 * McNaughton-Yamada-Thompson. Es capaz de trabajar tanto con una cadena de
 * caracteres como con una ExpresionRegular ya construida.
 * <p>
 * Asimismo actua como fachada del subsistema es.ubu.inf.tfg.regex.thompson,
 * evitando dependencias con los tipos de datos internos. Todas las salidas se
 * codifican en tipos de datos estandar.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ConstruccionSubconjuntos {

	private String problema;
	private ExpresionRegular expresion;
	private Automata automata;
	private Map<Character, Set<Nodo>> estados;

	private MapaEstados transiciones;

	/**
	 * Resuelve un problema de construcción de subconjuntos a partir de una
	 * expresión regular en forma de String, construyendo el autómata según el
	 * algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param problema
	 *            Cadena de caracteres conteniendo la expresión regular a
	 *            resolver.
	 * @throws UnsupportedOperationException
	 *             Error del parser o del token manager. Indica que la expresión
	 *             no es válida o que contiene caracteres no reconocidos.
	 */
	public ConstruccionSubconjuntos(String problema) throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		CharStream input = new JavaCharStream(new StringReader(problema));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
			// No utilizamos la expresion aumentada
			this.expresion = this.expresion.hijoIzquierdo();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresión no válida.");
		}

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();
		
		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Resuelve un problema de construcción de subconjuntos a partir de una
	 * expresión regular en forma de ExpresionRegular, construyendo el autómata
	 * según el algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param expresionExpresionRegular
	 *            conteniendo la expresión regular a resolver.
	 */
	public ConstruccionSubconjuntos(ExpresionRegular expresion) {
		// Expresion sin aumentar.
		this.expresion = this.expresion.hijoIzquierdo();
		this.problema = this.expresion.toString();

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Calcula la transición a partir de un estado dado mediante un símbolo
	 * concreto.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Símbolo de transición.
	 * @return Estado de destino.
	 */
	private char transicion(char estado, char simbolo) {
		Set<Nodo> posiciones = new TreeSet<>();
		
		for (Nodo nodo : estados.get(estado)) {
			if (this.estados.get(estado).contains(nodo))
				posiciones.addAll(automata.transicion(nodo, simbolo));
		}

		for (char est : estados()) {
			if (estados.get(est).equals(posiciones))
				return est;
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.put(est, posiciones);
		
		return est;
	}

	/**
	 * Devuelve el problema original, la expresión regular. Puede contener
	 * caracteres especiales.
	 * 
	 * @return Problema a resolver.
	 */
	public String problema() {
		return this.problema;
	}

	/**
	 * Devuelve el conjunto de símbolos que se utilizan en la expresión regular.
	 * 
	 * @return Símbolos que utiliza la expresión regular.
	 */
	public Set<Character> simbolos() {
		return automata.simbolos();
	}

	/**
	 * Devuelve un conjunto de caracteres representando los estados existentes
	 * en la tabla de transición.
	 * 
	 * @return Conjunto de estados en la tabla de transición.
	 */
	public Set<Character> estados() {
		return new TreeSet<>(estados.keySet());
	}

	/**
	 * Devuelve el conjunto de posiciones asociadas a un estado dado en la tabla
	 * de transición.
	 * 
	 * @param estado
	 *            Estado del que queremos calcular posiciones.
	 * @return Conjunto de posiciones asociadas al estado.
	 */
	public Set<Integer> posiciones(char estado) {
		Set<Integer> posiciones = new TreeSet<>();

		for (Nodo nodo : this.estados.get(estado))
			posiciones.add(nodo.posicion());

		return posiciones;
	}

	/**
	 * Calcula el estado de destino para un estado de origen y un símbolo de
	 * transición.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Símbolo de transición.
	 * @return Estado de destino.
	 */
	public char mueve(char estado, char simbolo) {
		return this.transiciones.get(estado, simbolo);
	}

	/**
	 * Comprueba si un estado es final, es decir, si su conjunto de posiciones
	 * contiene un nodo final. Devuelve <code>true</code> si el estado es final,
	 * <code>false</code> si no.
	 * 
	 * @param estado
	 *            Estado a comprobar.
	 * @return <code>true</code> si el estado es final, <code>false</code> si
	 *         no.
	 */
	public boolean esFinal(char estado) {
		boolean esFinal = false;

		for (Nodo nodo : this.estados.get(estado))
			esFinal = esFinal || nodo.esFinal();

		return esFinal;
	}
}
