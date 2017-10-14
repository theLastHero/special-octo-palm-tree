package PlayerWarpGUI;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import Handlers.ConfigHandler;
import Handlers.HookHandler;
import Handlers.LanguageHandler;
import Handlers.MessageHandler;
import Hooks.GriefProtectionHook;
import Hooks.VaultHook;
import Hooks.WorldGuardHook;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI plugin;

	public String pathMain = this.getDataFolder().toString();
	public String pathLangs = pathMain + File.separator + "languages" + File.separator;
	public String configName = "config.yml";
	public String pathConfig = pathMain + File.separator + configName;

	public PluginDescriptionFile pdf = this.getDescription(); // Gets plugin.yml
	public String PlayerWarpGUIVersion = pdf.getVersion();

	public VaultHook vaultHandler;
	public ConfigHandler configHandler;
	public MessageHandler messages;

	public HookHandler hookHandler =  new HookHandler(this);
	public WorldGuardHook worldGuardHook;
	public GriefProtectionHook greifProtectionHook;

	public String defaultLocale = "en_US";
	public Locale locale = null;
	public ResourceBundle resourceBundle = null;
	public File langanugeFile = null;
	public ClassLoader classLoader = null;
	public MessageFormat languageFormat = null;
	public LanguageHandler languageHandler;

	public static Economy econ = null;
	public static Permission perms = null;

	public ArrayList<String> criticalErrors = new ArrayList<String>();
	public ArrayList<String> nonCriticalErrors = new ArrayList<String>();

	@Override
	public void onEnable() {

		setPlugin(this);
		messages = new MessageHandler(this);
		configHandler = new ConfigHandler(this);
		languageHandler = new LanguageHandler(this);
		
		//hooks
		vaultHandler = new VaultHook(this);
		greifProtectionHook = new GriefProtectionHook(this);
		worldGuardHook =  new WorldGuardHook(this);
		
		//console stuff
		messages.sendTitle();
		
	}

	public void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(this);
	}

	/*
	 * private boolean checkPluginExsist(String pn) { Plugin p =
	 * Bukkit.getPluginManager().getPlugin(pn); if ((p != null) && (p.isEnabled()))
	 * { return true; } return false; }
	 */

	public ArrayList<String> getNonCriticalErrors() {
		return nonCriticalErrors;
	}

	public void setNonCriticalErrors(ArrayList<String> nonCriticalErrors) {
		this.nonCriticalErrors = nonCriticalErrors;
	}

	public VaultHook getVaultHandler() {
		return vaultHandler;
	}

	public void setVaultHandler(VaultHook vaultHandler) {
		this.vaultHandler = vaultHandler;
	}

	public static Economy getEcon() {
		return econ;
	}

	public static void setEcon(Economy econ) {
		PlayerWarpGUI.econ = econ;
	}

	public static Permission getPerms() {
		return perms;
	}

	public static void setPerms(Permission perms) {
		PlayerWarpGUI.perms = perms;
	}

	public ArrayList<String> getCriticalErrors() {
		return criticalErrors;
	}

	public void setCriticalErrors(ArrayList<String> criticalErrors) {
		this.criticalErrors = criticalErrors;
	}

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
