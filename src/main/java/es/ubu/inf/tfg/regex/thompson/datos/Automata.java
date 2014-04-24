package es.ubu.inf.tfg.regex.thompson.datos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

/**
 * Automata implementa el modelo lógico de un automata finito no determinista,
 * compuesto por una serie de nodos y de transiciones entre dichos nodos.
 * Permite obtener los nodos obtenidos a partir de uno dado utilizando un tipo
 * de transición determinado, y consumiendo o no un caracter dado.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;
	private Set<Character> simbolos;
	private BufferedImage imagen;

	/**
	 * Constructor. Define un automata finito no determinista a partir de un
	 * árbol de expresion regular dado, de manera recursiva.
	 * 
	 * @param expresion
	 *            Árbol de expresion regular a partir del cual generar el
	 *            autómata.
	 */
	public Automata(ExpresionRegular expresion, int posicionInicial) {

		this.simbolos = new TreeSet<>();

		if (expresion.esVacio()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.añadeTransicionVacia(this.nodoFinal);
		} else if (expresion.esSimbolo()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			this.nodoFinal = new Nodo(posicionInicial + 1, true);
			this.nodoInicial.añadeTransicion(expresion.simbolo(),
					this.nodoFinal);
			this.simbolos.add(expresion.simbolo());
		} else if (expresion.esCierre()) {
			this.nodoInicial = new Nodo(posicionInicial, false);
			Automata hijo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial + 1);
			this.nodoFinal = new Nodo(hijo.nodoFinal().posicion() + 1, true);

			this.nodoInicial.añadeTransicionVacia(hijo.nodoInicial());
			this.nodoInicial.añadeTransicionVacia(this.nodoFinal);
			hijo.nodoFinal().añadeTransicionVacia(hijo.nodoInicial());
			hijo.nodoFinal().añadeTransicionVacia(this.nodoFinal);

			this.simbolos.addAll(hijo.simbolos());
		} else if (expresion.esConcat()) {
			Automata hijoIzquierdo = new Automata(expresion.hijoIzquierdo(),
					posicionInicial);
			Automata hijoDerecho = new Automata(expresion.hijoDerecho(),
					hijoIzquierdo.nodoFinal().posicion());

			hijoIzquierdo.nodoFinal().unir(hijoDerecho.nodoInicial());
			this.nodoInicial = hijoIzquierdo.nodoInicial();
			this.nodoFinal = hijoDerecho.nodoFinal();

			this.simbolos.addAll(hijoIzquierdo.simbolos());
			this.simbolos.addAll(hijoDerecho.simbolos());
		} else if (expresion.esUnion()) {
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
		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresion regular de tipo desconocido.");
		}
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
	 * Devuelve el conjunto de símbolos que el automata utiliza en sus
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

	public BufferedImage imagen() {
		if (this.imagen == null) {
			// Visualización del autómata
			mxGraph graph = new mxGraph();
			Object parent = graph.getDefaultParent();
			Map<Integer, Object> gNodos = new HashMap<>();

			String estiloVertex = "shape=ellipse;fillColor=white;strokeColor=black;fontColor=black;movable=false;direction=north";// horizontal=false;";
			String estiloEdge = "strokeColor=black;fontColor=black;rounded=true;";

			graph.getModel().beginUpdate();
			try {
				Object gNodoInicial = graph.insertVertex(parent, null,
						nodoInicial.posicion(), 0, 0, 30, 30, estiloVertex);
				gNodos.put(nodoInicial.posicion(), gNodoInicial);

				// Añade iniciales
				Set<Nodo> visitados = new TreeSet<>();
				Set<Nodo> pendientes = new TreeSet<>();
				Set<Nodo> iniciales = nodoInicial.transicionVacia();
				for (char simbolo : simbolos) {
					Nodo nodo = nodoInicial.transicion(simbolo);
					if(nodo != null)
						iniciales.add(nodo);
				}
				
				for (Nodo nodo : iniciales) {
					Object gNodo = graph.insertVertex(parent, null,
							nodo.posicion(), 0, 0, 30, 30, estiloVertex);
					gNodos.put(nodo.posicion(), gNodo);
					graph.insertEdge(parent, null, null, gNodoInicial, gNodo,
							estiloEdge);

					pendientes.add(nodo);
				}
				visitados.add(nodoInicial);

				while (pendientes.size() > 0) {
					Nodo actual = pendientes.iterator().next();
					Object gActual = gNodos.get(actual.posicion());

					for (Nodo nodo : actual.transicionVacia()) {
						if (!visitados.contains(nodo)) {
							Object gNodo;
							if (!gNodos.containsKey(nodo.posicion())) {
								gNodo = graph.insertVertex(parent, null,
										nodo.posicion(), 0, 0, 30, 30,
										estiloVertex);
							} else {
								gNodo = gNodos.get(nodo.posicion());
							}

							gNodos.put(nodo.posicion(), gNodo);
							graph.insertEdge(parent, null, null, gActual,
									gNodo, estiloEdge);

							pendientes.add(nodo);
						}
					}

					for (char simbolo : simbolos) {
						Nodo nodo = actual.transicion(simbolo);
						if (nodo != null && !visitados.contains(nodo)) {
							Object gNodo;
							if (!gNodos.containsKey(nodo.posicion())) {
								gNodo = graph.insertVertex(parent, null,
										nodo.posicion(), 0, 0, 30, 30,
										estiloVertex);
							} else {
								gNodo = gNodos.get(nodo.posicion());
							}

							gNodos.put(nodo.posicion(), gNodo);
							graph.insertEdge(parent, null, simbolo, gActual,
									gNodo, estiloEdge);

							pendientes.add(nodo);
						}
					}

					visitados.add(actual);
					pendientes.remove(actual);
				}

			} finally {
				graph.getModel().endUpdate();
			}
			mxGraphComponent graphComponent = new mxGraphComponent(graph);

			new mxHierarchicalLayout(graph).execute(parent);
			new mxParallelEdgeLayout(graph).execute(parent);

			this.imagen = mxCellRenderer.createBufferedImage(graph, null, 1,
					Color.WHITE, graphComponent.isAntiAlias(), null,
					graphComponent.getCanvas());

//			AffineTransform tx = new AffineTransform();
//			tx.rotate(Math.PI / 2, this.imagen.getWidth() / 2,
//					this.imagen.getHeight() / 2);
//
//			AffineTransformOp op = new AffineTransformOp(tx,
//					AffineTransformOp.TYPE_BILINEAR);
//			this.imagen = op.filter(this.imagen, null);
		}

		return this.imagen;
	}
}
