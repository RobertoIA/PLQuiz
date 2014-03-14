package es.ubu.inf.tfg.regex.asu.datos;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * MapaPosiciones implementa una interfaz simple para acceder a un mapa genérico
 * que almacena un conjunto de posiciones como valores. Provee métodos para
 * añadir elementos al mapa en distintos formatos, así como métodos que permiten
 * acceder a las partes útiles de la información del mapa.
 * <p>
 * La única manera de modificar la información de un MapaPosiciones es a través
 * de los métodos {@link #add(E) add}, {@link #add(E, int) add}, {@link #add(E,
 * Set<Integer>) add} y {@link #add(Set<E>, Set<Integer>) add}. La información
 * obtenida no es nunca una referencia directa al elemento almacenado, sino una
 * copia.
 * <p>
 * La clase provee métodos estáticos que permiten realizar operaciones de unión
 * y copia sobre instancias de esta clase.
 * 
 * @author Roberto Izquierdo Amo
 * 
 * @param <E>
 *            Tipo de claves que manejará el mapa interno. Normalmente será
 *            <code>Integer</code> (función stePos) o <code>Character</code>
 *            (diccionario de símbolos).
 */
public class MapaPosiciones<E> {
	private Map<E, Set<Integer>> mapaPosiciones;

	/**
	 * Construye un mapa que asigna un conjunto de posiciones a una clave de un
	 * tipo dado.
	 */
	public MapaPosiciones() {
		this.mapaPosiciones = new TreeMap<>();
	}

	/**
	 * Añade una clave al mapa, asociada a un conjunto de posiciones vacío. Si
	 * el mapa ya contiene la clave, no hace nada.
	 * 
	 * @param n
	 *            Clave que añadir al mapa.
	 */
	public void add(E n) {
		if (!this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.put(n, new TreeSet<Integer>());
	}

	/**
	 * Añade una posición al conjunto de posiciones de una clave dada. Si la
	 * clave ya existe en el mapa, la posición se añade al conjunto existente.
	 * 
	 * @param n
	 *            Clave del conjunto.
	 * @param posicion
	 *            Posición a añadir.
	 */
	public void add(E n, int posicion) {
		if (this.mapaPosiciones.containsKey(n)) {
			this.mapaPosiciones.get(n).add(posicion);
		} else {
			Set<Integer> posiciones = new TreeSet<>();
			posiciones.add(posicion);
			this.mapaPosiciones.put(n, posiciones);
		}
	}

	/**
	 * Añade un conjunto de posiciones al conjunto de posiciones de una clave
	 * dada. Si la clave ya existe en el mapa, las posiciones se añaden al
	 * conjunto existente.
	 * 
	 * @param n
	 *            Clave del conjunto.
	 * @param posiciones
	 *            Posiciones a añadir.
	 */
	public void add(E n, Set<Integer> posiciones) {
		if (this.mapaPosiciones.containsKey(n))
			this.mapaPosiciones.get(n).addAll(posiciones);
		else
			this.mapaPosiciones.put(n, new TreeSet<>(posiciones));
	}

	/**
	 * Añade un conjunto de posiciones al conjunto de posiciones de una serie de
	 * clave dadas. Esta implementación utiliza el método {@link #add(E,
	 * Set<Integer>) add}.
	 * 
	 * @param ns
	 * @param posiciones
	 */
	public void add(Set<E> ns, Set<Integer> posiciones) {
		for (E n : ns)
			add(n, posiciones);
	}

	/**
	 * Devuelve una copia del conjunto de claves del mapa de posiciones. Si se
	 * devolviera el conjunto de claves original podrían introducirse cambios al
	 * mapa desde el exterior.
	 * 
	 * @return Conjunto de claves de posición.
	 */
	public Set<E> keys() {
		return new TreeSet<>(this.mapaPosiciones.keySet());
	}

	/**
	 * Devuelve una copia del conjunto de posiciones asociado a una determinada
	 * clave. Si la clave no existe, se devuelve el conjunto vacío.
	 * 
	 * @param key
	 *            Clave que buscar.
	 * @return Conjunto de posiciones asociado.
	 */
	public Set<Integer> get(E key) {
		if (this.mapaPosiciones.containsKey(key))
			return new TreeSet<Integer>(this.mapaPosiciones.get(key));
		else
			return new TreeSet<Integer>();
	}

	/**
	 * Devuelve el número de claves contenidas en el mapa.
	 * 
	 * @return Tamaño del mapa.
	 */
	public int size() {
		return this.mapaPosiciones.size();
	}

	/**
	 * Devuelve un mapa que constituye la unión de otros dos mapas. Si una clave
	 * se encuentra en ambos mapas, sus conjuntos de posiciones se unen. Si una
	 * clave se encuentra en solo un mapa, esta se añade al mapa tal cual. Esta
	 * implementación utiliza el método {@link #add(E, Set<Integer>) add}.
	 * 
	 * @param a
	 *            Primer mapa.
	 * @param b
	 *            Segundo mapa.
	 * @return Mapa unión del primer y segundo mapa.
	 */
	public static <E> MapaPosiciones<E> union(MapaPosiciones<E> a,
			MapaPosiciones<E> b) {
		MapaPosiciones<E> resultado = new MapaPosiciones<>();

		for (Entry<E, Set<Integer>> e : a.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		for (Entry<E, Set<Integer>> e : b.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		return resultado;
	}

	/**
	 * Devuelve un mapa copia del mapa dado, pero sin compartir referencias al
	 * mismo. Esta implementación utiliza el método {@link #add(E, Set<Integer>)
	 * add}.
	 * 
	 * @param original
	 *            Mapa original.
	 * @return Copia del mapa original.
	 */
	public static <E> MapaPosiciones<E> copia(MapaPosiciones<E> original) {
		MapaPosiciones<E> resultado = new MapaPosiciones<>();

		for (Entry<E, Set<Integer>> e : original.mapaPosiciones.entrySet())
			resultado.add(e.getKey(), e.getValue());

		return resultado;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof MapaPosiciones))
			return false;

		if (size() != ((MapaPosiciones<E>) o).size())
			return false;

		for (Entry<E, Set<Integer>> e : this.mapaPosiciones.entrySet()) {
			if (!(e.getValue().equals(((MapaPosiciones<E>) o).get(e.getKey()))))
				return false;
		}

		return true;
	}
}
