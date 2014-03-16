package es.ubu.inf.tfg.doc.datos;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

/**
 * Traductor presenta una interfaz común para aquellas clases que se encargen de
 * traducir los problemas a una representación textual especifica, con la
 * función de agruparlos en un documento de tipo concreto.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public interface Traductor {
	/**
	 * Genera un documento de un formato concreto a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento completo.
	 */
	public String documento(List<String> problemas);

	/**
	 * Traduce un problema de tipo AhoSethiUllman a un formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public String traduce(AhoSethiUllman problema);
}