package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
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
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a formato
	 * Latex. En latex el problema de construcción consiste en dibujar el árbol,
	 * no en identificar el correcto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Latex.
	 */
	@Override
	public String traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato construcción",
				problema.problema());

		String plantilla = formatoIntermedio(plantilla("plantillaASUConstruccion.tex"));
		String imagen = problema.alternativas().get(0).hashCode() + "";
		// String[] imagenes = new String[4];
		// List<BufferedImage> alternativas = problema.alternativas();
		// Collections.shuffle(alternativas);
		//
		// for(int i = 0; i < 4; i++)
		// imagenes[i] = "" + alternativas.get(i).hashCode();
		//
		 String expresion = problema.problema();
		 expresion = expresion.replace("|", "\\textbar ");
		 expresion = expresion.replace("\u2027", "·");
		 expresion = expresion.replace("\u03B5", "$\\epsilon$");
		 expresion = expresion.replace("$", "\\$");
		//
		// plantilla = MessageFormat.format(plantilla, "<%0%>",
		// expresion, imagenes[0], imagenes[1], imagenes[2],
		// imagenes[3], (char) ('a' +
		// alternativas.indexOf(problema.alternativas().get(0))));
		 
		 plantilla = MessageFormat.format(plantilla, "<%0%>", expresion, imagen);
		 plantilla = formatoFinal(plantilla);

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
	public String traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato etiquetado",
				problema.problema());

		String imagen = "" + problema.arbolVacio().hashCode();
		String plantilla = formatoIntermedio(plantilla("plantillaASUEtiquetado.tex"));
		StringBuilder soluciones = new StringBuilder();

		soluciones.append(" & tipo & primera-pos & última-pos");
		soluciones.append("\\\\ \\hline\n");
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append(simboloActual + " & ");
			soluciones.append(problema.tipo(simboloActual) + " & ");
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

		plantilla = MessageFormat.format(plantilla, "<%0%>", expresion, imagen,
				solucionesL);
		plantilla = formatoFinal(plantilla);

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
	public String traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Latex problema tipo Aho-Sethi-Ullman con expresion {}, formato tablas",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASUTablas.tex"));

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

		plantilla = MessageFormat.format(plantilla, "<%0%>", expresion,
				expresionAumentada, stePos.toString(), fTrans.toString());
		plantilla = formatoFinal(plantilla);

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
	public String traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresion {}, formato expresión",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSExpresion.tex"));

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

		plantilla = MessageFormat.format(plantilla, "<%0%>", expresion,
				fTrans.toString());
		plantilla = formatoFinal(plantilla);

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
	public String traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Latex problema tipo construcción de subconjuntos con expresion {}, formato autómata",
				problema.problema());

		String imagen = "" + problema.automata().hashCode();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSAutomata.tex"));

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

		plantilla = MessageFormat.format(plantilla, "<%0%>", imagen,
				fTrans.toString());
		plantilla = formatoFinal(plantilla);

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
