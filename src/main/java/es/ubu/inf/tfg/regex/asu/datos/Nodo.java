package es.ubu.inf.tfg.regex.asu.datos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.SwingConstants;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

/**
 * Nodo implementa la información calculada sobre un nodo ExpresionRegular. El
 * cálculo se realiza de forma recursiva recorriendo el árbol hacia abajo. El
 * valor de los atributos de un nodo depende directamente de los valores de los
 * atributos de los nodos de sus descendientes.
 * <p>
 * Nodo forma una estructura de árbol paralela a la de ExpresionRegular,
 * pudiendo accederse desde el nodo a la expresión correspondiente.
 * <p>
 * Nodo es una estructura de datos inmutable. Aunque la estructura de la
 * expresión regular sobre la que se calculó cambie, los datos y la estructura
 * de Nodo continuarán siendo válidos ya que mantiene una referencia nodo Nodo a
 * nodo ExpresionRegular, sin depender de la estructura de estos últimos.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Nodo {

	private final ExpresionRegular expresion;
	private final Nodo hijoIzquierdo;
	private final Nodo hijoDerecho;

	private boolean esAnulable;
	private Set<Integer> primeraPos;
	private Map<Character, Set<Integer>> primerasPos;
	private Set<Integer> ultimaPos;
	private Map<Character, Set<Integer>> ultimasPos;
	// private Map<Character, String> tipos;
	private Map<Character, Boolean> anulables;

	private MapaPosiciones<Character> simbolos;
	private MapaPosiciones<Integer> siguientePos;

	private BufferedImage imagen;
	private String imagenDot;
	private String imagenSvg;	//JBA
	
	//TikZ
	private String tipo;
	private char simb;
	public static int letra = 0;
	public static int pos = 0;
	public static Map<String, Integer> leavePos = new HashMap<String, Integer>();
	public static Map<String, String> annotationsPripos = new HashMap<String, String>();
	public static Map<String, String> annotationsUltpos = new HashMap<String, String>();

	/**
	 * Calcula los atributos de un nodo ExpresionRegular a partir de los de sus
	 * descendientes. La forma de realizar estos cálculos dependerá del tipo de
	 * nodo ExpresionRegular.
	 * 
	 * @param expresion
	 *            ExpresionRegular sobre la que realizar los cálculos.
	 * @throws IllegalArgumentException
	 *             En caso de que el nodo no pertenezca a ningún tipo conocido.
	 */
	public Nodo(ExpresionRegular expresion) {
		this.expresion = expresion;

		if (expresion.esVacio()) { // nodo vació
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = new TreeSet<Integer>();
			this.ultimaPos = new TreeSet<Integer>();
			this.simbolos = new MapaPosiciones<>();
			this.siguientePos = new MapaPosiciones<>();
			
			//TikZ
			this.tipo = "EPS";
			

		} else if (expresion.esSimbolo()) { // nodo símbolo / aumentado
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = false;
			this.primeraPos = new TreeSet<Integer>();
			this.primeraPos.add(expresion.posicion());
			this.ultimaPos = new TreeSet<Integer>(this.primeraPos);
			this.simbolos = new MapaPosiciones<>();
			this.simbolos.add(expresion.simbolo(), expresion.posicion());
			this.siguientePos = new MapaPosiciones<>();
			this.siguientePos.add(expresion.posicion());
			
			//TikZ
			this.tipo = "SYMB";
			this.simb = expresion.simbolo();
			

		} else if (expresion.esCierre()) { // nodo cierre
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			this.ultimaPos = this.hijoIzquierdo.ultimaPos();
			this.simbolos = this.hijoIzquierdo.simbolos();
			this.siguientePos = this.hijoIzquierdo.siguientePos();
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(), this.hijoIzquierdo.primeraPos());
			
			//TikZ
			this.tipo = "AST";

		} else if (expresion.esConcat()) { // nodo concat
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					&& this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			if (this.hijoIzquierdo.esAnulable())
				this.primeraPos.addAll(this.hijoDerecho.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			if (this.hijoDerecho.esAnulable())
				this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(),
					this.hijoDerecho.primeraPos());
			
			//TikZ
			this.tipo = "CAT";

		} else if (expresion.esUnion()) { // nodo union
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					|| this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoDerecho.primeraPos();
			this.primeraPos.addAll(this.hijoIzquierdo.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());
			
			//TikZ
			this.tipo = "SEL";

		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresión regular de tipo desconocido.");
		}

		this.primerasPos = new TreeMap<>();
		this.ultimasPos = new TreeMap<>();
		// this.tipos = new TreeMap<>();
		this.anulables = new TreeMap<>();
	}

	/**
	 * Devuelve una referencia a la expresión regular sobre la que se calculó
	 * este nodo. La manera correcta de recorrer el árbol es a través del nodo,
	 * no de la expresión regular obtenida mediante este método, ya que su
	 * estructura se considera mutable.
	 * 
	 * @return ExpresionRegular de la que se obtuvo este nodo.
	 */
	public ExpresionRegular expresion() {
		return this.expresion;
	}

	/**
	 * Devuelve una referencia al nodo hijo izquierdo de este nodo. Los nodos
	 * cuya expresión regular asociada sean tipo símbolo o vacío no tienen hijo
	 * izquierdo, y lanzan <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo de la expresión asociada sea de tipo
	 *             símbolo o vacío.
	 * @return Referencia al operando izquierdo del nodo.
	 */
	public Nodo hijoIzquierdo() {
		if (this.expresion.esSimbolo() || this.expresion.esVacio())
			throw new UnsupportedOperationException(
					"Los nodos símbolo y vacío no tienen hijo izquierdo.");
		return this.hijoIzquierdo;
	}

	/**
	 * Devuelve una referencia al nodo hijo izquierdo de este nodo. Los nodos
	 * cuya expresión regular asociada sean símbolo, vacío o cierre no tienen
	 * hijo derecho, y lanzan <code>UnsupportedOperationException</code>.
	 * 
	 * @throws UnsupportedOperationException
	 *             en caso de que el nodo de la expresión asociada sea de tipo
	 *             símbolo, vacío o cierre.
	 * @return Referencia al operando derecho del nodo.
	 */
	public Nodo hijoDerecho() {
		if (this.expresion.esSimbolo() || this.expresion.esVacio()
				|| this.expresion.esCierre())
			throw new UnsupportedOperationException(
					"Los nodos símbolo, vacío y cierre no tienen hijo derecho.");
		return this.hijoDerecho;
	}

	/**
	 * Comprueba si el nodo se define como anulable.
	 * 
	 * @return <code>true</code> si el nodo es anulable, <code>false</code> si
	 *         no.
	 */
	public boolean esAnulable() {
		return this.esAnulable;
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la primera-pos del nodo.
	 * 
	 * @return primera-pos del nodo.
	 */
	public Set<Integer> primeraPos() {
		return new TreeSet<>(this.primeraPos);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la primera-pos de uno de
	 * los nodos hijos del árbol, definidos con un carácter comenzando por 'A',
	 * y etiquetando cada nivel de izquierda a derecha.
	 * 
	 * @param simbolo
	 *            Etiqueta del nodo.
	 * @return primera-pos del nodo.
	 */
	public Set<Integer> primeraPos(char simbolo) {
		return primerasPos.get(simbolo);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la última-pos del nodo.
	 * 
	 * @return última-pos del nodo.
	 */
	public Set<Integer> ultimaPos() {
		return new TreeSet<>(this.ultimaPos);
	}

	/**
	 * Obtiene el conjunto de posiciones que definen la última-pos de uno de los
	 * nodos hijos del árbol, definidos con un carácter comenzando por 'A', y
	 * etiquetando cada nivel de izquierda a derecha.
	 * 
	 * @param simbolo
	 *            Etiqueta del nodo.
	 * @return última-pos del nodo.
	 */
	public Set<Integer> ultimaPos(char simbolo) {
		return ultimasPos.get(simbolo);
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
		return anulables.get(simbolo);
	}

	/**
	 * Devuelve un diccionario de los símbolos encontrados en la expresión, y
	 * sus posiciones.
	 * 
	 * @return Diccionario de símbolos.
	 */
	public MapaPosiciones<Character> simbolos() {
		return MapaPosiciones.copia(this.simbolos);
	}

	/**
	 * Obtiene el conjunto siguiente-pos del nodo.
	 * 
	 * @return siguiente-pos del nodo.
	 */
	public MapaPosiciones<Integer> siguientePos() {
		return MapaPosiciones.copia(this.siguientePos);
	}

	/**
	 * Genera una imagen representando la estructura del árbol de la expresión,
	 * con los nodos marcados pero vacíos. La imagen generada se cachea al ser
	 * solicitada por primera vez para evitar realizar los cálculos repetidas
	 * veces.
	 * 
	 * @return Imagen conteniendo el árbol que representa a la expresión.
	 */
	public BufferedImage imagen() {
		if (this.imagen == null) {
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<Nodo, Object> gNodos = new HashMap<>();
			Nodo actual;
			Object gNodo, gActual;
			List<Nodo> siguientes = new ArrayList<>();
			boolean tieneHijoIzquierdo, tieneHijoDerecho;
			char actualLetra = 'A';

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;";
			String estiloEdge = "strokeColor=black;fontColor=black;labelBackgroundColor=white;endArrow=open;";

			graph.getModel().beginUpdate();
			try {
				siguientes.add(this);

				while (!siguientes.isEmpty()) {
					actual = siguientes.get(0);

					if (!gNodos.containsKey(actual)) {
						gActual = graph.insertVertex(parent, null,
								actualLetra++ + "\n" + actual.tipo(), 0, 0, 30,
								30, estiloVertex);
						gNodos.put(actual, gActual);

						primerasPos.put((char) (actualLetra - 1),
								actual.primeraPos());
						ultimasPos.put((char) (actualLetra - 1),
								actual.ultimaPos());
						anulables.put((char) (actualLetra - 1),
								actual.esAnulable());
					} else {
						gActual = gNodos.get(actual);
					}

					tieneHijoIzquierdo = !actual.expresion().esSimbolo()
							&& !actual.expresion().esVacio();
					tieneHijoDerecho = tieneHijoIzquierdo
							&& !actual.expresion().esCierre();

					if (tieneHijoIzquierdo) {
						siguientes.add(actual.hijoIzquierdo());
						gNodo = graph.insertVertex(parent, null, actualLetra++
								+ " \n" + actual.hijoIzquierdo().tipo(), 0, 0,
								30, 30, estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoIzquierdo(), gNodo);

						primerasPos.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().primeraPos());
						ultimasPos.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().ultimaPos());
						anulables.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().esAnulable());
					}

					if (tieneHijoDerecho) {
						siguientes.add(actual.hijoDerecho());
						gNodo = graph.insertVertex(parent, null, actualLetra++
								+ "\n" + actual.hijoDerecho().tipo(), 0, 0, 30,
								30, estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoDerecho(), gNodo);

						primerasPos.put((char) (actualLetra - 1), actual
								.hijoDerecho().primeraPos());
						ultimasPos.put((char) (actualLetra - 1), actual
								.hijoDerecho().ultimaPos());
						anulables.put((char) (actualLetra - 1), actual
								.hijoDerecho().esAnulable());
					}

					siguientes.remove(actual);
				}
			} finally {
				graph.getModel().endUpdate();

				mxGraphComponent graphComponent = new mxGraphComponent(graph);

				new mxHierarchicalLayout(graph, SwingConstants.NORTH)
						.execute(parent);
				new mxParallelEdgeLayout(graph).execute(parent);

				this.imagen = mxCellRenderer.createBufferedImage(graph, null,
						1, Color.WHITE, graphComponent.isAntiAlias(), null,
						graphComponent.getCanvas());
				
			}
		}

		return this.imagen;
	}

	/**
	 * Genera el programa en formato dot para generar la imagen representando la
	 * estructura del árbol de la expresión. El programa generado se cachea al
	 * ser solicitado por primera vez para evitar realizar los cálculos
	 * repetidas veces.
	 * 
	 * @return Programa dot conteniendo el árbol que representa a la expresión.
	 */
	public String imagenDot() {
		if (this.imagenDot == null) {
			List<Nodo> siguientes = new ArrayList<>();
			Nodo actual;
			boolean tieneHijoIzquierdo, tieneHijoDerecho;
			char actualLetra = 'A';
			char preLetra, nuevaLetra;
			Map<Nodo, Character> nodos = new HashMap<>();

			this.imagenDot = "digraph {";

			siguientes.add(this);
			while (!siguientes.isEmpty()) {
				actual = siguientes.get(0);

				tieneHijoIzquierdo = !actual.expresion().esSimbolo()
						&& !actual.expresion().esVacio();
				tieneHijoDerecho = tieneHijoIzquierdo
						&& !actual.expresion().esCierre();

				if (!nodos.containsKey(actual)) {
					preLetra = actualLetra;
					imagenDot += "\n\t" + actualLetra + " [label=\""
							 + actualLetra + "\n" + tipo(actual.tipo()) +"\"];";
					nodos.put(actual, actualLetra);
					actualLetra++;
				} else {
					preLetra = nodos.get(actual);
				}

				if (tieneHijoIzquierdo) {
					nuevaLetra = (char) (actualLetra++);
					siguientes.add(actual.hijoIzquierdo());
					nodos.put(actual.hijoIzquierdo(), nuevaLetra);
					imagenDot += "\n\t" + preLetra + " -> " + nuevaLetra;
					imagenDot += "\n\t" + nuevaLetra + " [label=\""
							+ nuevaLetra + "\n" + tipo(actual.hijoIzquierdo().tipo()) + "\"];";
				}

				if (tieneHijoDerecho) {
					nuevaLetra = (char) (actualLetra++);
					siguientes.add(actual.hijoDerecho());
					nodos.put(actual.hijoDerecho(), nuevaLetra);
					imagenDot += "\n\t" + preLetra + " -> " + nuevaLetra;
					imagenDot += "\n\t" + nuevaLetra + " [label=\""
							+ nuevaLetra + "\n" + tipo(actual.hijoDerecho().tipo()) + "\"];";
				}

				siguientes.remove(actual);
			}

			this.imagenDot += "\n}";
		}

		return this.imagenDot;
	}
	
	
	/**
	 * Genera una imagen representando la estructura del ï¿½rbol de la expresiï¿½n,
	 * con los nodos marcados pero vacï¿½os. La imagen generada se cachea al ser
	 * solicitada por primera vez para evitar realizar los cï¿½lculos repetidas
	 * veces.
	 * 
	 * @return Imagen conteniendo el ï¿½rbol que representa a la expresiï¿½n.
	 */
	public String imagenSvg() {
		if (this.imagenSvg == null) {
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<Nodo, Object> gNodos = new HashMap<>();
			Nodo actual;
			Object gNodo, gActual;
			List<Nodo> siguientes = new ArrayList<>();
			boolean tieneHijoIzquierdo, tieneHijoDerecho;
			char actualLetra = 'A';

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;";
			String estiloEdge = "strokeColor=black;fontColor=black;labelBackgroundColor=white;endArrow=open;";

			graph.getModel().beginUpdate();
			siguientes.add(this);
			try {

				while (!siguientes.isEmpty()) {
					actual = siguientes.get(0);

					if (!gNodos.containsKey(actual)) {
						gActual = graph.insertVertex(parent, null,
								actualLetra++ + "\n" + tipoSVG(actual.tipo()), 0, 0, 30,
								30, estiloVertex);
						gNodos.put(actual, gActual);

						primerasPos.put((char) (actualLetra - 1),
								actual.primeraPos());
						ultimasPos.put((char) (actualLetra - 1),
								actual.ultimaPos());
						anulables.put((char) (actualLetra - 1),
								actual.esAnulable());
					} else {
						gActual = gNodos.get(actual);
					}

					tieneHijoIzquierdo = !actual.expresion().esSimbolo()
							&& !actual.expresion().esVacio();
					tieneHijoDerecho = tieneHijoIzquierdo
							&& !actual.expresion().esCierre();

					if (tieneHijoIzquierdo) {
						siguientes.add(actual.hijoIzquierdo());
						gNodo = graph.insertVertex(parent, null, actualLetra++
								+ " \n" + tipoSVG(actual.hijoIzquierdo().tipo()), 0, 0,
								30, 30, estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoIzquierdo(), gNodo);

						primerasPos.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().primeraPos());
						ultimasPos.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().ultimaPos());
						anulables.put((char) (actualLetra - 1), actual
								.hijoIzquierdo().esAnulable());
					}

					if (tieneHijoDerecho) {
						siguientes.add(actual.hijoDerecho());
						gNodo = graph.insertVertex(parent, null, actualLetra++
								+ "\n" + tipoSVG(actual.hijoDerecho().tipo()), 0, 0, 30,
								30, estiloVertex);
						graph.insertEdge(parent, null, "", gActual, gNodo,
								estiloEdge);
						gNodos.put(actual.hijoDerecho(), gNodo);

						primerasPos.put((char) (actualLetra - 1), actual
								.hijoDerecho().primeraPos());
						ultimasPos.put((char) (actualLetra - 1), actual
								.hijoDerecho().ultimaPos());
						anulables.put((char) (actualLetra - 1), actual
								.hijoDerecho().esAnulable());
					}

					siguientes.remove(actual);
				}
			} finally {
				graph.getModel().endUpdate();
				
				new mxHierarchicalLayout(graph, SwingConstants.NORTH)
						.execute(parent);
				new mxParallelEdgeLayout(graph).execute(parent);
				
				
				this.imagenSvg = mxXmlUtils.getXml(mxCellRenderer.createSvgDocument(graph, null,
						1, Color.WHITE, null));
				
				this.imagenSvg = fixXMLSVG(this.imagenSvg);
				
			}
		}

		return this.imagenSvg;
	}
	
	
	/**
	 * Genera una imagen representando la estructura del ï¿½rbol de la expresiï¿½n,
	 * con los nodos marcados pero vacï¿½os. La imagen generada se cachea al ser
	 * solicitada por primera vez para evitar realizar los cï¿½lculos repetidas
	 * veces.
	 * 
	 * @return Imagen conteniendo el ï¿½rbol que representa a la expresiï¿½n.
	 */
	public String imagenTikZ() {

		String out = "\\begin{adjustbox}{max size={\\textwidth}{\\textheight}}\n" + 
				"\\begin{tikzpicture}[sibling distance=2cm,level distance=1.5cm,\n" + 
				"    color=blue!40!black, level 1/.style={sibling distance=1.0cm},\n" + 
				"    level 2/.style={sibling distance=2.0cm,level distance=1.5cm},\n" + 
				"    level 3/.style={sibling distance=2.0cm},\n" + 
				"    % growth parent anchor={center}, nodes={anchor=center},\n" + 
				"    parent anchor={center},child anchor={center},%\n" + 
				"    ]\n" + 
				"\\tikzset{edge from parent/.append style={thick}}\n" + 
				"\\Tree";
		out += dibujaImagenTikZ();
		
		out += "\n\\iflabeled\n";
	    out += "% ===================================================\n";
	    out += "% VALUES OF THE POSITIONS FOR LEAVE NODES WITH SYMBOL\n";
	    for ( Map.Entry<String, Integer> entry : leavePos.entrySet() ) {
	        String l = entry.getKey();
	        int lPos = entry.getValue();
	        
	        out += String.format("\\node[below=.1mm of {%s}]{%d};\n", l, lPos);
	    }
	    out += "% ===================================================\n";
	    
	    out += "% ANNOTATIONS AT EACH NODE\n";
	    
	    for ( Entry<String, String> entry : annotationsPripos.entrySet() ) {
	    	String k = entry.getKey();
	        String pripos = entry.getValue();
	        String ultpos = annotationsUltpos.get(k);
	        
	        out += String.format("\\node[left=\\sep of %s,align=right]{\\{%s\\}};\n", k, pripos);
	        out += String.format("\\node[right=\\sep of %s,align=left]{\\{%s\\}};\n", k, ultpos);
	        
	    }
	    out += "\\fi\n";
		
		out += "\n\\end{tikzpicture}";
		out += "\n\\end{adjustbox}";
		
		// Reset
		letra = 0;
		pos = 0;
		leavePos = new HashMap<String, Integer>();
		annotationsPripos = new HashMap<String, String>();
		annotationsUltpos = new HashMap<String, String>();
		
		return out;
	}
	
	public String dibujaImagenTikZ() {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String out = "";
		HashMap<String, String>  op =  new HashMap<String, String>();
		op.put("CAT", "\\cat");
		op.put("SEL", "\\sel");
		op.put("AST", "\\ast"); 
		
		if (this.tipo == "EPS" || this.tipo == "SYMB") {
			String l = Character.toString(letras.charAt(letra));
			letra ++;
			
			if (this.esAnulable()) {
				out += String.format("\n\\node[nullable] (%s) {%s\\nodepart{lower}\\eps};", l, l);
				annotationsPripos.put(l, "\\empty");
				annotationsUltpos.put(l, "\\empty");
			} else {
				pos ++;
				String s = Character.toString(this.simb);
				s = s.equals("$") ? "\\$" : s;
				out += String.format("\n\\node[normal] (%s) {%s\\nodepart{lower}\\term{%s}};", l, l, s);
				annotationsPripos.put(l, Integer.toString(pos));
				annotationsUltpos.put(l, Integer.toString(pos));
				leavePos.put(l, pos);
			}
			return out;
		}
			
		String hijoI = "";
		String hijoD = "";
		
		if(this.hijoIzquierdo != null)
			hijoI = this.hijoIzquierdo().dibujaImagenTikZ();
		
		if(this.hijoDerecho != null) 
			hijoD = this.hijoDerecho().dibujaImagenTikZ();
		
		String l = Character.toString(letras.charAt(letra));
		letra ++;
		
		String pripos = this.primeraPos.toString().replace("[", "{").replace("]", "}");
		String ultpos = this.ultimaPos.toString().replace("[", "{").replace("]", "}");
		
		annotationsPripos.put(l, pripos);
		annotationsUltpos.put(l, ultpos);
		
		String nodeType = this.esAnulable() ? "nullable" : "normal";
		
		out += String.format("\n[.\\node[%s] (%s) {%s\\nodepart{lower}%s};", nodeType, l, l, op.get(this.tipo));
		out += hijoI;
		out += hijoD;

        out += "]";
				

		return out;
	}

	/**
	 * Convierte una cadena al formato compatible con graphviz.
	 * 
	 * @param string
	 *            Cadena original.
	 * @return Cadena en formato compatible.
	 */
	private String tipo(String tipo) {
		switch (tipo) {
		case "\u03B5":
			return "&#949;";
		case "\u2027":
			return "&#8226;";
		default:
			return tipo;
		}
	}
	
	/**
	 * Convierte una cadena al formato compatible con SVG.
	 * 
	 * @param string
	 *            Cadena original.
	 * @return Cadena en formato compatible.
	 */
	private String tipoSVG(String tipo) {
		switch (tipo) {
		case "\u03B5":
			return "&#949;";
		case "\u2027":
			return "&#8226;";
		default:
			return tipo;
		}
	}

	/**
	 * Devuelve una cadena describiendo el tipo del nodo de la expresión regular
	 * asociada a este nodo.
	 * 
	 * @return Tipo del nodo.
	 */
	String tipo() {
		if (expresion.esCierre())
			return "*";
		else if (expresion.esConcat())
			return "\u2027";
		else if (expresion.esSimbolo())
			return "" + expresion.simbolo();
		else if (expresion.esUnion())
			return "|";
		else
			// vacío
			return "\u03B5";
	}
	
	/**
	 * Soluciona problemas de encodificación de ciertos caracteres.
	 * 
	 * @param imagenSgv
	 */
	private String fixXMLSVG(String imagenSvg) {
				
		return imagenSvg.replace("&amp;#949;", "&#949;").replace("&amp;#8226;", "&#8226;");
		
	}
}
