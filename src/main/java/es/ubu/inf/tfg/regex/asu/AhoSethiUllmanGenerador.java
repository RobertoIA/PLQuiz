package es.ubu.inf.tfg.regex.asu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;

public class AhoSethiUllmanGenerador {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllmanGenerador.class);

	private static final Random random = new Random(new Date().getTime());

	private static final int MAX_ITERACIONES = Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 2;

	private static final int ELITISMO = 10;
	private static final int MUTACION = 0;
	private static final int NUEVOS = 50;

	private Generador generador;

	public AhoSethiUllman nuevo(int nSimbolos, int nEstados, boolean usaVacio) {
		log.info(
				"Generando problema de Aho-Sethi-Ullman con {} simbolos y {} estados, vacios = {}.",
				nSimbolos, nEstados, usaVacio);

		List<AhoSethiUllman> poblacion = new ArrayList<>();
		List<AhoSethiUllman> elite, mutacion, nuevos = new ArrayList<>();
		int evaluaCandidato = 0;
		ExpresionRegular expresion;
		generador = new Generador(nSimbolos, usaVacio, true);

		int iteraciones = 0;
		int profundidad;

		Comparator<AhoSethiUllman> evalua = Comparator.comparing(p -> evalua(p,
				nEstados, nSimbolos));

		// inicializa población
		for (int i = 0; i < (ELITISMO + MUTACION + NUEVOS); i++) {
			profundidad = random.nextInt(MAX_PROFUNDIDAD - MIN_PROFUNDIDAD)
					+ MIN_PROFUNDIDAD;
			expresion = generador.arbol(profundidad);
			poblacion.add(new AhoSethiUllman(expresion));
		}

		do {
			poblacion.sort(evalua);
			
			elite = poblacion.stream().limit(ELITISMO).collect(Collectors.toList());
			mutacion = poblacion.stream().skip(ELITISMO).limit(MUTACION).collect(Collectors.toList());
			
			for(int i = 0; i < NUEVOS; i++) {
				profundidad = random.nextInt(MAX_PROFUNDIDAD - MIN_PROFUNDIDAD)
						+ MIN_PROFUNDIDAD;
				expresion = generador.arbol(profundidad);
				nuevos.add(new AhoSethiUllman(expresion));
			}
			
			
			poblacion.clear();
			poblacion.addAll(elite);
			poblacion.addAll(mutacion);
			poblacion.addAll(nuevos);			

			nuevos.clear();
			
			evaluaCandidato = evalua(poblacion.get(0),
					nEstados, nSimbolos);
			
//			log.warn("{} -> {}", iteraciones, evaluaCandidato);
//			
//			log.warn("e{} s{} {}", poblacion.get(0).estados().size(), poblacion.get(0).simbolos().size(), poblacion.get(0).simbolos());

			iteraciones++;
		} while (evaluaCandidato != 0 && iteraciones < MAX_ITERACIONES);

		log.info("Solución encontrada en {} iteraciones.", iteraciones);

		return poblacion.get(0);
	}

	private int evalua(AhoSethiUllman problema, int nEstados, int nSimbolos) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size() - 1
				- nSimbolos);

		return diferenciaEstados + diferenciaSimbolos;
	}
}
