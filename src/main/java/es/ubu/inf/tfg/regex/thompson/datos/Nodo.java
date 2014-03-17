package es.ubu.inf.tfg.regex.thompson.datos;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Nodo implements Comparable<Nodo>{
	private int posicion;
	private boolean esFinal;
	
	private Map<Character, Nodo> transiciones;
	private Set<Nodo> transicionesVacias;

	public Nodo(int posicion, boolean esFinal) {
		this.posicion = posicion;
		this.esFinal = esFinal;
		
		this.transiciones = new TreeMap<>();
		this.transicionesVacias = new TreeSet<>();
	}

	public int posicion() {
		return this.posicion;
	}
	
	public boolean esFinal() {
		return this.esFinal;
	}

	public void añadeTransicion(char simbolo, Nodo destino) {
		this.transiciones.put(simbolo, destino);
	}

	public void añadeTransicionVacia(Nodo destino) {
		this.transicionesVacias.add(destino);
	}

	public Nodo transicion(char simbolo) {
		return this.transiciones.get(simbolo);
	}

	public Set<Nodo> transicionVacia() {
		return this.transicionesVacias;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Nodo))
			return false;

		// Consideramos dos nodos iguales si tienen la misma posición.
		if (!(((Nodo) o).posicion() == posicion()))
			return false;

		return true;
	}
	
	@Override //TODO temp
	public String toString() {
		return "" + posicion();
	}

	@Override
	public int compareTo(Nodo otro) {
		if(otro.posicion() < posicion())
			return 1;
		else if(otro.posicion() > posicion())
			return -1;
		return 0;
	}
}
