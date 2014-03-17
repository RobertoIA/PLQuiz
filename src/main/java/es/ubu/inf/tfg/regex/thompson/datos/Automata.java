package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Set;
import java.util.TreeSet;

public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;

	public Automata() {
		Nodo nodo0 = new Nodo(0, false);
		Nodo nodo1 = new Nodo(1, false);
		Nodo nodo2 = new Nodo(2, false);
		Nodo nodo3 = new Nodo(3, false);
		Nodo nodo4 = new Nodo(4, false);
		Nodo nodo5 = new Nodo(5, false);
		Nodo nodo6 = new Nodo(6, false);
		Nodo nodo7 = new Nodo(7, false);
		Nodo nodo8 = new Nodo(8, false);
		Nodo nodo9 = new Nodo(9, false);
		Nodo nodo10 = new Nodo(10, true);

		nodo0.añadeTransicionVacia(nodo1);
		nodo0.añadeTransicionVacia(nodo7);

		nodo1.añadeTransicionVacia(nodo2);
		nodo1.añadeTransicionVacia(nodo4);

		nodo2.añadeTransicion('a', nodo3);

		nodo3.añadeTransicionVacia(nodo6);

		nodo4.añadeTransicion('b', nodo5);

		nodo5.añadeTransicionVacia(nodo6);

		nodo6.añadeTransicionVacia(nodo7);
		nodo6.añadeTransicionVacia(nodo1);

		nodo7.añadeTransicion('a', nodo8);

		nodo8.añadeTransicion('b', nodo9);

		nodo9.añadeTransicion('b', nodo10);

		this.nodoInicial = nodo0;
		this.nodoFinal = nodo10;
	}

	public Nodo nodoInicial() {
		return this.nodoInicial;
	}
	
	public Nodo nodoFinal() {
		return this.nodoFinal;
	}

	public Set<Nodo> transicion(Nodo inicio, char simbolo) {
		// Nodos a los que llegamos sin consumir entrada
		Set<Nodo> actualesNoConsumido = new TreeSet<>();
		// Nodos a los que llegamos consumiendo entrada
		Set<Nodo> actualesConsumido = new TreeSet<>();

		Set<Nodo> visitados = new TreeSet<>();
		Nodo actual;

		actualesNoConsumido.add(inicio);
		while (!(actualesNoConsumido.isEmpty() && actualesConsumido.isEmpty())) {
			if (!actualesNoConsumido.isEmpty()) {
				actual = actualesNoConsumido.iterator().next();

//				actualesNoConsumido.addAll(actual.transicionVacia());
				Nodo transicionConsumiendo = actual.transicion(simbolo);
				if (transicionConsumiendo != null)
					actualesConsumido.add(transicionConsumiendo);
			} else {
				actual = actualesConsumido.iterator().next();

				actualesNoConsumido.addAll(actual.transicionVacia());
			}

			visitados.add(actual);
			actualesNoConsumido.removeAll(visitados);
			actualesConsumido.removeAll(visitados);
		}

		return visitados;
	}

	public Set<Nodo> transicion(Nodo inicio) {
		// Nodos a los que llegamos sin consumir entrada
		Set<Nodo> actuales = new TreeSet<>();
		Set<Nodo> visitados = new TreeSet<>();
		Nodo actual;

		actuales.add(inicio);
		while (!actuales.isEmpty()) {
			actual = actuales.iterator().next();
			actuales.addAll(actual.transicionVacia());

			visitados.add(actual);
			actuales.remove(actual);
		}

		return visitados;
	}
}
