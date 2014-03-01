package es.ubu.inf.tfg.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public class DocumentoMoodle implements Documento {
	private String inicio;
	private List<String> problemas;
	private String fin;

	private static String cabecera = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><style>td, th {border: 1px solid black; padding:5px;} table {border-collapse: collapse;}</style></head><body>";
	private static String enunciado = "Aplicar el algoritmo de Aho-Sethi-Ullman para obtener el AFD capaz de reconocer el lenguaje definido por la expresión regular ";
	private static String cabeceraStePos = "<tr><th>n</th><th>stePos(n)</th></tr>";

	public DocumentoMoodle() {
		this.inicio = cabecera;
		this.fin = "</html></body>";
		this.problemas = new ArrayList<String>();
	}

	public void añadirProblema(AhoSethiUllman asu) {
		StringBuilder problema = new StringBuilder();

		// enunciado
		problema.append("<p><b>");
		problema.append(this.problemas.size() + 1);
		problema.append(".- ");
		problema.append(enunciado);
		problema.append(asu.problema());
		problema.append("</b></p>");

		// expresión aumentada
		problema.append("<p>");
		problema.append("Expresión aumentada: ");
		problema.append(asu.expresionAumentada());
		problema.append("</p>");

		// siguiente-pos
		problema.append("<p><table border=\"1\">");
		problema.append(cabeceraStePos);
		for (int n : asu.posiciones()) {
			problema.append("<tr><td>");
			problema.append(n);
			problema.append("</td><td>");
			if (asu.siguientePos(n).size() > 0) {
				String prefijo = "";
				for (int pos : asu.siguientePos(n)) {
					problema.append(prefijo);
					prefijo = ", ";
					problema.append(pos);
				}
			} else {
				problema.append("-");
			}
			problema.append("</td></tr>");
		}
		problema.append("</table></p>");

		// transiciones
		Set<Character> estados = new TreeSet<>(asu.estados()); // TODO pendiente de 66683228
		for (char estado : estados) {
			for (char simbolo : asu.simbolos()) {
				if (simbolo != '$') {
					problema.append("<p>mueve(");
					problema.append(estado);
					problema.append(", ");
					problema.append(simbolo);
					problema.append(") = {");
					char estadoSiguiente = asu.mueve(estado, simbolo);
					String prefijo = "";
					for (int pos : asu.estado(estadoSiguiente)) {
						problema.append(prefijo);
						prefijo = ", ";
						problema.append(pos);
					}
					problema.append("} = ");
					problema.append(estadoSiguiente);
					problema.append("</p>");
				}
			}
		}

		// tabla de transiciones
		problema.append("<p><table border=\"1\"><tr><th></th>");
		for (char simbolo : asu.simbolos())
			problema.append("<th>" + simbolo + "</th>");
		problema.append("<th></th></tr>");

		for (char estado : estados) { // TODO pendiente de 66683228
			if (asu.esFinal(estado))
				problema.append("<tr><td>(" + estado + ")</td>");
			else
				problema.append("<tr><td>" + estado + "</td>");
			for (char simbolo : asu.simbolos()) {
				problema.append("<td>" + asu.mueve(estado, simbolo) + "</td>");
			}
			problema.append("<td>");
			for (int posicion : asu.estado(estado))
				problema.append(posicion + " ");
			problema.append("</td></tr>");
		}
		problema.append("</table></p>");

		this.problemas.add(problema.toString());
	}

	public String toString() {
		StringBuilder documento = new StringBuilder();

		documento.append(this.inicio);
		for (String problema : this.problemas)
			documento.append(problema);
		documento.append(this.fin);

		return documento.toString();
	}
}
