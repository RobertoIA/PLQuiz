package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
import java.util.List;

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
	public String documento(List<String> problemas) {
		log.info("Generando documento Latex a partir de {} problemas.",
				problemas.size());
		
		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema),
					n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.tex"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman a formato Latex.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public String traduce(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}",
				problema.problema());
		
		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASU.tex"));

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
					fTrans.append(problema.mueve(estado, simbolo)
							+ " & ");
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
		expresionAumentada = expresionAumentada.replace("\u03B5", "$\\epsilon$");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				expresion, expresionAumentada,
				stePos.toString(), fTrans.toString());
		plantilla = formatoFinal(plantilla);
		
		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos a formato Latex.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public String traduce(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresion {}",
				problema.problema());
		
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.tex"));

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
					fTrans.append(problema.mueve(estado, simbolo)
							+ " & ");
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

		plantilla = MessageFormat.format(plantilla, "<%0%>", expresion, fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	@Override
	public String traduceASU(AhoSethiUllman problema) {
		return traduce(problema);
	}

	@Override
	public String traduceCS_Expresion(ConstruccionSubconjuntos problema) {
		return traduce(problema);
	}

	@Override
	public String traduceCS_Automata(ConstruccionSubconjuntos problema) {
		return traduce(problema);
	}

}
