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
			problema.set("numero", "" + n++); //$NON-NLS-1$ //$NON-NLS-2$
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.html"); //$NON-NLS-1$
		plantilla.set("documento", documento.toString()); //$NON-NLS-1$

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
		
		problema.set("numero", "" + num); //$NON-NLS-1$ //$NON-NLS-2$
		
		Plantilla plantilla = new Plantilla("plantilla.html"); //$NON-NLS-1$
		plantilla.set("documento", problema.toString()); //$NON-NLS-1$
	
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

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.html"); //$NON-NLS-1$
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);

		for (int i = 0; i < 4; i++)
			imagenes[i] = "http:\\" + Math.abs(alternativas.get(i).hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$

		int index = alternativas.indexOf(problema.alternativas().get(0));
		String solucion = "" + (char) ('a' + index); //$NON-NLS-1$

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("opcionA", imagenes[0]); //$NON-NLS-1$
		plantilla.set("opcionB", imagenes[1]); //$NON-NLS-1$
		plantilla.set("opcionC", imagenes[2]); //$NON-NLS-1$
		plantilla.set("opcionD", imagenes[3]); //$NON-NLS-1$
		plantilla.set("solucion", solucion); //$NON-NLS-1$

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

		String url = "http:\\" + Math.abs(problema.arbolVacio().hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.html"); //$NON-NLS-1$
		StringBuilder soluciones = new StringBuilder();

		// cabecera
		soluciones.append("<table><tr><th><em>"); //$NON-NLS-1$
		soluciones.append(Messages.getString("TraductorHTML.node")); //$NON-NLS-1$
		soluciones.append("</em></th><th><em>"); //$NON-NLS-1$
		soluciones.append(Messages.getString("TraductorHTML.nullable")); //$NON-NLS-1$
		soluciones.append("</em></th><th><em>"); //$NON-NLS-1$
		soluciones.append(Messages.getString("TraductorHTML.firstpos")); //$NON-NLS-1$
		soluciones.append("</em></th><th><em>"); //$NON-NLS-1$
		soluciones.append(Messages.getString("TraductorHTML.lastpos")); //$NON-NLS-1$
		soluciones.append("</em></th></tr>\n\t\t\t"); //$NON-NLS-1$
		// contenido
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append("<tr><td>" + simboloActual + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			soluciones.append("<td>" //$NON-NLS-1$
					+ (problema.esAnulable(simboloActual) ? Messages.getString("TraductorHTML.yes") : "No") //$NON-NLS-1$ //$NON-NLS-2$
					+ "</td>"); //$NON-NLS-1$
			soluciones
					.append("<td>" //$NON-NLS-1$
							+ setToRanges(problema.primeraPos(simboloActual)) 
							+ "</td>"); //$NON-NLS-1$
			soluciones.append("<td>" //$NON-NLS-1$
					+ setToRanges(problema.ultimaPos(simboloActual)) + "</td>");  //$NON-NLS-1$
			soluciones.append("</tr>"); //$NON-NLS-1$

			simboloActual++;
		}

		// cierre
		soluciones.append("</table>"); //$NON-NLS-1$

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("imagen", url); //$NON-NLS-1$
		plantilla.set("tabla", soluciones.toString()); //$NON-NLS-1$

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

		Plantilla plantilla = new Plantilla("plantillaASUTablas.html"); //$NON-NLS-1$

		// siguiente-pos
		stePos.append("<p><table><tr><th><em>n</em></th><th><em>"); //$NON-NLS-1$
		stePos.append(Messages.getString("TraductorHTML.followpos")); //$NON-NLS-1$
		stePos.append("</em>(<em>n</em>)</th></tr>"); //$NON-NLS-1$
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>"); //$NON-NLS-1$
			stePos.append(n);
			stePos.append("</td><td>"); //$NON-NLS-1$
			stePos.append(setToRanges(problema.siguientePos(n)));
			stePos.append("</td></tr>"); //$NON-NLS-1$
		}
		stePos.append("</table></p>"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("<table><tr><th></th>"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>"); //$NON-NLS-1$ //$NON-NLS-2$
		fTrans.append("<th></th></tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			else
				fTrans.append("<tr><td>" + estado + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo) //$NON-NLS-1$
							+ "</td>"); //$NON-NLS-1$
			}
			fTrans.append("<td>"); //$NON-NLS-1$
			fTrans.append(setToRanges(problema.estado(estado)));
			fTrans.append("</td></tr>"); //$NON-NLS-1$
		}
		fTrans.append("</table>"); //$NON-NLS-1$

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("aumentada", problema.expresionAumentada()); //$NON-NLS-1$
		plantilla.set("siguientePos", stePos.toString()); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$

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

		Plantilla plantilla = new Plantilla("plantillaCSConstruccion.html"); //$NON-NLS-1$
		
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		Collections.shuffle(alternativas);

		for (int i = 0; i < 4; i++)
			imagenes[i] = "http:\\" + Math.abs(alternativas.get(i).hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$

		int index = alternativas.indexOf(problema.alternativas().get(0));
		String solucion = "" + (char) ('a' + index); //$NON-NLS-1$

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("opcionA", imagenes[0]); //$NON-NLS-1$
		plantilla.set("opcionB", imagenes[1]); //$NON-NLS-1$
		plantilla.set("opcionC", imagenes[2]); //$NON-NLS-1$
		plantilla.set("opcionD", imagenes[3]); //$NON-NLS-1$
		plantilla.set("solucion", solucion); //$NON-NLS-1$

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

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.html"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("<table><tr><th></th>"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>"); //$NON-NLS-1$ //$NON-NLS-2$
		fTrans.append("<th></th></tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			else
				fTrans.append("<tr><td>" + estado + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo) //$NON-NLS-1$
							+ "</td>"); //$NON-NLS-1$
			}
			fTrans.append("<td>"); //$NON-NLS-1$
			fTrans.append(setToRanges(problema.posiciones(estado)));
			fTrans.append("</td></tr>"); //$NON-NLS-1$
		}
		fTrans.append("</table>"); //$NON-NLS-1$

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$

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

		String url = "http:\\" + Math.abs(problema.automata().hashCode()) + ".jpg"; //$NON-NLS-1$ //$NON-NLS-2$
		StringBuilder fTrans = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.html"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("<table><tr><th></th>"); //$NON-NLS-1$  // Q/𝚺
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("<th>" + simbolo + "</th>"); //$NON-NLS-1$ //$NON-NLS-2$
		fTrans.append("<th></th></tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				fTrans.append("<tr><td>(" + estado + ")</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			else
				fTrans.append("<tr><td>" + estado + "</td>"); //$NON-NLS-1$ //$NON-NLS-2$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("<td>" + problema.mueve(estado, simbolo) //$NON-NLS-1$
							+ "</td>"); //$NON-NLS-1$
			}
			fTrans.append("<td>"); //$NON-NLS-1$
			fTrans.append(setToRanges(problema.posiciones(estado)));
			fTrans.append("</td></tr>"); //$NON-NLS-1$
		}
		fTrans.append("</table>"); //$NON-NLS-1$

		plantilla.set("imagen", url); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$

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
			setToString.append(Messages.getString("TraductorHTML.emptySet")); //$NON-NLS-1$
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
			out.append("\u2205"); // Unicode empty set //$NON-NLS-1$
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
						out.append(""+first+", "+last); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						out.append(""+first+"-"+last); //$NON-NLS-1$ //$NON-NLS-2$
					}
					last = first = (int) e;
					length = 1;
				}
			}
			out.append(sep);
			if (length == 1) {
				out.append(""+last); //$NON-NLS-1$
			} else if (length == 2) {
				out.append(""+first+", "+last); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				out.append(""+first+"-"+last); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return out.toString();
	}
}
