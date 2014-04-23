package es.ubu.inf.tfg.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import es.ubu.inf.tfg.regex.thompson.datos.Automata;
import es.ubu.inf.tfg.regex.thompson.datos.Nodo;

@SuppressWarnings("serial")
public class GraphFrame extends JFrame {

	private static final Logger log = LoggerFactory.getLogger(GraphFrame.class);

	private JPanel contentPane;

	public GraphFrame(Automata automata) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);

		// Visualización del autómata
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		Map<Integer, Object> gNodos = new HashMap<>();

		graph.getModel().beginUpdate();
		try {
			Object gNodoInicial = graph.insertVertex(parent, ""
					+ automata.nodoInicial().posicion(), automata.nodoInicial()
					.posicion(), 0, 0, 30, 30);
			log.debug("Añade nodo inicial ({})", automata.nodoInicial()
					.posicion());
			gNodos.put(automata.nodoInicial().posicion(), gNodoInicial);

			// Añade iniciales
			Set<Nodo> visitados = new TreeSet<>();
			Set<Nodo> pendientes = new TreeSet<>();
			Set<Nodo> iniciales = automata.nodoInicial().transicionVacia();
			for (Nodo nodo : iniciales) {
				Object gNodo = graph.insertVertex(parent, "" + nodo.posicion(),
						nodo.posicion(), 0, 0, 30, 30);
				log.debug("Añade nodo {}", nodo.posicion());
				gNodos.put(nodo.posicion(), gNodo);
				graph.insertEdge(parent, null, null, gNodoInicial, gNodo);
				log.debug("Añade transicion {} -> {}", automata.nodoInicial()
						.posicion(), nodo.posicion());

				pendientes.add(nodo);
			}
			visitados.add(automata.nodoInicial());

			while (pendientes.size() > 0) {
				Nodo actual = pendientes.iterator().next();
				Object gActual = gNodos.get(actual.posicion());

				for (Nodo nodo : actual.transicionVacia()) {
					if (!visitados.contains(nodo)) {
						Object gNodo;
						if (!gNodos.containsKey(nodo.posicion())) {
							gNodo = graph.insertVertex(parent,
									"" + nodo.posicion(), nodo.posicion(), 0,
									0, 30, 30);
							log.debug("Añade nodo {}", nodo.posicion());
						} else {
							gNodo = gNodos.get(nodo.posicion());
						}

						gNodos.put(nodo.posicion(), gNodo);
						graph.insertEdge(parent, null, null, gActual, gNodo);
						log.debug("Añade transicion {} -> {}",
								actual.posicion(), nodo.posicion());

						pendientes.add(nodo);
					}
				}

				for (char simbolo : automata.simbolos()) {
					Nodo nodo = actual.transicion(simbolo);
					if (nodo != null && !visitados.contains(nodo)) {
						Object gNodo;
						if (!gNodos.containsKey(nodo.posicion())) {
							gNodo = graph.insertVertex(parent,
									"" + nodo.posicion(), nodo.posicion(), 0,
									0, 30, 30);
							log.debug("Añade nodo {}", nodo.posicion());
						} else {
							gNodo = gNodos.get(nodo.posicion());
						}

						gNodos.put(nodo.posicion(), gNodo);
						graph.insertEdge(parent, null, simbolo, gActual, gNodo);
						log.debug("Añade transicion {} -> {}",
								actual.posicion(), nodo.posicion());

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
		contentPane.add(graphComponent);

		new mxHierarchicalLayout(graph).execute(parent);
		new mxParallelEdgeLayout(graph).execute(parent);

		setVisible(true);
	}

}
