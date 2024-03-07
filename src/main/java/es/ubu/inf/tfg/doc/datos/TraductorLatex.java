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
		log.info("Generando documento Latex a partir de {} problemas.", //$NON-NLS-1$
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (Plantilla problema : problemas) {
			problema.set("numero", "" + n++); //$NON-NLS-1$
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.tex"); //$NON-NLS-1$
		plantilla.set("documento", documento.toString()); //$NON-NLS-1$

		return plantilla.toString();
	}


	/**
	 * Genera un documento Latex a partir de un único problema ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @return Documento Latex completo.
	 */
	@Override
	public String traduceProblema(Plantilla problema, int num){
		log.info("Generando documento Latex de un único problema."); //$NON-NLS-1$
		
		problema.set("numero", "" + num); //$NON-NLS-1$
		
		Plantilla plantilla = new Plantilla("plantilla.tex"); //$NON-NLS-1$
		plantilla.set("documento", problema.toString()); //$NON-NLS-1$
	
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
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresión {}, formato construcción", //$NON-NLS-1$
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.tex"); //$NON-NLS-1$
		String imagen = Math.abs(problema.alternativas().get(0).hashCode()) + ""; //$NON-NLS-1$

		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresion = expresion.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresion = expresion.replace("$", "\\$ "); //$NON-NLS-1$
		expresion = expresion.replace("*", "^*"); //$NON-NLS-1$

		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphicssol"); //$NON-NLS-1$

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
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresión {}, formato etiquetado", //$NON-NLS-1$
				problema.problema());

		String imagen = "" + Math.abs(problema.arbolVacio().hashCode()); //$NON-NLS-1$
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.tex"); //$NON-NLS-1$
		StringBuilder soluciones = new StringBuilder();
		
		soluciones.append("\\toprule % =============================\n"); //$NON-NLS-1$
		soluciones.append(Messages.getString("TraductorLatex.tableHeader")); //$NON-NLS-1$
		soluciones.append("\\\\ \n"); //$NON-NLS-1$
		soluciones.append("\\midrule %-------------------------------\n"); //$NON-NLS-1$
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append(simboloActual + " & "); //$NON-NLS-1$
			soluciones
					.append((problema.esAnulable(simboloActual) ? Messages.getString("TraductorLatex.yes") : "\\h{No}") //$NON-NLS-1$
							+ " & "); //$NON-NLS-1$
			soluciones.append("\\h{" + setToRanges(problema.primeraPos(simboloActual)) + "}"  //$NON-NLS-1$
					+ " & "); //$NON-NLS-1$
			soluciones.append("\\h{" + setToRanges(problema.ultimaPos(simboloActual)) + "}"   //$NON-NLS-1$
					+ "\\\\\n"); //$NON-NLS-1$

			simboloActual++;
		}
		soluciones.append("\\bottomrule % =============================\n"); //$NON-NLS-1$
		soluciones.append("\\end{tabular}"); //$NON-NLS-1$

		String solucionesL = soluciones.toString();
		solucionesL = solucionesL.replace("|", "\\textbar "); //$NON-NLS-1$
		solucionesL = solucionesL.replace("\u2027", "·"); //$NON-NLS-1$
		solucionesL = solucionesL.replace("\u03B5", "$\\epsilon$"); //$NON-NLS-1$
		solucionesL = solucionesL.replace("$", "\\$"); //$NON-NLS-1$
		solucionesL = solucionesL.replace("\\$\\varnothing\\$", "$ \\varnothing $"); //$NON-NLS-1$
		
		solucionesL = "\\rowcolors{2}{gray!25}{white}\n" + "\\begin{tabular} {c@{\\hspace{4mm}}c@{\\hspace{4mm}}c@{\\hspace{4mm}}c}\n" //$NON-NLS-1$
				+ solucionesL;

		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresion = expresion.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresion = expresion.replace("$", "\\$"); //$NON-NLS-1$
		expresion = expresion.replace("*", "^*"); //$NON-NLS-1$

		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("tabla", solucionesL); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphics"); //$NON-NLS-1$

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
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresión {}, formato tablas", //$NON-NLS-1$
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaASUTablas.tex"); //$NON-NLS-1$
		String imagen = Math.abs(problema.alternativas().get(0).hashCode()) + ""; //$NON-NLS-1$

		// siguiente-pos
		stePos.append("\\rowcolors{2}{gray!25}{white}\n"); //$NON-NLS-1$
		stePos.append("\\begin{tabular} {c@{\\hspace{4mm}}l}\n\\toprule % =============================\n$n$ & \\emph{"); ; //$NON-NLS-1$
		stePos.append(Messages.getString("TraductorLatex.followpos"));
		stePos.append("}($n$) \\\\\n\\midrule %-------------------------------\n"); //$NON-NLS-1$
		for (int n : problema.posiciones()) {
			stePos.append(n);
			stePos.append(" & "); //$NON-NLS-1$
			stePos.append("\\h{" + setToRanges(problema.siguientePos(n)) + "}"); //$NON-NLS-1$
			stePos.append(" \\\\ \n"); //$NON-NLS-1$
		}
		stePos.append("\\bottomrule % =============================\n"); //$NON-NLS-1$
		stePos.append("\\end{tabular}"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("\\rowcolors{2}{gray!25}{white}\n"); //$NON-NLS-1$
		fTrans.append("\\begin{tabular} {c@{\\hspace{4mm}}"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c"); //$NON-NLS-1$
		fTrans.append("@{\\hspace{4mm}}l}\n\\toprule % ============================= \n$\\mathcal{Q}$ & "); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & "); //$NON-NLS-1$
		fTrans.append("\\emph{"); //$NON-NLS-1$
		fTrans.append(Messages.getString("TraductorLatex.positions")); //$NON-NLS-1$
		fTrans.append("}\\\\ \n\\midrule %-------------------------------\n"); //$NON-NLS-1$
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("\\h{(" + estado + ")} & "); //$NON-NLS-1$
			else
				fTrans.append("\\h{" + estado + "} & "); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("\\h{" + problema.mueve(estado, simbolo) + "} & "); //$NON-NLS-1$
			}
			fTrans.append("\\h{" + setToRanges(problema.estado(estado)) + "}"); //$NON-NLS-1$
			//for (int posicion : problema.estado(estado)) fTrans.append(posicion + " ");
			fTrans.append("\\\\ \n"); //$NON-NLS-1$
		}
		fTrans.append("\\bottomrule % =============================\n"); //$NON-NLS-1$
		fTrans.append("\\end{tabular}"); //$NON-NLS-1$

		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresion = expresion.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresion = expresion.replace("$", "\\$ "); //$NON-NLS-1$
		expresion = expresion.replace("*", "^*"); //$NON-NLS-1$
		String expresionAumentada = problema.expresionAumentada();
		expresionAumentada = expresionAumentada.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresionAumentada = expresionAumentada.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresionAumentada = expresionAumentada.replace("$", "\\$ "); //$NON-NLS-1$
		expresionAumentada = expresionAumentada.replace("*", "^*"); //$NON-NLS-1$
		expresionAumentada = "$ " + expresionAumentada + " $"; //$NON-NLS-1$

		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("aumentada", expresionAumentada); //$NON-NLS-1$
		plantilla.set("siguientePos", stePos.toString()); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphicssol"); //$NON-NLS-1$

		return plantilla;
	}


	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * construcción a formato Latex.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public Plantilla traduceCSConstruccion(ConstruccionSubconjuntos problema) {
		log.info("Traduciendo a Latex problema tipo construcción de subconjuntos con expresión {}, formato construcción", //$NON-NLS-1$
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaCSConstruccion.tex"); //$NON-NLS-1$
		String imagen = Math.abs(problema.automata().hashCode()) + ""; //$NON-NLS-1$
		
		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresion = expresion.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresion = expresion.replace("$", "\\$ "); //$NON-NLS-1$
		expresion = expresion.replace("*", "^*"); //$NON-NLS-1$
		
		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphicssol"); //$NON-NLS-1$

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
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresión {}, formato expresión", //$NON-NLS-1$
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.tex"); //$NON-NLS-1$
		String imagen = Math.abs(problema.automata().hashCode()) + ""; //$NON-NLS-1$

		// Función de transición
		fTrans.append("\\rowcolors{2}{gray!25}{white}\n"); //$NON-NLS-1$
		fTrans.append("\\begin{tabular} {c@{\\hspace{4mm}}"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c"); //$NON-NLS-1$
		fTrans.append("@{\\hspace{4mm}}l}\n\\toprule % =============================\n$\\mathcal{Q}$ & "); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & "); //$NON-NLS-1$
		fTrans.append("\\emph{"); //$NON-NLS-1$
		fTrans.append(Messages.getString("TraductorLatex.NFAStates")); //$NON-NLS-1$
		fTrans.append("}\\\\\n"); //$NON-NLS-1$
		fTrans.append("\\midrule %-------------------------------\n"); //$NON-NLS-1$
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("\\h{(" + estado + ")} & "); //$NON-NLS-1$
			else
				fTrans.append("\\h{" + estado + "} & "); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("\\h{" + problema.mueve(estado, simbolo) + "} & "); //$NON-NLS-1$
			}
			fTrans.append("\\h{" + setToRanges(problema.posiciones(estado)) + "}"); //$NON-NLS-1$
			//for (int posicion : problema.posiciones(estado)) fTrans.append(posicion + " ");
			fTrans.append("\\\\\n"); //$NON-NLS-1$
		}
		fTrans.append("\\bottomrule % =============================\n"); //$NON-NLS-1$
		fTrans.append("\\end{tabular}"); //$NON-NLS-1$

		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "\\cdot ").replace("·", "\\cdot ").replace(".", "\\cdot "); //$NON-NLS-1$
		expresion = expresion.replace("\u03B5", "\\epsilon "); //$NON-NLS-1$
		expresion = expresion.replace("$", "\\$ "); //$NON-NLS-1$
		expresion = expresion.replace("*", "^*"); //$NON-NLS-1$

		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphicssol"); //$NON-NLS-1$

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
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresión {}, formato autómata", //$NON-NLS-1$
				problema.problema());

		String imagen = "" + Math.abs(problema.automata().hashCode()); //$NON-NLS-1$
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.tex"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("\\rowcolors{2}{gray!25}{white}\n"); //$NON-NLS-1$
		fTrans.append("\\begin{tabular} {c@{\\hspace{4mm}}"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("c"); //$NON-NLS-1$
		fTrans.append("@{\\hspace{4mm}}l}\n\\toprule % =============================\n$\\mathcal{Q}$ & "); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append(simbolo + " & "); //$NON-NLS-1$
		fTrans.append("\\emph{"); //$NON-NLS-1$
		fTrans.append(Messages.getString("TraductorLatex.NFAStates")); //$NON-NLS-1$
		fTrans.append("}\\\\\n"); //$NON-NLS-1$
		fTrans.append("\\midrule %-------------------------------\n"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("\\h{(" + estado + ")} & "); //$NON-NLS-1$
			else
				fTrans.append("\\h{" + estado + "} & "); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("\\h{" + problema.mueve(estado, simbolo) + "} & "); //$NON-NLS-1$
			}
			fTrans.append("\\h{" + setToRanges(problema.posiciones(estado)) + "}"); //$NON-NLS-1$
			fTrans.append("\\\\\n"); //$NON-NLS-1$
		}
		fTrans.append("\\bottomrule % =============================\n"); //$NON-NLS-1$
		fTrans.append("\\end{tabular}"); //$NON-NLS-1$

		plantilla.set("imagen", imagen); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("includetool", "\\myincludegraphics"); //$NON-NLS-1$

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
	@SuppressWarnings("unused")
	private String setToString(Set<?> lista) {
		StringBuilder setToString = new StringBuilder();

		if (lista.size() > 0) {
			String prefijo = ""; //$NON-NLS-1$
			for (Object elemento : lista) {
				setToString.append(prefijo);
				prefijo = ", "; //$NON-NLS-1$
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append(Messages.getString("TraductorLatex.emptySet")); //$NON-NLS-1$
		}
		return setToString.toString();
	}


	/**
	 * Devuelve una representación de un conjunto de elementos como rangos separados por comas.
	 * 
	 * @param lista
	 *            Conjunto de elementos.
	 * @return Representación del conjunto como rangos separados por comas.
	 */
	// TODO: ¿se podría mover a la clase padre?
	private String setToRanges(Set<?> lista) {
		StringBuilder out = new StringBuilder();
		log.info("Ejecutando setToRanges({})", lista); //$NON-NLS-1$
		int last=0, first=0, length=0; // inicializo para evitar warnings
		
		if (lista.size() == 0) {
			//out.append("\u2205"); // Unicode empty set
			out.append("$\\varnothing$"); // LaTeX empty set //$NON-NLS-1$
		} else {
			String sep = ""; //$NON-NLS-1$
			boolean isFirst = true;
			for (Object e: lista) {
				if (isFirst) {
					last = first = (int) e;
					length = 1;
					isFirst = false;
					continue;
				}
				if ((int) e - last == 1) {
					last = (int) e;
					length++;
				} else {
					out.append(sep);
					sep = ", "; //$NON-NLS-1$
					if (length == 1) {
						out.append(""+last); //$NON-NLS-1$
					} else if (length == 2) {
						out.append(""+first+", "+last); //$NON-NLS-1$
					} else {
						out.append(""+first+"-"+last); //$NON-NLS-1$
					}
					last = first = (int) e;
					length = 1;
				}
			}
			out.append(sep);
			if (length == 1) {
				out.append(""+last); //$NON-NLS-1$
			} else if (length == 2) {
				out.append(""+first+", "+last); //$NON-NLS-1$
			} else {
				out.append(""+first+"-"+last); //$NON-NLS-1$
			}
		}
		return out.toString();
	}
}
