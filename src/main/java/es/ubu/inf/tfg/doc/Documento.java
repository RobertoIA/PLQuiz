package es.ubu.inf.tfg.doc;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxgraph.util.mxUtils;

import es.ubu.inf.tfg.doc.datos.Plantilla;
import es.ubu.inf.tfg.doc.datos.Traductor;
import es.ubu.inf.tfg.doc.datos.TraductorHTML;
import es.ubu.inf.tfg.doc.datos.TraductorLatex;
import es.ubu.inf.tfg.doc.datos.TraductorLatexSVG;
import es.ubu.inf.tfg.doc.datos.TraductorLatexTikZ;
import es.ubu.inf.tfg.doc.datos.TraductorMoodleXML;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

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
	 * Devuelve un documento HTML en forma de cadena de caracteres que se
	 * utilizará como vista previa de un problema.
	 * 
	 * @param problema
	 *            Problema del que mostrar la vista previa.
	 * 
	 * @return Vista previa del problema en formato HTML.
	 */
	public static String vistaPrevia(Problema<?> problema) {
		Traductor traductor = new TraductorHTML();
		Plantilla plantilla;
		
		if(problema != null) {
			switch (problema.getTipo()) {
			case "AhoSethiUllmanConstruccion":
				AhoSethiUllman asuProblemaConstruccion = (AhoSethiUllman) problema.getProblema();
				plantilla = traductor.traduceASUConstruccion(asuProblemaConstruccion);
				break;
			case "AhoSethiUllmanEtiquetado":
				AhoSethiUllman asuProblemaEtiquetado = (AhoSethiUllman) problema.getProblema();
				plantilla = traductor.traduceASUEtiquetado(asuProblemaEtiquetado);
				break;
			case "AhoSethiUllmanTablas":
				AhoSethiUllman asuProblemaTablas = (AhoSethiUllman) problema.getProblema();
				plantilla = traductor.traduceASUTablas(asuProblemaTablas);
				break;
			case "ConstruccionSubconjuntosConstruccion":
				ConstruccionSubconjuntos csConstruccion = (ConstruccionSubconjuntos) problema.getProblema();
				plantilla = traductor.traduceCSConstruccion(csConstruccion);
				break;
			case "ConstruccionSubconjuntosExpresion":
				ConstruccionSubconjuntos csProblemaExpresion = (ConstruccionSubconjuntos) problema.getProblema();
				plantilla = traductor.traduceCSExpresion(csProblemaExpresion);
				break;
			case "ConstruccionSubconjuntosAutomata":
				ConstruccionSubconjuntos csProblemaAutomata = (ConstruccionSubconjuntos) problema.getProblema();
				plantilla = traductor.traduceCSAutomata(csProblemaAutomata);
				break;
			default:
				throw new UnsupportedOperationException(
						"Argumento tipo no soportado.");
			}
	
			return traductor.traduceProblema(plantilla, problema.getNumero());
		} else {
			return traductor.documento(new ArrayList<>());
		}
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

		List<BufferedImage> imagenes = new ArrayList<>();

		for (Problema<?> problema : problemas) {
			if (problema.getTipo().equals("ConstruccionSubconjuntosConstruccion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema.getProblema();
				imagenes.add(p.automata());
			} else if (problema.getTipo().equals("ConstruccionSubconjuntosAutomata")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema.getProblema();
				imagenes.add(p.automata());
			} else if (problema.getTipo().equals("ConstruccionSubconjuntosExpresion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema.getProblema();
				imagenes.add(p.automata());
			} else if (problema.getTipo().equals("AhoSethiUllmanEtiquetado")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				imagenes.add(p.arbolVacio());
			} else if (problema.getTipo().equals("AhoSethiUllmanConstruccion")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				imagenes.add(p.alternativas().get(0));
			} else if (problema.getTipo().equals("AhoSethiUllmanTablas")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				imagenes.add(p.alternativas().get(0));
			} 
		}

		guardar(ruta, traduce(new TraductorLatex()));
		guardar(ruta, imagenes);
	}

	/**
	 * Exporta el documento como un fichero de formato XML al fichero destino
	 * especificado, guardando las imágenes que contenga en formato dot.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 */
	public void exportaGraphvizLatex(File fichero) throws IOException {
		log.info("Exportando documento como Latex con imágenes graphviz a {}",
				fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".tex"))
			ruta += ".tex";
		String carpeta = fichero.getParent().toString();
		carpeta += File.separator;

		for (Problema<?> problema : problemas) {
			if (problema.getTipo().equals(
					"ConstruccionSubconjuntosConstruccion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardar(carpeta + Math.abs(p.automata().hashCode()) + ".gv",
						p.automataDot());
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosAutomata")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardar(carpeta + Math.abs(p.automata().hashCode()) + ".gv",
						p.automataDot());
			} else if (problema.getTipo().equals("ConstruccionSubconjuntosExpresion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardar(carpeta + Math.abs(p.automata().hashCode()) + ".gv",
						p.automataDot());
			} else if (problema.getTipo().equals("AhoSethiUllmanEtiquetado")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardar(carpeta + Math.abs(p.arbolVacio().hashCode()) + ".gv",
						p.arbolVacioDot());
			} else if (problema.getTipo().equals("AhoSethiUllmanConstruccion")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardar(carpeta + Math.abs(p.alternativas().get(0).hashCode())  // FIXME ¿p.alternativas().get(0)==p.automata()?
						+ ".gv", p.alternativasDot().get(0));
			} else if (problema.getTipo().equals("AhoSethiUllmanTablas")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardar(carpeta + Math.abs(p.alternativas().get(0).hashCode()) // FIXME  ¿p.alternativas().get(0)==p.automata()?
						+ ".gv", p.alternativasDot().get(0));
			}
		}

		guardar(ruta, traduce(new TraductorLatex()));
	}

	/**
	 * Exporta el documento como un fichero de formato LaTeX al fichero destino
	 * especificado, guardando las imágenes que contenga en formato SVG.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 * @author JBA
	 * @throws IOException
	 */
	public void exportaSVGLatex(File fichero) throws IOException {
		log.info("Exportando documento como Latex con imágenes SVG a {}",
				fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".tex"))
			ruta += ".tex";
		String carpeta = fichero.getParent().toString();
		carpeta += File.separator;


		for (Problema<?> problema : problemas) {
			if (problema.getTipo().equals(
					"ConstruccionSubconjuntosConstruccion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarSVG(carpeta + Math.abs(p.automataSvgSolucion().hashCode()) + ".svg",
						p.automataSvgSolucion());
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosAutomata")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarSVG(carpeta + Math.abs(p.automataSvg().hashCode()) + ".svg",
						p.automataSvg());
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosExpresion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarSVG(carpeta + Math.abs(p.automataSvgSolucion().hashCode()) + ".svg",
						p.automataSvgSolucion());
			} else if (problema.getTipo().equals("AhoSethiUllmanEtiquetado")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarSVG(carpeta + Math.abs(p.arbolVacioSvg().hashCode()) + ".svg",
						p.arbolVacioSvg());
			} else if (problema.getTipo().equals("AhoSethiUllmanConstruccion")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarSVG(carpeta + Math.abs(p.svgSolucion().hashCode())
						+ ".svg", p.svgSolucion());
			} else if (problema.getTipo().equals("AhoSethiUllmanTablas")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarSVG(carpeta + Math.abs(p.svgSolucion().hashCode())
						+ ".svg", p.svgSolucion());
			}
		}

		guardar(ruta, traduce(new TraductorLatexSVG()));
	}
	
	
	/**
	 * Exporta el documento como un fichero de formato LaTeX al fichero destino
	 * especificado, guardando las imágenes que contenga en formato SVG.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 * @author JBA
	 * @throws IOException
	 */
	public void exportaPDFLatex(File fichero) throws IOException {
		log.info("Exportando documento como Latex con imágenes PDF a {}",
				fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".tex"))
			ruta += ".tex";
		String carpeta = fichero.getParent().toString();
		carpeta += File.separator;


		for (Problema<?> problema : problemas) {
			if (problema.getTipo().equals(
					"ConstruccionSubconjuntosConstruccion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarPDF(carpeta + Math.abs(p.automata().hashCode()) + ".pdf",
						p.automataSvgSolucion());
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosAutomata")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarPDF(carpeta + Math.abs(p.automata().hashCode()) + ".pdf",
						p.automataSvg());
			} else if (problema.getTipo().equals(
					"ConstruccionSubconjuntosExpresion")) {
				ConstruccionSubconjuntos p = (ConstruccionSubconjuntos) problema
						.getProblema();
				guardarPDF(carpeta + Math.abs(p.automata().hashCode()) + ".pdf",
						p.automataSvgSolucion());
			} else if (problema.getTipo().equals("AhoSethiUllmanEtiquetado")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarPDF(carpeta + Math.abs(p.arbolVacio().hashCode()) + ".pdf",
						p.arbolVacioSvg());
			} else if (problema.getTipo().equals("AhoSethiUllmanConstruccion")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarPDF(carpeta + Math.abs(p.alternativas().get(0).hashCode())
						+ ".pdf", p.svgSolucion());
			} else if (problema.getTipo().equals("AhoSethiUllmanTablas")) {
				AhoSethiUllman p = (AhoSethiUllman) problema.getProblema();
				guardarPDF(carpeta + Math.abs(p.alternativas().get(0).hashCode())
						+ ".pdf", p.svgSolucion());
			}
		}

		guardar(ruta, traduce(new TraductorLatex()));
	}
	
	
	/**
	 * Exporta el documento como un fichero de formato LaTeX al fichero destino
	 * especificado, incluyendo las imágenes que contenga en formato TikZ dentro del propio LaTeX.
	 * 
	 * @param fichero
	 *            Fichero destino.
	 * @throws IOException
	 *             Indica un error durante la exportación.
	 * @author JBA
	 * @throws IOException
	 */
	public void exportaTikZLatex(File fichero) throws IOException {
		log.info("Exportando documento como Latex con imágenes TikZ a {}",
				fichero);
		String ruta = fichero.toString();
		if (!ruta.toLowerCase().endsWith(".tex"))
			ruta += ".tex";
		
		guardar(ruta, traduce(new TraductorLatexTikZ()));
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
				problemas.add(traductor.traduceCSConstruccion(csConstruccion));
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
	 * Crea o sobreescribe un documento en la ruta dada, con el contenido dado.
	 * 
	 * @param ruta
	 *            Ruta en la que guardar el documento.
	 * @param documento
	 *            Contenido del documento.
	 * @throws IOException
	 *             Indica un error durante el guardado.
	 * @author JBA
	 */
	private void guardarSVG(String ruta, String documento) throws IOException {
		mxUtils.writeFile(documento, ruta);
	}
	
	/**
	 * Crea o sobreescribe un documento en la ruta dada, con el contenido dado.
	 * 
	 * @param ruta
	 *            Ruta en la que guardar el documento.
	 * @param outputStream
	 *            Contenido del documento.
	 * @throws IOException
	 *             Indica un error durante el guardado.
	 */
	private void guardarPDF(String ruta, String documento) throws IOException {
		String rutaSvg = ruta + ".svg";
		mxUtils.writeFile(documento, rutaSvg);
		Transcoder transcoder = new PDFTranscoder();
        TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(new File(rutaSvg)));
        TranscoderOutput transcoderOutput = new TranscoderOutput(new FileOutputStream(new File(ruta)));
        try {
			transcoder.transcode(transcoderInput, transcoderOutput);
		} catch (TranscoderException e) {
			log.error("Encontrado error durante el guardado de imágenes pdf", e);
		}
        File tmpsvg = new File(rutaSvg); 
        if (tmpsvg.delete()) { 
        	log.info("Eliminando ficheros temporales.");
        } else {
        	log.error("Encontrado error durante el borrado del fichero temporal " + tmpsvg.getName());
        }
	}
	

	/**
	 * Crea o sobreescribe una serie de imágenes en la ruta dada.
	 * 
	 * @param ruta
	 *            Ruta en la que guardar las imágenes.
	 * @param imagenes
	 *            Imágenes que guardar en disco.
	 */
	private void guardar(String ruta, List<BufferedImage> imagenes) {
		log.info("Guardando {} imágenes", imagenes.size());

		try {
			File parent = new File(ruta);
			for (BufferedImage imagen : imagenes) {
				String nombre = Math.abs(imagen.hashCode()) + ".jpg";
				File salida = new File(parent.getParent() + File.separator
						+ nombre);
				ImageIO.write(imagen, "jpg", salida);
			}
		} catch (IOException e) {
			log.error("Encontrado error durante el guardado de imágenes", e);
		}
	}
	
}
