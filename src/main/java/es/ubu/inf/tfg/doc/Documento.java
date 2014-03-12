package es.ubu.inf.tfg.doc;

import es.ubu.inf.tfg.asu.AhoSethiUllman;

public interface Documento {
	public void añadirProblema(AhoSethiUllman problema);
	public void eliminarProblema(AhoSethiUllman problema);
	public String toString();
}
