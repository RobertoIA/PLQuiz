package es.ubu.inf.tfg.doc.datos;

import java.awt.image.BufferedImage;
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
	public String documento(List<Plantilla> problemas) {
		log.info("Generando documento HTML a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (Plantilla problema : problemas) {
			problema.set("numero", "" + n++);
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.html");
		plantilla.set("documento", documento.toString());

		return plantilla.toString();
	}
	
	/**
	 * Genera un documento HTML a partir de un único problema ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @return Documento HTML completo.
	 */
	@Override
	public String traduceProblema(Plantilla problema, int num){
		log.info("Generando documento HTML de un único problema.");
		
		problema.set("numero", "" + num);
		
		Plantilla plantilla = new Plantilla("plantilla.html");
		plantilla.set("documento", problema.toString());
	
		return plantilla.toString();
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
	public Plantilla traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresión {}, formato construcción",
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.html");
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);

		for (int i = 0; i < 4; i++)
			imagenes[i] = "http:\\" + alternativas.get(i).hashCode() + ".jpg";

		int index = alternativas.indexOf(problema.alternativas().get(0));
		String solucion = "" + (char) ('a' + index);

		plantilla.set("expresion", problema.problema());
		plantilla.set("opcionA", imagenes[0]);
		plantilla.set("opcionB", imagenes[1]);
		plantilla.set("opcionC", imagenes[2]);
		plantilla.set("opcionD", imagenes[3]);
		plantilla.set("solucion", solucion);

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
	public Plantilla traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresión {}, formato etiquetado",
				problema.problema());

		String url = "http:\\" + problema.arbolVacio().hashCode() + ".jpg";
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.html");
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
							+ setToRanges(problema.primeraPos(simboloActual)) 
							+ "</td>");
			soluciones.append("<td>"
					+ setToRanges(problema.ultimaPos(simboloActual)) + "</td>"); 
			soluciones.append("</tr>");

			simboloActual++;
		}

		// cierre
		soluciones.append("</table>");

		plantilla.set("expresion", problema.problema());
		plantilla.set("imagen", url);
		plantilla.set("tabla", soluciones.toString());

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
	public Plantilla traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a HTML problema tipo Aho-Sethi-Ullman con expresión {}, formato tablas",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaASUTablas.html");

		// siguiente-pos
		stePos.append("<p><table>");
		stePos.append("<tr><th>n</th><th>stePos(n)</th></tr>");
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>");
			stePos.append(n);
			stePos.append("</td><td>");
			stePos.append(setToRanges(problema.siguientePos(n)));
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
			fTrans.append(setToRanges(problema.estado(estado)));
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla.set("expresion", problema.problema());
		plantilla.set("aumentada", problema.expresionAumentada());
		plantilla.set("siguientePos", stePos.toString());
		plantilla.set("transicion", fTrans.toString());

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * construcción a formato HTML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public Plantilla traduceCSConstruccion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcción de subconjuntos con expresión {}, formato construcción",
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaCSConstruccion.html");
		
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);

		for (int i = 0; i < 4; i++)
			imagenes[i] = "http:\\" + alternativas.get(i).hashCode() + ".jpg";

		int index = alternativas.indexOf(problema.alternativas().get(0));
		String solucion = "" + (char) ('a' + index);

		plantilla.set("expresion", problema.problema());
		plantilla.set("opcionA", imagenes[0]);
		plantilla.set("opcionB", imagenes[1]);
		plantilla.set("opcionC", imagenes[2]);
		plantilla.set("opcionD", imagenes[3]);
		plantilla.set("solucion", solucion);

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
	public Plantilla traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcción de subconjuntos con expresión {}, formato expresión",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.html");

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
			fTrans.append(setToRanges(problema.posiciones(estado)));
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla.set("expresion", problema.problema());
		plantilla.set("transicion", fTrans.toString());

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
	public Plantilla traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a HTML problema tipo construcción de subconjuntos con expresión {}, formato autómata",
				problema.problema());

		String url = "http:\\" + problema.automata().hashCode() + ".jpg";
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.html");

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
			fTrans.append(setToRanges(problema.posiciones(estado)));
			fTrans.append("</td></tr>");
		}
		fTrans.append("</table>");

		plantilla.set("imagen", url);
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
	@SuppressWarnings("unused")
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
		int last=0, first=0, length=0; // inicializo para evitar warnings
		
		if (lista.size() == 0) {
			out.append("\u2205"); // Unicode empty set
		} else {
			String sep = "";
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
					sep = ", ";
					if (length == 1) {
						out.append(""+last);
					} else if (length == 2) {
						out.append(""+first+", "+last);
					} else {
						out.append(""+first+"-"+last);
					}
					last = first = (int) e;
					length = 1;
				}
			}
			out.append(sep);
			if (length == 1) {
				out.append(""+last);
			} else if (length == 2) {
				out.append(""+first+", "+last);
			} else {
				out.append(""+first+"-"+last);
			}
		}
		return out.toString();
	}

}
