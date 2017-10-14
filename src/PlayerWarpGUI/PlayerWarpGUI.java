package PlayerWarpGUI;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


import Handlers.ConfigHandler;
import Handlers.ErrorHandler;
import Handlers.FileHandler;
import Handlers.MessageHandler;
import Handlers.PlayerWarpsHandler;
import Handlers.VaultHandler;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI instance;
	private PlayerWarpGUI plugin;

	public PluginDescriptionFile pdf = this.getDescription(); // Gets plugin.yml
	public String PlayerWarpGUIVersion = pdf.getVersion();


	public MessageHandler messageHandler = new MessageHandler(this);
	public ConfigHandler configHandler = new ConfigHandler(this);
	public PlayerWarpsHandler playerwarpsHandler = new PlayerWarpsHandler();
	public ErrorHandler errorHandler = new ErrorHandler(this);
	public VaultHandler vaultHandler = new VaultHandler(this);
	public FileHandler fileHandler = new FileHandler(this);

	public ArrayList<String> EnableErrors = new ArrayList<String>();
	
	public String dataFolder = this.getDataFolder() + File.separator; 
	public String configFolder = dataFolder;
	public String configFile = "config.yml"; // main config file location
	public String languageFolder = dataFolder + "languages" + File.separator; // main config file location
	public String defaultConfigFile = "defaultConfig.yml";
	
	public ResourceBundle messageResourceBundle;
	public MessageFormat formatter = new MessageFormat("");
	public String locale = "en_US";
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {

		instance = this;

		//check and create locale files
		fileHandler.checkFileExsists(languageFolder,"messages_en_US.properties", "messages_en_US.properties");
		
		//startup procedure
		configHandler.loadLocale();
		configHandler.loadConfig();
		playerwarpsHandler.loadPlayerWarps();
		vaultHandler.setupVaultHook();
		errorHandler.displayConsoleTitle();

	}

	public String getLanguageFolder() {
		return languageFolder;
	}

	public void setLanguageFolder(String languageFolder) {
		this.languageFolder = languageFolder;
	}

	@Override
	public void onDisable() {
		
	}


	public String getConfigFile() {
		return configFolder+configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
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

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public ConfigHandler getConfigHandler() {
		return configHandler;
	}

	public void setConfigHandler(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	public PlayerWarpsHandler getPlayerwarpsHandler() {
		return playerwarpsHandler;
	}

	public void setPlayerwarpsHandler(PlayerWarpsHandler playerwarpsHandler) {
		this.playerwarpsHandler = playerwarpsHandler;
	}

	public ErrorHandler getEnableHandler() {
		return errorHandler;
	}

	public void setEnableHandler(ErrorHandler enableHandler) {
		this.errorHandler = enableHandler;
	}

	public VaultHandler getVaultHandler() {
		return vaultHandler;
	}

	public void setVaultHandler(VaultHandler vaultHandler) {
		this.vaultHandler = vaultHandler;
	}

	public ArrayList<String> getEnableErrors() {
		return EnableErrors;
	}

	public void setEnableErrors(ArrayList<String> enableErrors) {
		EnableErrors = enableErrors;
	}

	public String getDefaultConfigFile() {
		return defaultConfigFile;
	}

	public void setDefaultConfigFile(String defaultConfigFile) {
		this.defaultConfigFile = defaultConfigFile;
	}

	public ResourceBundle getMessageResourceBundle() {
		return messageResourceBundle;
	}

	public void setMessageResourceBundle(ResourceBundle messageResourceBundle) {
		this.messageResourceBundle = messageResourceBundle;
	}

	public MessageFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(MessageFormat formatter) {
		this.formatter = formatter;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public static void setInstance(PlayerWarpGUI instance) {
		PlayerWarpGUI.instance = instance;
	}

	// -------------------------------------------------------------------------------------
	// getInstance
	// -------------------------------------------------------------------------------------
	public static Plugin getInstance() {
		return instance;
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
