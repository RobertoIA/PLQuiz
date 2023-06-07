package es.ubu.inf.tfg.doc.datos;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

/**
 * Traductor presenta una interfaz común para aquellas clases que se encarguen de
 * traducir los problemas a una representación textual específica, con la
 * función de agruparlos en un documento de tipo concreto.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public abstract class Traductor {

	/**
	 * Genera un documento de un formato concreto a partir de una lista de
	 * problemas ya traducidos.
	 * 
	 * @param problemas
	 *            Lista de problemas traducidos.
	 * @return Documento completo.
	 */
	public abstract String documento(List<Plantilla> problemas);

	/**
	 * Genera un documento de un formato concreto a partir de un único problema
	 * ya traducido.
	 * 
	 * @param problema
	 *            Problema traducido.
	 * @param num
	 *            Número del problema.
	 * @return Documento completo.
	 */
	public abstract String traduceProblema(Plantilla problema, int num);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo construcción a un
	 * formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUConstruccion(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo etiquetado a un
	 * formato concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUEtiquetado(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo AhoSethiUllman subtipo tablas a un formato
	 * concreto.
	 * 
	 * @param problema
	 *            Problema AhoSethiUllman.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceASUTablas(AhoSethiUllman problema);

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * construcción a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcción de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSConstruccion(
			ConstruccionSubconjuntos problema);

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo
	 * expresión a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcción de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSExpresion(
			ConstruccionSubconjuntos problema);

	/**
	 * Traduce un problema de tipo construcción de subconjuntos subtipo autómata
	 * a un formato concreto.
	 * 
	 * @param problema
	 *            Problema construcción de subconjuntos.
	 * @return Problema traducido.
	 */
	public abstract Plantilla traduceCSAutomata(
			ConstruccionSubconjuntos problema);
}
