package Handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import PlayerWarpGUI.LinkedProperties;
import PlayerWarpGUI.PlayerWarpGUI;

public class LanguageHandler {

	public static PlayerWarpGUI pl;
	LinkedProperties langProperties;
	LinkedProperties defaultLanguageFile = new LinkedProperties();

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public LanguageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	public boolean checkLanguageFileExsists(String fn) {
		String fileName = fn + ".properties";
		File languageFile = new File(pl.getPathLangs() + fileName);

		// Check it exsists
		if (!languageFile.exists()) {
			return false;
		}
		return true;
	}

	public void loadLanguageFile(String lang) {

		String error = "Cannot load default lang file.";
		String error2 = "Cannot load language file. Reverting back to default language file.";

		pl.setLanguageFormat(new MessageFormat(""));
		setDefaultLangProperties(error);
		setLangProperties(lang, error);

	}

	/**
	 * @param error
	 */
	private void setDefaultLangProperties(String error) {
		try {
			defaultLanguageFile.load(pl.getResource("lang_.properties"));
		} catch (FileNotFoundException e) {
			pl.getCriticalErrors().add(error);
		} catch (IOException e1) {
			pl.getCriticalErrors().add(error);
		}
	}

	/**
	 * @param lang
	 * @param error
	 */
	private void setLangProperties(String lang, String error) {
		langProperties = new LinkedProperties(defaultLanguageFile);
		try {
			langProperties.load(new FileInputStream(new File(pl.pathLangs + lang + ".properties")));
		} catch (FileNotFoundException e) {
			try {
				langProperties.load(pl.getResource("lang_.properties"));
			} catch (IOException e1) {
				pl.getCriticalErrors().add(error);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMessage(String key, Object... args) {
		pl.getLanguageFormat().applyPattern(langProperties.getProperty(key).toString());
		String output = pl.getLanguageFormat().format(args);
		return output;
	}

	public String statusValue(boolean compare) {
		String status = langProperties.getProperty("SUCCESS");
		if (!compare) {
			status = langProperties.getProperty("FAILED");
		}
		return status;

	}

}
