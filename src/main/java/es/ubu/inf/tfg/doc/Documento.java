package es.ubu.inf.tfg.doc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorHTML;
import es.ubu.inf.tfg.doc.datos.TraductorMoodleXML;
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

	public void sustituirProblema(AhoSethiUllman anterior, AhoSethiUllman nuevo) {
		int index = this.problemas.indexOf(this.traductor.traduce(anterior));
		if (index >= 0)
			this.problemas.set(index, this.traductor.traduce(nuevo));
	}

	public void guardar(File fichero) {
		System.err.println("guardando " + fichero);

		String ruta = fichero.toString();
		Writer writer = null;

		if (this.traductor instanceof TraductorHTML) {
			System.err.println("formato html");
			if (!ruta.toLowerCase().endsWith(".html"))
				ruta += ".html";
		} else if (this.traductor instanceof TraductorMoodleXML) {
			System.err.println("formato moodle xml");
			if (!ruta.toLowerCase().endsWith(".xml"))
				ruta += ".xml";
		}

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ruta), "UTF16"));
			writer.write(toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.err.println("guardado a " + fichero);
	}

	public String toString() {
		return this.traductor.documento(this.problemas);
	}
}
