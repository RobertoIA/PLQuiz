package es.ubu.inf.tfg.doc.trad;

import java.util.List;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;

public interface Traductor {
	public String documento(List<String> problemas);
	public String traduce(AhoSethiUllman problema);
}