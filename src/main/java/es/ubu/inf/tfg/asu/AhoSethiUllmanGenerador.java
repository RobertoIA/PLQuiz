package es.ubu.inf.tfg.asu;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;

/**
 * AhoSethiUllmanGenerador implementa una clase encargada de generar problemas
 * de tipo AhoSethiUllman con los parámetros especificados, siguiendo un
 * algoritmo de búsqueda aleatoria, con generación de árboles basada en el
 * algoritmo GROW.
 * <p>
 * El generador implementa el patrón de diseño singleton, representando un
 * proveedor de problemas que recibe peticiones y devuelve los resultados de la
 * búsqueda.
 * <p>
 * El generador no garantiza que los resultados se adapten perfectamente a los
 * parámetros de entrada.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllmanGenerador {

	private static final int MAX_ITERACIONES = 25;

	private static final AhoSethiUllmanGenerador instance = new AhoSethiUllmanGenerador();
	private static final Random random = new Random(new Date().getTime());

	/**
	 * Operador implementa un tipo enumerado que contiene los tipos de
	 * operadores que utilizará el árbol, los conjuntos de operadores que se
	 * utilizan en cada circunstancia, y una serie de operaciones relacionadas.
	 * <p>
	 * Se utiliza también para almacenar el estado del árbol en un momento
	 * determinado.
	 * 
	 * @author Roberto Izquierdo Amo
	 * 
	 */
	private static enum Operador {
		SIMBOLO, VACIO, CIERRE, CONCAT, UNION;

		private static final EnumSet<Operador> COMPLETO = EnumSet.range(CIERRE,
				UNION);
		private static final EnumSet<Operador> PARCIAL = EnumSet.of(CONCAT,
				UNION);
		private static final EnumSet<Operador> FINAL_COMPLETO = EnumSet.of(
				SIMBOLO, VACIO);
		private static final EnumSet<Operador> FINAL_PARCIAL = EnumSet
				.of(SIMBOLO);

		private static List<Character> simbolos;
		private static List<Character> simbolosRepetidos;
		private static int posicion;

		/**
		 * Inicializa los valores del enumerado para empezar a trabajar en un
		 * nuevo árbol.
		 * 
		 * @param nSimbolos
		 *            Número de símbolos a generar.
		 * @param usaVacio
		 *            <code>true</code> si la expresión contendrá nodos vacíos,
		 *            <code>false</code> de lo contrario.
		 */
		private static void inicializa(int nSimbolos, boolean usaVacio) {
			simbolos = new ArrayList<>();
			for (int i = 0; i < nSimbolos; i++)
				simbolos.add((char) ('a' + i));
			if (usaVacio)
				simbolos.add('E');
			simbolosRepetidos = new ArrayList<>(simbolos);

			posicion = 0;
		}

		/**
		 * Genera un símbolo cualquiera de aquellos que puede incluir el árbol.
		 * Puede contener el símbolo vacío. Prioritiza símbolos que aún no hayan
		 * aparecido.
		 * 
		 * @return Un símbolo cualquiera o el símbolo vacío.
		 */
		private static char simbolo() {
			int index;

			if (simbolos.size() > 0) {
				index = random.nextInt(simbolos.size());
				char simbolo = simbolos.get(index);
				simbolos.remove(index);
				return simbolo;
			} else {
				index = random.nextInt(simbolosRepetidos.size());
				return simbolosRepetidos.get(index);
			}
		}

		/**
		 * Genera un símbolo cualquiera de aquellos que puede incluir el árbol,
		 * excluyendo el símbolo vacío. Prioritiza símbolos que aún no hayan
		 * aparecido.
		 * 
		 * @return Un símbolo cualquiera.
		 */
		private static char simboloNoVacio() {
			int index;

			if (simbolosRepetidos.contains('E')) {
				List<Character> simbolosParcial;
				if (simbolos.size() > 1) {
					simbolosParcial = new ArrayList<>(simbolos);
					simbolosParcial.remove(simbolosParcial.indexOf('E'));
					index = random.nextInt(simbolosParcial.size());
					char simbolo = simbolosParcial.get(index);
					simbolos.remove(index);
					return simbolo;
				} else {
					simbolosParcial = new ArrayList<>(simbolosRepetidos);
					simbolosParcial.remove(simbolosParcial.indexOf('E'));
					index = random.nextInt(simbolosParcial.size());
					return simbolosParcial.get(index);
				}
			} else
				return simbolo();
		}

		/**
		 * Devuelve la siguiente posición a añadir en el árbol.
		 * 
		 * @return Siguiente posición.
		 */
		private static int posicion() {
			return posicion++;
		}

		/**
		 * Devuelve la cantidad de símbolos distintos que debe contener el
		 * árbol.
		 * 
		 * @return Cantidad de símbolos en el árbol.
		 */
		private static int simbolos() {
			return simbolosRepetidos.size();
		}

		/**
		 * Devuelve un operador aleatorio de entre un conjunto de operadores.
		 * 
		 * @param operadores
		 *            Conjunto de operadores entre los que elegir.
		 * @return Operador cualquiera del conjunto.
		 */
		private static Operador random(EnumSet<Operador> operadores) {
			int index = random.nextInt(operadores.size());
			return (Operador) operadores.toArray()[index];
		}

		/**
		 * Comprueba si el árbol contendrá nodos vacíos.
		 * 
		 * @return<code>true</code> si la expresión contendrá nodos vacíos,
		 *                          <code>false</code> de lo contrario.
		 */
		private static boolean usaVacio() {
			return simbolosRepetidos.contains('E');
		}
	}

	/**
	 * Constructor privado, no se le llama.
	 */
	private AhoSethiUllmanGenerador() {
	}

	/**
	 * Devuelve la instancia compartida de la clase.
	 * 
	 * @return Instancia única de AhoSethiUllmanGenerador.
	 */
	public static AhoSethiUllmanGenerador getInstance() {
		return instance;
	}

	/**
	 * Genera un nuevo problema de tipo AhoSethiUllman. Intentará acercarse lo
	 * más posible al número de símbolos y de estados especificado.
	 * 
	 * @param nSimbolos
	 *            Número de símbolos que se quiere que el problema utilice.
	 * @param nEstados
	 *            Número de estados que se quiere que contenga la tabla de
	 *            transición del problema.
	 * @param usaVacio
	 *            Si queremos que el problema genere nodos vacíos. Su aparición
	 *            no se garantiza.
	 * @return Un nuevo problema de tipo AhoSethiUllman.
	 */
	public AhoSethiUllman nuevo(int nSimbolos, int nEstados, boolean usaVacio) {
		AhoSethiUllman candidato = null, actual = null;
		ExpresionRegular expresion;
		int iteraciones = 0;
		int profundidad = 4;

		do {
			// Inicializa variables
			Operador.inicializa(nSimbolos, usaVacio);

			// Genera expresión
			expresion = subArbol(profundidad, null);

			// Evalua candidato
			if (candidato == null) {
				candidato = new AhoSethiUllman(expresion);
			} else {
				actual = new AhoSethiUllman(expresion);
				if (evalua(actual, nEstados) < evalua(candidato, nEstados))
					candidato = actual;
			}

			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);
		return candidato;
	}

	/**
	 * Genera un sub-árbol de expresion regular con la profundidad dada y
	 * utilizando un conjunto de operadores concreto. Se llama a si mismo de
	 * manera recursiva para completar la construcción.
	 * 
	 * @param profundidad
	 *            Profundidad del árbol pedido.
	 * @param operadores
	 *            Operadores a utilizar.
	 * @return Expresión regular generada.
	 */
	private ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {

		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		// Raiz del árbol, aumenta la expresión.
		if (operadores == null) {
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = ExpresionRegular.nodoAumentado(Operador.posicion());
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		}

		// Hoja del árbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && Operador.usaVacio())
				operadores = Operador.FINAL_COMPLETO;
			else
				operadores = Operador.FINAL_PARCIAL;
		}

		// Nodo operador.
		switch (Operador.random(operadores)) {
		case VACIO: // Vacío solo actua como marcador.
		case SIMBOLO:
			char simbolo;
			if (operadores.equals(Operador.FINAL_COMPLETO))
				simbolo = Operador.simbolo();
			else
				simbolo = Operador.simboloNoVacio();

			if (simbolo == 'E')
				return ExpresionRegular.nodoVacio();
			return ExpresionRegular.nodoSimbolo(Operador.posicion(), simbolo);
		case CIERRE:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.PARCIAL);
			return ExpresionRegular.nodoCierre(hijoIzquierdo);
		case CONCAT:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		case UNION:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoUnion(hijoDerecho, hijoIzquierdo);
		default:
			return null;
		}
	}

	/**
	 * Evalua un problema en función a como se adapta a los parámetros pedidos.
	 * Tiene en cuenta tanto que el número de estados sea el pedido, como que
	 * use todos los símbolos.
	 * <p>
	 * Cuanto más cerca este del número, más cerca esta el problema de la
	 * solución.
	 * 
	 * @param problema
	 *            Problema a evaluar.
	 * @param nEstados
	 *            Número de estados en el problema pedido.
	 * @return Función de evaluación del problema.
	 */
	private int evalua(AhoSethiUllman problema, int nEstados) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size()
				- Operador.simbolos());

		return diferenciaEstados + diferenciaSimbolos;
	}
}
