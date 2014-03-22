package es.ubu.inf.tfg.doc.datos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor HTML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorHTML implements Traductor {

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
		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(problema, n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.html"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman a formato HTML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduce(AhoSethiUllman problema) {
		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASU.html"));

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
		fTrans.append("<p><table border=\"1\"><tr><th></th>");
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
		fTrans.append("</table></p>");

		plantilla = MessageFormat.format(plantilla, "{0}", problema.problema(),
				problema.expresionAumentada(), stePos.toString(),
				fTrans.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos a formato HTML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a HTML.
	 */
	@Override
	public String traduce(ConstruccionSubconjuntos problema) {
		StringBuilder fTrans = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.html"));

		// Función de transición
		fTrans.append("<p><table border=\"1\"><tr><th></th>");
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
		fTrans.append("</table></p>");

		plantilla = MessageFormat.format(plantilla, "{0}", problema.problema(), fTrans);
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	private String plantilla(String plantilla) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = getClass().getResourceAsStream(plantilla);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			System.err.println("Error al recuperar la plantilla " + plantilla);
			return "";
		}
	}

	private String formatoIntermedio(String plantilla) {
		plantilla = plantilla.replace("{", "\\'{\\'");
		plantilla = plantilla.replace("}", "\\'}\\'");
		plantilla = plantilla.replace("<%", "{");
		plantilla = plantilla.replace("%>", "}");

		return plantilla;
	}

	private String formatoFinal(String plantilla) {
		plantilla = plantilla.replace("\\", "");
		plantilla = plantilla.replace("\'{\'", "{");
		plantilla = plantilla.replace("\'}\'", "}");

		return plantilla;
	}
}
