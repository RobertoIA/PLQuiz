package es.ubu.inf.tfg.doc;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
			pwd = RESOURCE_BUNDLE.getString("pwd");
			res = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			pwd = "pwd error";
			res = '!' + key + '!';
		}
		
		System.out.println("\u001B[0;1mdatos.Messages: BUNDLE_NAME:\033[0;0m " + BUNDLE_NAME);
		System.out.println("\u001B[0;1mdatos.Messages: pwd:\033[0;0m " + pwd);
		System.out.println("\u001B[0;1mdatos.Messages: locale_default:\033[0;0m " + locale_default);
		System.out.println("\u001B[0;1mdatos.Messages: key:\033[0;0m " + key);
		System.out.println("\u001B[0;1mdatos.Messages res:\033[0;0m " + res);
		
		return res;		
	}
}
