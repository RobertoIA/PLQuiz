package es.ubu.inf.tfg.asu.datos;

import java.util.Set;
import java.util.TreeSet;

public class Nodo {

	private final ExpresionRegular expresion;
	private final Nodo hijoIzquierdo;
	private final Nodo hijoDerecho;

	private boolean esAnulable;
	private Set<Integer> primeraPos;
	private Set<Integer> ultimaPos;

	private MapaPosiciones<Character> simbolos;
	private MapaPosiciones<Integer> siguientePos;

	public Nodo(ExpresionRegular expresion) {
		this.expresion = expresion;

		if (expresion.esVacio()) { // nodo vacio
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = new TreeSet<Integer>();
			this.ultimaPos = new TreeSet<Integer>();
			this.simbolos = new MapaPosiciones<>();
			this.siguientePos = new MapaPosiciones<>();

		} else if (expresion.esSimbolo()) { // nodo símbolo / aumentado
			this.hijoIzquierdo = null;
			this.hijoDerecho = null;

			this.esAnulable = false;
			this.primeraPos = new TreeSet<Integer>();
			this.primeraPos.add(expresion.posicion());
			this.ultimaPos = new TreeSet<Integer>(this.primeraPos);
			this.simbolos = new MapaPosiciones<>();
			this.simbolos.add(expresion.simbolo(), expresion.posicion());
			this.siguientePos = new MapaPosiciones<>();
			this.siguientePos.add(expresion.posicion());

		} else if (expresion.esCierre()) { // nodo cierre
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = null;

			this.esAnulable = true;
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			this.ultimaPos = this.hijoIzquierdo.ultimaPos();
			this.simbolos = this.hijoIzquierdo.simbolos();
			this.siguientePos = this.hijoIzquierdo.siguientePos();
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(),
					this.hijoIzquierdo.primeraPos());

		} else if (expresion.esConcat()) { // nodo concat
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					&& this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoIzquierdo.primeraPos();
			if (this.hijoIzquierdo.esAnulable())
				this.primeraPos.addAll(this.hijoDerecho.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			if (this.hijoDerecho.esAnulable())
				this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());
			this.siguientePos.add(this.hijoIzquierdo.ultimaPos(),
					this.hijoDerecho.primeraPos());

		} else if (expresion.esUnion()) { // nodo union
			this.hijoIzquierdo = new Nodo(expresion.hijoIzquierdo());
			this.hijoDerecho = new Nodo(expresion.hijoDerecho());

			this.esAnulable = this.hijoIzquierdo.esAnulable()
					|| this.hijoDerecho.esAnulable();
			this.primeraPos = this.hijoDerecho.primeraPos();
			this.primeraPos.addAll(this.hijoIzquierdo.primeraPos());
			this.ultimaPos = this.hijoDerecho.ultimaPos();
			this.ultimaPos.addAll(this.hijoIzquierdo.ultimaPos());
			this.simbolos = MapaPosiciones.union(this.hijoIzquierdo.simbolos(),
					this.hijoDerecho.simbolos());
			this.siguientePos = MapaPosiciones.union(
					this.hijoDerecho.siguientePos(),
					this.hijoIzquierdo.siguientePos());

		} else { // runtime exception
			throw new IllegalArgumentException(
					"Expresion regular de tipo desconocido.");
		}
	}

	public ExpresionRegular expresion() {
		return this.expresion;
	}

	public Nodo hijoIzquierdo() {
		return this.hijoIzquierdo;
	}

	public Nodo hijoDerecho() {
		return this.hijoDerecho;
	}

	public boolean esAnulable() {
		return this.esAnulable;
	}

	public Set<Integer> primeraPos() {
		return new TreeSet<>(this.primeraPos);
	}

	public Set<Integer> ultimaPos() {
		return new TreeSet<>(this.ultimaPos);
	}

	public MapaPosiciones<Character> simbolos() {
		return this.simbolos; // TODO copia defensiva
	}

	public MapaPosiciones<Integer> siguientePos() {
		return this.siguientePos; // TODO copia defensiva
	}
}
