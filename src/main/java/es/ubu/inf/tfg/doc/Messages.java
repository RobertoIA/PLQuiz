package es.ubu.inf.tfg.doc;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	// This is for Java 9
	// private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	// This is for Java 8
	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$

	private static final Locale locale_default = Locale.getDefault();

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale_default);

	private Messages() {
	}

	public static String getString(String key) {
		String res;
		@SuppressWarnings("unused")
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
		System.out.println("\u001B[0;1mdatos.Messages: BUNDLE_NAME:\033[0;0m " + BUNDLE_NAME);
		System.out.println("\u001B[0;1mdatos.Messages: pwd:\033[0;0m " + pwd);
		System.out.println("\u001B[0;1mdatos.Messages: locale_default:\033[0;0m " + locale_default);
		System.out.println("\u001B[0;1mdatos.Messages: key:\033[0;0m " + key);
		System.out.println("\u001B[0;1mdatos.Messages res:\033[0;0m " + res);
		*/
		
		return res;		
	}
}
