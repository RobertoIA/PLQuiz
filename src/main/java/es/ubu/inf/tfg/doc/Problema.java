package es.ubu.inf.tfg.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class Problema<T> {
	
	private static final Logger log = LoggerFactory.getLogger(Problema.class);

	private enum Tipo {
		AHOSETHIULLMAN_CONSTRUCCION, // Aho-Sethi-Ullman construcción de árbol
		AHOSETHIULLMAN_ETIQUETADO, // Aho-Sethi-Ullman etiquetado de árbol
		AHOSETHIULLMAN_TABLAS, // Aho-Sethi-Ullman tablas stePos y transición
		CONSTRUCCIONSUBCONJUNTOS_CONSTRUCCION, // Construcción de autómata
		CONSTRUCCIONSUBCONJUNTOS_EXPRESION, // Subconjuntos desde expresión
		CONSTRUCCIONSUBCONJUNTOS_AUTOMATA, // Subconjuntos desde autómata
	}

	private Tipo tipo;
	private T problema;
	private int numero;

	private Problema(T problema) {
		this.problema = problema;
	}

	public static Problema<AhoSethiUllman> asuConstruccion(
			AhoSethiUllman problema, int numero) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_CONSTRUCCION;
		asuProblema.setNumero(numero);
		return asuProblema;
	}

	public static Problema<AhoSethiUllman> asuEtiquetado(
			AhoSethiUllman problema, int numero) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_ETIQUETADO;
		asuProblema.setNumero(numero);
		return asuProblema;
	}

	public static Problema<AhoSethiUllman> asuTablas(AhoSethiUllman problema,
			int numero) {
		Problema<AhoSethiUllman> asuProblema = new Problema<>(problema);
		asuProblema.tipo = Tipo.AHOSETHIULLMAN_TABLAS;
		asuProblema.setNumero(numero);
		return asuProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSConstruccion(
			ConstruccionSubconjuntos problema, int numero) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONSTRUCCIONSUBCONJUNTOS_CONSTRUCCION;
		csProblema.setNumero(numero);
		return csProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSExpresion(
			ConstruccionSubconjuntos problema, int numero) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONSTRUCCIONSUBCONJUNTOS_EXPRESION;
		csProblema.setNumero(numero);
		return csProblema;
	}

	public static Problema<ConstruccionSubconjuntos> CSAutomata(
			ConstruccionSubconjuntos problema, int numero) {
		Problema<ConstruccionSubconjuntos> csProblema = new Problema<>(problema);
		csProblema.tipo = Tipo.CONSTRUCCIONSUBCONJUNTOS_AUTOMATA;
		csProblema.setNumero(numero);
		return csProblema;
	}

	public T getProblema() {
		return this.problema;
	}

	public String getTipo() {
		switch (tipo) {
		case AHOSETHIULLMAN_CONSTRUCCION:
			return "AhoSethiUllmanConstruccion"; //$NON-NLS-1$
		case AHOSETHIULLMAN_ETIQUETADO:
			return "AhoSethiUllmanEtiquetado"; //$NON-NLS-1$
		case AHOSETHIULLMAN_TABLAS:
			return "AhoSethiUllmanTablas"; //$NON-NLS-1$
		case CONSTRUCCIONSUBCONJUNTOS_CONSTRUCCION:
			return "ConstruccionSubconjuntosConstruccion"; //$NON-NLS-1$
		case CONSTRUCCIONSUBCONJUNTOS_EXPRESION:
			return "ConstruccionSubconjuntosExpresion"; //$NON-NLS-1$
		case CONSTRUCCIONSUBCONJUNTOS_AUTOMATA:
			return "ConstruccionSubconjuntosAutomata"; //$NON-NLS-1$
		default:
			throw new UnsupportedOperationException(
					Messages.getString("Problema.UnsupportedTypeArgument")); //$NON-NLS-1$
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
		case CONSTRUCCIONSUBCONJUNTOS_CONSTRUCCION:
		case CONSTRUCCIONSUBCONJUNTOS_EXPRESION:
		case CONSTRUCCIONSUBCONJUNTOS_AUTOMATA:
			estaExpresion = ((ConstruccionSubconjuntos) problema).problema();
			otraExpresion = ((Problema<ConstruccionSubconjuntos>) o)
					.getProblema().problema();
			return estaExpresion.equals(otraExpresion);
		default:
			return false;
		}
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		log.info("Número de problema cambiado a {}", numero); //$NON-NLS-1$
		
		this.numero = numero;
	}
}
