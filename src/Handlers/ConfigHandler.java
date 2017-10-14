package Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;

public class ConfigHandler {

	public static PlayerWarpGUI pl;
	FileConfiguration config;

	String status;

	public boolean useMetrics;
	public String MessagesPrefix;
	public String MessagesLanguage;
	public int teleportCooldown;
	public int teleportGodmodeAfter;
	public boolean teleportCancelOnMovement;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ConfigHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}
	
	// +-----------------------------------------------------------------------------------
	// | loadConfig
	// +-----------------------------------------------------------------------------------
	public void loadLocale() {
		// check if config file exsists else make it
		pl.fileHandler.checkFileExsists(pl.configFolder, pl.configFile, pl.defaultConfigFile);
		config = new YamlConfiguration();
		try {
			config.load(pl.getConfigFile());
			pl.setLocale(config.getString("Messages.language"));
		} catch (Exception e) {
			pl.errorHandler.addError("Config load error");
		}

		String[] localeString = pl.locale.split("_");
		Locale currentLocale = new Locale(localeString[0], localeString[1]);

		File file = new File(pl.languageFolder);
		ClassLoader loader = null;
		
		try {
			loader = new URLClassLoader(new URL[] { file.toURI().toURL() });
		} catch (MalformedURLException e) {
			pl.errorHandler.addError("Language file error");
		}
		
		pl.messageResourceBundle = ResourceBundle.getBundle("messages", currentLocale, loader);

		Bukkit.getLogger().info(pl.messageResourceBundle.getLocale().toString());
		
		pl.formatter.setLocale(currentLocale);
		Bukkit.getLogger().info(pl.getMessageHandler().getMessage("LANGUAGE_STATUS") + pl.locale);


	}

	// +-----------------------------------------------------------------------------------
	// | loadConfig
	// +-----------------------------------------------------------------------------------
	public void loadConfig() {

		status = pl.getMessageHandler().getMessage("PASSED");
		// check if config file exsists else make it
		pl.fileHandler.checkFileExsists(pl.configFolder, pl.configFile, pl.defaultConfigFile);

		config = new YamlConfiguration();
		try {
			config.load(pl.getConfigFile());

			setUseMetrics(checkIsBoolean("Metrics.enabled", true));
			setMessagesPrefix(checkIsString("Messages.prefix", "&6[&aPlayerWarpGUI&6] &f"));
			setTeleportCooldown(checkIsInt("Teleport.cooldown", 0));
			setTeleportCancelOnMovement(checkIsBoolean("Teleport.cancelOnMovement", true));
			setTeleportGodmodeAfter(checkIsInt("Teleport.godModeAfterTP", 0));
			
		} catch (Exception e) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(pl.getMessageHandler().getMessage("CONFIG_ERROR_LOADING"));
		}

		Bukkit.getLogger().info(pl.getMessageHandler().getMessage("CONFIG_STATUS") + status);

	}


	// +-----------------------------------------------------------------------------------
	// | checkIsBoolean
	// +-----------------------------------------------------------------------------------
	public boolean checkIsBoolean(String s, boolean defaultValue) {
		if (!config.isSet(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s +  pl.getMessageHandler().getMessage("CONFIG_VALUE_MISSING"));
			return defaultValue;
		}

		if (!config.isBoolean(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s + "");
			return defaultValue;
		}
		return config.getBoolean(s);
	}

	// +-----------------------------------------------------------------------------------
	// | checkIsString
	// +-----------------------------------------------------------------------------------
	public String checkIsString(String s, String defaultValue) {
		if (!config.isSet(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s + pl.getMessageHandler().getMessage("CONFIG_VALUE_MISSING"));
			return defaultValue;
		}

		if (!config.isString(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s + pl.getMessageHandler().getMessage("CONFIG_STRING_ERROR"));
			return defaultValue;
		}
		return config.getString(s);
	}

	// +-----------------------------------------------------------------------------------
	// | checkIsInt
	// +-----------------------------------------------------------------------------------
	public int checkIsInt(String s, int defaultValue) {
		if (!config.isSet(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s +  pl.getMessageHandler().getMessage("CONFIG_VALUE_MISSING"));
			return defaultValue;
		}

		if (!config.isInt(s)) {
			status = pl.getMessageHandler().getMessage("FAILED");
			pl.errorHandler.addError(s + pl.getMessageHandler().getMessage("CONFIG_INTEGER_ERROR"));
			return defaultValue;
		}
		

		if (config.getInt(s) < 0) {
			status = "Failed";
			pl.errorHandler.addError(s +  pl.getMessageHandler().getMessage("CONFIG_INTEGER_LESSTHAN"));
			return defaultValue;
		}
		
		return config.getInt(s);
	}

	// +-----------------------------------------------------------------------------------
	// | checkConfigFile
	// +-----------------------------------------------------------------------------------
	public boolean checkConfigFile() {
		File configFile = new File(pl.getConfigFile());

		// Check it exsists
		if (!configFile.exists()) {
			return false;
		}
		return true;
	}

	// +-----------------------------------------------------------------------------------
	// | createConfigFile
	// +-----------------------------------------------------------------------------------
	public void createConfigFile() {
		Bukkit.getLogger().info(pl.getMessageHandler().getMessage("CONFIG_CREATE_START"));
		File configFile = new File(pl.getConfigFile());

		configFile.getParentFile().mkdirs();
		pl.fileHandler.copy(pl.getResource(pl.defaultConfigFile), configFile);
		Bukkit.getLogger().info(pl.getMessageHandler().getMessage("CONFIG_CREATE_FINISHED"));

	}



	public int getTeleportCooldown() {
		return teleportCooldown;
	}

	public void setTeleportCooldown(int teleportCooldown) {
		this.teleportCooldown = teleportCooldown;
	}

	public boolean getTeleportCancelOnMovement() {
		return teleportCancelOnMovement;
	}

	public void setTeleportCancelOnMovement(boolean teleportCancelOnMovement) {
		this.teleportCancelOnMovement = teleportCancelOnMovement;
	}

	public String getMessagesPrefix() {
		return MessagesPrefix;
	}

	public void setMessagesPrefix(String messagesPrefix) {
		MessagesPrefix = messagesPrefix;
	}

	public String getMessagesLanguage() {
		return MessagesLanguage;
	}

	public void setMessagesLanguage(String messagesLanguage) {
		MessagesLanguage = messagesLanguage;
	}

	public boolean isUseMetrics() {
		return useMetrics;
	}

	public void setUseMetrics(boolean useMetrics) {
		this.useMetrics = useMetrics;
	}
	
	public int getTeleportGodmodeAfter() {
		return teleportGodmodeAfter;
	}

	public void setTeleportGodmodeAfter(int teleportGodmodeAfter) {
		this.teleportGodmodeAfter = teleportGodmodeAfter;
	}

}
