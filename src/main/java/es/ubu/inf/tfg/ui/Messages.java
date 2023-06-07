package es.ubu.inf.tfg.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import java.util.Locale;

public class Messages {
	private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);  // default initialization
	//private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en", "EN"));  // to test English

	private Messages() {
	}

	public static String getString(String key) {
		System.out.println("ui.Messages: locale_default: " + Locale.getDefault() + ", key: " + key);
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
