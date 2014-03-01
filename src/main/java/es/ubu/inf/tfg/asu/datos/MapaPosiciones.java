package es.ubu.inf.tfg.asu.datos;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapaPosiciones<E> {
	private Map<E, Set<Integer>> mapaPosiciones;

	public MapaPosiciones() {
		this.mapaPosiciones = new TreeMap<>();
	}

	public void add(E n) { // TODO tiene sentido??
		if (!this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.put(n, new TreeSet<Integer>());
	}

	public void add(E n, int posicion) {
		if (this.mapaPosiciones.containsKey(n)) {
			this.mapaPosiciones.get(n).add(posicion);
		} else {
			Set<Integer> posiciones = new TreeSet<>();
			posiciones.add(posicion);
			this.mapaPosiciones.put(n, posiciones);
			// TODO exception?
			//System.err.println("addPos: No existe clave");
		}
	}

	public void add(E n, Set<Integer> posiciones) {
		if (this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.get(n).addAll(posiciones);
		else
			this.mapaPosiciones.put(n, posiciones);
		// TODO?
			//System.err.println("addSet: No existe clave"); // TODO exception?
	}

	public void add(Set<E> ns, Set<Integer> posiciones) {
		for (E n : ns)
			add(n, posiciones);
	}

	public Set<E> keys() {
		return this.mapaPosiciones.keySet();
	}

	public Set<Integer> get(E key) {
		if (this.mapaPosiciones.containsKey(key))
			return this.mapaPosiciones.get(key);
		else
			return new TreeSet<Integer>();
	}
	
	public int size() {
		return this.mapaPosiciones.size();
	}

	public static <E> MapaPosiciones<E> union(MapaPosiciones<E> a,
			MapaPosiciones<E> b) {
		MapaPosiciones<E> resultado = new MapaPosiciones<E>();

		for (Map.Entry<E, Set<Integer>> pos : a.mapaPosiciones.entrySet()) {
			if (resultado.mapaPosiciones.containsKey(pos.getKey())) {
				resultado.mapaPosiciones.get(pos.getKey()).addAll(
						pos.getValue());
			} else {
				resultado.mapaPosiciones.put(pos.getKey(),
						new TreeSet<Integer>(pos.getValue()));
			}
		}

		for (Map.Entry<E, Set<Integer>> pos : b.mapaPosiciones.entrySet()) {
			if (resultado.mapaPosiciones.containsKey(pos.getKey())) {
				resultado.mapaPosiciones.get(pos.getKey()).addAll(
						pos.getValue());
			} else {
				resultado.mapaPosiciones.put(pos.getKey(),
						new TreeSet<Integer>(pos.getValue()));
			}
		}

		return resultado;
	}
}
