package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor HTML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorHTML extends Traductor {

	private static final Logger log = LoggerFactory
			.getLogger(TraductorHTML.class);

	/**
	 * Genera un documento HTML a partir de una lista de problemas ya
	 * traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento HTML completo.
	 */
	@Override
	public String documento(List<String> problemas) {
		log.info("Generando documento HTML a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema),
					n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.html"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo completo a formato
	 * HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceASUCompleto(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}, formato completo",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASUCompleto.html"));

		// siguiente-pos
		stePos.append("<p><table>");
		stePos.append("<tr><th>n</th><th>stePos(n)</th></tr>");
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>");
			stePos.append(n);
			stePos.append("</td><td>");
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
			stePos.append("</td></tr>");
		}
		stePos.append("</table></p>");

		// Función de transición
		fTrans.append("<table><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>");
		fTrans.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>");
			else
				fTrans.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			fTrans.append("<td>");
			for (int posicion : problema.estado(estado))
				fTrans.append(posicion + " ");
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), problema.expresionAumentada(),
				stePos.toString(), fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo árbol a formato HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceASUArbol(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}, formato árbol",
				problema.problema());
		
		String url = "http:\\" + problema.arbolVacio().hashCode() + ".jpg";
		String plantilla = formatoIntermedio(plantilla("plantillaASUArbol.html"));
		String soluciones = ""; // TODO
		
		plantilla = MessageFormat.format(plantilla, "<%0%>", problema.problema(), url, soluciones);
		plantilla = formatoFinal(plantilla);
		
		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a formato HTML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcción de subconjuntos con expresion {}, formato expresión",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSExpresion.html"));

		// Función de transición
		fTrans.append("<table><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>");
		fTrans.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>");
			else
				fTrans.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			fTrans.append("<td>");
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a formato HTML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcción de subconjuntos con expresion {}, formato autómata",
				problema.problema());

		String url = "http:\\" + problema.automata().hashCode() + ".jpg";
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSAutomata.html"));

		// Función de transición
		fTrans.append("<table><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>");
		fTrans.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>");
			else
				fTrans.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			fTrans.append("<td>");
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>", url,
				fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}
}
