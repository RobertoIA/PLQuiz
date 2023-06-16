package es.ubu.inf.tfg.doc.datos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plantilla {

	private static final Logger log = LoggerFactory.getLogger(Plantilla.class);

	String plantilla;

	public Plantilla(String fichero) {
		StringBuilder contenido;
		String linea;
		String languageFolder = Messages.getString("Plantilla.lang");  // to be ready for full internationalization //$NON-NLS-1$
		String langfichero = languageFolder + fichero; //$NON-NLS-1$

		
		/*
		System.out.println("\u001B[0;1mPlantilla: Locale default:\u001B[0m " + Locale.getDefault());
		System.out.println("\u001B[0;1mPlantilla: languageFolder:\u001B[0m " + languageFolder);
		System.out.println("\u001B[0;1mPlantilla: langfichero:\u001B[0m " + langfichero);
		*/
		
		try (InputStream entrada = getClass().getResourceAsStream(langfichero);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(entrada, "UTF8"))) { //$NON-NLS-1$
			contenido = new StringBuilder();
			linea = lector.readLine();
			while (linea != null) {
				contenido.append(linea);
				linea = lector.readLine();
				if (linea != null)
					contenido.append("\n"); //$NON-NLS-1$
			}

			plantilla = contenido.toString();
		} catch (IOException e) {
			log.error("Error al recuperar la plantilla {}", plantilla); //$NON-NLS-1$
		}
	}

	public void set(String atributo, String valor) {
		plantilla = plantilla.replace("{" + atributo + "}", valor); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String toString() {
		return plantilla;
	}
	
	public static void main (String [ ] args) {
		Plantilla plantilla;
		Locale.setDefault(new Locale("es", "ES"));  // <=== Locale active at the first instantiation of Plantilla
		plantilla = new Plantilla("plantillaASUConstruccion.html"); //$NON-NLS-1$
		System.out.println("\u001B[1mPlantilla.main: plantilla: " + plantilla);
		System.out.println("\n");
		Locale.setDefault(new Locale("en", "EN"));
		// What follows is still in Spanish, since the ResourceBundle in Messages is a static variable, so it uses the default Locale at
		//    the first time the class Messages is initialized in the first instantiation of Plantilla.
		// The Locale dynamically changed in the previous line has no effect, since the active Locale at the first instantiation
		//    of Plantilla was Locale("es", "ES")
		plantilla = new Plantilla("plantillaASUConstruccion.html"); //$NON-NLS-1$
		System.out.println(plantilla);
		System.out.println("DONE!"); //$NON-NLS-1$
	}
}
