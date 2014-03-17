package es.ubu.inf.tfg.regex.thompson;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import es.ubu.inf.tfg.regex.datos.ExpresionRegular;
import es.ubu.inf.tfg.regex.thompson.datos.Automata;
import es.ubu.inf.tfg.regex.thompson.datos.Nodo;

public class Thompson {

	private Automata automata;
	private Map<Character, Set<Nodo>> estados;
	//private MapaEstados transiciones;

	public Thompson(String problema) {
		this.automata = new Automata();
		//this.transiciones = new MapaEstados();
		this.estados = new TreeMap<>();

		char estadoActual = 'A';
		Set<Nodo> posiciones = automata.transicion(automata.nodoInicial());
		estados.put(estadoActual, posiciones);

		Set<Character> simbolos = new TreeSet<>();// TODO temp
		simbolos.add('a');
		simbolos.add('b');

		while (estados.keySet().contains(estadoActual)) {
			for (char simbolo : simbolos) {
				posiciones = new TreeSet<>();
				for (Nodo nodo : estados.get(estadoActual)) {
					posiciones.addAll(automata.transicion(nodo, simbolo));
				}

				// TODO ! chapuza temp!
				boolean encontrado = false;
				for (Entry<Character, Set<Nodo>> e : estados.entrySet()) {
					if (e.getValue().equals(posiciones)) {
						encontrado = true;
					}
				}
				if (!encontrado) {
					char nuevoEstado = (char) (estados.size() + 'A');
					estados.put(nuevoEstado, posiciones);
				}
			}
			estadoActual++;
		}

		for (Entry<Character, Set<Nodo>> e : estados.entrySet()) {
			System.err.println(e.getKey() + " : " + e.getValue());
		}
	}

	public Thompson(ExpresionRegular expresion) {

	}
}
