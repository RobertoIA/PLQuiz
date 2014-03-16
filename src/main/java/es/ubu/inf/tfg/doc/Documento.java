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
	private List<Object> problemas;

	public Documento() {
		this.problemas = new ArrayList<>();
	}

	public void añadirProblema(AhoSethiUllman problema) {
		this.problemas.add(problema);
	}

	public void eliminarProblema(AhoSethiUllman problema) {
		this.problemas.remove(problema);
	}

	public void sustituirProblema(AhoSethiUllman anterior, AhoSethiUllman nuevo) {
		int index = this.problemas.indexOf(anterior);
		if (index >= 0)
			this.problemas.set(index, nuevo);
	}

	public String vistaPrevia() {
		return traduce(new TraductorHTML());
	}

	public void exportaHTML(File fichero) {
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".html"))
			ruta += ".html";

		guardar(ruta, traduce(new TraductorHTML()));
	}

	public void exportaXML(File fichero) {
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".xml"))
			ruta += ".xml";

		guardar(ruta, traduce(new TraductorMoodleXML()));
	}

	private String traduce(Traductor traductor) {
		List<String> problemas = new ArrayList<>();

		for (Object problema : this.problemas) {
			if (problema instanceof AhoSethiUllman)
				problemas.add(traductor.traduce((AhoSethiUllman) problema));
		}

		return traductor.documento(problemas);
	}

	private void guardar(String ruta, String documento) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ruta), "UTF16"));
			writer.write(documento);
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
	}
}
