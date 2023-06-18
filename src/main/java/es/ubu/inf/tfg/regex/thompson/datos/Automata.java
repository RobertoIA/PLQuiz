package es.ubu.inf.tfg.regex.thompson.datos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

// This is needed to access Locale.US and force the use of dot in String.format
// SEE: https://stackoverflow.com/questions/49069386/make-java-write-floats-with-dot-instead-of-comma
import java.util.Locale;

import javax.swing.SwingConstants;

import org.w3c.dom.Document;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

/**
 * Automata implementa el modelo lógico de un autómata finito no determinista,
 * compuesto por una serie de nodos y de transiciones entre dichos nodos.
 * Permite obtener los nodos obtenidos a partir de uno dado utilizando un tipo
 * de transición determinado, y consumiendo o no un carácter dado.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;
	private Set<Character> simbolos;
	private BufferedImage imagen;
	private String imagenDot;
	private String imagenSvg;	// JBA
	private int idCounter;	// JBA
	private static int r = 12;	// JBA
	
	//TikZ
	private double initialStateCoordinatesX = (double) 0;
	private double initialStateCoordinatesY = (double) 0;
	private double finalStateCoordinatesX = (double) 0;
	private double finalStateCoordinatesY = (double) 0;
	private int biggestState = 0;
	private static int newStateCounter = 0;
	NodoTikZ nodoInicialTikZ;	// TODO getters
	NodoTikZ nodoFinalTikZ;
	public String tipo;
	public int id;
	public double ancho;
	public double alto;
	public double yNew;
	public String texto;
	public Automata hijoI;
	public Automata hijoD;

	/**
	 * Constructor. Define un autómata finito no determinista a partir de un
	 * árbol de expresión regular dado, de manera recursiva.
	 * 
	 * @param expresion
	 *            Árbol de expresión regular a partir del cual generar el
	 *            autómata.
	 */
	public Automata(ExpresionRegular expresion, int posicionInicial) {
		

		this.simbolos = new TreeSet<>();

		if (expresion.esVacio()) {
			
			// Automata
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.añadeTransicionVacia(this.nodoFinal);
			
			//TikZ
			this.tipo = "EPS";
			this.ancho = 6*r;
			this.alto = 2*r;
			this.yNew = r;
			this.texto = "&#917";
			
			this.nodoFinalTikZ = new NodoTikZ(-(newStateCounter+2), "FINAL");
			this.nodoInicialTikZ = new NodoTikZ(-(newStateCounter+1), "SIMP");
			this.nodoInicialTikZ.next = this.nodoFinalTikZ;
			newStateCounter +=2;
			
			
		} else if (expresion.esSimbolo()) {
			// Automata
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.añadeTransicion(expresion.simbolo(),
					this.nodoFinal);
			this.simbolos.add(expresion.simbolo());
			
			//TikZ	
			this.tipo = "ID";
			this.ancho = 6*r;
			this.alto = 2*r;
			this.yNew = r;
			this.texto = Character.toString(expresion.simbolo());
			
			this.nodoFinalTikZ = new NodoTikZ(-(newStateCounter+2), "FINAL");
			this.nodoInicialTikZ = new NodoTikZ(-(newStateCounter+1), "SIMP");
			this.nodoInicialTikZ.next = this.nodoFinalTikZ;
			newStateCounter +=2;
			
		} else if (expresion.esCierre()) {
			// Automata
			this.nodoInicial = new Nodo(posicionInicial, false);
			Automata hijo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial + 1);
			this.nodoFinal = new Nodo(hijo.nodoFinal().posicion() + 1, true);

			this.nodoInicial.añadeTransicionVacia(hijo.nodoInicial());
			this.nodoInicial.añadeTransicionVacia(this.nodoFinal);
			hijo.nodoFinal().añadeTransicionVacia(hijo.nodoInicial());
			hijo.nodoFinal().añadeTransicionVacia(this.nodoFinal);

			this.simbolos.addAll(hijo.simbolos());
			
			//TikZ
			double ancho = hijo.ancho+8*r;	
			double alto = hijo.alto+2*r;
			double yNew = hijo.yNew+r;
			String thI = hijo.tipo;
			
			String lop = "";
			String lcp = "";
			if (thI == "SEL") {
		        lop = "(";
				lcp = ")";
			}
				
			String texto = lop+hijo.texto+lcp+"*";
			
			this.tipo = "AST";
			this.ancho = ancho;
			this.alto = alto;
			this.yNew = yNew;
			this.texto = texto;
			this.hijoI = hijo;
			
			
			this.nodoFinalTikZ = new NodoTikZ(-(newStateCounter+2), "FINAL");
			this.nodoInicialTikZ = new NodoTikZ(-(newStateCounter+1), "BSTAR");
			this.nodoInicialTikZ.next = this.nodoFinalTikZ;
			this.nodoInicialTikZ.inside = hijo.nodoInicialTikZ;
			

			newStateCounter +=2;
			
			hijo.nodoFinalTikZ.tipo = "ESTAR";
			hijo.nodoFinalTikZ.next = this.nodoFinalTikZ;
			hijo.nodoFinalTikZ.inside = hijo.nodoInicialTikZ;
			
			
		} else if (expresion.esConcat()) {
			// Automata
			Automata hijoIzquierdo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial);
			Automata hijoDerecho = new Automata(expresion.hijoDerecho(),
					hijoIzquierdo.nodoFinal().posicion());

			hijoIzquierdo.nodoFinal().unir(hijoDerecho.nodoInicial());
			this.nodoInicial = hijoIzquierdo.nodoInicial();
			this.nodoFinal = hijoDerecho.nodoFinal();

			this.simbolos.addAll(hijoIzquierdo.simbolos());
			this.simbolos.addAll(hijoDerecho.simbolos());
			
			//TikZ
			double yI = hijoIzquierdo.yNew;
			double yD = hijoDerecho.yNew;
			
			double altoI = hijoDerecho.alto;
			double altoD = hijoDerecho.alto;
			
			double yNew = (yI > yD) ? yI : yD;
			
			double difI = altoI - yI;
			double difD = altoD - yD;
			
			double maxDif = (difI > difD) ? difI : difD;
			
			double ancho = hijoIzquierdo.ancho + hijoDerecho.ancho - 2*r;
			
			double alto = yNew+maxDif;
			
			String thI =  hijoIzquierdo.tipo;
			String thD =  hijoDerecho.tipo;
			
			String lop = "";
			String lcp = "";
			if (thI == "SEL") {
		        lop = "(";
				lcp = ")";
			}
				
			String rop = "";
			String rcp = "";
			if (thD == "SEL" || thD == "CAT") {
		        rop = "(";
				rcp = ")";
			}
			
			String texto = lop+hijoIzquierdo.texto+lcp+"."+rop+hijoDerecho.texto+rcp;
			
			this.tipo = "CAT";
			this.ancho = ancho;
			this.alto = alto;
			this.yNew = yNew;
			this.texto = texto;
			this.hijoI = hijoIzquierdo;
			this.hijoD = hijoDerecho;
			
			
			this.nodoInicialTikZ = hijoIzquierdo.nodoInicialTikZ;
			this.nodoFinalTikZ = hijoDerecho.nodoFinalTikZ;
			
			 hijoIzquierdo.nodoFinalTikZ.tipo = hijoDerecho.nodoInicialTikZ.tipo;
			 hijoIzquierdo.nodoFinalTikZ.pair = hijoDerecho.nodoInicialTikZ.pair;
			 hijoIzquierdo.nodoFinalTikZ.id = hijoDerecho.nodoInicialTikZ.id;
			 hijoIzquierdo.nodoFinalTikZ.next = hijoDerecho.nodoInicialTikZ.next;
			 hijoIzquierdo.nodoFinalTikZ.inside = hijoDerecho.nodoInicialTikZ.inside;
			 hijoIzquierdo.nodoFinalTikZ.up = hijoDerecho.nodoInicialTikZ.up;
			 hijoIzquierdo.nodoFinalTikZ.down = hijoDerecho.nodoInicialTikZ.down;
			
			
		} else if (expresion.esUnion()) {
			// Automata
			this.nodoInicial = new Nodo(posicionInicial, false);

			Automata hijoIzquierdo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial + 1);
			Automata hijoDerecho = new Automata(expresion.hijoDerecho(),
					hijoIzquierdo.nodoFinal().posicion() + 1);

			this.nodoFinal = new Nodo(hijoDerecho.nodoFinal().posicion() + 1,
					true);

			this.nodoInicial.añadeTransicionVacia(hijoIzquierdo.nodoInicial());
			this.nodoInicial.añadeTransicionVacia(hijoDerecho.nodoInicial());
			hijoIzquierdo.nodoFinal().añadeTransicionVacia(this.nodoFinal);
			hijoDerecho.nodoFinal().añadeTransicionVacia(this.nodoFinal);

			this.simbolos.addAll(hijoIzquierdo.simbolos());
			this.simbolos.addAll(hijoDerecho.simbolos());
			
			//TikZ
			double anchoI = hijoIzquierdo.ancho;
			double anchoD = hijoDerecho.ancho;
			
			double maxAncho = (anchoI > anchoD) ? anchoI : anchoD;
			double ancho = maxAncho + 6*r;
			
			double alto = hijoIzquierdo.alto + hijoDerecho.alto + 2*r;
			double yNew = hijoIzquierdo.alto + r;
			String thD = hijoDerecho.tipo;
			
			String rop = "";
			String rcp = "";
			if (thD == "SEL") {
		        rop = "(";
				rcp = ")";
			}
			
			String texto = hijoIzquierdo.texto + "|" + rop + hijoDerecho.texto + rcp;
			
			this.tipo = "SEL";
			this.ancho = ancho;
			this.alto = alto;
			this.yNew = yNew;
			this.texto = texto;
			this.hijoI = hijoIzquierdo;
			this.hijoD = hijoDerecho;
			
			// New initial
			newStateCounter += 1;
			this.nodoInicialTikZ = new NodoTikZ(-newStateCounter, "SEL");
			this.nodoInicialTikZ.up = hijoIzquierdo.nodoInicialTikZ;
			this.nodoInicialTikZ.down = hijoDerecho.nodoInicialTikZ;

			// New final
			newStateCounter += 1;
			this.nodoFinalTikZ = new NodoTikZ(-newStateCounter, "FINAL");
			
			this.nodoInicialTikZ.pair = this.nodoFinalTikZ;
			this.nodoFinalTikZ.invpair = this.nodoInicialTikZ;
			
			//Change old finals
			hijoIzquierdo.nodoFinalTikZ.tipo = "FINAL";
			hijoDerecho.nodoFinalTikZ.tipo = "SIMP";
			hijoDerecho.nodoFinalTikZ.next= this.nodoFinalTikZ;
			
			
		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresión regular de tipo desconocido.");
		}
		resetAllIds(this.nodoInicialTikZ, -1);
	}
	

	/**
	 * Nodo de entrada del autómata.
	 * 
	 * @return Nodo inicial.
	 */
	public Nodo nodoInicial() {
		return this.nodoInicial;
	}

	/**
	 * Nodo final del autómata.
	 * 
	 * @return Nodo final.
	 */
	public Nodo nodoFinal() {
		return this.nodoFinal;
	}

	/**
	 * Devuelve el conjunto de símbolos que el autómata utiliza en sus
	 * transiciones. No se incluye epsilon ni cualquier otro indicador de
	 * transición vacía.
	 * 
	 * @return Conjunto de símbolos del autómata.
	 */
	public Set<Character> simbolos() {
		return new TreeSet<>(this.simbolos);
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y tras consumir un símbolo determinado. Solo se cuentan las transiciones
	 * vacías efectuadas tras consumir la entrada.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @param simbolo
	 *            Símbolo de entrada.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicion(Nodo inicio, char simbolo) {
		// Nodos a los que llegamos desde el inicio sin consumir
		Set<Nodo> iniciales = transicionVacia(inicio);
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionConsumiendo = new TreeSet<>();
		// Nodos a los que llegamos tras consumir la entrada
		Set<Nodo> transicionNoConsumiendo = new TreeSet<>();
		Nodo actual;

		for (Nodo nodo : iniciales) {
			actual = nodo.transicion(simbolo);
			if (actual != null)
				transicionConsumiendo.add(actual);
		}

		for (Nodo nodo : transicionConsumiendo)
			transicionNoConsumiendo.addAll(transicionVacia(nodo));

		transicionConsumiendo.addAll(transicionNoConsumiendo);

		return transicionConsumiendo;
	}

	/**
	 * Obtiene el conjunto de nodos al que se llega a partir de un nodo inicial
	 * y sin consumir ningún símbolo.
	 * 
	 * @param inicio
	 *            Nodo de inicio.
	 * @return Conjunto de nodos de llegada.
	 */
	public Set<Nodo> transicionVacia(Nodo inicio) {
		// Nodos a los que llegamos sin consumir entrada
		Set<Nodo> actuales = new TreeSet<>();
		Set<Nodo> visitados = new TreeSet<>();
		Nodo actual;

		actuales.add(inicio);
		while (!actuales.isEmpty()) {
			actual = actuales.iterator().next();
			actuales.addAll(actual.transicionVacia());

			visitados.add(actual);
			actuales.removeAll(visitados);
		}

		return visitados;
	}

	/**
	 * Genera un grafo representando el autómata. La imagen generada se cachea
	 * al ser solicitada por primera vez para evitar realizar los cálculos
	 * repetidas veces.
	 * 
	 * @return Imagen conteniendo el grafo que representa al autómata.
	 */
	public BufferedImage imagen() {
		if (this.imagen == null) {
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<Integer, Object> gNodos = new HashMap<>();
			Object gNodo, gActual;
			List<Nodo> pendientes = new ArrayList<>();
			Map<Nodo, Character> siguientes;
			Nodo actual;

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;";
			String estiloEdge = "strokeColor=black;fontColor=black;labelBackgroundColor=white;rounded=true;";

			graph.getModel().beginUpdate();
			try {
				actual = nodoInicial;
				do {
					gActual = gNodos.get(actual.posicion());
					if (gActual == null) { // Primer nodo
						gActual = graph.insertVertex(parent, null,
								actual.posicion(), 0, 0, 30, 30, estiloVertex);
						gNodos.put(actual.posicion(), gActual);
					}

					// Calcula transiciones
					siguientes = new HashMap<>();
					for (Nodo nodo : actual.transicionVacia())
						siguientes.put(nodo, null);
					Nodo siguiente;
					for (char simbolo : simbolos) {
						siguiente = actual.transicion(simbolo);
						if (siguiente != null)
							siguientes.put(siguiente, simbolo);
					}

					pendientes.addAll(siguientes.keySet().stream()
							.filter(n -> !gNodos.containsKey(n.posicion()))
							.collect(Collectors.toList()));

					for (Nodo nodo : siguientes.keySet()) {
						if (!gNodos.containsKey(nodo.posicion())) { // Añade
																	// nodo
							gNodo = graph
									.insertVertex(parent, null,
											nodo.posicion(), 0, 0, 30, 30,
											estiloVertex);
							gNodos.put(nodo.posicion(), gNodo);
						} else { // Recupera nodo
							gNodo = gNodos.get(nodo.posicion());
						}
						// Añade transición
						graph.insertEdge(parent, null, siguientes.get(nodo),
								gActual, gNodo, estiloEdge);
					}

					actual = pendientes.isEmpty() ? null : pendientes.remove(0);
				} while (actual != null);

			} finally {
				graph.getModel().endUpdate();

				mxGraphComponent graphComponent = new mxGraphComponent(graph);

				new mxHierarchicalLayout(graph, SwingConstants.WEST)
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
	 * Genera el programa en formato dot para generar la imagen representando el
	 * autómata asociado a la expresión, con los nodos marcados pero vacíos. El
	 * programa generado se cachea al ser solicitado por primera vez para evitar
	 * realizar los cálculos repetidas veces.
	 * 
	 * @return Programa dot conteniendo el autómata que genera la expresión.
	 */
	public String imagenDot() {
		if (this.imagenDot == null) {
			List<Nodo> pendientes = new ArrayList<>();
			List<Nodo> visitados = new ArrayList<>();
			Nodo actual;

			this.imagenDot = "digraph {\n\trankdir=LR;";
			pendientes.add(this.nodoInicial);

			while (!pendientes.isEmpty()) {
				actual = pendientes.remove(0);

				for (Nodo nodo : actual.transicionVacia()) {
					this.imagenDot += "\n\t" + actual.posicion() + " -> "
							+ nodo.posicion();
					if (!visitados.contains(nodo) && !pendientes.contains(nodo))
						pendientes.add(nodo);
				}

				Nodo nodo;
				for (char simbolo : this.simbolos) {
					nodo = actual.transicion(simbolo);
					if (nodo != null) {
						this.imagenDot += "\n\t" + actual.posicion() + " -> "
								+ nodo.posicion();
						this.imagenDot += "[label=\"" + simbolo + "\"];";
						if (!visitados.contains(nodo)
								&& !pendientes.contains(nodo))
							pendientes.add(nodo);
					}
				}

				visitados.add(actual);
			}

			this.imagenDot += "\n}";
		}

		return this.imagenDot;
	}


	/**
	 * Genera el programa en formato SVG para generar la imagen representando el
	 * autómata asociado a la expresión, con los nodos marcados pero vacíos. El
	 * programa generado se cachea al ser solicitado por primera vez para evitar
	 * realizar los cálculos repetidas veces.
	 * 
	 * @return Programa SVG conteniendo el autómata que genera la expresión.
	 */
	public String imagenSvg() {
		if (this.imagenSvg == null) {
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<Integer, Object> gNodos = new HashMap<>();
			Object gNodo, gActual;
			List<Nodo> pendientes = new ArrayList<>();
			Map<Nodo, Character> siguientes;
			Nodo actual;

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;";
			String estiloEdge = "strokeColor=black;fontColor=black;labelBackgroundColor=white;rounded=true;";

			graph.getModel().beginUpdate();
			try {
				actual = nodoInicial;
				do {
					gActual = gNodos.get(actual.posicion());
					if (gActual == null) { // Primer nodo
						gActual = graph.insertVertex(parent, null,
								actual.posicion(), 0, 0, 30, 30, estiloVertex);
						gNodos.put(actual.posicion(), gActual);
					}

					// Calcula transiciones
					siguientes = new HashMap<>();
					for (Nodo nodo : actual.transicionVacia())
						siguientes.put(nodo, null);
					Nodo siguiente;
					for (char simbolo : simbolos) {
						siguiente = actual.transicion(simbolo);
						if (siguiente != null)
							siguientes.put(siguiente, simbolo);
					}

					pendientes.addAll(siguientes.keySet().stream()
							.filter(n -> !gNodos.containsKey(n.posicion()))
							.collect(Collectors.toList()));

					for (Nodo nodo : siguientes.keySet()) {
						if (!gNodos.containsKey(nodo.posicion())) { // Añade
																	// nodo
							gNodo = graph
									.insertVertex(parent, null,
											nodo.posicion(), 0, 0, 30, 30,
											estiloVertex);
							gNodos.put(nodo.posicion(), gNodo);
						} else { // Recupera nodo
							gNodo = gNodos.get(nodo.posicion());
						}
						// Añade transición
						graph.insertEdge(parent, null, siguientes.get(nodo),
								gActual, gNodo, estiloEdge);
					}

					actual = pendientes.isEmpty() ? null : pendientes.remove(0);
				} while (actual != null);

			} finally {
				graph.getModel().endUpdate();

				new mxHierarchicalLayout(graph, SwingConstants.WEST)
						.execute(parent);
				new mxParallelEdgeLayout(graph).execute(parent);

				
				Document document = mxCellRenderer.createSvgDocument(graph, null, 1, Color.WHITE, null);
				imagenSvg = mxXmlUtils.getXml(document);
				
			}
		}

		return imagenSvg;
	}
	
	
	/**
	 * Genera el programa en formato SVG para generar la imagen representando el
	 * autómata asociado a la expresión, con los nodos marcados pero vacíos. El
	 * programa generado se cachea al ser solicitado por primera vez para evitar
	 * realizar los cálculos repetidas veces.
	 * 
	 * @return Programa TikZ conteniendo el autómata que genera la expresión.
	 */
	public String imagenTikZ() {
		return tz_printAll(this);
	}
	
	
	private String tzDibuja(double x, double y, Automata tree, boolean omitFirst) {
		
		String out = "";
		
		double ancho = tree.ancho;
		double alto = tree.alto;
		double yNew = tree.yNew;
		Automata hijoI = tree.hijoI;
		Automata hijoD = tree.hijoD;
		String texto = tree.texto;
		String tipo = tree.tipo;
		int startId = tree.nodoInicialTikZ.id;
		int finalId = tree.nodoFinalTikZ.id;
		
		if (tipo == "EPS") {
			
			if (!omitFirst)
				out += tz_circle(x+r, y+r, startId);
			
			out += tz_arco(x+2*r,y+r,x+4*r,y+r);
	        out += tz_circle(x+5*r,y+r,finalId);
	        out += tz_textE(x+3*r-r/4,y+r-r/8);
			
		} else if (tipo == "ID"){
			
			if (!omitFirst)
				out += tz_circle(x+r, y+r, startId);
			
			out += tz_arco(x+2*r,y+r,x+4*r,y+r);
	        out += tz_circle(x+5*r,y+r,finalId);
	        out += tz_text(x+3*r-r/4, y+r-r/8, texto, "");
			
		} else if (tipo == "AST"){
			
			// ** Dibuja el hijo **
	        out += tzDibuja(x+4*r,y+r,hijoI, false);
	        
	        // estado ini
	        if (!omitFirst)
				out += tz_circle(x+r, y+yNew, startId); 
	        
	        // arco del ini al old-ini
	        out += tz_arco(x+2*r,y+yNew,x+4*r,y+yNew);

	        // ** arco del ini al fin **
	        out += tz_lazo(x+r,y+yNew,x+ancho-r,y+0);

	        // estado fin
	        out += tz_circle(x+ancho-r,y+yNew,finalId);
	        out += tz_arco(x+ancho-4*r,y+yNew,x+ancho-2*r,y+yNew);
	        out += tz_lazo(x+ancho-5*r,y+yNew,x+5*r,y+alto);

			
		} else if (tipo == "SEL"){
			
			double auxY1 = hijoI.yNew;
			double auxX1 = hijoD.ancho-hijoI.ancho;
			
			auxX1 = (0 > auxX1) ? 0 : auxX1/2;
			double deltaI = auxX1;
			
			double auxY2 = hijoD.yNew+hijoI.alto+2*r;
			double auxX2 = hijoI.ancho-hijoD.ancho;
			
			auxX2 = (0 > auxX2) ? 0 : auxX2/2;
			double deltaD = auxX2;
	        
	        // ** Dibuja hijos **
	        out += tzDibuja(x+3*r+deltaI,y+0,hijoI, false);
	        out += tzDibuja(x+3*r+deltaD,y+hijoI.alto+2*r,hijoD, false);
	        
	        // estado ini
	        if (!omitFirst)
	            out += tz_circle(x+r,y+yNew,startId);
	        
	        // arco del ini al alfa ini
	        out += tz_arcoSelRecto(x+r,y+yNew,x+4*r+auxX1,y+auxY1);
	        
	        // arco del alfa fin al fin
	        out += tz_arcoSelRecto(x+ancho-4*r-auxX1,y+auxY1,x+ancho-r,y+yNew);
	        
	        // arco del ini al beta ini
	        out += tz_arcoSelRecto(x+r,y+yNew,x+4*r+auxX2,y+auxY2);
	        
	        // arco del beta fin al fin
	        out += tz_arcoSelRecto(x+ancho-4*r-auxX2,y+auxY2,x+ancho-r,y+yNew);

	        // estado fin
	        out += tz_circle(x+ancho-r,y+yNew,finalId);
			
		} else if (tipo == "CAT"){
			double auxY1=yNew-hijoI.yNew;
			double deltaI = (0 > auxY1) ? 0 : auxY1;
	        out += tzDibuja(x+0,y+deltaI,hijoI,omitFirst);
	        double auxY2=yNew-hijoD.yNew;
	        double deltaD = (0 > auxY2) ? 0 : auxY2;
	        out += tzDibuja(x+ancho-hijoD.ancho,y+deltaD,hijoD,true);
		} else {
			return out;
		}
			
		
		return out;
	}
	
	private String tz_printAll(Automata tree) {		//TODO
		
		String out = "";
		
		String dibujo = tzDibuja(18,24,tree, false);
		
		double x = this.initialStateCoordinatesX;
		double y = this.initialStateCoordinatesY;
	    
		out += "\n% Line INI to 0\n"; 
		out += "\\begin{tikzpicture}[y=0.80pt, x=0.80pt, yscale=-\\globalscale, xscale=\\globalscale, inner sep=0pt, outer sep=0pt]";
		out += String.format(Locale.US, "\\draw[color=black,line width=0.400pt,line cap=butt,line join=miter -{Stealth[length=2.4mm, width=1.5mm]}] (%d,%f) -- (%f,%f);\n", 0, y, x-12, y);
		out += "% Initial state color\n";
		out += String.format(Locale.US, "\\draw[fill=yellow, fill opacity=0.3] (%f,%f) circle (%d);\n", x, y, r);
		
	    x = this.finalStateCoordinatesX;
	    y = this.finalStateCoordinatesY;
	    
	    out += "% Final state inner circle \n";
	    out += String.format(Locale.US, "\\draw[color=black, fill=pink, fill opacity=0.5] (%f,%f) circle (%f);\n", x , y, r-1.5);
	    
	    //if(bigestState > 99)
	    //	out = out.replace("\\s{{"+this.bigestState+"}}", "\\s{{\\smaller{"+this.bigestState+"}}}");
	    //if(bigestState > 999)
	    //	out = out.replace("\\s{{\\footnotesize{"+this.bigestState+"}}}", "\\s{{\\footnotesize\\relsize{{-2}}{"+this.bigestState+"}}}");
	    
	    out += dibujo;
	    
	    out += "\\end{tikzpicture}\n";
		
		return out;
	}
		
	

	private String tz_text(double x, double y, String string, String cls) {
		idCounter++;
		return String.format(Locale.US, "\\draw (%f, %f) node[above, text=blue] (text%d) {\\car{%s}};\n", (x+r/4), (y-r/5), idCounter, string);
	}
	
	private String tz_textE(double x, double y) {
		idCounter++;
		return String.format(Locale.US, "\\draw (%f, %f) node[above right, text=red] (text%d) {$\\varepsilon$};\n", (x+r/4), (y-r/5), idCounter);
	}
	
	private String tz_line(double xo, double yo, double xd, double yd) {
		return String.format(Locale.US, "\\draw[color=black,line cap=butt,line join=miter,line width=0.400pt, -{Stealth[length=2.4mm, width=1.5mm]}] (%f, %f) -- (%f, %f);\n",
				xo, yo, xd, yd);
	}
	
	private String tz_arco(double xo, double yo, double xd, double yd) {
		return tz_line(xo,yo,xd,yd);
	}
	
	private String tz_arcoSelRecto(double xo, double yo, double xd, double yd) {
		double ylen = yd-yo;
		int sy = (ylen > 0) ? 1 : -1;
		
		double xlen = xd-xo;
		int sx = (xlen > 0) ? 1 : -1;
		
		xlen = xlen*sx;
		ylen = ylen*sy;
		
		double d = Math.sqrt(ylen*ylen+xlen*xlen);
		
		double ex = r*xlen/d;
		double ey = r*ylen/d;
		
		
		return tz_line(xo+ex*sx, yo+ey*sy, xd-ex*sx, yd-ey*sy);
	}
	
	private String tz_circle(double x, double y, int stateCounter) {
		
		if (stateCounter == 0)
			this.initialStateCoordinatesX = x;
			this.initialStateCoordinatesY = y;
		
		if (stateCounter > biggestState) {
			this.finalStateCoordinatesX = x;
			this.finalStateCoordinatesY = y;
			biggestState = stateCounter;
		}
		
		String out = "";
		out += String.format(Locale.US, "\\draw[color=black] (%f, %f) circle (%d);\n", x, y, r);
		
		String fs = "";
		
		if (stateCounter > 999)
			fs = "\\footnotesize ";
		
		out += String.format(Locale.US, "\\draw (%f, %f) node[text=green] (s%d) {\\s{%s%d}};\n", x, y, stateCounter, fs, stateCounter);
				
		return out;
		
	}
	
	
	private double[] QtoC(double x1, double y1, double x2, double y2, double x3, double y3) {
		
		double[] coordinates = new double[8];
		
		double c1x = (x1 + 2*x2)/3;
		double c1y = (y1 + 2*y2)/3;
		double c2x = (x3 + 2*x2)/3;
	    double c2y = (y3 + 2*y2)/3;
		
	    coordinates[0] = x1;
	    coordinates[1] = y1;
	    coordinates[2] = c1x;
	    coordinates[3] = c1y;
	    coordinates[4] = c2x;
	    coordinates[5] = c2y;
	    coordinates[6] = x3;
	    coordinates[7] = y3;
	    
		return coordinates;
	}
	
	private String tz_lazo(double x1, double y1, double x2, double alto) {
		
		double ylen=alto-y1;
		
		int sy = (ylen > 0) ? 1 : -1;
		int sx = (x2 - x1 > 0) ? 1 : -1;
		
	    ylen=sy*ylen;
	    double d = Math.sqrt(ylen*ylen+4*r*r);

	    // Coordenadas del punto sobre el círculo del estado
	    double ex = 2*r*r/d;  // d es a 2r, como r es a ex
	    double ey = r*ylen/d; // d es a ylen, como r es a ey 
	    
	    // Coordenadas del punto a la altura del top del hijo
	    double iy = ylen-r;
	    double ix = 2*r*iy/ylen;
		
	    String out = "\\draw[color=red,line width=0.400pt,line cap=butt, line join=miter,-{Stealth[length=2.4mm, width=1.5mm]}]";
	    if (x1+2*r*sx == x2-2*r*sx){ // esto pasa en a*
	            d=r*Math.cos(Math.PI/4);
	            
	            // First coordinate
	            out += String.format(Locale.US, "(%f, %f)", x1+d*sx, y1+d*sy);
	            
	            // From quadratic to cubic Bezier
	            double q1_x = x1+r*sx;
	            double q1_y = y1+r*sy;
	            double q2_x = x1+2*r*sx;
	            double q2_y = y1+2*r*sy;
	            double q3_x = x2-r*sx;
	            double q3_y = y1+r*sy;
	            
	            double[] c1 = new double[2];
	            double[] c2 = new double[2];
	            double[] c3 = new double[2];
	            double[] c4 = new double[2];
	            double[] qtoc_out = QtoC(q1_x, q1_y, q2_x, q2_y, q3_x, q3_y);
	            
	            c1[0] = qtoc_out[0];
	            c1[1] = qtoc_out[1];
	            c2[0] = qtoc_out[2];
	            c2[1] = qtoc_out[3];
	            c3[0] = qtoc_out[4];
	            c3[1] = qtoc_out[5];
	            c4[0] = qtoc_out[6];
	            c4[1] = qtoc_out[7];
	             
	            // Cubic Bezier coordinates
	            out += String.format(Locale.US, "-- (%f, %f)", c1[0], c1[1]);
	            out += String.format(Locale.US, ".. controls (%f, %f)", c2[0],c2[1]);
	            out += String.format(Locale.US, "and (%f, %f)", c3[0],c3[1]);
	            out += String.format(Locale.US, ".. (%f, %f)", c4[0], c4[1]);
	            
	            // Last coordinate
	            out += String.format(Locale.US, "-- (%f, %f);\n", x2-d*sx, y1+d*sy);
	            out += tz_textE((x1+x2)/2, y1+2.4*r*sy);
	    } else {
	            // The following are some lines and two cubic Bezier curves
	            // First coordinate
	            out += String.format(Locale.US, "(%f, %f)", x1+ex*sx, y1+ey*sy);
	            
	            // From quadratic to cubic Bezier
	            double q1_x = x1+ix*sx;
	            double q1_y = y1+iy*sy;
	            double q2_x = x1+2*r*sx;
	            double q2_y = y1+ylen*sy;
	            double q3_x = x1+3*r*sx;
	            double q3_y = y1+ylen*sy;
	            
	            double[] c1 = new double[2];
	            double[] c2 = new double[2];
	            double[] c3 = new double[2];
	            double[] c4 = new double[2];
	            double[] qtoc_out = QtoC(q1_x, q1_y, q2_x, q2_y, q3_x, q3_y);
	            
	            c1[0] = qtoc_out[0];
	            c1[1] = qtoc_out[1];
	            c2[0] = qtoc_out[2];
	            c2[1] = qtoc_out[3];
	            c3[0] = qtoc_out[4];
	            c3[1] = qtoc_out[5];
	            c4[0] = qtoc_out[6];
	            c4[1] = qtoc_out[7];
	            
	            // Cubic Bezier coordinates
	            out += String.format(Locale.US, "-- (%f, %f)", c1[0], c1[1]);
	            out += String.format(Locale.US, ".. controls (%f, %f)", c2[0], c2[1]);
	            out += String.format(Locale.US, "and (%f, %f)", c3[0], c3[1]);
	            out += String.format(Locale.US, ".. (%f, %f)", c4[0], c4[1]);
	            
	            // From quadratic to cubic Bezier
	            q1_x = x2-3*r*sx;
	            q1_y = y1+ylen*sy;
	            q2_x = x2-2*r*sx;
	            q2_y = y1+ylen*sy;
	            q3_x = x2-ix*sx;
	            q3_y = y1+iy*sy;
	            
	            qtoc_out = QtoC(q1_x, q1_y, q2_x, q2_y, q3_x, q3_y);
	            c1[0] = qtoc_out[0];
	            c1[1] = qtoc_out[1];
	            c2[0] = qtoc_out[2];
	            c2[1] = qtoc_out[3];
	            c3[0] = qtoc_out[4];
	            c3[1] = qtoc_out[5];
	            c4[0] = qtoc_out[6];
	            c4[1] = qtoc_out[7]; 
	            
	            // Cubic Bezier coordinates
	            out += String.format(Locale.US, "-- (%f, %f)", c1[0], c1[1]);
	            out += String.format(Locale.US, ".. controls (%f, %f)", c2[0], c2[1]);
	            out += String.format(Locale.US, "and (%f, %f)", c3[0], c3[1]);
	            out += String.format(Locale.US, ".. (%f, %f)", c4[0], c4[1]);
	            
	            // Last coordinate
	            out += String.format(Locale.US, "-- (%f, %f);\n", x2-ex*sx, y1+ey*sy);
	            out += tz_textE((x1+x2)/2, y1+ylen*sy);
	        }
	    
	        return out;
	}
	
	private int resetAllIds(NodoTikZ afnd, int stateId) {
		
		stateId ++;
		afnd.id = stateId;
		
		//System.out.println("Tipo " + afnd.tipo + "   id: "+ stateId);
		if (afnd.tipo == "FINAL") {
			return stateId;
		}
		if (afnd.tipo == "SIMP" || afnd.tipo == "ESTAR") {
			stateId = resetAllIds(afnd.next, stateId);
		} else if (afnd.tipo == "BSTAR"){
			stateId = resetAllIds(afnd.inside, stateId);
			
		} else if (afnd.tipo == "SEL") {
			stateId = resetAllIds(afnd.up, stateId);
			stateId = resetAllIds(afnd.down, stateId);
		}
		return stateId;
	}

}
