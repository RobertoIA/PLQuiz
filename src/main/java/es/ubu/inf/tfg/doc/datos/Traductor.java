package es.ubu.inf.tfg.doc.datos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Traductor presenta una interfaz común para aquellas clases que se encargen de
 * traducir los problemas a una representación textual especifica, con la
 * función de agruparlos en un documento de tipo concreto.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public abstract class Traductor {

	private static final Logger log = LoggerFactory.getLogger(Traductor.class);

	/**
	 * Genera un documento de un formato concreto a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento completo.
	 */
	public abstract String documento(List<Plantilla> problemas);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUConstruccion(AhoSethiUllman problema);
	
	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUEtiquetado(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUTablas(AhoSethiUllman problema);


	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcción de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSExpresion(ConstruccionSubconjuntos problema);

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo autómata
	 * a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcción de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSAutomata(ConstruccionSubconjuntos problema);

	/**
	 * Carga el contenido de una plantilla y lo devuelve como cadena de
	 * caracteres.
	 * 
	 * @param plantilla
	 *            Plantilla a cargar.
	 * @return Plantilla como cadena de caracteres.
	 */
	String plantilla(String plantilla) {
		String resultado;
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = getClass().getResourceAsStream(plantilla);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			resultado = contenido.toString();
			return resultado;
		} catch (IOException e) {
			log.error("Error al recuperar la plantilla {}", plantilla);
			return "";
		}
	}

	/**
	 * Transforma una plantilla a su forma intermedia, haciendola compatible con
	 * <code>MessageFormat.format</code>. Es decir, escapa los caracteres '{' y
	 * '}' y convierte los marcadores '<%' y '%>' en llaves.
	 * 
	 * @param plantilla
	 *            Plantilla original.
	 * @return Plantilla en forma intermedia.
	 */
	@Deprecated
	String formatoIntermedio(String plantilla) {
		plantilla = plantilla.replace("{", "\\'{\\'");
		plantilla = plantilla.replace("}", "\\'}\\'");
		plantilla = plantilla.replace("<%", "{");
		plantilla = plantilla.replace("%>", "}");

		return plantilla;
	}

	/**
	 * Transforma una plantilla de su forma intermedia a su forma original, es
	 * decir, al formato que debe tener el archivo final.
	 * 
	 * @param plantilla
	 *            Plantilla en formato intermedio.
	 * @return Plantilla en formato final.
	 */
	@Deprecated
	String formatoFinal(String plantilla) {
		plantilla = plantilla.replace("\\{\\", "{");
		plantilla = plantilla.replace("\\}\\", "}");

		return plantilla;
	}
}