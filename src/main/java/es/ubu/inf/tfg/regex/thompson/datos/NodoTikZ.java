package es.ubu.inf.tfg.regex.thompson.datos;


/**
 * Nodo implementa cada uno de los estados del autómata finito no determinista
 * definido por una expresión regular dada. Contiene referencias a su posición,
 * una serie de transiciones que no consumen entrada y una serie de transiciones
 * que consumen cada una un símbolo dado.
 * <p>
 * Implementa sus propios métodos <code>equals</code> y <code>compareTo</code>
 * para su uso dentro de sets ordenados.
 * 
 * @author Roberto Izquierdo Amo
 * 
 */
public class NodoTikZ{
	
	public String tipo;
	public int id;
	public NodoTikZ next;
	public NodoTikZ inside;
	public NodoTikZ pair;
	public NodoTikZ invpair;
	public NodoTikZ up;
	public NodoTikZ down;
	
	
	
	/**
	 * Constructor. Define un nodo desconectado con la posición dada y un estado
	 * final o no.
	 * 
	 * @param posicion
	 *            Posición del nodo.
	 * @param esFinal
	 *            Si el nodo es final.
	 */
	public NodoTikZ(int id, String tipo) {
		this.id = id;
		this.tipo = tipo;
	}
	
	public String toString(){
		if(this.isEmpty()){
			return "";
		} else {
			//System.out.println("Not empty:");
			//System.out.println("id:"+ this.id);
			//System.out.println("Tipo:"+ this.tipo);
		}
		String salida = "{";
		salida += String.format("'tipo': '%s',", this.tipo);
		return salida+"}";
		
	}
	
	public boolean isEmpty() {
		
		if (this.id == 0 && 
			this.tipo == ""){
				return true;
		} else {
			return false;
		}
	}

}
