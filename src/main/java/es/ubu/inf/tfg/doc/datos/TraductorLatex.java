package es.ubu.inf.tfg.doc.datos;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor Latex.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorLatex extends Traductor {

	private static final Logger log = LoggerFactory
			.getLogger(TraductorLatex.class);

	/**
	 * Genera un documento Latex a partir de una lista de problemas ya
	 * traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento Latex completo.
	 */
	@Override
	public String documento(List<Plantilla> problemas) {
		log.info("Generando documento Latex a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (Plantilla problema : problemas) {
			problema.set("numero", "" + n++);
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.tex");
		plantilla.set("documento", documento.toString());

		return plantilla.toString();
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a formato
	 * Latex. En latex el problema de construcción consiste en dibujar el árbol,
	 * no en identificar el correcto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato construcción",
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.tex");
		String imagen = problema.alternativas().get(0).hashCode() + "";
		
		String expresion = problema.problema();
		expresion = expresion.replace("|", "\\textbar ");
		expresion = expresion.replace("\u2027", "·");
		expresion = expresion.replace("\u03B5", "$\\epsilon$");
		expresion = expresion.replace("$", "\\$");

		plantilla.set("expresion", expresion);
		plantilla.set("imagen", imagen);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a formato
	 * Latex.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato etiquetado",
				problema.problema());

		String imagen = "" + problema.arbolVacio().hashCode();
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.tex");
		StringBuilder soluciones = new StringBuilder();

		soluciones.append(" & anulable? & primera-pos & última-pos");
		soluciones.append("\\\\ \\hline\n");
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append(simboloActual + " & ");
			soluciones.append((problema.esAnulable(simboloActual) ? "Si" : "No") + " & ");
			soluciones.append(setToString(problema.primeraPos(simboloActual))
					+ " & ");
			soluciones.append(setToString(problema.ultimaPos(simboloActual))
					+ "\\\\ \\hline\n");

			simboloActual++;
		}
		soluciones.append("\\end{tabular}");

		String solucionesL = soluciones.toString();
		solucionesL = solucionesL.replace("|", "\\textbar ");
		solucionesL = solucionesL.replace("\u2027", "·");
		solucionesL = solucionesL.replace("\u03B5", "$\\epsilon$");
		solucionesL = solucionesL.replace("$", "\\$");

		solucionesL = "\\begin{tabular} {| c | c | c | c |}\\hline\n"
				+ solucionesL;

		String expresion = problema.problema();
		expresion = expresion.replace("|", "\\textbar ");
		expresion = expresion.replace("\u2027", "·");
		expresion = expresion.replace("\u03B5", "$\\epsilon$");

		plantilla.set("expresion", expresion);
		plantilla.set("iamgen", imagen);
		plantilla.set("tabla", solucionesL);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a formato
	 * Latex.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato tablas",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaASUTablas.tex");

		// siguiente-pos
		stePos.append("\\begin{tabular} {| c | l |}\n\\hline\nn & stePos(n) \\\\ \\hline\n");
		for (int n : problema.posiciones()) {
			stePos.append(n);
			stePos.append(" & ");
			if (problema.siguientePos(n).size() > 0) {
				String prefijo = "";
				for (int pos : problema.siguientePos(n)) {
					stePos.append(prefijo);
					prefijo = ", ";
					stePos.append(pos);
				}
			} else {
				stePos.append("-");
			}
			stePos.append(" \\\\ \\hline\n");
		}
		stePos.append("\\end{tabular}");

		// Función de transición
		fTrans.append("\\begin{tabular} {| c | ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c |");
		fTrans.append(" l |}\n\\hline \n& ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & ");
		fTrans.append("\\\\ \\hline\n");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("(" + estado + ") & ");
			else
				fTrans.append(estado + " & ");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append(problema.mueve(estado, simbolo) + " & ");
			}
			for (int posicion : problema.estado(estado))
				fTrans.append(posicion + " ");
			fTrans.append("\\\\ \\hline\n");
		}
		fTrans.append("\\end{tabular}");

		String expresion = problema.problema();
		expresion = expresion.replace("|", "\\textbar ");
		expresion = expresion.replace("\u2027", "·");
		expresion = expresion.replace("\u03B5", "$\\epsilon$");
		String expresionAumentada = problema.expresionAumentada();
		expresionAumentada = expresionAumentada.replace("$", "\\$");
		expresionAumentada = expresionAumentada.replace("|", "\\textbar ");
		expresionAumentada = expresionAumentada.replace("\u2027", "·");
		expresionAumentada = expresionAumentada
				.replace("\u03B5", "$\\epsilon$");

		plantilla.set("expresion", expresion);
		plantilla.set("aumentada", expresionAumentada);
		plantilla.set("siguientePos", stePos.toString());
		plantilla.set("transicion", fTrans.toString());

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a formato Latex.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresion {}, formato expresión",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.tex");

		// Función de transición
		fTrans.append("\\begin{tabular} {| c | ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c |");
		fTrans.append(" l |}\n\\hline \n& ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & ");
		fTrans.append("\\\\ \\hline\n");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("(" + estado + ") & ");
			else
				fTrans.append(estado + " & ");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append(problema.mueve(estado, simbolo) + " & ");
			}
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			fTrans.append("\\\\ \\hline\n");
		}
		fTrans.append("\\end{tabular}");

		String expresion = problema.problema();
		expresion = expresion.replace("|", "\\textbar ");
		expresion = expresion.replace("\u2027", "·");
		expresion = expresion.replace("\u03B5", "$\\epsilon$");

		plantilla.set("expresion", expresion);
		plantilla.set("transicion", fTrans.toString());

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo autómata
	 * a formato Latex.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresion {}, formato autómata",
				problema.problema());

		String imagen = "" + problema.automata().hashCode();
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.tex");

		// Función de transición
		fTrans.append("\\begin{tabular} {| c | ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c |");
		fTrans.append(" l |}\n\\hline \n& ");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & ");
		fTrans.append("\\\\ \\hline\n");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("(" + estado + ") & ");
			else
				fTrans.append(estado + " & ");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append(problema.mueve(estado, simbolo) + " & ");
			}
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			fTrans.append("\\\\ \\hline\n");
		}
		fTrans.append("\\end{tabular}");

		plantilla.set("imagen", imagen);
		plantilla.set("transicion", fTrans.toString());

		return plantilla;
	}

	/**
	 * Devuelve una representación de un conjunto de elementos separados con
	 * comas.
	 * 
	 * @param lista
	 *            Conjunto de elementos.
	 * @return Representación del conjunto como elementos separados por comas.
	 */
	private String setToString(Set<?> lista) {
		StringBuilder setToString = new StringBuilder();

		if (lista.size() > 0) {
			String prefijo = "";
			for (Object elemento : lista) {
				setToString.append(prefijo);
				prefijo = ", ";
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append("Cjto. vacío");
		}
		return setToString.toString();
	}

}
