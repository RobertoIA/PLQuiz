package es.ubu.inf.tfg.doc.datos;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a formato
	 * HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}, formato construcción",
				problema.problema());

		String plantilla = formatoIntermedio(plantilla("plantillaASUConstruccion.html"));
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);

		for (int i = 0; i < 4; i++)
			imagenes[i] = "http:\\" + alternativas.get(i).hashCode() + ".jpg";

		plantilla = MessageFormat.format(
				plantilla,
				"<%0%>",
				problema.problema(),
				imagenes[0],
				imagenes[1],
				imagenes[2],
				imagenes[3],
				(char) ('a' + alternativas.indexOf(problema.alternativas().get(
						0))));
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a formato
	 * HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}, formato etiquetado",
				problema.problema());

		String url = "http:\\" + problema.arbolVacio().hashCode() + ".jpg";
		String plantilla = formatoIntermedio(plantilla("plantillaASUEtiquetado.html"));
		StringBuilder soluciones = new StringBuilder();

		// cabecera
		soluciones
				.append("<table><tr><th></th><th>anulable?</th><th>primera-pos</th><th>última-pos</th>");
		// contenido
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append("<tr><td>" + simboloActual + "</td>");
			soluciones.append("<td>"
					+ (problema.esAnulable(simboloActual) ? "Si" : "No")
					+ "</td>");
			soluciones
					.append("<td>"
							+ setToString(problema.primeraPos(simboloActual))
							+ "</td>");
			soluciones.append("<td>"
					+ setToString(problema.ultimaPos(simboloActual)) + "</td>");
			soluciones.append("</tr>");

			simboloActual++;
		}

		// cierre
		soluciones.append("</table>");

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), url, soluciones.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a formato HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresion {}, formato tablas",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASUTablas.html"));

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
