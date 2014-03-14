package es.ubu.inf.tfg.doc.trad;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

/**
 * Implementa un traductor al formato propietario Moodle XML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorMoodleXML implements Traductor {
	private static final String cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz>";
	private static final String cierre = "</quiz>";
	private static final String enunciadoASU = "Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ";

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
							+ "  --><question type=\"cloze\"><name><text>PLQuiz</text></name><questiontext><text><![CDATA[");
			documento.append(problema);
			documento
					.append("]]></text></questiontext><generalfeedback><text></text></generalfeedback><shuffleanswers>0</shuffleanswers></question>");
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
		xml.append("<p>Función de transición:");
		xml.append("<table border=\"1\"><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				xml.append("<th>" + simbolo + "</th>");
		xml.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				xml.append("<tr><td>(" + estado + ")</td>");
			else
				xml.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					xml.append("<td>{:MULTICHOICE:="
							+ problema.mueve(estado, simbolo)
							+ "#CORRECT~Z#Falso}</td>"); // TODO placeholder
			}
			xml.append("<td>");
			xml.append("{:MULTICHOICE:=");
			for (int posicion : problema.estado(estado))
				xml.append(posicion + " ");
			if (problema.estado(estado).size() == 0)
				xml.append("Cjto. vacio");
			xml.append("#CORRECT~0#Falso}"); // TODO placeholder
			xml.append("</td></tr>");
		}
		xml.append("</table></p>");

		// Estados finales
		xml.append("\nLos estados finales son: ");
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

}
