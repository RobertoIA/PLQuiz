package es.ubu.inf.tfg.doc;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.doc.datos.Plantilla;
import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorHTML;
import es.ubu.inf.tfg.doc.datos.TraductorLatex;
import es.ubu.inf.tfg.doc.datos.TraductorMoodleXML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Documento implementa un documento completo generado por la aplicación, sin
 * dependencia del formato. Implementa operaciones de añadido, eliminación y
 * sustitución de problemas.
 * <p>
 * Un documento puede exportarse a disco en cualquiera de los formatos
 * objetivos. Asimismo Documento nos permite obtener una vista previa de los
 * contenidos en formato HTML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Documento {

	private static final Logger log = LoggerFactory.getLogger(Documento.class);

	private List<Problema<?>> problemas;

	/**
	 * Crea un nuevo documento vacío.
	 */
	public Documento() {
		this.problemas = new ArrayList<>();
	}

	/**
	 * Añade un problema al documento.
	 * 
	 * @param problema
	 *            Nuevo problema.
	 */
	public void añadirProblema(Problema<?> problema) {
		log.info("Añadiendo problema de tipo {} al documento.",
				problema.getTipo());
		this.problemas.add(problema);
	}

	/**
	 * Elimina un problema del documento.
	 * 
	 * @param problema
	 *            Problema a eliminar.
	 */
	public void eliminarProblema(Problema<?> problema) {
		log.info("Eliminando problema de tipo {} del documento.",
				problema.getTipo());
		this.problemas.remove(problema);
	}

	/**
	 * Sustituye un problema en el documento por otro nuevo. Si el problema a
	 * sustituir no existe, añade el nuevo al documento.
	 * 
	 * @param anterior
	 *            Problema a sustituir.
	 * @param nuevo
	 *            Problema a añadir.
	 */
	public void sustituirProblema(Problema<?> anterior, Problema<?> nuevo) {
		log.info("Sustituyendo problema de tipo {} en el documento.",
				anterior.getTipo());

		int index = this.problemas.indexOf(anterior);
		if (index >= 0)
			this.problemas.set(index, nuevo);
		else
			añadirProblema(nuevo);

	}

	/**
	 * Devuelve un documento HTML en forma de cadena de caracteres que se
	 * utilizará como vista previa del documento.
	 * 
	 * @return Vista previa del documento en formato HTML.
	 */
	public String vistaPrevia() {
		return traduce(new TraductorHTML());
	}

	/**
	 * Exporta el documento como un fichero de formato XML al fichero destino
	 * especificado.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 */
	public void exportaXML(File fichero) throws IOException {
		log.info("Exportando documento como Moodle XML a {}", fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".xml"))
			ruta += ".xml";

		guardar(ruta, traduce(new TraductorMoodleXML()));
	}

	/**
	 * Exporta el documento como un fichero de formato XML al fichero destino
	 * especificado.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 */
	public void exportaLatex(File fichero) throws IOException {
		log.info("Exportando documento como Latex a {}", fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".tex"))
			ruta += ".tex";
		String carpeta = fichero.getParent().toString();
		carpeta += File.separator;

		List<BufferedImage> imagenes = new ArrayList<>();

		for (Problema<?> problema : problemas) {
			if (problema.getTipo().equals(
					"ConstruccionSubconjuntosConstruccion")) {
				// TODO guardado
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosAutomata")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				imagenes.add(p.automata());
				guardar(carpeta + p.automataDot().hashCode() + ".gv",
						p.automataDot());
			} else if (problema.getTipo().equals("AhoSethiUllmanEtiquetado")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				imagenes.add(p.arbolVacio());
				guardar(carpeta + p.arbolVacioDot().hashCode() + ".gv",
						p.arbolVacioDot());
			} else if (problema.getTipo().equals("AhoSethiUllmanConstruccion")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				imagenes.add(p.alternativas().get(0));
				for (String alternativa : p.alternativasDot())
					guardar(carpeta + alternativa.hashCode() + ".gv",
							alternativa);
			}
		}

		guardar(ruta, traduce(new TraductorLatex()));
		guardar(ruta, imagenes);
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
		List<Plantilla> problemas = new ArrayList<>();

		for (Problema<?> problema : this.problemas) {
			switch (problema.getTipo()) {
			case "AhoSethiUllmanConstruccion":
				AhoSethiUllman asuProblemaConstruccion = (AhoSethiUllman) problema
						.getProblema();
				problemas.add(traductor
						.traduceASUConstruccion(asuProblemaConstruccion));
				break;
			case "AhoSethiUllmanEtiquetado":
				AhoSethiUllman asuProblemaEtiquetado = (AhoSethiUllman) problema
						.getProblema();
				problemas.add(traductor
						.traduceASUEtiquetado(asuProblemaEtiquetado));
				break;
			case "AhoSethiUllmanTablas":
				AhoSethiUllman asuProblemaTablas = (AhoSethiUllman) problema
						.getProblema();
				problemas.add(traductor.traduceASUTablas(asuProblemaTablas));
				break;
			case "ConstruccionSubconjuntosConstruccion":
				ConstruccionSubconjuntos csConstruccion = (ConstruccionSubconjuntos) problema
						.getProblema();
				problemas.add(traductor.traduceCSExpresion(csConstruccion));
				break;
			case "ConstruccionSubconjuntosExpresion":
				ConstruccionSubconjuntos csProblemaExpresion = (ConstruccionSubconjuntos) problema
						.getProblema();
				problemas
						.add(traductor.traduceCSExpresion(csProblemaExpresion));
				break;
			case "ConstruccionSubconjuntosAutomata":
				ConstruccionSubconjuntos csProblemaAutomata = (ConstruccionSubconjuntos) problema
						.getProblema();
				problemas.add(traductor.traduceCSAutomata(csProblemaAutomata));
				break;
			default:
				throw new UnsupportedOperationException(
						"Argumento tipo no soportado.");
			}
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
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(ruta), "UTF8"))) {
			writer.write(documento);
		}
	}

	/**
	 * Crea o sobreescribe una serie de imagenes en la ruta dada.
	 * 
	 * @param ruta
	 *            Ruta en la que guardar las imágenes.
	 * @param imagenes
	 *            Imagenes que guardar en disco.
	 */
	private void guardar(String ruta, List<BufferedImage> imagenes) {
		log.info("Guardando {} imagenes", imagenes.size());

		try {
			File parent = new File(ruta);
			for (BufferedImage imagen : imagenes) {
				String nombre = imagen.hashCode() + ".jpg";
				File salida = new File(parent.getParent() + File.separator
						+ nombre);
				ImageIO.write(imagen, "jpg", salida);
			}
		} catch (IOException e) {
			log.error("Encontrado error durante el guardado de imágenes");
		}

	}
}
