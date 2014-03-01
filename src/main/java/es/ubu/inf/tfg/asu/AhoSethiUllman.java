package es.ubu.inf.tfg.asu;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;
import es.ubu.inf.tfg.asu.datos.MapaPosiciones;
import es.ubu.inf.tfg.asu.datos.Nodo;
import es.ubu.inf.tfg.asu.parser.ExpresionRegularParser;
import es.ubu.inf.tfg.asu.parser.ParseException;

public class AhoSethiUllman {

	private String problema;
	private ExpresionRegular expresion;
	private Nodo solucion;
	private MapaPosiciones<Character> estados;

	public AhoSethiUllman(String problema) {
		if (problema.charAt(problema.length() - 1) != '\n')
			problema += '\n';

		this.problema = problema.substring(0, problema.length() - 1);

		Reader input = new StringReader(problema);
		ExpresionRegularParser parser = new ExpresionRegularParser(input);

		try {
			this.expresion = parser.expresion();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.solucion = new Nodo(this.expresion);
		
		
		// calculo estados
		this.estados = new MapaPosiciones<>();
		this.estados.add('A', primeraPos());

		char estadoActual = 'A';
		while (estados.keys().contains(estadoActual)) {
			for (char simbolo : simbolos()) {
				if (simbolo != '$')
					mueve(estadoActual, simbolo);
			}
			estadoActual++;
		}
	}

	public String problema() {
		return this.problema;
	}

	public String expresionAumentada() {
		return this.expresion.toString();
	}

	public Set<Integer> primeraPos() {
		return this.solucion.primeraPos();
	}

	public Set<Character> simbolos() {
		return this.solucion.simbolos().keys();
	}
	
	public Set<Integer> posiciones(char simbolo) {
		return this.solucion.simbolos().get(simbolo);
	}

	public Set<Integer> posiciones() {
		return this.solucion.siguientePos().keys();
	}

	public Set<Integer> siguientePos(int n) {
		return this.solucion.siguientePos().get(n);
	}
	
	public Set<Character> estados() {
		return this.estados.keys();
	}
	
	public Set<Integer> estado(char key) {
		return this.estados.get(key);
	}
	
	public char mueve(char estado, char simbolo) {
		Set<Integer> posiciones = new TreeSet<>();

		for (int pos : posiciones(simbolo)) {
			if (this.estados.get(estado).contains(pos))
				posiciones.addAll(siguientePos(pos));
		}

		// TODO comprobar si existe el estado o crear uno nuevo.
		for (char est : estados()) {
			if (estado(est).equals(posiciones)) {
				return est;
			}
		}
		char e = (char) (this.estados.size() + 'A');
		this.estados.add(e, posiciones);
		return e;
	}

	public boolean esFinal(char estado) {
		// TODO revisar. Vamos a tener más de un $ alguna vez?
		Set<Integer> posicionesFinales = posiciones('$');
		for(int posicion : posicionesFinales)
			if(this.estados.get(estado).contains(posicion))
				return true;
		return false;
	}
}
