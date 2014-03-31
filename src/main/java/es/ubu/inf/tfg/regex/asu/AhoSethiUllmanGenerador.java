package es.ubu.inf.tfg.regex.asu;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;

/**
 * AhoSethiUllmanGenerador implementa una clase encargada de generar problemas
 * de tipo AhoSethiUllman con los parámetros especificados, siguiendo un
 * algoritmo de búsqueda aleatoria.
 * <p>
 * El generador no garantiza que los resultados se adapten perfectamente a los
 * parámetros de entrada.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllmanGenerador {

	private static final int MAX_ITERACIONES = Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 2;

	private Generador generador;

	/**
	 * Genera un nuevo problema de tipo AhoSethiUllman. Intentará acercarse lo
	 * más posible al número de símbolos y de estados especificado. El algoritmo
	 * es capaz de variar la profundidad a la que busca en función de los
	 * resultados que vaya obteniendo, entre ciertos márgenes.
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
		int profundidad = MIN_PROFUNDIDAD;

		do {
			// Inicializa variables
			generador = new Generador(nSimbolos, usaVacio);

			// Genera expresión
			expresion = generador.subArbol(profundidad, null);

			// Evalua candidato
			actual = new AhoSethiUllman(expresion);
			if (candidato == null
					|| evalua(actual, nEstados) < evalua(candidato, nEstados))
				candidato = actual;

			// Modifica la profundidad
			int dif = nEstados - actual.estados().size();
			if (dif > 1)
				profundidad++;
			else if (dif < 1)
				profundidad--;

			if (profundidad < MIN_PROFUNDIDAD)
				profundidad = MIN_PROFUNDIDAD;
			else if (profundidad > MAX_PROFUNDIDAD)
				profundidad = MAX_PROFUNDIDAD;

			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);

		return candidato;
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
		int diferenciaSimbolos;

		if (generador.usaVacio())
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos());
		else
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos() - 1);
		return diferenciaEstados + diferenciaSimbolos;
	}
}
