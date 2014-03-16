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

/**
 * Documento implementa un documento completo generado por la aplicaci�n, sin
 * dependencia del formato. Implementa operaciones de a�adido, eliminaci�n y
 * sustituci�n de problemas.
 * <p>
 * Un documento puede exportarse a disco en cualquiera de los formatos
 * objetivos. Asimismo Documento nos permite obtener una vista previa de los
 * contenidos en formato HTML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Documento {
	private List<Object> problemas;

	/**
	 * Crea un nuevo documento vac�o.
	 */
	public Documento() {
		this.problemas = new ArrayList<>();
	}

	/**
	 * A�ade un problema de tipo Aho-Sethi-Ullman al final del documento.
	 * 
	 * @param problema
	 *            Nuevo problema Aho-Sethi-Ullman.
	 */
	public void a�adirProblema(AhoSethiUllman problema) {
		this.problemas.add(problema);
	}

	/**
	 * Elimina un problema de tipo Aho-Sethi-Ullman del documento.
	 * 
	 * @param problema
	 *            Problema Aho-Sethi-Ullman a eliminar.
	 */
	public void eliminarProblema(AhoSethiUllman problema) {
		this.problemas.remove(problema);
	}

	/**
	 * Sustituye un problema de tipo Aho-Sethi-Ullman en el documento por otro
	 * nuevo. Si el problema a sustituir no existe, a�ade el nuevo al final del
	 * documento.
	 * 
	 * @param anterior
	 *            Problema Aho-Sethi-Ullman a sustituir.
	 * @param nuevo
	 *            Problema Aho-Sethi-Ullman a a�adir.
	 */
	public void sustituirProblema(AhoSethiUllman anterior, AhoSethiUllman nuevo) {
		int index = this.problemas.indexOf(anterior);
		if (index >= 0)
			this.problemas.set(index, nuevo);
		else
			a�adirProblema(nuevo);
	}

	/**
	 * Devuelve un documento HTML en forma de cadena de caracteres que se
	 * utilizar� como vista previa del documento.
	 * 
	 * @return Vista previa del documento en formato HTML.
	 */
	public String vistaPrevia() {
		return traduce(new TraductorHTML());
	}

	/**
	 * Exporta el documento como un fichero de formato HTML al fichero destino
	 * especificado.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportaci�n.
	 */
	public void exportaHTML(File fichero) throws IOException {
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".html"))
			ruta += ".html";

		guardar(ruta, traduce(new TraductorHTML()));
	}

	/**
	 * Exporta el documento como un fichero de formato XML al fichero destino
	 * especificado.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportaci�n.
	 */
	public void exportaXML(File fichero) throws IOException {
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".xml"))
			ruta += ".xml";

		guardar(ruta, traduce(new TraductorMoodleXML()));
	}

	/**
	 * Traduce el documento al formato dado por un traductor especifico, y
	 * devuelve el documento completo como una cadena de caracteres.
	 * 
	 * @param traductor
	 *            Traductor a utilizar.
	 * @return Documento traducido como cadena de caracteres.
	 */
	private String traduce(Traductor traductor) {
		List<String> problemas = new ArrayList<>();

		for (Object problema : this.problemas) {
			if (problema instanceof AhoSethiUllman)
				problemas.add(traductor.traduce((AhoSethiUllman) problema));
		}

		return traductor.documento(problemas);
	}

	/**
	 * Crea o sobreescribe un documento en la ruta dada, con el contenido dado.
	 * 
	 * @param ruta
	 *            Ruta en la que guardar el documento.
	 * @param documento
	 *            Contenido del documento.
	 * @throws IOException
	 *             Indica un error durante el guardado.
	 */
	private void guardar(String ruta, String documento) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(ruta), "UTF16"));
		writer.write(documento);

		writer.close();
	}
}
