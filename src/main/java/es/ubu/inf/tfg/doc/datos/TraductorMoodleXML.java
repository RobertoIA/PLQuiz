package es.ubu.inf.tfg.doc.datos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;
import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Implementa un traductor al formato propietario Moodle XML.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class TraductorMoodleXML extends Traductor {

	private static final Logger log = LoggerFactory
			.getLogger(TraductorMoodleXML.class);
	private static final Random random = new Random(new Date().getTime());

	/**
	 * Genera un documento en formato Moodle XML a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String documento(List<String> problemas) {
		log.info("Generando documento Moodle XML a partir de {} problemas.",
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (String problema : problemas)
			documento.append(MessageFormat.format(formatoIntermedio(problema),
					n++));

		String plantilla = formatoIntermedio(plantilla("plantilla.xml"));
		plantilla = MessageFormat.format(plantilla, documento.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo completo a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public String traduceASUCompleto(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresion {}, formato completo",
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaASUCompleto.xml"));

		// siguiente-pos
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>");
			stePos.append(n);
			stePos.append("</td><td>");
			stePos.append(opcionesPosiciones(problema.siguientePos(n),
					problema.posiciones()));
			stePos.append("</td></tr>");
		}

		// Función de transición
		fTrans.append("\n\t<tr><th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>");
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>");
				}
			}
			fTrans.append("\n\t<td>");
			fTrans.append(opcionesPosiciones(problema.estado(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), stePos.toString(), fTrans.toString(),
				eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo árbol a formato Moodle
	 * XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public String traduceASUArbol(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresion {}, formato árbol",
				problema.problema());

		String url = problema.arbolVacio().hashCode() + ".jpg";
		String plantilla = formatoIntermedio(plantilla("plantillaASUArbol.xml"));
		StringBuilder soluciones = new StringBuilder();

		// cabecera
		soluciones.append("");
		// contenido
		char simboloActual = 'A';
		Set<Character> simbolos = problema.simbolos();
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append("<tr><td>" + simboloActual + "</td>");
			soluciones.append("<td>"
					+ opcionesTipos(problema.tipo(simboloActual), simbolos)
					+ "</td>");
			soluciones.append("<td>"
					+ opcionesPosiciones(problema.primeraPos(simboloActual),
							problema.posiciones()) + "</td>");
			soluciones.append("<td>"
					+ opcionesPosiciones(problema.ultimaPos(simboloActual),
							problema.posiciones()) + "</td>");
			soluciones.append("</tr>");

			simboloActual++;
		}

		String solucionesXML = soluciones.toString();
		solucionesXML = solucionesXML.replace("\u2027", "·");
		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "·");

		plantilla = MessageFormat.format(plantilla, "<%0%>", expresion, url,
				solucionesXML, imageToBase64(problema.arbolVacio()));
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcción de subconjuntos con expresion {, formato expresión}",
				problema.problema());

		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSExpresion.xml"));

		// Función de transición
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>");
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>");
				}
			}
			fTrans.append("\n\t<td>");
			fTrans.append(opcionesPosiciones(problema.posiciones(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla = MessageFormat.format(plantilla, "<%0%>",
				problema.problema(), fTrans.toString(), eFinales.toString());
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo autómata
	 * a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public String traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcción de subconjuntos con expresion {}, formato autómata",
				problema.problema());

		String url = problema.automata().hashCode() + ".jpg";
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		String plantilla = formatoIntermedio(plantilla("plantillaCSAutomata.xml"));

		// Función de transición
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>");
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>");
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>");

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>");
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>");
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>");
				}
			}
			fTrans.append("\n\t<td>");
			fTrans.append(opcionesPosiciones(problema.posiciones(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>");
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla = MessageFormat.format(plantilla, "<%0%>", url,
				fTrans.toString(), eFinales.toString(),
				imageToBase64(problema.automata()));
		plantilla = formatoFinal(plantilla);

		return plantilla;
	}

	/**
	 * Devuelve la lista de opciones posibles a la hora de resolver el estado de
	 * destino en una tabla de transición, a partir de la solución y de la lista
	 * de estados existentes.
	 * <p>
	 * La lista de opciones incluirá la solución correcta, dos soluciones
	 * similares y una solución completamente distinta.
	 * 
	 * @param solucion
	 *            Estado de destino correcto.
	 * @param estados
	 *            Estados existentes.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesTransicion(char solucion, Set<Character> estados) {
		log.debug(
				"Generando opciones para transición a estado {} con estados {}",
				solucion, estados);

		estados.remove(solucion);

		List<Character> similares = new ArrayList<>();
		for (char estado : estados) {
			if (Math.abs(estado - solucion) <= 2)
				similares.add(estado);
		}
		List<Character> diferentes = new ArrayList<>(estados);
		diferentes.removeAll(similares);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(solucion);

		int index;
		if (similares.size() > 0) { // Opcion similar 1
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("Añadiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (similares.size() > 0) { // Opcion similar 2
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("Añadiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}
		if (diferentes.size() > 0) { // Opcion diferente a ser posible
			index = random.nextInt(diferentes.size());
			opciones.append("~");
			log.debug("Añadiendo opcion {} (diferente)", diferentes.get(index));
			opciones.append(diferentes.remove(index));
		} else {
			index = random.nextInt(similares.size());
			opciones.append("~");
			log.debug("Añadiendo opcion {} (similar)", similares.get(index));
			opciones.append(similares.remove(index));
		}

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera una lista de opciones para resolver el conjunto de estados
	 * finales, a partir del conjunto de estados finales real y del conjunto
	 * total de estados del problema.
	 * <p>
	 * La lista de opciones incluirá la solución correcta, dos soluciones
	 * similares y una solución completamente distinta. Las soluciones similares
	 * se obtienen añadiendo y quitando un elemento del conjunto
	 * respectivamente. La solución distinta es el conjunto complementario al
	 * dado.
	 * 
	 * @param solucion
	 *            Conjunto de estados finales del problema.
	 * @param estados
	 *            Conjunto de estados del problema.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesFinales(Set<Character> solucion,
			Set<Character> estados) {
		log.debug(
				"Generando opciones para estados finales con finales {} y estados {}",
				solucion, estados);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(listToString(new ArrayList<>(solucion)));

		List<Character> complementarios = new ArrayList<>(estados);
		complementarios.removeAll(solucion);
		List<Character> conjunto;
		int index;
		// Opción similar 1 (eliminamos un estado)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() > 0) {
			index = random.nextInt(conjunto.size());
			conjunto.remove(index);
			log.debug("Añadiendo opcion {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto));
		}

		// Opción similar 2 (añadimos un estado)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opcion {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto));
		}

		// Opción diferente
		log.debug("Añadiendo opcion {} (diferente)", complementarios);
		opciones.append("~" + listToString(complementarios));

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * Genera una lista de opciones para resolver el conjunto de posiciones, a
	 * partir del conjunto de posiciones real y del conjunto total de posiciones
	 * del problema.
	 * <p>
	 * La lista de opciones incluirá la solución correcta, dos soluciones
	 * similares y una solución completamente distinta. Las soluciones similares
	 * se obtienen añadiendo y quitando un elemento del conjunto
	 * respectivamente. La solución distinta es el conjunto complementario al
	 * dado.
	 * 
	 * @param solucion
	 *            Conjunto de posiciones real.
	 * @param posiciones
	 *            Conjunto de posiciones del problema.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesPosiciones(Set<Integer> solucion,
			Set<Integer> posiciones) {
		log.debug(
				"Generando opciones para posiciones {} con posiciones totales {}",
				solucion, posiciones);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(listToString(new ArrayList<>(solucion)));

		List<Integer> complementarios = new ArrayList<>(posiciones);
		complementarios.removeAll(solucion);
		List<Integer> conjunto;
		int index;
		// Opción similar 1 (eliminamos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() > 0) {
			index = random.nextInt(conjunto.size());
			conjunto.remove(index);
			log.debug("Añadiendo opcion {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto));
		}

		// Opción similar 2 (añadimos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opcion {} (similar)", conjunto);
			opciones.append("~" + listToString(conjunto));
		}

		// Opción diferente
		log.debug("Añadiendo opcion {} (diferente)", complementarios);
		opciones.append("~" + listToString(complementarios));

		opciones.append("}");
		return opciones.toString();
	}

	/**
	 * 
	 * @param solucion
	 * @param simbolos
	 * @return
	 */
	private String opcionesTipos(String solucion, Set<Character> simbolos) {
		log.debug(
				"Generando opciones para tipos con solucion {} y simbolos {}",
				solucion, simbolos);

		StringBuilder opciones = new StringBuilder();
		List<String> tipos = new ArrayList<>();
		for (char simbolo : simbolos)
			tipos.add("" + simbolo);
		tipos.add("*");
		tipos.add("\u2027");
		tipos.add("|");
		tipos.remove(solucion);
		
		opciones.append("{1:MULTICHOICE:%100%");
		opciones.append(solucion);
		
		int index;
		for(int i = 0; i < 3; i++) {
			index = random.nextInt(tipos.size());
			log.debug("Añadiendo opción {}", tipos.get(index));
			opciones.append("~" + tipos.remove(index));
		}
		opciones.append("}");

		return opciones.toString();
	}

	/**
	 * Devuelve una representación de una lista de elementos separados con
	 * comas.
	 * 
	 * @param lista
	 *            Lista de elementos.
	 * @return Representación de la lista como elementos separados por comas.
	 */
	private String listToString(List<?> lista) {
		StringBuilder setToString = new StringBuilder();

		if (lista.size() > 0) {
			String prefijo = "";
			for (Object elemento : lista) {
				setToString.append(prefijo);
				prefijo = ", ";
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append("Cjto. vacío");
		}
		return setToString.toString();
	}

	/**
	 * Convierte una imagen en una cadena representando a la misma en base 64.
	 * 
	 * @param imagen
	 *            Imagen a convertir.
	 * @return Cadena en base 64 representando la imagen dada.
	 */
	private String imageToBase64(BufferedImage imagen) {
		String imagenBase64 = "";

		try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			ImageIO.write(imagen, "JPG", output);
			byte[] imageBytes = output.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imagenBase64 = encoder.encode(imageBytes);

		} catch (IOException e) {
			log.error("Error convirtiendo imagen a base 64", e);
		}

		return imagenBase64;
	}
}
