package es.ubu.inf.tfg.regex.thompson;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;
import es.ubu.inf.tfg.regex.datos.MapaEstados;
import es.ubu.inf.tfg.regex.parser.CharStream;
import es.ubu.inf.tfg.regex.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.regex.parser.JavaCharStream;
import es.ubu.inf.tfg.regex.parser.ParseException;
import es.ubu.inf.tfg.regex.parser.TokenMgrError;
import es.ubu.inf.tfg.regex.thompson.datos.Automata;
import es.ubu.inf.tfg.regex.thompson.datos.Nodo;

/**
 * * Resuelve un problema de construcción de subconjuntos a partir de un AFND,
 * generado a partir de una expresión regular con el método de
 * McNaughton-Yamada-Thompson. Es capaz de trabajar tanto con una cadena de
 * caracteres como con una ExpresionRegular ya construida.
 * <p>
 * Asimismo actúa como fachada del subsistema es.ubu.inf.tfg.regex.thompson,
 * evitando dependencias con los tipos de datos internos. Todas las salidas se
 * codifican en tipos de datos estándar.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class ConstruccionSubconjuntos {

	private static final Logger log = LoggerFactory
			.getLogger(ConstruccionSubconjuntos.class);

	private String problema;
	private ExpresionRegular expresion;
	private Automata automata;
	private Map<Character, Set<Nodo>> estados;

	private MapaEstados transiciones;
	private List<BufferedImage> alternativas;
	private List<String> alternativasDot;

	/**
	 * Resuelve un problema de construcción de subconjuntos a partir de una
	 * expresión regular en forma de String, construyendo el autómata según el
	 * algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param problema
	 *            Cadena de caracteres conteniendo la expresión regular a
	 *            resolver.
	 * @throws UnsupportedOperationException
	 *             Error del parser o del token manager. Indica que la expresión
	 *             no es válida o que contiene caracteres no reconocidos.
	 */
	public ConstruccionSubconjuntos(String problema)
			throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		CharStream input = new JavaCharStream(new StringReader(problema));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
			// No utilizamos la expresión aumentada
			this.expresion = this.expresion.hijoIzquierdo();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresión no válida.");
		}

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Resuelve un problema de construcción de subconjuntos a partir de una
	 * expresión regular en forma de ExpresionRegular, construyendo el autómata
	 * según el algoritmo de McNaughton-Yamada-Thompson.
	 * 
	 * @param expresionExpresionRegular
	 *            conteniendo la expresión regular a resolver.
	 */
	public ConstruccionSubconjuntos(ExpresionRegular expresion) {
		// Expresión sin aumentar.
		this.expresion = expresion.hijoIzquierdo();
		this.problema = this.expresion.toString();

		this.automata = new Automata(this.expresion, 0);

		// Calculo de estados
		this.estados = new TreeMap<>();
		this.transiciones = new MapaEstados();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicionVacia(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : this.automata.simbolos()) {
				char destino = transicion(estadoActual, simbolo);
				this.transiciones.add(estadoActual, simbolo, destino);
			}
			estadoActual++;
		}
	}

	/**
	 * Calcula la transición a partir de un estado dado mediante un símbolo
	 * concreto.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Símbolo de transición.
	 * @return Estado de destino.
	 */
	private char transicion(char estado, char simbolo) {
		Set<Nodo> posiciones = new TreeSet<>();

		for (Nodo nodo : estados.get(estado)) {
			if (this.estados.get(estado).contains(nodo))
				posiciones.addAll(automata.transicion(nodo, simbolo));
		}

		for (char est : estados()) {
			if (estados.get(est).equals(posiciones))
				return est;
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.put(est, posiciones);

		return est;
	}

	/**
	 * Devuelve el problema original, la expresión regular. Puede contener
	 * caracteres especiales.
	 * 
	 * @return Problema a resolver.
	 */
	public String problema() {
		return this.problema;
	}

	/**
	 * Devuelve el conjunto de símbolos que se utilizan en la expresión regular.
	 * 
	 * @return Símbolos que utiliza la expresión regular.
	 */
	public Set<Character> simbolos() {
		return automata.simbolos();
	}

	/**
	 * Devuelve un conjunto de caracteres representando los estados existentes
	 * en la tabla de transición.
	 * 
	 * @return Conjunto de estados en la tabla de transición.
	 */
	public Set<Character> estados() {
		return new TreeSet<>(estados.keySet());
	}

	/**
	 * Devuelve el conjunto de posiciones asociadas a un estado dado en la tabla
	 * de transición.
	 * 
	 * @param estado
	 *            Estado del que queremos calcular posiciones.
	 * @return Conjunto de posiciones asociadas al estado.
	 */
	public Set<Integer> posiciones(char estado) {
		Set<Integer> posiciones = new TreeSet<>();

		for (Nodo nodo : this.estados.get(estado))
			posiciones.add(nodo.posicion());

		return posiciones;
	}

	/**
	 * Devuelve el conjunto de posiciones encontradas en la expresión regular.
	 * El conjunto será un rango [1, n].
	 * 
	 * @return Conjunto de posiciones en la expresión regular.
	 */
	public Set<Integer> posiciones() {
		Set<Integer> posiciones = new TreeSet<Integer>();
		for (int i = this.automata.nodoInicial().posicion(); i <= this.automata
				.nodoFinal().posicion(); i++) {
			posiciones.add(i);
		}
		return posiciones;
	}

	/**
	 * Calcula el estado de destino para un estado de origen y un símbolo de
	 * transición.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Símbolo de transición.
	 * @return Estado de destino.
	 */
	public char mueve(char estado, char simbolo) {
		return this.transiciones.get(estado, simbolo);
	}

	/**
	 * Comprueba si un estado es final, es decir, si su conjunto de posiciones
	 * contiene un nodo final. Devuelve <code>true</code> si el estado es final,
	 * <code>false</code> si no.
	 * 
	 * @param estado
	 *            Estado a comprobar.
	 * @return <code>true</code> si el estado es final, <code>false</code> si
	 *         no.
	 */
	public boolean esFinal(char estado) {
		boolean esFinal = false;

		for (Nodo nodo : this.estados.get(estado))
			esFinal = esFinal || nodo.esFinal();

		return esFinal;
	}

	/**
	 * Devuelve una imagen representando el autómata asociado a este problema.
	 * 
	 * @return Imagen del autómata.
	 */
	public BufferedImage automata() {
		return automata.imagen();
	}

	/**
	 * Devuelve una programa en formato dot para generar la imagen representando
	 * el autómata asociado a este problema.
	 * 
	 * @return Programa formato dot para representar el autómata.
	 */
	public String automataDot() {
		return automata.imagenDot();
	}
	
	
	/**
	 * Devuelve una programa en formato TikZ para generar la imagen representando
	 * el autómata asociado a este problema.
	 * 
	 * @return Programa formato dot para representar el autómata.
	 */
	public String automataTikZ() {			//TODO
		return automata.imagenTikZ();
	}
	
	
	/**
	 * Devuelve una programa en formato svg para generar la imagen representando
	 * el autómata asociado a este problema.
	 * 
	 * @return Programa formato svg para representar el autómata.
	 */
	public String automataSvg() {
		return automata.imagenSvg();
	}
	
	/**
	 * Devuelve una solución (en azul) en formato svg para generar la imagen representando
	 * el autómata asociado a este problema.
	 * 
	 * @return Programa formato svg para representar el autómata.
	 */
	public String automataSvgSolucion() {
		String imagensvg = automata.imagenSvg();
		imagensvg = imagensvg.replace("stroke=\"black\"", "stroke=\"navy\"");
		imagensvg = imagensvg.replace("fill=\"black\"", "fill=\"navy\"");
		return imagensvg;
	}
	
	
	/**
	 * Devuelve una solución (en azul) en formato svg para generar la imagen representando
	 * el autómata asociado a este problema.
	 * 
	 * @return Programa formato svg para representar el autómata.
	 */
	public String automataTikZSolucion() {			//TODO
		String imagentikz = automata.imagenTikZ();
		imagentikz = imagentikz.replace("stroke=\"black\"", "stroke=\"navy\"");
		imagentikz = imagentikz.replace("fill=\"black\"", "fill=\"navy\"");
		return imagentikz;
	}
	

	/**
	 * Genera una serie de cuatro imágenes correspondientes a los autómatas de
	 * la expresión regular original del problema y de tres mutaciones de la
	 * misma, como alternativas en un problema de construcción de árbol.
	 * 
	 * @return Array de cuatro imágenes representando árboles de expresión
	 *         regular, una correspondiente al del problema y tres alternativas.
	 */
	public List<BufferedImage> alternativas() {
		if (this.alternativas == null) {
			alternativas = new ArrayList<>();

			Automata automata;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				automata = new Automata(expresion, 0);
				alternativas.add(automata.imagen());
			}
		}

		return new ArrayList<>(alternativas);
	}

	/**
	 * Genera una serie de cuatro programas dot con las imágenes
	 * correspondientes los autómatas de la expresión regular original del
	 * problema y de tres mutaciones de la misma, como alternativas en un
	 * problema de construcción de árbol.
	 * 
	 * @return Array de cuatro cadenas de caracteres conteniendo programas dot
	 *         representando autómatas de expresión regular, una correspondiente
	 *         al del problema y tres alternativas.
	 */
	public List<String> alternativasDot() {
		if (this.alternativasDot == null) {
			alternativasDot = new ArrayList<>();
			Automata automata;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				automata = new Automata(expresion, 0);
				alternativasDot.add(automata.imagenDot());
			}
		}

		return new ArrayList<>(alternativasDot);
	}

	/**
	 * Genera un set de alternativas para una expresión regular, incluyendo la
	 * original y tres otras.
	 * 
	 * @return Set completo de alternativas.
	 */
	public Set<ExpresionRegular> expresionesAlternativas() {
		log.info("Generando imágenes alternativas");

		int nSimbolos = simbolos().size();
		boolean usaVacio = simbolos().contains('\u0000');
		if (usaVacio)
			nSimbolos--;
		Generador generador = new Generador(nSimbolos, usaVacio, true);

		Set<ExpresionRegular> expresiones = new HashSet<>();
		expresiones.add(expresion);
		ExpresionRegular alternativa;
		while (expresiones.size() < 4) {
			alternativa = generador.mutacion(expresion);
			log.debug("Generada expresión alternativa {}", alternativa);
			expresiones.add(alternativa);
		}

		return expresiones;
	}
}
