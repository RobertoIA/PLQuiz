package es.ubu.inf.tfg.doc.datos;

import java.text.MessageFormat;
import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor al formato propietario Moodle XML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorMoodleXML extends Traductor {

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

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema), n++));
		
		String plantilla = formatoIntermedio(plantilla("plantilla.xml"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
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
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASU.xml"));

		// Función de transición
		fTrans.append("\n\t<tr><th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("\n\t<td>{:MULTICHOICE:="
							+ problema.mueve(estado, simbolo)
							+ "#CORRECT~Z#Falso}</td>"); // TODO placeholder
			}
			fTrans.append("\n\t<td>");
			fTrans.append("{:MULTICHOICE:=");
			for (int posicion : problema.estado(estado))
				fTrans.append(posicion + " ");
			if (problema.estado(estado).size() == 0)
				fTrans.append("Cjto. vacio");
			fTrans.append("#CORRECT~0#Falso}"); // TODO placeholder
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		eFinales.append("{:MULTICHOICE:=");
		String prefijo = "";
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado)) {
				eFinales.append(prefijo);
				prefijo = ", ";
				eFinales.append(estado);
			}
		}
		eFinales.append("#Correct");
		eFinales.append("~X, Y, Z#Falso}"); // TODO placeholder

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString(), eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
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
	public String traduce(ConstruccionSubconjuntos problema) {
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCS.xml"));

		// Función de transición
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					fTrans.append("\n\t<td>{:MULTICHOICE:="
							+ problema.mueve(estado, simbolo)
							+ "#CORRECT~Z#Falso}</td>"); // TODO placeholder
			}
			fTrans.append("\n\t<td>");
			fTrans.append("{:MULTICHOICE:=");
			for (int posicion : problema.posiciones(estado))
				fTrans.append(posicion + " ");
			if (problema.posiciones(estado).size() == 0)
				fTrans.append("Cjto. vacio");
			fTrans.append("#CORRECT~0#Falso}"); // TODO placeholder
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		eFinales.append("{:MULTICHOICE:=");
		String prefijo = "";
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado)) {
				eFinales.append(prefijo);
				prefijo = ", ";
				eFinales.append(estado);
			}
		}
		eFinales.append("#Correct");
		eFinales.append("~X, Y, Z#Falso}"); // TODO placeholder

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString(), eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}
}
