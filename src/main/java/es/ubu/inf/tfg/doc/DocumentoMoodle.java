package es.ubu.inf.tfg.doc;

import java.util.ArrayList;
import java.util.List;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public class DocumentoMoodle implements Documento {
	private List<String> problemas;

	private static final String cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><quiz>";
	private static final String cierre = "</quiz>";
	private static final String enunciadoASU = "Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ";

	public DocumentoMoodle() {
		this.problemas = new ArrayList<>();
	}

	@Override
	public void añadirProblema(AhoSethiUllman problema) {
		this.problemas.add(AhoSethiUllmanToXML(problema));
	}

	@Override
	public void eliminarProblema(AhoSethiUllman problema) {
		this.problemas.remove(AhoSethiUllmanToXML(problema));
	}

	private String AhoSethiUllmanToXML(AhoSethiUllman problema) {
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
					xml.append("<td>{:MULTICHOICE:=" + problema.mueve(estado, simbolo)
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

	@Override
	public String toString() {
		StringBuilder documento = new StringBuilder();

		documento.append(cabecera);
		int n = 1;
		for (Object problema : this.problemas) {
			documento.append("<!-- question: " + (n++) + "  --><question type=\"cloze\"><name><text>PLQuiz</text></name><questiontext><text><![CDATA[");
			documento.append(problema);
			documento.append("]]></text></questiontext><generalfeedback><text></text></generalfeedback><shuffleanswers>0</shuffleanswers></question>");
		}
		documento.append(cierre);

		return documento.toString();
	}
}
