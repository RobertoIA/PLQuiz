package es.ubu.inf.tfg.asu;

import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;
import es.ubu.inf.tfg.asu.datos.MapaEstados;
import es.ubu.inf.tfg.asu.datos.MapaPosiciones;
import es.ubu.inf.tfg.asu.datos.Nodo;
import es.ubu.inf.tfg.asu.parser.CharStream;
import es.ubu.inf.tfg.asu.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.asu.parser.JavaCharStream;
import es.ubu.inf.tfg.asu.parser.ParseException;
import es.ubu.inf.tfg.asu.parser.TokenMgrError;

/**
 * AhoSethiUllman implementa la solución a un problema de construcción de un AFD
 * a partir de una expresión regular. Es capaz de trabajar tanto con una cadena
 * de caracteres como con una ExpresionRegular ya construida.
 * <p>
 * Asimismo actua como fachada del subsistema es.ubu.inf.tfg.asu, junto con
 * AhoSethiUllmanGenetico, evitando dependencias con los tipos de datos
 * internos. Todas las salidas se codifican en tipos de datos estandar.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllman {

	private String problema;
	private ExpresionRegular expresion;
	private Nodo solucion;
	private MapaPosiciones<Character> estados;
	private MapaEstados transiciones;

	/**
	 * Resuelve un problema de construcción de AFD a partir de una expresión
	 * regular en forma de String utilizando el algoritmo de Aho-Sethi-Ullman.
	 * 
	 * @param problema
	 *            Cadena de caracteres conteniendo la expresión regular a
	 *            resolver.
	 * @throws UnsupportedOperationException
	 *             Error del parser o del token manager. Indica que la expresión
	 *             no es válida o que contiene caracteres no reconocidos.
	 */
	public AhoSethiUllman(String problema) throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		CharStream input = new JavaCharStream(new StringReader(problema));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresión no válida.");
		}

		this.solucion = new Nodo(this.expresion);

		// calculo estados
		this.estados = new MapaPosiciones<>();
		this.transiciones = new MapaEstados();

		this.estados.add('A', primeraPos());
		char estadoActual = 'A';

		// Mientras queden estados por rellenar
		while (estados.keys().contains(estadoActual)) {
			// Para cada símbolo no final
			for (char simbolo : simbolos()) {
				if (simbolo != '$') {
					// Calculamos y almacenamos la transición
					char destino = transicion(estadoActual, simbolo);
					this.transiciones.add(estadoActual, simbolo, destino);
				}
			}
			// Pasamos al siguiente estado, alfabeticamente
			estadoActual++;
		}
	}

	/**
	 * Resuelve un problema de construcción de AFD a partir de una expresión
	 * regular en forma ExpresionRegular utilizando el algoritmo de
	 * Aho-Sethi-Ullman. Se considera que la expresión recibida ya ha sido
	 * aumentada.
	 * 
	 * @param expresion
	 *            ExpresionRegular conteniendo la expresión regular a resolver.
	 */
	public AhoSethiUllman(ExpresionRegular expresion) {
		// Expresion sin aumentar.
		this.problema = expresion.hijoIzquierdo().toString();
		this.expresion = expresion;

		this.solucion = new Nodo(this.expresion);

		// calculo estados
		this.estados = new MapaPosiciones<>();
		this.transiciones = new MapaEstados();

		this.estados.add('A', primeraPos());
		char estadoActual = 'A';

		// Mientras queden estados por rellenar
		while (estados.keys().contains(estadoActual)) {
			// Para cada símbolo no final
			for (char simbolo : simbolos()) {
				if (simbolo != '$') {
					// Calculamos y almacenamos la transición
					char destino = transicion(estadoActual, simbolo);
					this.transiciones.add(estadoActual, simbolo, destino);
				}
			}
			// Pasamos al siguiente estado, alfabeticamente
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
		Set<Integer> posiciones = new TreeSet<>();

		for (int pos : posiciones(simbolo)) {
			if (this.estados.get(estado).contains(pos))
				posiciones.addAll(siguientePos(pos));
		}

		// Comprobar si existe el estado o crear uno nuevo.
		for (char est : estados()) {
			if (estado(est).equals(posiciones)) {
				return est;
			}
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.add(est, posiciones);
		return est;
	}

	/**
	 * Devuelve el problema original, la expresión regular sin aumentar. Puede
	 * contener caracteres especiales.
	 * 
	 * @return Problema a resolver.
	 */
	public String problema() {
		return this.problema;
	}

	/**
	 * Devuelve la expresion procesada y aumentada con la que vamos a trabajar.
	 * 
	 * @return Expresión regular aumentada.
	 */
	public String expresionAumentada() {
		return this.expresion.toString();
	}

	/**
	 * Devuelve el conjunto primera-pos del problema.
	 * 
	 * @return Conjunto primera-pos.
	 */
	public Set<Integer> primeraPos() {
		return this.solucion.primeraPos();
	}

	/**
	 * Devuelve el conjunto de símbolos que se utilizan en la expresión regular.
	 * 
	 * @return Símbolos que utiliza la expresión regular.
	 */
	public Set<Character> simbolos() {
		return this.solucion.simbolos().keys();
	}

	/**
	 * Devuelve el conjunto de posiciones en las que se encuentra un símbolo
	 * dentro de la expresión regular.
	 * 
	 * @param simbolo
	 *            Símbolo a buscar.
	 * @return Conjunto de posiciones en las que encontramos el símbolo.
	 */
	public Set<Integer> posiciones(char simbolo) {
		return this.solucion.simbolos().get(simbolo);
	}

	/**
	 * Devuelve el conjunto de posiciones encontradas en la expresión regular.
	 * El conjunto será un rango [1, n].
	 * 
	 * @return Conjunto de posiciones en la expresión regular.
	 */
	public Set<Integer> posiciones() {
		return this.solucion.siguientePos().keys();
	}

	/**
	 * Calcula la función siguiente-pos(n) para un n dado.
	 * 
	 * @param n
	 *            Posición para la que calculamos la función.
	 * @return Resultado de la functión siguiente-pos para la posición dada.
	 */
	public Set<Integer> siguientePos(int n) {
		return this.solucion.siguientePos().get(n);
	}

	/**
	 * Devuelve un conjunto de caracteres representando los estados existentes
	 * en la tabla de transición.
	 * 
	 * @return Conjunto de estados en la tabla de transición.
	 */
	public Set<Character> estados() {
		return this.estados.keys();
	}

	/**
	 * Devuelve el conjunto de posiciones asociadas a un estado dado en la tabla
	 * de transición.
	 * 
	 * @param key
	 *            Estado del que queremos calcular posiciones.
	 * @return Conjunto de posiciones asociadas al estado.
	 */
	public Set<Integer> estado(char key) {
		return this.estados.get(key);
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
	 * Comprueba si un estado es final, es decir, si contiene el símbolo
	 * aumentado ('$'). Devuelve <code>true</code> si el estado es final,
	 * <code>false</code> si no.
	 * 
	 * @param estado
	 *            Estado a comprobar.
	 * @return <code>true</code> si el estado es final, <code>false</code> si
	 *         no.
	 */
	public boolean esFinal(char estado) {
		// Solo hay una posición final por expresión.
		int posicionfinal = posiciones('$').iterator().next();
		return this.estados.get(estado).contains(posicionfinal);
	}
}
