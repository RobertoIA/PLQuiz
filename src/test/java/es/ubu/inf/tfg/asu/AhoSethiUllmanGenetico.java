package es.ubu.inf.tfg.asu;

import java.util.Random;

import es.ubu.inf.tfg.asu.datos.ExpresionRegular;

public class AhoSethiUllmanGenetico {

	private Random random;
	private int nSimbolos;
	private int nEstados;
	private int currPos;
	private AhoSethiUllman resultado;

	public AhoSethiUllmanGenetico(int nSimbolos, int nEstados) {
		this.random = new Random();
		this.nSimbolos = nSimbolos;
		this.nEstados = nEstados;
		this.currPos = 1;
		
		int it = 0;
		AhoSethiUllman asu;
		ExpresionRegular exp;
		do {
			exp = subArbol(4);
			asu = new AhoSethiUllman(exp.normalString());
			it++;
		} while (Math.abs(asu.estados().size() - nEstados) > 0 && it < 1000);
		this.resultado = asu;
	}

	private ExpresionRegular subArbol(int profundidad) {
		if (profundidad == 0) {
			// solo terminales
			if(random.nextDouble()>0.9)
				return ExpresionRegular.nodoVacio();
			else {
				int simbolo = (int) (random.nextDouble() * this.nSimbolos);
				char s = (char) (simbolo + 'a');
				return ExpresionRegular.nodoSimbolo(currPos++, s);
			}
		} else {
			// no terminales principalmente
			double tipo = random.nextDouble() * 3;
			if(tipo < 1.0 && false) {
				return ExpresionRegular.nodoCierre(subArbol(profundidad - 1));
			}else if (tipo < 2.0) {
				return ExpresionRegular.nodoUnion(subArbol(profundidad - 1), subArbol(profundidad - 1));
			}else{
				return ExpresionRegular.nodoConcat(subArbol(profundidad - 1), subArbol(profundidad - 1));
			}
		}
	}
	
	public AhoSethiUllman resultado() {
		return this.resultado;
	}
}
