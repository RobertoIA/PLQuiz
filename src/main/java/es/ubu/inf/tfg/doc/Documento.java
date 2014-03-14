package es.ubu.inf.tfg.doc;

import java.util.ArrayList;
import java.util.List;

import es.ubu.inf.tfg.doc.trad.Traductor;
import es.ubu.inf.tfg.doc.trad.TraductorHTML;
import es.ubu.inf.tfg.doc.trad.TraductorMoodleXML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

public class Documento {
	private List<String> problemas;
	private Traductor traductor;

	private Documento(Traductor traductor) {
		this.problemas = new ArrayList<>();
		this.traductor = traductor;
	}

	public static Documento DocumentoHTML() {
		return new Documento(new TraductorHTML());
	}

	public static Documento DocumentoMoodleXML() {
		return new Documento(new TraductorMoodleXML());
	}

	public void añadirProblema(AhoSethiUllman problema) {
		this.problemas.add(this.traductor.traduce(problema));
	}

	public void eliminarProblema(AhoSethiUllman problema) {
		this.problemas.remove(this.traductor.traduce(problema));
	}

	public String toString() {
		return this.traductor.documento(this.problemas);
	}
}
