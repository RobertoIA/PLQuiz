package es.ubu.inf.tfg.doc;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class Problema<T> {
	private enum Tipo {
		AHOSETHIULLMAN_CONSTRUCCION, // Aho-Sethi-Ullman construcción de árbol
		AHOSETHIULLMAN_ETIQUETADO, // Aho-Sethi-Ullman etiquetado de árbol
		AHOSETHIULLMAN_TABLAS, // Aho-Sethi-Ullman tablas stePos y transición
		CONSTRUCCIONSUBCONJUNTOS_EXPRESION, // Subconjuntos desde expresión
		CONSTRUCCIONSUBCONJUNTOS_AUTOMATA, // Subconjuntos desde autómata
		THOMPSON // Thompson
	}

	private Tipo tipo;
	private T problema;

	private Problema(T problema) {
		this.problema = problema;
	}
	
	public static Problema<AhoSethiUllman> asuConstruccion(AhoSethiUllman problema) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_CONSTRUCCION;
		return asuProblema;
	}

	public static Problema<AhoSethiUllman> asuEtiquetado(AhoSethiUllman problema) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_ETIQUETADO;
		return asuProblema;
	}

	public static Problema<AhoSethiUllman> asuTablas(AhoSethiUllman problema) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_TABLAS;
		return asuProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSExpresion(
			ConstruccionSubconjuntos problema) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONSTRUCCIONSUBCONJUNTOS_EXPRESION;
		return csProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSAutomata(
			ConstruccionSubconjuntos problema) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONSTRUCCIONSUBCONJUNTOS_AUTOMATA;
		return csProblema;
	}

	public T getProblema() {
		return this.problema;
	}

	public String getTipo() {
		switch (tipo) {
		case AHOSETHIULLMAN_CONSTRUCCION:
			return "AhoSethiUllmanConstruccion";
		case AHOSETHIULLMAN_ETIQUETADO:
			return "AhoSethiUllmanEtiquetado";
		case AHOSETHIULLMAN_TABLAS:
			return "AhoSethiUllmanTablas";
		case CONSTRUCCIONSUBCONJUNTOS_EXPRESION:
			return "ConstruccionSubconjuntosExpresion";
		case CONSTRUCCIONSUBCONJUNTOS_AUTOMATA:
			return "ConstruccionSubconjuntosAutomata";
		case THOMPSON:
			return "Thompson";
		default:
			throw new UnsupportedOperationException(
					"Argumento tipo no soportado.");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Problema))
			return false;
		if (!((Problema) o).getTipo().equals(getTipo()))
			return false;

		String estaExpresion, otraExpresion;

		switch (tipo) {
		case AHOSETHIULLMAN_CONSTRUCCION:
		case AHOSETHIULLMAN_ETIQUETADO:
		case AHOSETHIULLMAN_TABLAS:
			estaExpresion = ((AhoSethiUllman) problema).problema();
			otraExpresion = ((Problema<AhoSethiUllman>) o).getProblema()
					.problema();
			return estaExpresion.equals(otraExpresion);
		case CONSTRUCCIONSUBCONJUNTOS_EXPRESION:
		case CONSTRUCCIONSUBCONJUNTOS_AUTOMATA:
			estaExpresion = ((ConstruccionSubconjuntos) problema).problema();
			otraExpresion = ((Problema<ConstruccionSubconjuntos>) o)
					.getProblema().problema();
			return estaExpresion.equals(otraExpresion);
		case THOMPSON:
		default:
			return false;
		}
	}
}
