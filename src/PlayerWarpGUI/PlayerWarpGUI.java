package PlayerWarpGUI;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import Handlers.ConfigHandler;
import Handlers.LanguageHandler;
import Handlers.MessageHandler;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI plugin;

	public String pathMain = this.getDataFolder().toString();
	public String pathLangs = pathMain + File.separator + "languages" + File.separator;
	public String configName = "config.yml";
	public String pathConfig = pathMain + File.separator + configName;

	public PluginDescriptionFile pdf = this.getDescription(); // Gets plugin.yml
	public String PlayerWarpGUIVersion = pdf.getVersion();
	public ConfigHandler configHandler;
	
	public MessageHandler messages;

	public String defaultLocale = "en_US";
	public Locale locale = null;
	public ResourceBundle resourceBundle = null;
	public File langanugeFile = null;
	public ClassLoader classLoader = null;
	public MessageFormat languageFormat = null;
	public LanguageHandler languageHandler;

	@Override
	public void onEnable() {

		plugin = this;
		
		//configHandler.setUp();
		messages = new MessageHandler(this);
		configHandler = new ConfigHandler(this);
		languageHandler = new LanguageHandler(this);

	}
	
	public void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(this);
		Bukkit.getLogger().info("plugin diabled");
	}

	/*
	 * private boolean checkPluginExsist(String pn) { Plugin p =
	 * Bukkit.getPluginManager().getPlugin(pn); if ((p != null) && (p.isEnabled()))
	 * { return true; } return false; }
	 */

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public ConfigHandler getConfigHandler() {
		return configHandler;
	}

	public void setConfigHandler(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	public MessageHandler getMessages() {
		return messages;
	}

	public void setMessages(MessageHandler messages) {
		this.messages = messages;
	}


	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public LanguageHandler getLanguageHandler() {
		return languageHandler;
	}

	public void setLanguageHandler(LanguageHandler languageHandler) {
		this.languageHandler = languageHandler;
	}

	public MessageFormat getLanguageFormat() {
		return languageFormat;
	}

	public void setLanguageFormat(MessageFormat languageFormat) {
		this.languageFormat = languageFormat;
	}

	public ClassLoader getClassLoaderThis() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public File getLanganugeFile() {
		return langanugeFile;
	}

	public void setLanganugeFile(File langanugeFile) {
		this.langanugeFile = langanugeFile;
	}

	public String getPathMain() {
		return pathMain;
	}

	public void setPathMain(String pathMain) {
		this.pathMain = pathMain;
	}

	public String getPathLangs() {
		return pathLangs;
	}

	public void setPathLangs(String pathLangs) {
		this.pathLangs = pathLangs;
	}

	public String getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(String pathConfig) {
		this.pathConfig = pathConfig;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public PluginDescriptionFile getPdf() {
		return pdf;
	}

	public void setPdf(PluginDescriptionFile pdf) {
		this.pdf = pdf;
	}

	public String getPlayerWarpGUIVersion() {
		return PlayerWarpGUIVersion;
	}

	public void setPlayerWarpGUIVersion(String playerWarpGUIVersion) {
		PlayerWarpGUIVersion = playerWarpGUIVersion;
	}

	/**
	 * @return the plugin
	 */
	public PlayerWarpGUI getPlugin() {
		return plugin;
	}

	/**
	 * @param plugin
	 *            the plugin to set
	 */
	public void setPlugin(PlayerWarpGUI plugin) {
		this.plugin = plugin;
	}

}
