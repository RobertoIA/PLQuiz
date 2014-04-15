package es.ubu.inf.tfg.regex.asu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

	private static final Random random = new Random(new Date().getTime());

	private static final int MAX_ITERACIONES = Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 7;
	private static final int MIN_PROFUNDIDAD = 2;

	private static final int ELITISMO = 2;
	private static final int MUTACION = 4;
	private static final int NUEVOS = 4;

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

		List<ExpresionRegular> poblacion = new ArrayList<>();
		List<ExpresionRegular> elite, mutacion, nuevos = new ArrayList<>();
		AhoSethiUllman candidato = null;
		ExpresionRegular candidatoExpresion = null;
		int candidatoEvalua = 0;
		generador = new Generador(nSimbolos, usaVacio, true);
		cancelar.set(false);

		int iteraciones = 0;
		int profundidad;

		Comparator<ExpresionRegular> evalua = Comparator.comparing(e -> evalua(
				new AhoSethiUllman(e), nEstados, nSimbolos));

		// inicializa población
		for (int i = 0; i < (ELITISMO + MUTACION + NUEVOS); i++) {
			profundidad = random.nextInt(MAX_PROFUNDIDAD - MIN_PROFUNDIDAD)
					+ MIN_PROFUNDIDAD;
			poblacion.add(generador.arbol(profundidad));
		}

		do {
			poblacion.sort(evalua);

			elite = poblacion.stream().limit(ELITISMO)
					.collect(Collectors.toList());
			mutacion = poblacion.stream()
					// .skip(ELITISMO)
					.limit(MUTACION).map(e -> generador.mutacion(e))
					.collect(Collectors.toList());

			nuevos.clear();

			for (int i = 0; i < NUEVOS; i++) {
				profundidad = elite.get(0).profundidad()
						+ (random.nextInt(3) - 1);
				if (profundidad < MIN_PROFUNDIDAD)
					profundidad = MIN_PROFUNDIDAD;
				if (profundidad > MAX_PROFUNDIDAD)
					profundidad = MAX_PROFUNDIDAD;

				nuevos.add(generador.arbol(profundidad));
			}

			if (candidatoExpresion == null
					|| !poblacion.get(0).equals(candidatoExpresion)) {
				candidatoExpresion = poblacion.get(0);
				candidato = new AhoSethiUllman(candidatoExpresion);
				candidatoEvalua = evalua(candidato, nEstados, nSimbolos);
			}

			poblacion.clear();
			poblacion.addAll(elite);
			poblacion.addAll(mutacion);
			poblacion.addAll(nuevos);

			iteraciones++;
		} while (candidatoEvalua != 0 && iteraciones < MAX_ITERACIONES
				&& !cancelar.get());

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
