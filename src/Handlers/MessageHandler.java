package Handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;

import PlayerWarpGUI.PlayerWarpGUI;

public class MessageHandler {

	public static PlayerWarpGUI pl;

	// +-----------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public MessageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@SuppressWarnings("deprecation")
	public String getMessage(String key, Object... args) {
		pl.formatter.applyPattern(pl.messageResourceBundle.getString(key));
		String output = pl.formatter.format(args);
		return output;
	}

}
