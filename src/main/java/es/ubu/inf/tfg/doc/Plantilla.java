package es.ubu.inf.tfg.doc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plantilla {

	private static final Logger log = LoggerFactory.getLogger(Plantilla.class);

	String plantilla;

	public Plantilla(String fichero) {
		StringBuilder contenido;
		String linea;

		try (InputStream entrada = getClass().getResourceAsStream(fichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) {

			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n");
			}

			plantilla = contenido.toString();
		} catch (IOException e) {
			log.error("Error al recuperar la plantilla {}", plantilla);
		}
	}

	public void set(String atributo, String valor) {
		plantilla = plantilla.replace("{" + atributo + "}", valor);
	}

	@Override
	public String toString() {
		return plantilla;
	}

}
