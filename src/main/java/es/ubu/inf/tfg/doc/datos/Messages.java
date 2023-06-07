package es.ubu.inf.tfg.doc.datos;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;

public class Messages {
	private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	
	private static final Locale locale_default = Locale.getDefault();

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale_default);

	private Messages() {
	}

	public static String getString(String key) {
		String res;
		try {
			res = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			res = '!' + key + '!';
		}
		System.out.println("datos.Messages: locale_default: " + locale_default + ", key: " + key + ", res: " + res);
		return res;
	}
}

