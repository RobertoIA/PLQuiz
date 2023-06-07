package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Locale;

import es.ubu.inf.tfg.doc.datos.Plantilla;

public class PlantillaTest {
	
	@Test
	public void testPlantilla() {
		Plantilla plantilla = new Plantilla("Plantilla.txt");
		String esperado;
		if (Locale.getDefault().getLanguage() == "es") {
				esperado = "ES{1}{2}{3}{4}";
		} else {
			esperado = "{1}{2}{3}{4}";		
		}
		
		assertEquals("Incorrecto recuperado de plantilla.", esperado, plantilla.toString());
	}

	@Test
	public void testSet() {
		Plantilla plantilla = new Plantilla("Plantilla.txt");
		String esperado;
		if (Locale.getDefault().getLanguage() == "es") {
			esperado = "ES1234";
		} else {
			esperado = "1234";		
		}
		
		plantilla.set("1", "1");
		plantilla.set("2", "2");
		plantilla.set("3", "3");
		plantilla.set("4", "4");
		
		assertEquals("Incorrecto modificado de plantilla.", esperado, plantilla.toString());
	}
}
