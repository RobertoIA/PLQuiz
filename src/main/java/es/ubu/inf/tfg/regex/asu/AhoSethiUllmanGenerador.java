package es.ubu.inf.tfg.regex.asu;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanGenerador.class);

	private static final int MAX_ITERACIONES = 3000;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 3;

	private Generador generador;
	private AtomicBoolean cancelar = new AtomicBoolean();

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
		log.info(
				"Generando problema de Aho-Sethi-Ullman con {} simbolos y {} estados, vacios = {}.",
				nSimbolos, nEstados, usaVacio);

		AhoSethiUllman candidato = null, actual = null;
		int evaluaCandidato = 0, evaluaActual;
		ExpresionRegular expresion;

		int iteraciones = 0;
		int profundidad = MIN_PROFUNDIDAD;

		// Inicializa variables
		generador = new Generador(nSimbolos, usaVacio, true);

		do {
			expresion = generador.arbol(profundidad);
			actual = new AhoSethiUllman(expresion);
			
			evaluaActual = evalua(actual, nEstados, nSimbolos);
			
			if (candidato == null
					||  (evaluaActual < evaluaCandidato)) {
				candidato = actual;
				evaluaCandidato = evaluaActual;
			}

			// Modifica la profundidad
			int dif = nEstados - actual.estados().size();
			if (dif > 1 && profundidad < MAX_PROFUNDIDAD)
				profundidad++;
			else if (dif < 1 && profundidad > MIN_PROFUNDIDAD)
				profundidad--;

			iteraciones++;
		} while (evalua(candidato, nEstados, nSimbolos) != 0
				&& iteraciones < MAX_ITERACIONES);

		log.info("Solución encontrada en {} iteraciones.", iteraciones);

		return candidato;
	}

	/**
	 * Evalua un problema en función a como se adapta a los parámetros pedidos.
	 * Tiene en cuenta tanto que el número de estados sea el pedido, como que
	 * use todos los símbolos.
	 * <p>
	 * Cuanto más cerca este del cero, más cerca esta el problema de la
	 * solución.
	 * 
	 * @param problema
	 *            Problema a evaluar.
	 * @param nEstados
	 *            Número de estados en el problema pedido.
	 * @return Función de evaluación del problema.
	 */
	private int evalua(AhoSethiUllman problema, int nEstados, int nSimbolos) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size() - 1
				- nSimbolos);

		return diferenciaEstados + diferenciaSimbolos;
	}

	/**
	 * Cancela la generación del problema, devolviendo el resultado de la
	 * iteración actual.
	 */
	public void cancelar() {
		log.info("Cancelando generación de problema.");
		cancelar.compareAndSet(false, true);
	}
}
