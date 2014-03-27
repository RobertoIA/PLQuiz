package es.ubu.inf.tfg.regex.thompson;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;

public class ConstruccionSubconjuntosGenerador {

	private static final int MAX_ITERACIONES = Integer.MAX_VALUE;
	private static final int MAX_PROFUNDIDAD = 6;
	private static final int MIN_PROFUNDIDAD = 2;

	private static Generador generador;

	public ConstruccionSubconjuntos nuevo(int nSimbolos, int nEstados,
			boolean usaVacio) {
		ConstruccionSubconjuntos candidato = null, actual = null;
		ExpresionRegular expresion;

		int iteraciones = 0;
		int profundidad = MIN_PROFUNDIDAD;

		do {
			// Inicializa variables
			generador = new Generador(nSimbolos, usaVacio);

			// Genera expresión
			expresion = generador.subArbol(profundidad, null);

			// Evalua candidato
			actual = new ConstruccionSubconjuntos(expresion);
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

	private int evalua(ConstruccionSubconjuntos problema, int nEstados) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos;

		if (generador.usaVacio())
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos() + 1);
		else
			diferenciaSimbolos = Math.abs(problema.simbolos().size()
					- generador.simbolos());
		
		return diferenciaEstados + diferenciaSimbolos;
	}
}
