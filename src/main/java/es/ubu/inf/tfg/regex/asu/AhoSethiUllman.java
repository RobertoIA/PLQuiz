package es.ubu.inf.tfg.regex.asu;

import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.inf.tfg.regex.asu.datos.MapaPosiciones;
import es.ubu.inf.tfg.regex.asu.datos.Nodo;
import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.datos.Generador;
import es.ubu.inf.tfg.regex.datos.MapaEstados;
import es.ubu.inf.tfg.regex.parser.CharStream;
import es.ubu.inf.tfg.regex.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.regex.parser.JavaCharStream;
import es.ubu.inf.tfg.regex.parser.ParseException;
import es.ubu.inf.tfg.regex.parser.TokenMgrError;

/**
 * AhoSethiUllman implementa la solución a un problema de construcción de un AFD
 * a partir de una expresión regular. Es capaz de trabajar tanto con una cadena
 * de caracteres como con una ExpresionRegular ya construida.
 * <p>
 * Asimismo actúa como fachada del subsistema es.ubu.inf.tfg.regex.asu, junto
 * con AhoSethiUllmanGenetico, evitando dependencias con los tipos de datos
 * internos. Todas las salidas se codifican en tipos de datos estándar.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class AhoSethiUllman {

	private static final Logger log = LoggerFactory
			.getLogger(AhoSethiUllman.class);

	private String problema;
	private ExpresionRegular expresion;
	private Nodo solucion;
	private MapaPosiciones<Character> estados;
	private MapaEstados transiciones;
	private List<BufferedImage> alternativas;
	private List<String> alternativasDot;
	private List<String> alternativasSvg;	/// JBA

	/**
	 * Resuelve un problema de construcción de AFD a partir de una expresión
	 * regular en forma de String utilizando el algoritmo de Aho-Sethi-Ullman.
	 * 
	 * @param problema
	 *            Cadena de caracteres conteniendo la expresión regular a
	 *            resolver.
	 * @throws UnsupportedOperationException
	 *             Error del parser o del token manager. Indica que la expresión
	 *             no es válida o que contiene caracteres no reconocidos.
	 */
	public AhoSethiUllman(String problema) throws UnsupportedOperationException {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		CharStream input = new JavaCharStream(new StringReader(problema));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
		} catch (ParseException | TokenMgrError e) {
			throw new UnsupportedOperationException("Expresión no válida.");
		}

		this.solucion = new Nodo(this.expresion);

		// calculo estados
		this.estados = new MapaPosiciones<>();
		this.transiciones = new MapaEstados();

		this.estados.add('A', primeraPos());
		char estadoActual = 'A';

		// Mientras queden estados por rellenar
		while (estados.keys().contains(estadoActual)) {
			// Para cada símbolo no final
			for (char simbolo : simbolos()) {
				if (simbolo != '$') {
					// Calculamos y almacenamos la transición
					char destino = transicion(estadoActual, simbolo);
					this.transiciones.add(estadoActual, simbolo, destino);
				}
			}
			// Pasamos al siguiente estado, alfabéticamente
			estadoActual++;
		}
	}

	/**
	 * Resuelve un problema de construcción de AFD a partir de una expresión
	 * regular en forma ExpresionRegular utilizando el algoritmo de
	 * Aho-Sethi-Ullman. Se considera que la expresión recibida ya ha sido
	 * aumentada.
	 * 
	 * @param expresion
	 *            ExpresionRegular conteniendo la expresión regular a resolver.
	 */
	public AhoSethiUllman(ExpresionRegular expresion) {
		// Expresión sin aumentar.
		this.problema = expresion.hijoIzquierdo().toString();
		this.expresion = expresion;

		this.solucion = new Nodo(this.expresion);

		// Calculo estados
		this.estados = new MapaPosiciones<>();
		this.transiciones = new MapaEstados();

		this.estados.add('A', primeraPos());
		char estadoActual = 'A';

		// Mientras queden estados por rellenar
		while (estados.keys().contains(estadoActual)) {
			// Para cada símbolo no final
			for (char simbolo : simbolos()) {
				if (simbolo != '$') {
					// Calculamos y almacenamos la transición
					char destino = transicion(estadoActual, simbolo);
					this.transiciones.add(estadoActual, simbolo, destino);
				}
			}
			// Pasamos al siguiente estado, alfabéticamente
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
		Set<Integer> posiciones = new TreeSet<>();

		for (int pos : posiciones(simbolo)) {
			if (this.estados.get(estado).contains(pos))
				posiciones.addAll(siguientePos(pos));
		}

		// Comprobar si existe el estado o crear uno nuevo.
		for (char est : estados()) {
			if (estado(est).equals(posiciones)) {
				return est;
			}
		}

		char est = (char) (this.estados.size() + 'A');
		this.estados.add(est, posiciones);
		return est;
	}

	/**
	 * Devuelve el problema original, la expresión regular sin aumentar. Puede
	 * contener caracteres especiales.
	 * 
	 * @return Problema a resolver.
	 */
	public String problema() {
		return this.problema;
	}

	/**
	 * Devuelve la expresión procesada y aumentada con la que vamos a trabajar.
	 * 
	 * @return Expresión regular aumentada.
	 */
	public String expresionAumentada() {
		return this.expresion.toString();
	}

	/**
	 * Devuelve el conjunto primera-pos del problema.
	 * 
	 * @return Conjunto primera-pos.
	 */
	public Set<Integer> primeraPos() {
		return this.solucion.primeraPos();
	}

	/**
	 * Devuelve el conjunto de símbolos que se utilizan en la expresión regular.
	 * 
	 * @return Símbolos que utiliza la expresión regular.
	 */
	public Set<Character> simbolos() {
		return this.solucion.simbolos().keys();
	}

	/**
	 * Devuelve el conjunto de posiciones en las que se encuentra un símbolo
	 * dentro de la expresión regular.
	 * 
	 * @param simbolo
	 *            Símbolo a buscar.
	 * @return Conjunto de posiciones en las que encontramos el símbolo.
	 */
	public Set<Integer> posiciones(char simbolo) {
		return this.solucion.simbolos().get(simbolo);
	}

	/**
	 * Devuelve el conjunto de posiciones encontradas en la expresión regular.
	 * El conjunto será un rango [1, n].
	 * 
	 * @return Conjunto de posiciones en la expresión regular.
	 */
	public Set<Integer> posiciones() {
		return this.solucion.siguientePos().keys();
	}

	/**
	 * Calcula la función siguiente-pos(n) para un n dado.
	 * 
	 * @param n
	 *            Posición para la que calculamos la función.
	 * @return Resultado de la función siguiente-pos para la posición dada.
	 */
	public Set<Integer> siguientePos(int n) {
		return this.solucion.siguientePos().get(n);
	}

	/**
	 * Devuelve un conjunto de caracteres representando los estados existentes
	 * en la tabla de transición.
	 * 
	 * @return Conjunto de estados en la tabla de transición.
	 */
	public Set<Character> estados() {
		return this.estados.keys();
	}

	/**
	 * Devuelve el conjunto de posiciones asociadas a un estado dado en la tabla
	 * de transición.
	 * 
	 * @param key
	 *            Estado del que queremos calcular posiciones.
	 * @return Conjunto de posiciones asociadas al estado.
	 */
	public Set<Integer> estado(char key) {
		return this.estados.get(key);
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
	 * Comprueba si un estado es final, es decir, si contiene el símbolo
	 * aumentado ('$'). Devuelve <code>true</code> si el estado es final,
	 * <code>false</code> si no.
	 * 
	 * @param estado
	 *            Estado a comprobar.
	 * @return <code>true</code> si el estado es final, <code>false</code> si
	 *         no.
	 */
	public boolean esFinal(char estado) {
		// Solo hay una posición final por expresión.
		int posicionfinal = posiciones('$').iterator().next();
		return this.estados.get(estado).contains(posicionfinal);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la última-pos de uno de los
	 * nodos del árbol, definidos con un carácter comenzando por 'A', y
	 * etiquetando cada nivel de izquierda a derecha.
	 * 
	 * @param simbolo
	 *            Etiqueta del nodo.
	 * @return última-pos del nodo.
	 */
	public Set<Integer> primeraPos(char simbolo) {
		return solucion.primeraPos(simbolo);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la última-pos de uno de los
	 * nodos del árbol, definidos con un carácter comenzando por 'A', y
	 * etiquetando cada nivel de izquierda a derecha.
	 * 
	 * @param simbolo
	 *            Etiqueta del nodo.
	 * @return última-pos del nodo.
	 */
	public Set<Integer> ultimaPos(char simbolo) {
		return solucion.ultimaPos(simbolo);
	}

	/**
	 * Comprueba si uno de los nodos hijos del árbol es anulable, definidos con
	 * un carácter comenzando por 'A', y etiquetando cada nivel de izquierda a
	 * derecha.
	 * 
	 * @param simbolo
	 *            Etiqueta del nodo.
	 * @return <code>true</code> si es anulable, <code>false</code> si no.
	 */
	public boolean esAnulable(char simbolo) {
		return solucion.esAnulable(simbolo);
	}

	/**
	 * Devuelve una imagen representando el árbol de expresión regular asociado
	 * a este problema, sin completar y con los nodos etiquetados.
	 * 
	 * @return Imagen del árbol vacío.
	 */
	public BufferedImage arbolVacio() {
		return this.solucion.imagen();
	}

	/**
	 * Devuelve una programa en formato dot para generar la imagen representando
	 * el árbol de expresión regular asociado a este problema, sin completar y
	 * con los nodos etiquetados.
	 * 
	 * @return Programa formato dot para representar el árbol vacío.
	 */
	public String arbolVacioDot() {
		return this.solucion.imagenDot();
	}
	
	/**
	 * Devuelve una programa en formato SVG para generar la imagen representando
	 * el árbol de expresión regular asociado a este problema, sin completar y
	 * con los nodos etiquetados.
	 * 
	 * @return Programa formato dot para representar el árbol vacío.
	 */
	public String arbolVacioSvg() {
		return this.solucion.imagenSvg();
	}
	
	/**
	 * Devuelve una programa en formato PDF para generar la imagen representando
	 * el árbol de expresión regular asociado a este problema, sin completar y
	 * con los nodos etiquetados.
	 * 
	 * @return Programa formato dot para representar el árbol vacío.
	 */
	public String arbolVacioPdf() {
		return this.solucion.imagenSvg();
	}

	/**
	 * Genera una serie de cuatro imágenes correspondientes la expresión regular
	 * original del problema y tres mutaciones de la misma, como alternativas
	 * en un problema de construcción de árbol.
	 * 
	 * @return Array de cuatro imágenes representando árboles de expresión
	 *         regular, una correspondiente al del problema y tres alternativas.
	 */
	public List<BufferedImage> alternativas() {
		if (this.alternativas == null) {
			alternativas = new ArrayList<>();
			
			for (ExpresionRegular expresion : expresionesAlternativas())
				alternativas.add(expresion.imagen());
		}

		return new ArrayList<>(alternativas);
	}

	/**
	 * Genera una serie de cuatro programas dot con las imágenes
	 * correspondientes la expresión regular original del problema y tres
	 * mutaciones de la misma, como alternativas en un problema de construcción
	 * de árbol.
	 * 
	 * @return Array de cuatro cadenas de caracteres conteniendo programas dot
	 *         representando árboles de expresión regular, una correspondiente
	 *         al del problema y tres alternativas.
	 */
	public List<String> alternativasDot() {
		if (this.alternativasDot == null) {
			alternativasDot = new ArrayList<>();
			Nodo nodo;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				nodo = new Nodo(expresion);
				alternativasDot.add(nodo.imagenDot());
			}
		}

		return new ArrayList<>(alternativasDot);
	}
	
	/**
	 * Genera una serie de cuatro programas svg con las imágenes
	 * correspondientes la expresión regular original del problema y tres
	 * mutaciones de la misma, como alternativas en un problema de construcción
	 * de árbol.
	 * 
	 * @return Array de cuatro cadenas de caracteres conteniendo programas dot
	 *         representando árboles de expresión regular, una correspondiente
	 *         al del problema y tres alternativas.
	 *         
	 * @author JBA
	 */
	public List<String> alternativasSvg() {			
		if (this.alternativasSvg == null) {
			alternativasSvg = new ArrayList<>();
			Nodo nodo;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				nodo = new Nodo(expresion);
				alternativasSvg.add(nodo.imagenSvg());
			}
		}

		return alternativasSvg;
	}
	
	/**
	 * Genera una serie de cuatro soluciones (azul) svg con las imágenes
	 * correspondientes la expresión regular original del problema y tres
	 * mutaciones de la misma, como alternativas en un problema de construcción
	 * de árbol.
	 * 
	 * @return Array de cuatro cadenas de caracteres conteniendo programas dot
	 *         representando árboles de expresión regular, una correspondiente
	 *         al del problema y tres alternativas.
	 *         
	 * @author JBA
	 */
	public List<String> alternativasSvgSolucion() {			
		if (this.alternativasSvg == null) {
			alternativasSvg = new ArrayList<>();
			Nodo nodo;
			for (ExpresionRegular expresion : expresionesAlternativas()) {
				nodo = new Nodo(expresion);
				String imagensvg = nodo.imagenSvg();
				
				imagensvg = imagensvg.replace("stroke=\"black\"", "stroke=\"navy\"");
				imagensvg = imagensvg.replace("fill=\"black\"", "fill=\"navy\"");
				
				alternativasSvg.add(imagensvg);
			}
		}

		return alternativasSvg;
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
