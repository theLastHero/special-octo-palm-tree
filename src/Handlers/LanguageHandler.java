package Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import PlayerWarpGUI.PlayerWarpGUI;

public class LanguageHandler {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public LanguageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;

	}

	// sets plugin locale
	public void setLocale(String locale) {
		String[] localeString = locale.split("_");
		pl.setLocale(new Locale(localeString[0], localeString[1]));
	}

	public void setResourceBundle() {

		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("lang", pl.getLocale(), pl.getClassLoaderThis());
		} catch (MissingResourceException e) {
			// TODO Auto-generated catch block
			//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
			//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
			setupLocale(pl.getDefaultLocale());
		}

		if (!rb.getLocale().toString().equals(pl.getLocale().toString())) {
			//pl.messageHandler.sendConsoleMessage("Could not find language file >> " + pl.getLocale().toString());
			//pl.messageHandler.sendConsoleMessage("Reverting back to default language >> " + pl.getDefaultLocale());
			setLocale(pl.getDefaultLocale());
		}

		pl.setResourceBundle(rb);
	}

	public void setLanguageFile() {
		pl.setLanganugeFile(new File(pl.pathLangs));
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

	public void setupLocale(String s) {
		setLocale(s);
		setFormatter();
		setLanguageFile();
		setClassLoader();
		setResourceBundle();
		setFormatterLocale();
		pl.messageHandler.sendConsoleMessage( getMessage("CONSOLE_MSG_LANGUAGE_FILE", pl.getLocale().toString()));
	}
	
	public void setupLocaleSilent(String s) {
		setLocale(s);
		setFormatter();
		setLanguageFile();
		setClassLoader();
		setResourceBundle();
		setFormatterLocale();
	}

	public String getMessage(String key, Object... args) {
		pl.getLanguageFormat().applyPattern(pl.getResourceBundle().getString(key));
		String output = pl.getLanguageFormat().format(args);
		return output;
	}

	public void checkLanguageFileExsists(String fn) {
		String fileName = "lang_" + fn + ".properties";
		File languageFile = new File(pl.getPathLangs() + fileName);

		// Check it exsists
		if (!languageFile.exists()) {
			//Bukkit.getConsoleSender().sendMessage("Creating language file >> " + fn);
			languageFile.getParentFile().mkdirs();
			pl.getOtherFunctions().copy(pl.getResource("defaults/" + fileName), languageFile);

		}

	}
	
	public String statusValue(boolean compare) {
		String status = pl.getLanguageHandler().getMessage("SUCCESS");
		if (!compare) {
			status = pl.getLanguageHandler().getMessage("FAILED");
		}
		return status;

	}


}
