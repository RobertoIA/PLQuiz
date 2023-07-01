package es.ubu.inf.tfg.doc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Locale;

import es.ubu.inf.tfg.doc.datos.Plantilla;

public class PlantillaTest {
	
	@Test
	public void testPlantilla() {
		Plantilla plantilla = new Plantilla("Plantilla.txt"); //$NON-NLS-1$
		String esperado;
		String lang = Messages.getString("PlantillaTest.lang"); //$NON-NLS-1$
		if (lang.equals("ES/")) { //$NON-NLS-1$
			esperado = "ES{1}{2}{3}{4}"; //$NON-NLS-1$
		} else {
			esperado = "{1}{2}{3}{4}";		 //$NON-NLS-1$
		}
		
		assertEquals("Incorrecto recuperado de plantilla.", esperado, plantilla.toString()); //$NON-NLS-1$
	}

	@Test
	public void testSet() {
		Plantilla plantilla = new Plantilla("Plantilla.txt"); //$NON-NLS-1$
		String esperado;
		String lang = Messages.getString("PlantillaTest.lang"); //$NON-NLS-1$
		if (lang.equals("ES/")) { //$NON-NLS-1$
			esperado = "ES1234"; //$NON-NLS-1$
		} else {
			esperado = "1234";		 //$NON-NLS-1$
		}
		
		plantilla.set("1", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		plantilla.set("2", "2"); //$NON-NLS-1$ //$NON-NLS-2$
		plantilla.set("3", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		plantilla.set("4", "4"); //$NON-NLS-1$ //$NON-NLS-2$
		
		assertEquals("Incorrecto modificado de plantilla.", esperado, plantilla.toString()); //$NON-NLS-1$
	}
}
