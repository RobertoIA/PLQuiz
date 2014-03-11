package es.ubu.inf.tfg.asu;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;
import es.ubu.inf.tfg.asu.parser.CharStream;
import es.ubu.inf.tfg.asu.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.asu.parser.JavaCharStream;
import es.ubu.inf.tfg.asu.parser.ParseException;
import es.ubu.inf.tfg.asu.parser.TokenMgrError;

public class AhoSethiUllmanGenerador {

	private static final int MAX_ITERACIONES = 25;

	private static final AhoSethiUllmanGenerador instance = new AhoSethiUllmanGenerador();
	private static final Random random = new Random(new Date().getTime());

	private boolean usaVacio;

	private static enum Operador {
		SIMBOLO, VACIO, CIERRE, CONCAT, UNION;

		private static final EnumSet<Operador> COMPLETO = EnumSet.range(CIERRE, UNION);
		private static final EnumSet<Operador> PARCIAL = EnumSet.of(CONCAT, UNION);
		private static final EnumSet<Operador> FINAL_COMPLETO = EnumSet.of(SIMBOLO,
				VACIO);
		private static final EnumSet<Operador> FINAL_PARCIAL = EnumSet.of(SIMBOLO);

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
	}

	/**
	 * Constructor privado, no se le llama.
	 */
	private AhoSethiUllmanGenerador() {
	}

	/**
	 * Devuelve la instancia compartida de la clase.
	 * 
	 * @return Instancia única de AhoSethiUllmanGenerador.
	 */
	public static AhoSethiUllmanGenerador getInstance() {
		return instance;
	}

	public AhoSethiUllman nuevo(int nSimbolos, int nEstados, boolean usaVacio) {
		this.usaVacio = usaVacio;

		AhoSethiUllman candidato = null, actual = null;
		ExpresionRegular expresion;
		int iteraciones = 0;
		int profundidad = 4;

		do {
			// Inicializa variables
			Operador.inicializa(nSimbolos, usaVacio);

			// Genera expresión
			expresion = subArbol(profundidad, null);

			// Evalua candidato
			if (candidato == null) {
				candidato = new AhoSethiUllman(expresion);
			} else {
				actual = new AhoSethiUllman(expresion);
				if (evalua(actual, nEstados) < evalua(candidato, nEstados))
					candidato = actual;
			}

			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);
		return candidato;
	}

	private ExpresionRegular subArbol(int profundidad,
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
			if (operadores.equals(Operador.COMPLETO) && this.usaVacio)
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

	private int evalua(AhoSethiUllman problema, int nEstados) {
		int diferenciaEstados = Math.abs(problema.estados().size() - nEstados);
		int diferenciaSimbolos = Math.abs(problema.simbolos().size() - Operador.simbolos());
		
		return diferenciaEstados + diferenciaSimbolos;
	}
	
	// TODO temp
	public static void main(String[] args) {
		AhoSethiUllmanGenerador asug = AhoSethiUllmanGenerador.getInstance();
		AhoSethiUllman asu = asug.nuevo(2, 6, true);
		System.out.println(asu.problema());
		System.out.println(asu.estados().size());

		CharStream input = new JavaCharStream(new StringReader(
				asu.expresionAumentada() + '\n'));
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			parser.expresion();
		} catch (ParseException | TokenMgrError e) {
			e.printStackTrace();
			throw new UnsupportedOperationException("Expresión no válida.");
		}
	}
}
