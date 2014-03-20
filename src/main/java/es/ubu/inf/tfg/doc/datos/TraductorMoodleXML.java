package es.ubu.inf.tfg.doc.datos;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.Thompson;

/**
 * Implementa un traductor al formato propietario Moodle XML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorMoodleXML implements Traductor {
	private static final String cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<quiz>";
	private static final String cierre = "\n</quiz>";
	private static final String enunciadoASU = "Completa la tabla de la función de transición del AFD que se obtendría de aplicar el método de Aho-Sethi-Ullman a la expresión regular ";
	private static final String enunciadoCS = "Completa la tabla de la función de transición para el AFD que se obtendría al aplicar el método de construcción de subconjuntos al AFND de la expresión regular ";

	/**
	 * Genera un documento en formato Moodle XML a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String documento(List<String> problemas) {
		StringBuilder documento = new StringBuilder();

		documento.append(cabecera);
		int n = 1;
		for (Object problema : problemas) {
			documento
					.append("<!-- question: "
							+ (n++)
							+ "  -->\n<question type=\"cloze\">\n<name>\n<text>PLQuiz</text>\n</name>\n<questiontext>\n<text><![CDATA[");
			documento.append(problema);
			documento
					.append("]]></text>\n</questiontext>\n<generalfeedback><text></text></generalfeedback>\n<shuffleanswers>0</shuffleanswers>\n</question>\n\n");
		}
		documento.append(cierre);

		return documento.toString();
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduce(AhoSethiUllman problema) {
		StringBuilder xml = new StringBuilder();

		xml.append("<p>");
		xml.append(enunciadoASU);
		xml.append(problema.problema());
		xml.append("</p>");

		// Función de transición
		xml.append("\n\t<table border=\"1\" cellspacing=\"0\" align=\"left\">\n\t<tbody>");
		xml.append("\n\t<tr><th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				xml.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		xml.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			xml.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					xml.append("\n\t<td>{:MULTICHOICE:="
							+ problema.mueve(estado, simbolo)
							+ "#CORRECT~Z#Falso}</td>"); // TODO placeholder
			}
			xml.append("\n\t<td>");
			xml.append("{:MULTICHOICE:=");
			for (int posicion : problema.estado(estado))
				xml.append(posicion + " ");
			if (problema.estado(estado).size() == 0)
				xml.append("Cjto. vacio");
			xml.append("#CORRECT~0#Falso}"); // TODO placeholder
			xml.append("</td>\n\t</tr>");
		}
		xml.append("\n\t</tbody>\n\t</table>");

		// Estados finales
		for (int i = 0; i < problema.estados().size() * 2 + 2; i++)
			xml.append("\n\t<p><br /></p>"); //TODO espacio mínimo?
		xml.append("\n\tLos estados finales son: ");
		xml.append(" {:MULTICHOICE:=");
		String prefijo = "";
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado)) {
				xml.append(prefijo);
				prefijo = ", ";
				xml.append(estado);
			}
		}
		xml.append("#Correct");
		xml.append("~X, Y, Z#Falso}"); // TODO placeholder

		return xml.toString();
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos a formato Moodle
	 * XML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduce(Thompson problema) {
		StringBuilder xml = new StringBuilder();

		xml.append("<p>");
		xml.append(enunciadoCS);
		xml.append(problema.problema());
		xml.append("</p>");

		// Función de transición
		xml.append("\n\t<table border=\"1\" cellspacing=\"0\" align=\"left\">\n\t<tbody>");
		xml.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				xml.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		xml.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			xml.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					xml.append("\n\t<td>{:MULTICHOICE:="
							+ problema.mueve(estado, simbolo)
							+ "#CORRECT~Z#Falso}</td>"); // TODO placeholder
			}
			xml.append("\n\t<td>");
			xml.append("{:MULTICHOICE:=");
			for (int posicion : problema.posiciones(estado))
				xml.append(posicion + " ");
			if (problema.posiciones(estado).size() == 0)
				xml.append("Cjto. vacio");
			xml.append("#CORRECT~0#Falso}"); // TODO placeholder
			xml.append("</td>\n\t</tr>");
		}
		xml.append("\n\t</tbody>\n\t</table>");

		// Estados finales
		for (int i = 0; i < problema.estados().size() * 2 + 2; i++)
			xml.append("\n\t<p><br /></p>"); //TODO espacio mínimo?
		xml.append("\n<p>Los estados finales son: ");
		xml.append(" {:MULTICHOICE:=");
		String prefijo = "";
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado)) {
				xml.append(prefijo);
				prefijo = ", ";
				xml.append(estado);
			}
		}
		xml.append("#Correct");
		xml.append("~X, Y, Z#Falso}"); // TODO placeholder
		xml.append("</p>");

		return xml.toString();
	}
}
