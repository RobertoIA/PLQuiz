package es.ubu.inf.tfg.regex.thompson.datos;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class NodoTest {

	/**
	 * Comprueba que las posiciones se asignan y recuperan correctamente.
	 */
	@Test
	public void testPosicion() {
		Nodo nodo = new Nodo(1, false);
		assertEquals("Error asignando posición al nodo.", 1, nodo.posicion());

		nodo = new Nodo(15, true);
		assertEquals("Error asignando posición al nodo.", 15, nodo.posicion());
	}

	/**
	 * Comprueba que los nodos finales o no finales se reconocen correctamente
	 * como tal.
	 */
	@Test
	public void testEsFinal() {
		Nodo nodo = new Nodo(1, false);
		assertEquals("Error reconociendo nodo no final.", false, nodo.esFinal());

		nodo = new Nodo(15, true);
		assertEquals("Error reconociendo nodo final.", true, nodo.esFinal());
	}

	/**
	 * Comprueba que las transiciones se añaden y recuperan correctamente, y que
	 * no se confunden con las transiciones vacías.
	 */
	@Test
	public void testTransicion() {
		Nodo nodo0 = new Nodo(0, false);
		Nodo nodo1 = new Nodo(1, false);
		Nodo nodo2 = new Nodo(2, false);
		Nodo nodo3 = new Nodo(3, false);
		Nodo nodo4 = new Nodo(4, true);

		nodo0.añadeTransicionVacia(nodo1);
		nodo1.añadeTransicion('a', nodo2);
		nodo1.añadeTransicionVacia(nodo3);
		nodo2.añadeTransicion('b', nodo4);
		nodo3.añadeTransicion('c', nodo4);

		// nodo0
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo0.transicion('a'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo0.transicion('b'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo0.transicion('c'));
		// nodo1
		assertEquals("Error al recuperar transición que consume entrada.", nodo2, nodo1.transicion('a'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo1.transicion('b'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo1.transicion('c'));
		// nodo2
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo2.transicion('a'));
		assertEquals("Error al recuperar transición que consume entrada.", nodo4, nodo2.transicion('b'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo2.transicion('c'));
		// nodo3
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo3.transicion('a'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo3.transicion('b'));
		assertEquals("Error al recuperar transición que consume entrada.", nodo4, nodo3.transicion('c'));
		// nodo4
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo4.transicion('a'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo4.transicion('b'));
		assertEquals("Error al recuperar transición que consume entrada.", null, nodo4.transicion('c'));
	}

	/**
	 * Comprueba que las transiciones vacías se añaden y recuperan
	 * correctamente, y que no se confunden con las transiciones que consumen
	 * entrada.
	 */
	@Test
	public void testTransicionVacia() {
		Nodo nodo0 = new Nodo(0, false);
		Nodo nodo1 = new Nodo(1, false);
		Nodo nodo2 = new Nodo(2, false);
		Nodo nodo3 = new Nodo(3, false);
		Nodo nodo4 = new Nodo(4, true);

		nodo0.añadeTransicionVacia(nodo1);
		nodo1.añadeTransicion('a', nodo2);
		nodo1.añadeTransicionVacia(nodo3);
		nodo2.añadeTransicion('b', nodo4);
		nodo3.añadeTransicion('c', nodo4);

		// nodo0
		assertEquals("Error al recuperar transición que no consume entrada.", set(nodo1), nodo0.transicionVacia());
		// nodo1
		assertEquals("Error al recuperar transición que no consume entrada.", set(nodo3), nodo1.transicionVacia());
		// nodo2
		assertEquals("Error al recuperar transición que no consume entrada.", set(), nodo2.transicionVacia());
		// nodo3
		assertEquals("Error al recuperar transición que no consume entrada.", set(), nodo3.transicionVacia());
		// nodo4
		assertEquals("Error al recuperar transición que no consume entrada.", set(), nodo4.transicionVacia());
	}

	/**
	 * Genera un set a partir de una lista de nodos de longitud variable.
	 * 
	 * @param ns
	 *            Lista de nodos de longitud variable.
	 * @return Set de nodos.
	 */
	private static Set<Nodo> set(Nodo... ns) {
		Set<Nodo> set = new TreeSet<>();
		for (Nodo n : ns)
			set.add(n);
		return set;
	}
}
