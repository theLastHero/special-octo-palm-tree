package Handlers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;

import PlayerWarpGUI.PlayerWarpGUI;

public class LanguageHandlerOld {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public LanguageHandlerOld(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		setupLocale();
	}

	// sets plugin locale
	public void setLocale(String locale) {
		String[] localeString = locale.split("_");
		pl.setLocale(new Locale(localeString[0], localeString[1]));
	}

	public void setResourceBundle() {

		ResourceBundle rb = null;
		try {
			//rb = ResourceBundle.getBundle("lang", pl.getLocale(), pl.getClassLoaderThis());
			
			rb = ResourceBundle.getBundle("lang");
		} catch (MissingResourceException e) {
			// TODO Auto-generated catch block
			//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
			//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
			//setupLocale(pl.getDefaultLocale());
		}

	//	if (!rb.getLocale().toString().equals(pl.getLocale().toString())) {
	//		//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
	//		//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
	//		setLocale(pl.getDefaultLocale());
	//	}

		pl.setResourceBundle(rb);
	}
	
	public void setResourceBundle2() {

		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("lang", pl.getLocale(), pl.getClassLoaderThis());
			//rb = ResourceBundle.getBundle("lang");
		} catch (MissingResourceException e) {
			// TODO Auto-generated catch block
			//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
			//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
			//setupLocale(pl.getDefaultLocale());
		}

	//	if (!rb.getLocale().toString().equals(pl.getLocale().toString())) {
	//		//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
	//		//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
	//		setLocale(pl.getDefaultLocale());
	//	}

		pl.setResourceBundle(rb);
	}

	public void setLanguageFile() {
		pl.setLanganugeFile(new File(pl.getPathLangs()));
	}

	public void setClassLoader() {
		ClassLoader cl = null;
		try {
			cl = new URLClassLoader(new URL[] { pl.getLanganugeFile().toURI().toURL() });
		} catch (MalformedURLException e) {

		}
		pl.setClassLoader(cl);
	}

	public void setFormatter() {
		pl.setLanguageFormat(new MessageFormat(""));
	}

	public void setFormatterLocale() {
		pl.getLanguageFormat().setLocale(pl.getLocale());
	}

	public void setupLocale() {
		//setLocale(s);
		setFormatter();
		setLanguageFile();
		setClassLoader();
		setResourceBundle();
		setFormatterLocale();
		//pl.getMessageHandler().sendConsoleMessage("Loaded default language file.");
	}
	
	public void setupLocaleReload(String s) {
		setLocale(s);
		//setFormatter();
		setLanguageFile();
		setClassLoader();
		setResourceBundle2();
		setFormatterLocale();
		pl.getMessageHandler().sendConsoleMessage( getMessage("CONSOLE_MSG_LANGUAGE_FILE", pl.getLocale().toString()));
	}
	
	public String getMessage(String key, Object... args) {
		String s;
		try {
			s = pl.getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
			// TODO Auto-generated catch block
			s = pl.getResourceBundle().getBaseBundleName().valueOf(args).toString();
		}
		

		pl.getLanguageFormat().applyPattern(s);
		String output = pl.getLanguageFormat().format(args);
		return output;
	}

	public void makeLanguageFile(String fn) {
		String fileName = "lang_" + fn + ".properties";
		File languageFile = new File(pl.getPathLangs() + fileName);

		// Check it exsists
		if (!languageFile.exists()) {
			//Bukkit.getConsoleSender().sendMessage("Creating language file >> " + fn);
			languageFile.getParentFile().mkdirs();
			pl.getOtherFunctions().copy(pl.getResource("defaults/" + fileName), languageFile);

		}

	}
	
	public boolean checkLanguageFileExsists(String fn) {
		String fileName = "lang_" + fn + ".properties";
		File languageFile = new File(pl.getPathLangs() + fileName);

		// Check it exsists
		if (!languageFile.exists()) {
			return false;
		}
		return true;
	}
	
	public String statusValue(boolean compare) {
		String status = pl.getLanguageHandler().getMessage("SUCCESS");
		if (!compare) {
			status = pl.getLanguageHandler().getMessage("FAILED");
		}
		return status;

	}


}
