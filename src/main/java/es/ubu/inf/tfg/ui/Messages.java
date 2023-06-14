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
		String res;
		String pwd;
		try {
			pwd =  RESOURCE_BUNDLE.getString("pwd");
			res = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			pwd = "pwd error";
			res = '!' + key + '!';
		}
		
		System.out.println("\u001B[0;1mui.Messages: BUNDLE_NAME:\033[0;0m " + BUNDLE_NAME);
		System.out.println("\u001B[0;1mui.Messages: pwd:\033[0;0m " + pwd);
		System.out.println("\u001B[0;1mui.Messages: locale_default:\033[0;0m " + Locale.getDefault());
		System.out.println("\u001B[0;1mui.Messages: key:\033[0;0m " + key);
		System.out.println("\u001B[0;1mui.Messages res:\033[0;0m " + res);

		return res;
	}
}
