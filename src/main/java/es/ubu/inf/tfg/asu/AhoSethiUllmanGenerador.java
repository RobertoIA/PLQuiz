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

	private static final int MAX_ITERACIONES = 10;

	private static final AhoSethiUllmanGenerador instance = new AhoSethiUllmanGenerador();
	private static final Random random = new Random(new Date().getTime());

	private boolean usaVacio;
	private List<Character> simbolos;
	private List<Character> simbolosRepetidos;
	private int posicion;

	private static enum Operador {
		SIMBOLO, VACIO, CIERRE, CONCAT, UNION;

		static final EnumSet<Operador> COMPLETO = EnumSet.range(CIERRE, UNION);
		static final EnumSet<Operador> PARCIAL = EnumSet.of(CONCAT, UNION);
		static final EnumSet<Operador> FINAL_COMPLETO = EnumSet.of(SIMBOLO,
				VACIO);
		static final EnumSet<Operador> FINAL_PARCIAL = EnumSet.of(SIMBOLO);

		static Operador random(EnumSet<Operador> operadores) {
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
			this.simbolos = new ArrayList<>();
			for (int i = 0; i < nSimbolos; i++)
				this.simbolos.add((char) ('a' + i));
			if (usaVacio)
				this.simbolos.add('E');
			this.simbolosRepetidos = new ArrayList<>(this.simbolos);

			this.posicion = 0;

			expresion = subArbol(profundidad, null);
			System.err.println("Genera: " + expresion);

			if (candidato == null)
				candidato = new AhoSethiUllman(expresion);
			else {
				actual = new AhoSethiUllman(expresion);
				if (evalua(actual, nEstados) < evalua(candidato, nEstados))
					candidato = actual;
			}
			
			profundidad += evalua(candidato, nEstados);
			System.err.println("PROFUNDIDAD " + profundidad);
			iteraciones++;
		} while (evalua(candidato, nEstados) != 0
				&& iteraciones < MAX_ITERACIONES);

		return candidato;
	}

	private ExpresionRegular subArbol(int profundidad,
			EnumSet<Operador> operadores) {

		ExpresionRegular hijoIzquierdo;
		ExpresionRegular hijoDerecho;

		if (operadores == null) {
			System.err.println("Aumenta expresión");
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = ExpresionRegular.nodoAumentado(posicion);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		}

		if (profundidad <= 0) {
			if (operadores.equals(Operador.COMPLETO) && this.usaVacio) {
				System.err.println("Utilizará símbolos y vacíos");
				operadores = Operador.FINAL_COMPLETO;
			} else {
				operadores = Operador.FINAL_PARCIAL;
				System.err.println("Utilizará solo símbolos");
			}
		}

		switch (Operador.random(operadores)) {
		case VACIO: // Vacío solo actua como marcador.
		case SIMBOLO:
			char simbolo = simbolo();
			if (simbolo == 'E' && operadores.contains(Operador.VACIO)) { // si no hay Operador.VACIO puede meter E
				System.err.println("GENERA VACIO");
				return ExpresionRegular.nodoVacio();
			}
			System.err.println("nodo símbolo: " + (this.posicion + 1) + " "
					+ simbolo);
			return ExpresionRegular.nodoSimbolo(this.posicion++, simbolo);
		case CIERRE:
			System.err.println("nodo cierre");
			hijoIzquierdo = subArbol(profundidad - 1, Operador.PARCIAL);
			return ExpresionRegular.nodoCierre(hijoIzquierdo);
		case CONCAT:
			System.err.println("nodo concat");
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoConcat(hijoDerecho, hijoIzquierdo);
		case UNION:
			System.err.println("nodo union");
			hijoIzquierdo = subArbol(profundidad - 1, Operador.COMPLETO);
			hijoDerecho = subArbol(profundidad - 1, Operador.COMPLETO);
			return ExpresionRegular.nodoUnion(hijoDerecho, hijoIzquierdo);
		default:
			System.err.println("DEFAULT");
			return null;
		}

	}

	// utiliza todos los símbolos una vez (orden al azar) antes de empezar a
	// repetir.
	private char simbolo() {
		int index;

		if (this.simbolos.size() > 0) {
			index = random.nextInt(this.simbolos.size());
			char simbolo = this.simbolos.get(index);
			this.simbolos.remove(index);
			return simbolo;
		} else {
			index = random.nextInt(this.simbolosRepetidos.size());
			return this.simbolosRepetidos.get(index);
		}
	}

	private int evalua(AhoSethiUllman problema, int nEstados) {
		return problema.estados().size() - nEstados;
	}
	

	// TODO temp
	public static void main(String[] args) {
		AhoSethiUllmanGenerador asug = AhoSethiUllmanGenerador.getInstance();
		AhoSethiUllman asu = asug.nuevo(1, 6, true);
		System.out.println(asu.problema());
		System.out.println(asu.expresionAumentada());
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
