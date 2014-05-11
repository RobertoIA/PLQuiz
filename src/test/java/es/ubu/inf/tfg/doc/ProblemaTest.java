package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ubu.inf.tfg.regex.asu.AhoSethiUllman;
import es.ubu.inf.tfg.regex.thompson.ConstruccionSubconjuntos;

public class ProblemaTest {
	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo completo se
	 * crean correctamente.
	 */
	@Test
	public void testASUCompleto() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.ASUCompleto(asu);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo completo",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo completo", "AhoSethiUllmanCompleto", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo Aho-Sethi-Ullman subtipo árbol se
	 * crean correctamente.
	 */
	@Test
	public void testASUArbol() {
		AhoSethiUllman asu = new AhoSethiUllman("a*");
		Problema<AhoSethiUllman> problema = Problema.ASUArbol(asu);
		
		assertEquals("Error recuperando problema original tipo AhoSethiUllman subtipo árbol",  asu, problema.getProblema());
		assertEquals("Error identificando problema tipo AhoSethiUllman subtipo árbol", "AhoSethiUllmanArbol", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo construcción de subconjuntos subtipo
	 * expresión se crean correctamente.
	 */
	@Test
	public void testCSExpresion() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSExpresion(cs);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo expresión",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo expresión", "ConstruccionSubconjuntosExpresion", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo construcción de subconjuntos subtipo
	 * autómata se crean correctamente.
	 */
	@Test
	public void testCSAutomata() {
		ConstruccionSubconjuntos cs = new ConstruccionSubconjuntos("a*");
		Problema<ConstruccionSubconjuntos> problema = Problema.CSAutomata(cs);
		
		assertEquals("Error recuperando problema original tipo ConstruccionSubconjuntos subtipo autómata",  cs, problema.getProblema());
		assertEquals("Error identificando problema tipo ConstruccionSubconjuntos subtipo autómata", "ConstruccionSubconjuntosAutomata", problema.getTipo());
	}

	/**
	 * Comprueba que los problemas de tipo thompson se crean correctamente.
	 */
	@Test
	public void testThompson() {
		// TODO
	}

}
