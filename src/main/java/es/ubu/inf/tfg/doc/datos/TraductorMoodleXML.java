package es.ubu.inf.tfg.doc.datos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
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


	// Método privado que mezcla las opciones de respuesta
	private String shuffleChoices(String question) {
		// Encuentra la segunda ocurrencia de ":"
		int firstColonIndex = question.indexOf(":"); //$NON-NLS-1$ 
		int secondColonIndex = question.indexOf(":", firstColonIndex + 1); //$NON-NLS-1$ 
		int suffixStart = question.lastIndexOf("}"); //$NON-NLS-1$ 

		if (firstColonIndex == -1 || secondColonIndex == -1 || suffixStart == -1 || secondColonIndex >= suffixStart) {
			throw new IllegalArgumentException("El formato de la pregunta no es válido."); //$NON-NLS-1$ 
		}

		// Separa el prefijo, las opciones y el sufijo
		String prefix = question.substring(0, secondColonIndex + 1); // Incluye el segundo ':'
		String optionsPart = question.substring(secondColonIndex + 1, suffixStart);
		String suffix = question.substring(suffixStart); // Incluye el '}'

		// Divide las opciones por "~"
		String[] options = optionsPart.split("~"); //$NON-NLS-1$ 

		// Usa una lista para mezclar las opciones
		List<String> optionsList = new ArrayList<>();
		Collections.addAll(optionsList, options);

		// Mezcla las opciones
		Collections.shuffle(optionsList);

		// Reconstruye la pregunta
		String shuffledOptions = String.join("~", optionsList); //$NON-NLS-1$ 
		return prefix + shuffledOptions + suffix;
	}

	/**
	 * Genera un documento en formato Moodle XML a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String documento(List<Plantilla> problemas) {
		log.info("Generando documento Moodle XML a partir de {} problemas.", //$NON-NLS-1$
				problemas.size());

		StringBuilder documento = new StringBuilder();

		int n = 1;
		for (Plantilla problema : problemas) {
			problema.set("numero", "" + n++); //$NON-NLS-1$
			documento.append(problema.toString());
		}

		Plantilla plantilla = new Plantilla("plantilla.xml"); //$NON-NLS-1$
		plantilla.set("documento", documento.toString()); //$NON-NLS-1$

		return plantilla.toString();
	}
	
	/**
	 * Genera un documento Moodle XML a partir de un único problema ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @return Documento Moodle XML completo.
	 */
	@Override
	public String traduceProblema(Plantilla problema, int num){
		log.info("Generando documento Moodle XML de un único problema."); //$NON-NLS-1$
		
		problema.set("numero", "" + num); //$NON-NLS-1$
		
		Plantilla plantilla = new Plantilla("plantilla.xml"); //$NON-NLS-1$
		plantilla.set("documento", problema.toString()); //$NON-NLS-1$
	
		return plantilla.toString();
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceASUConstruccion(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresión {}, formato construcción", //$NON-NLS-1$
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaASUConstruccion.xml"); //$NON-NLS-1$
		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		// CGO added this
		BufferedImage solutionImage = alternativas.get(0);  // I need to get the solution before the shuffle
		Collections.shuffle(alternativas);
		String[] alternativasBase64 = new String[4];

		for (int i = 0; i < 4; i++) {
			imagenes[i] = Math.abs(alternativas.get(i).hashCode()) + ".jpg"; //$NON-NLS-1$
			alternativasBase64[i] = imageToBase64(alternativas.get(i));
		}
		
		// CGO commented this
		//int index = alternativas.indexOf(problema.alternativas().get(0)); // XXX after shuffling the solution was not the first one any more
		// CGO added this
		int index = alternativas.indexOf(solutionImage);
		char solucion = (char) ('a' + index); //$NON-NLS-1$

		Set<Character> opciones = new HashSet<>();
		opciones.add('a');
		opciones.add('b');
		opciones.add('c');
		opciones.add('d');

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("urlA", imagenes[0]); //$NON-NLS-1$
		plantilla.set("urlB", imagenes[1]); //$NON-NLS-1$
		plantilla.set("urlC", imagenes[2]); //$NON-NLS-1$
		plantilla.set("urlD", imagenes[3]); //$NON-NLS-1$
		plantilla.set("imagenA", alternativasBase64[0]); //$NON-NLS-1$
		plantilla.set("imagenB", alternativasBase64[1]); //$NON-NLS-1$
		plantilla.set("imagenC", alternativasBase64[2]); //$NON-NLS-1$
		plantilla.set("imagenD", alternativasBase64[3]); //$NON-NLS-1$
		plantilla.set("solucion", opcionesTransicion(solucion, opciones)); //$NON-NLS-1$

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public Plantilla traduceASUEtiquetado(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresión {}, formato etiquetado", //$NON-NLS-1$
				problema.problema());

		String url = Math.abs(problema.arbolVacio().hashCode()) + ".jpg"; //$NON-NLS-1$
		Plantilla plantilla = new Plantilla("plantillaASUEtiquetado.xml"); //$NON-NLS-1$
		StringBuilder soluciones = new StringBuilder();

		// cabecera
		soluciones.append(""); //$NON-NLS-1$
		// contenido
		char simboloActual = 'A';
		while (problema.primeraPos(simboloActual) != null) {
			soluciones.append("<tr><td>" + simboloActual + "</td>"); //$NON-NLS-1$
			soluciones.append("<td>\n"
					+ opcionesAnulables(problema.esAnulable(simboloActual))
					+ "\n</td>"); //$NON-NLS-1$
			soluciones.append("<td>\n"
					+ opcionesPosiciones(problema.primeraPos(simboloActual),
							problema.posiciones()) + "\n</td>"); //$NON-NLS-1$
			soluciones.append("<td>\n"
					+ opcionesPosiciones(problema.ultimaPos(simboloActual),
							problema.posiciones()) + "\n</td>"); //$NON-NLS-1$
			soluciones.append("</tr>"); //$NON-NLS-1$

			simboloActual++;
		}

		String solucionesXML = soluciones.toString();
		solucionesXML = solucionesXML.replace("\u2027", "·"); //$NON-NLS-1$
		String expresion = problema.problema();
		expresion = expresion.replace("\u2027", "·"); //$NON-NLS-1$

		plantilla.set("expresion", expresion); //$NON-NLS-1$
		plantilla.set("url", url); //$NON-NLS-1$
		plantilla.set("imagen", imageToBase64(problema.arbolVacio())); //$NON-NLS-1$
		plantilla.set("tabla", solucionesXML); //$NON-NLS-1$

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a formato
	 * Moodle XML.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido a Moodle XML.
	 */

	@Override
	public Plantilla traduceASUTablas(AhoSethiUllman problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo Aho-Sethi-Ullman con expresión {}, formato tablas", //$NON-NLS-1$
				problema.problema());

		StringBuilder stePos = new StringBuilder();
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaASUTablas.xml"); //$NON-NLS-1$

		// siguiente-pos
		for (int n : problema.posiciones()) {
			stePos.append("<tr><td>"); //$NON-NLS-1$
			stePos.append(n);
			stePos.append("</td><td>"); //$NON-NLS-1$
			stePos.append(opcionesPosiciones(problema.siguientePos(n),
					problema.posiciones()));
			stePos.append("</td></tr>"); //$NON-NLS-1$
		}

		// Función de transición
		fTrans.append("\n\t<tr><th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>"); //$NON-NLS-1$
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>"); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>"); //$NON-NLS-1$
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>"); //$NON-NLS-1$
				}
			}
			fTrans.append("\n\t<td>"); //$NON-NLS-1$
			fTrans.append(opcionesPosiciones(problema.estado(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>"); //$NON-NLS-1$
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("siguientePos", stePos.toString()); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("finales", eFinales.toString()); //$NON-NLS-1$

		return plantilla;
	}

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * construcción a formato Moodle XML.
	 * 
	 * @param problema
	 *            Problema de construcción de subconjuntos.
	 * @return Problema traducido a Moodle XML.
	 */
	@Override
	public Plantilla traduceCSConstruccion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcción de subconjuntos con expresión {}, formato construcción", //$NON-NLS-1$
				problema.problema());

		Plantilla plantilla = new Plantilla("plantillaCSConstruccion.xml"); //$NON-NLS-1$

		String[] imagenes = new String[4];
		List<BufferedImage> alternativas = problema.alternativas();
		// CGO added this
		BufferedImage solutionImage = alternativas.get(0);  // I need to get the solution before the shuffle
		Collections.shuffle(alternativas);
		String[] alternativasBase64 = new String[4];

		for (int i = 0; i < 4; i++) {
			imagenes[i] = Math.abs(alternativas.get(i).hashCode()) + ".jpg"; //$NON-NLS-1$
			alternativasBase64[i] = imageToBase64(alternativas.get(i));
		}

		// CGO commented this
		//char solucion = (char) ('a' + alternativas.indexOf(problema.alternativas().get(0))); // XXX after shuffling the solution was not the first one any more
		// CGO added these two lines
		int index = alternativas.indexOf(solutionImage);
		char solucion = (char) ('a' + index);
		Set<Character> opciones = new HashSet<>();
		opciones.add('a');
		opciones.add('b');
		opciones.add('c');
		opciones.add('d');

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("urlA", imagenes[0]); //$NON-NLS-1$
		plantilla.set("urlB", imagenes[1]); //$NON-NLS-1$
		plantilla.set("urlC", imagenes[2]); //$NON-NLS-1$
		plantilla.set("urlD", imagenes[3]); //$NON-NLS-1$
		plantilla.set("imagenA", alternativasBase64[0]); //$NON-NLS-1$
		plantilla.set("imagenB", alternativasBase64[1]); //$NON-NLS-1$
		plantilla.set("imagenC", alternativasBase64[2]); //$NON-NLS-1$
		plantilla.set("imagenD", alternativasBase64[3]); //$NON-NLS-1$
		plantilla.set("solucion", opcionesTransicion(solucion, opciones)); //$NON-NLS-1$

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
	public Plantilla traduceCSExpresion(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcción de subconjuntos con expresión {}, formato expresión", //$NON-NLS-1$
				problema.problema());

		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSExpresion.xml"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>"); //$NON-NLS-1$
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>"); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>"); //$NON-NLS-1$
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>"); //$NON-NLS-1$
				}
			}
			fTrans.append("\n\t<td>"); //$NON-NLS-1$
			fTrans.append(opcionesPosiciones(problema.posiciones(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>"); //$NON-NLS-1$
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla.set("expresion", problema.problema()); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("finales", eFinales.toString()); //$NON-NLS-1$

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
	public Plantilla traduceCSAutomata(ConstruccionSubconjuntos problema) {
		log.info(
				"Traduciendo a Moodle XML problema tipo construcción de subconjuntos con expresión {}, formato autómata", //$NON-NLS-1$
				problema.problema());

		String url = Math.abs(problema.automata().hashCode()) + ".jpg"; //$NON-NLS-1$
		StringBuilder fTrans = new StringBuilder();
		StringBuilder eFinales = new StringBuilder();

		Plantilla plantilla = new Plantilla("plantillaCSAutomata.xml"); //$NON-NLS-1$

		// Función de transición
		fTrans.append("\n\t<tr>\n\t<th scope=\"col\">$$\\mathcal{Q}/\\Sigma$$</th>"); //$NON-NLS-1$
		for (char simbolo : problema.simbolos())
			if (simbolo != '$')
				fTrans.append("\n\t<th scope=\"col\">" + simbolo + "</th>"); //$NON-NLS-1$
		fTrans.append("\n\t<th scope=\"col\"> </th>\n\t</tr>"); //$NON-NLS-1$

		for (char estado : problema.estados()) {
			fTrans.append("\n\t<tr>\n\t<td>" + estado + "</td>"); //$NON-NLS-1$
			for (char simbolo : problema.simbolos()) {
				if (simbolo != '$') {
					fTrans.append("\n\t<td>"); //$NON-NLS-1$
					fTrans.append(opcionesTransicion(
							problema.mueve(estado, simbolo), problema.estados()));
					fTrans.append("</td>"); //$NON-NLS-1$
				}
			}
			fTrans.append("\n\t<td>"); //$NON-NLS-1$
			fTrans.append(opcionesPosiciones(problema.posiciones(estado),
					problema.posiciones()));
			fTrans.append("</td>\n\t</tr>"); //$NON-NLS-1$
		}

		// Estados finales
		Set<Character> finales = new TreeSet<>();
		for (char estado : problema.estados()) {
			if (problema.esFinal(estado))
				finales.add(estado);
		}
		eFinales.append(opcionesFinales(finales, problema.estados()));

		plantilla.set("url", url); //$NON-NLS-1$
		plantilla.set("imagen", imageToBase64(problema.automata())); //$NON-NLS-1$
		plantilla.set("transicion", fTrans.toString()); //$NON-NLS-1$
		plantilla.set("finales", eFinales.toString()); //$NON-NLS-1$

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
				"Generando opciones para transición a estado {} con estados {}", //$NON-NLS-1$
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
		opciones.append("{1:MULTICHOICE:%100%"); //$NON-NLS-1$
		opciones.append(solucion);

		int index;
		if (similares.size() > 0) { // Opción similar 1
			index = random.nextInt(similares.size());
			opciones.append("~"); //$NON-NLS-1$
			log.debug("Añadiendo opción {} (similar)", similares.get(index)); //$NON-NLS-1$
			opciones.append(similares.remove(index));
		}
		if (similares.size() > 0) { // Opción similar 2
			index = random.nextInt(similares.size());
			opciones.append("~"); //$NON-NLS-1$
			log.debug("Añadiendo opción {} (similar)", similares.get(index)); //$NON-NLS-1$
			opciones.append(similares.remove(index));
		}
		if (diferentes.size() > 0) { // Opción diferente a ser posible
			index = random.nextInt(diferentes.size());
			opciones.append("~"); //$NON-NLS-1$
			log.debug("Añadiendo opción {} (diferente)", diferentes.get(index)); //$NON-NLS-1$
			opciones.append(diferentes.remove(index));
		} else {
			index = random.nextInt(similares.size());
			opciones.append("~"); //$NON-NLS-1$
			log.debug("Añadiendo opción {} (similar)", similares.get(index)); //$NON-NLS-1$
			opciones.append(similares.remove(index));
		}

		opciones.append("}"); //$NON-NLS-1$

		return shuffleChoices(opciones.toString());
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
				"Generando opciones para estados finales con finales {} y estados {}", //$NON-NLS-1$
				solucion, estados);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%"); //$NON-NLS-1$
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
			log.debug("Añadiendo opción {} (similar)", conjunto); //$NON-NLS-1$
			opciones.append("~" + listToString(conjunto)); //$NON-NLS-1$ 
		}

		// Opción similar 2 (aÃ±adimos un estado)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opción {} (similar)", conjunto); //$NON-NLS-1$
			opciones.append("~" + listToString(conjunto)); //$NON-NLS-1$ 
		}

		// Opción diferente
		log.debug("Añadiendo opción {} (diferente)", complementarios); //$NON-NLS-1$
		opciones.append("~" + listToString(complementarios)); //$NON-NLS-1$ 

		opciones.append("}"); //$NON-NLS-1$

		return shuffleChoices(opciones.toString());
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
				"Generando opciones para posiciones {} con posiciones totales {}", //$NON-NLS-1$
				solucion, posiciones);

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%"); //$NON-NLS-1$
		opciones.append(listToRanges(new ArrayList<>(solucion)));

		List<Integer> complementarios = new ArrayList<>(posiciones);
		complementarios.removeAll(solucion);
		List<Integer> conjunto;
		int index;
		// Opción similar 1 (eliminamos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() > 0) {
			index = random.nextInt(conjunto.size());
			conjunto.remove(index);
			log.debug("Añadiendo opción {} (similar)", conjunto); //$NON-NLS-1$
			opciones.append("~" + listToRanges(conjunto)); //$NON-NLS-1$
		}

		// Opción similar 2 (aÃ±adimos un estado si es posible)
		conjunto = new ArrayList<>(solucion);
		if (conjunto.size() < solucion.size()) {
			index = random.nextInt(complementarios.size());
			conjunto.add(complementarios.get(index));
			log.debug("Añadiendo opción {} (similar)", conjunto); //$NON-NLS-1$
			opciones.append("~" + listToRanges(conjunto)); //$NON-NLS-1$
		}

		// Opción diferente
		log.debug("Añadiendo opción {} (diferente)", complementarios); //$NON-NLS-1$
		opciones.append("~" + listToRanges(complementarios)); //$NON-NLS-1$

		opciones.append("}"); //$NON-NLS-1$

		return shuffleChoices(opciones.toString());
	}

	/**
	 * Genera opciones para decidir si un nodo es anulable o no (Si/No),
	 * recibiendo la opción correcta como parámetro booleano (<code>true</code>
	 * para sí, <code>false</code> para no).
	 * 
	 * @param esAnulable
	 *            Opción correcta.
	 * @return Cadena de caracteres en formato Moodle XML representando las
	 *         opciones.
	 */
	private String opcionesAnulables(boolean esAnulable) {
		log.debug("Generando opciones para anulable {}", esAnulable); //$NON-NLS-1$

		StringBuilder opciones = new StringBuilder();
		opciones.append("{1:MULTICHOICE:%100%"); //$NON-NLS-1$
		opciones.append((esAnulable ? Messages.getString("TraductorMoodle.yes") : "No")); //$NON-NLS-1$
		opciones.append("~"); //$NON-NLS-1$
		opciones.append((esAnulable ? "No" : Messages.getString("TraductorMoodle.yes"))); //$NON-NLS-1$
		opciones.append("}"); //$NON-NLS-1$

		return shuffleChoices(opciones.toString());
	}

	/**
	 * Devuelve una representación de una lista de elementos separados con
	 * comas.
	 * 
	 * @param lista
	 *            Lista de elementos.
	 * @return Representación de la lista como elementos separados por comas.
	 */
	@SuppressWarnings("unused")
	private String listToString(List<?> lista) {
		StringBuilder setToString = new StringBuilder();

		if (lista.size() > 0) {
			String prefijo = ""; //$NON-NLS-1$
			for (Object elemento : lista) {
				setToString.append(prefijo);
				prefijo = ", "; //$NON-NLS-1$
				setToString.append(elemento.toString());
			}
		} else {
			setToString.append(Messages.getString("TraductorMoodle.emptySet")); //$NON-NLS-1$
		}
		return setToString.toString();
	}

	/**
	 * Devuelve una representación de un conjunto de elementos como rangos separados por comas.
	 * 
	 * @param lista
	 *            Conjunto de elementos.
	 * @return Representación del conjunto como rangos separados por comas.
	 */
	// TODO: ¿se podría mover a la clase padre?
	// TODO: ¿setToRanges y listToRanges se podrían fusionar?
	private String listToRanges(List<?> lista) {
		StringBuilder out = new StringBuilder();
		int last=0, first=0, length=0; // inicializo para evitar warnings
		
		log.info("***** lista {}.", lista); //$NON-NLS-1$

		
		if (lista.size() == 0) {
			out.append("\u2205"); //$NON-NLS-1$ // Unicode empty set
		} else {
			String sep = ""; //$NON-NLS-1$
			boolean isFirst = true;
			for (Object e: lista) {
				log.info("***** lista {}.", e); //$NON-NLS-1$
				if (isFirst) {
					last = first = (int) e;
					length = 1;
					isFirst = false;
					continue;
				}
				if ((int) e - last == 1) {
					last = (int) e;
					length++;
				} else {
					out.append(sep);
					sep = ", "; //$NON-NLS-1$
					if (length == 1) {
						out.append(""+last); //$NON-NLS-1$
					} else if (length == 2) {
						out.append(""+first+", "+last); //$NON-NLS-1$
					} else {
						out.append(""+first+"–"+last); //$NON-NLS-1$
					}
					last = first = (int) e;
					length = 1;
				}
			}
			out.append(sep);
			if (length == 1) {
				out.append(""+last); //$NON-NLS-1$
			} else if (length == 2) {
				out.append(""+first+", "+last); //$NON-NLS-1$
			} else {
				out.append(""+first+"–"+last); //$NON-NLS-1$
			}
		}
		return out.toString();
	}

	/**
	 * Convierte una imagen en una cadena representando a la misma en base 64.
	 * 
	 * @param imagen
	 *            Imagen a convertir.
	 * @return Cadena en base 64 representando la imagen dada.
	 */
	private String imageToBase64(BufferedImage imagen) {
		String imagenBase64 = ""; //$NON-NLS-1$

		try (ByteArrayOutputStream output = new ByteArrayOutputStream();) {
			ImageIO.write(imagen, "JPG", output); //$NON-NLS-1$
			byte[] imageBytes = output.toByteArray();

			imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);

		} catch (IOException e) {
			log.error("Error convirtiendo imagen a base 64", e); //$NON-NLS-1$
		}

		return imagenBase64;
	}
}
