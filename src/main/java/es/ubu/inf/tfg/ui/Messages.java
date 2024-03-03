package es.ubu.inf.tfg.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import java.util.Locale;

public class Messages {
	// This is for Java 9
	// private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	// This is for Java 8
	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);  // default initialization
	//private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en", "EN"));  // to test English

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
		System.out.println("\u001B[0;1mui.Messages: BUNDLE_NAME:\033[0;0m " + BUNDLE_NAME);
		System.out.println("\u001B[0;1mui.Messages: pwd:\033[0;0m " + pwd);
		System.out.println("\u001B[0;1mui.Messages: locale_default:\033[0;0m " + Locale.getDefault());
		System.out.println("\u001B[0;1mui.Messages: key:\033[0;0m " + key);
		System.out.println("\u001B[0;1mui.Messages res:\033[0;0m " + res);
		*/

		return res;
	}
}
