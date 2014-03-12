package es.ubu.inf.tfg.doc;

import java.util.ArrayList;
import java.util.List;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public class DocumentoHTML implements Documento {
	private String inicio;
	private List<Object> problemas;
	private String fin;

	private static String cabecera = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body>";
	private static String enunciado = "Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ";
	private static String cabeceraStePos = "<tr><th>n</th><th>stePos(n)</th></tr>";

	public DocumentoHTML() {
		this.inicio = cabecera;
		this.fin = "</html></body>";
		this.problemas = new ArrayList<>();
	}

	@Override
	public void añadirProblema(AhoSethiUllman problema) {
		StringBuilder html = new StringBuilder();

		// enunciado
		html.append("<p><b>");
		html.append(this.problemas.size() + 1);
		html.append(".- ");
		html.append(enunciado);
		html.append(problema.problema());
		html.append("</b></p>");

		// expresión aumentada
		html.append("<p>");
		html.append("Expresión aumentada: ");
		html.append(problema.expresionAumentada());
		html.append("</p>");

		// siguiente-pos
		html.append("<p><table border=\"1\">");
		html.append(cabeceraStePos);
		for (int n : problema.posiciones()) {
			html.append("<tr><td>");
			html.append(n);
			html.append("</td><td>");
			if (problema.siguientePos(n).size() > 0) {
				String prefijo = "";
				for (int pos : problema.siguientePos(n)) {
					html.append(prefijo);
					prefijo = ", ";
					html.append(pos);
				}
			} else {
				html.append("-");
			}
			html.append("</td></tr>");
		}
		html.append("</table></p>");

		// transiciones
		/*
		for (char estado : problema.estados()) {
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					html.append("<p>mueve(");
					html.append(estado);
					html.append(", ");
					html.append(simbolo);
					html.append(") = {");
					char estadoSiguiente = problema.mueve(estado, simbolo);
					String prefijo = "";
					for (int pos : problema.estado(estadoSiguiente)) {
						html.append(prefijo);
						prefijo = ", ";
						html.append(pos);
					}
					html.append("} = ");
					html.append(estadoSiguiente);
					html.append("</p>");
				}
			}
		}
		*/

		// tabla de transiciones
		html.append("<p><table border=\"1\"><tr><th></th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				html.append("<th>" + simbolo + "</th>");
		html.append("<th></th></tr>");

		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				html.append("<tr><td>(" + estado + ")</td>");
			else
				html.append("<tr><td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$')
					html.append("<td>" + problema.mueve(estado, simbolo)
							+ "</td>");
			}
			html.append("<td>");
			for (int posicion : problema.estado(estado))
				html.append(posicion + " ");
			html.append("</td></tr>");
		}
		html.append("</table></p>");

		this.problemas.add(html);
	}

	@Override
	public void eliminarProblema(AhoSethiUllman asu) {

	}

	@Override
	public String toString() {
		StringBuilder documento = new StringBuilder();

		documento.append(this.inicio);
		for (Object problema : this.problemas)
			documento.append(problema);
		documento.append(this.fin);

		return documento.toString();
	}
}
