package es.ubu.inf.tfg.regex.datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Generador {

	private static final Random random = new Random(new Date().getTime());

	private static enum Operador {
		SIMBOLO, VACIO, CIERRE, CONCAT, UNION;

		private static final EnumSet<Operador> COMPLETO = EnumSet.range(CIERRE,
				UNION);
		private static final EnumSet<Operador> PARCIAL = EnumSet.of(CONCAT,
				UNION);
		private static final EnumSet<Operador> FINAL_COMPLETO = EnumSet.of(
				SIMBOLO, VACIO);
		private static final EnumSet<Operador> FINAL_PARCIAL = EnumSet
				.of(SIMBOLO);

		private static List<Character> simbolos;
		private static List<Character> simbolosRepetidos;
		private static int posicion;

		private static void inicializa(int nSimbolos, boolean usaVacio) {
			simbolos = new ArrayList<>();
			for (int i = 0; i < nSimbolos; i++)
				simbolos.add((char) ('a' + i));
			if (usaVacio)
				simbolos.add('E');
			simbolosRepetidos = new ArrayList<>(simbolos);

			posicion = 0;
		}

		private static char simbolo() {
			int index;

			if (simbolos.size() > 0) {
				index = random.nextInt(simbolos.size());
				char simbolo = simbolos.get(index);
				simbolos.remove(index);
				return simbolo;
			} else {
				index = random.nextInt(simbolosRepetidos.size());
				return simbolosRepetidos.get(index);
			}
		}

		private static char simboloNoVacio() {
			int index;

			if (simbolosRepetidos.contains('E')) {
				List<Character> simbolosParcial;
				if (simbolos.size() > 1) {
					simbolosParcial = new ArrayList<>(simbolos);
					if (simbolosParcial.contains('E'))
						simbolosParcial.remove(simbolosParcial.indexOf('E'));
					index = random.nextInt(simbolosParcial.size());
					char simbolo = simbolosParcial.get(index);
					simbolos.remove(index);
					return simbolo;
				} else {
					simbolosParcial = new ArrayList<>(simbolosRepetidos);
					simbolosParcial.remove(simbolosParcial.indexOf('E'));
					index = random.nextInt(simbolosParcial.size());
					return simbolosParcial.get(index);
				}
			} else
				return simbolo();
		}

		private static int posicion() {
			return posicion++;
		}

		private static int simbolos() {
			return simbolosRepetidos.size();
		}

		private static Operador random(EnumSet<Operador> operadores) {
			int index = random.nextInt(operadores.size());
			return (Operador) operadores.toArray()[index];
		}

		private static boolean usaVacio() {
			return simbolosRepetidos.contains('E');
		}
	}

	public Generador() {
	}

	public void inicializa(int nSimbolos, boolean usaVacio) {
		Operador.inicializa(nSimbolos, usaVacio);
	}

	public boolean usaVacio() {
		return Operador.usaVacio();
	}

	public int simbolos() {
		return Operador.simbolos();
	}

	public ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {

		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		// Raiz del árbol, aumenta la expresión.
		if (operadores == null) {
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = ExpresionRegular.nodoAumentado(Operador.posicion());
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		}

		// Hoja del árbol.
		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && Operador.usaVacio())
				operadores = Operador.FINAL_COMPLETO;
			else
				operadores = Operador.FINAL_PARCIAL;
		}

		// Nodo operador.
		switch (Operador.random(operadores)) {
		case VACIO: // Vacío solo actua como marcador.
		case SIMBOLO:
			char simbolo;
			if (operadores.equals(Operador.FINAL_COMPLETO))
				simbolo = Operador.simbolo();
			else
				simbolo = Operador.simboloNoVacio();

			if (simbolo == 'E')
				return ExpresionRegular.nodoVacio();
			return ExpresionRegular.nodoSimbolo(Operador.posicion(), simbolo);
		case CIERRE:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.PARCIAL);
			return ExpresionRegular.nodoCierre(hijoIzquierdo);
		case CONCAT:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		case UNION:
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoUnion(hijoDerecho, hijoIzquierdo);
		default:
			return null;
		}
	}
}
