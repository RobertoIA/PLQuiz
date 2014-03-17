package es.ubu.inf.tfg.regex.datos;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * MapaEstados implementa una interfaz simple para trabajar con un mapa de mapas
 * que almacena una tabla de transición. Permite obtener tanto una lista de
 * estados existentes, como el resultado de una operacion mueve, tal que
 * mueve(estado, simbolo) -> destino.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class MapaEstados {
	private Map<Character, Map<Character, Character>> mapaEstados;

	/**
	 * Construye un nuevo mapa de estados.
	 */
	public MapaEstados() {
		this.mapaEstados = new TreeMap<>();
	}

	/**
	 * Añade una nueva transición al mapa de estados.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Simbolo con el que realizamos la transición.
	 * @param destino
	 *            Estado de destino.
	 */
	public void add(char estado, char simbolo, char destino) {
		Map<Character, Character> transicion;

		if (!(this.mapaEstados.containsKey(estado))) {
			transicion = new TreeMap<>();
			transicion.put(simbolo, destino);
			this.mapaEstados.put(estado, transicion);
		} else {
			transicion = this.mapaEstados.get(estado);
			transicion.put(simbolo, destino);
		}
	}

	/**
	 * Obtiene un estado de destino para una combinación de estado de origen y
	 * símbolo de transición.
	 * 
	 * @param estado
	 *            Estado de origen.
	 * @param simbolo
	 *            Simbolo con el que realizamos la transición.
	 * @return Estado de destino.
	 */
	public char get(char estado, char simbolo) {
		Map<Character, Character> transicion = this.mapaEstados.get(estado);
		return transicion.get(simbolo);
	}

	/**
	 * Obtiene una lista de todos los estados existentes en la tabla, que se
	 * corresponden con las claves del mapa externo.
	 * 
	 * @return Lista de estados en la tabla.
	 */
	public Set<Character> estados() {
		return this.mapaEstados.keySet();
	}
}
