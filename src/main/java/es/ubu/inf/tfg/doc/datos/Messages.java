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
		String pwd;
		try {
			res = RESOURCE_BUNDLE.getString(key);
			try {
				pwd = RESOURCE_BUNDLE.getString("pwd");
			} catch (MissingResourceException ex) {
				pwd = "**** PWD error ****";
			}
		} catch (MissingResourceException e) {
			try {
				pwd = RESOURCE_BUNDLE.getString("pwd");
			} catch (MissingResourceException ex) {
				pwd = "**** PWD error ****";
			}
			res = "**** KEY error ****";
		}
		
		/*
		System.out.println("\u001B[0;1mdoc.datos.Messages: BUNDLE_NAME:\033[0;0m " + BUNDLE_NAME);
		System.out.println("\u001B[0;1mdoc.datos.Messages: pwd:\033[0;0m " + pwd);
		System.out.println("\u001B[0;1mdoc.datos.Messages: locale_default:\033[0;0m " + locale_default);
		System.out.println("\u001B[0;1mdoc.datos.Messages: key:\033[0;0m " + key);
		System.out.println("\u001B[0;1mdoc.datos.Messages: res:\033[0;0m " + res);
		*/
		
		return res;
	}
}

