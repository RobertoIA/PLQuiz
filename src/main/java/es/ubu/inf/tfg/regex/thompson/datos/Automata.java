package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

public class Automata {

	private Nodo nodoInicial;
	private Nodo nodoFinal;

	public Automata(ExpresionRegular expresion) {
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

		nodo0.aņadeTransicionVacia(nodo1);
		nodo0.aņadeTransicionVacia(nodo7);

		nodo1.aņadeTransicionVacia(nodo2);
		nodo1.aņadeTransicionVacia(nodo4);

		nodo2.aņadeTransicion('a', nodo3);

		nodo3.aņadeTransicionVacia(nodo6);

		nodo4.aņadeTransicion('b', nodo5);

		nodo5.aņadeTransicionVacia(nodo6);

		nodo6.aņadeTransicionVacia(nodo7);
		nodo6.aņadeTransicionVacia(nodo1);

		nodo7.aņadeTransicion('a', nodo8);

		nodo8.aņadeTransicion('b', nodo9);

		nodo9.aņadeTransicion('b', nodo10);

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
			actuales.remove(actual);
		}

		return visitados;
	}
}
