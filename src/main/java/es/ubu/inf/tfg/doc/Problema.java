package es.ubu.inf.tfg.doc;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class Problema<T> {
	private enum Tipo {
		AHOSETHIULLMAN, // Aho-Sethi-Ullman
		CONJUNTOS_EXP, // Construcción de subconjuntos desde expresión regular
		CONJUNTOS_AUT // Construcción de subconjuntos desde autómata
	}

	private Tipo tipo;
	private T problema;

	private Problema(T problema) {
		this.problema = problema;
	}

	public static Problema<AhoSethiUllman> ASU(AhoSethiUllman problema) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN;
		return asuProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSExpresion(
			ConstruccionSubconjuntos problema) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONJUNTOS_EXP;
		return csProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSAutomata(
			ConstruccionSubconjuntos problema) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONJUNTOS_AUT;
		return csProblema;
	}

	public T getProblema() {
		return this.problema;
	}

	public String getTipo() {
		switch (tipo) {
		case AHOSETHIULLMAN:
			return "AhoSethiUllman";
		case CONJUNTOS_EXP:
			return "ConstruccionSubconjuntosExpresion";
		case CONJUNTOS_AUT:
			return "ConstruccionSubconjuntosAutomata";
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
		case AHOSETHIULLMAN:
			estaExpresion = ((AhoSethiUllman) problema).problema();
			otraExpresion = ((Problema<AhoSethiUllman>) o).getProblema()
					.problema();
			return estaExpresion.equals(otraExpresion);
		case CONJUNTOS_EXP:
		case CONJUNTOS_AUT:
			estaExpresion = ((ConstruccionSubconjuntos) problema).problema();
			otraExpresion = ((Problema<ConstruccionSubconjuntos>) o)
					.getProblema().problema();
			return estaExpresion.equals(otraExpresion);
		default:
			return false;
		}
	}
}
