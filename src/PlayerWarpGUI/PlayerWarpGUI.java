package PlayerWarpGUI;

import java.io.File;
import java.io.IOException;
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
import Handlers.PlayerWarpFileHandler;
import Handlers.PlayerWarpObjectHandler;
import Handlers.TeleportHandler;
import Handlers.WarpHandler;
import Hooks.GriefPreventionHook;
import Hooks.RedProtectHook;
import Hooks.VaultHook;
import Hooks.WorldGuardHook;
import Listeners.ChestListener;
import Listeners.CommandListener;
import Listeners.PlayerListener;
import Objects.GUIObject;
import Objects.PlayerWarpObject;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PlayerWarpGUI extends JavaPlugin {

	public static PlayerWarpGUI plugin;

	// file paths
	public String pathMain = this.getDataFolder().toString();
	public String pathLangs = pathMain + File.separator + "languages" + File.separator;
	public String configName = "config.yml";
	public String pathConfig = pathMain + File.separator + configName;
	public String warpsName = "warps";
	public String pathWarps = pathMain + File.separator + warpsName + File.separator;

	// plugin details
	public PluginDescriptionFile pdf = this.getDescription(); // Gets plugin.yml
	public String PlayerWarpGUIVersion = pdf.getVersion();

	// handlers
	public ConfigHandler configHandler;
	public MessageHandler messageHandler;
	public HookHandler hookHandler = new HookHandler(this);
	public PlayerWarpObjectHandler playerWarpObjectHandler;
	public WarpHandler warpHandler = new WarpHandler(this);
	public TeleportHandler teleportHandler = new TeleportHandler(this);
	public PlayerWarpFileHandler playerWarpFileHandler;


	// others
	public OtherFunctions otherFunctions = new OtherFunctions(this);
	public GUIObject guiObject = new GUIObject(this);
	
	// hooks
	public VaultHook vaultHook;
	public WorldGuardHook worldGuardHook;
	public GriefPreventionHook greifPrevetionHook;
	public RedProtectHook redProtectHook;

	// locales
	public String defaultLocale = "en_US";
	public Locale locale = null;
	public ResourceBundle resourceBundle = null;
	public File langanugeFile = null;
	public ClassLoader classLoader = null;
	public MessageFormat languageFormat = null;
	public LanguageHandler languageHandler;

	// econ/perms
	public static Economy econ = null;
	public static Permission perms = null;

	// error arrays
	public ArrayList<String> criticalErrors = new ArrayList<String>();
	public ArrayList<String> nonCriticalErrors = new ArrayList<String>();

	// warps
	public static ArrayList<PlayerWarpObject> playerWarpObjects = new ArrayList<PlayerWarpObject>();

	@Override
	public void onEnable() {

		setPlugin(this);

		// listeners
		this.getCommand("playerwarps").setExecutor(new CommandListener(this));
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

		// handlers
		configHandler = new ConfigHandler(this);
		languageHandler = new LanguageHandler(this);
		
		getLanguageHandler().checkLanguageFileExsists("en_US");
		getLanguageHandler().setupLocaleSilent("en_US");

		messageHandler = new MessageHandler(this);
		
		getConfigHandler().setUp();
		getLanguageHandler().setupLocale(getConfig().getString("language"));
		getConfigHandler().msg();
		
		
		//messageHandler = new MessageHandler(this);
		playerWarpObjectHandler = new PlayerWarpObjectHandler(this);
		
		playerWarpFileHandler = new PlayerWarpFileHandler(this);
		playerWarpFileHandler.checkWarpFolder();
		playerWarpFileHandler.createAllFromWarpFiles(true);

		// hooks
		vaultHook = new VaultHook(this);
		greifPrevetionHook = new GriefPreventionHook(this);
		redProtectHook = new RedProtectHook(this);
		worldGuardHook = new WorldGuardHook(this);

		// other stuff

		

		
		useMetrics();

		// console stuff
		messageHandler.sendTitle();

	}

	public void useMetrics() {
		// metrics
		if (getConfig().getBoolean("metrics.enabled") == true) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				messageHandler.sendConsoleMessage(languageHandler.getMessage("CONSOLE_MSG_METRICS",languageHandler.getMessage("SUCCESS")));
			} catch (IOException e) {
				messageHandler.sendConsoleMessage(languageHandler.getMessage("CONSOLE_MSG_METRICS",languageHandler.getMessage("FAILED")));
				getNonCriticalErrors().add(languageHandler.getMessage("CONSOLE_NONCRITIAL_ERROR"));
			}
		}
	}
	
	public PlayerWarpFileHandler getPlayerWarpFileHandler() {
		return playerWarpFileHandler;
	}

	public void setPlayerWarpFileHandler(PlayerWarpFileHandler playerWarpFileHandler) {
		this.playerWarpFileHandler = playerWarpFileHandler;
	}
	
	public WarpHandler getWarpHandler() {
		return warpHandler;
	}

	public void setWarpHandler(WarpHandler warpHandler) {
		this.warpHandler = warpHandler;
	}

	public GUIObject getGuiObject() {
		return guiObject;
	}

	public void setGuiObject(GUIObject guiObject) {
		this.guiObject = guiObject;
	}

	public void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(this);
	}

	public PlayerWarpObjectHandler getPlayerWarpObjectHandler() {
		return playerWarpObjectHandler;
	}

	public void setPlayerWarpObjectHandler(PlayerWarpObjectHandler playerWarpObjectHandler) {
		this.playerWarpObjectHandler = playerWarpObjectHandler;
	}

	public OtherFunctions getOtherFunctions() {
		return otherFunctions;
	}

	public void setOtherFunctions(OtherFunctions otherFunctions) {
		this.otherFunctions = otherFunctions;
	}

	public RedProtectHook getRedProtectHook() {
		return redProtectHook;
	}

	public void setRedProtectHook(RedProtectHook redProtectHook) {
		this.redProtectHook = redProtectHook;
	}

	public OtherFunctions getCalc() {
		return otherFunctions;
	}

	public void setCalc(OtherFunctions calc) {
		this.otherFunctions = calc;
	}

	public String getWarpsName() {
		return warpsName;
	}

	public void setWarpsName(String warpsName) {
		this.warpsName = warpsName;
	}


	public String getPathWarps() {
		return pathWarps;
	}

	public void setPathWarps(String pathWarps) {
		this.pathWarps = pathWarps;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public HookHandler getHookHandler() {
		return hookHandler;
	}

	public void setHookHandler(HookHandler hookHandler) {
		this.hookHandler = hookHandler;
	}

	public VaultHook getVaultHook() {
		return vaultHook;
	}

	public void setVaultHook(VaultHook vaultHook) {
		this.vaultHook = vaultHook;
	}

	public WorldGuardHook getWorldGuardHook() {
		return worldGuardHook;
	}

	public void setWorldGuardHook(WorldGuardHook worldGuardHook) {
		this.worldGuardHook = worldGuardHook;
	}

	public GriefPreventionHook getGreifProtectionHook() {
		return greifPrevetionHook;
	}

	public void setGreifProtectionHook(GriefPreventionHook greifProtectionHook) {
		this.greifPrevetionHook = greifProtectionHook;
	}

	public ArrayList<PlayerWarpObject> getPlayerWarpObjects() {
		return playerWarpObjects;
	}

	public void setPlayerWarpObjects(ArrayList<PlayerWarpObject> playerWarpObjects) {
		this.playerWarpObjects = playerWarpObjects;
	}

	public ArrayList<String> getNonCriticalErrors() {
		return nonCriticalErrors;
	}

	public void setNonCriticalErrors(ArrayList<String> nonCriticalErrors) {
		this.nonCriticalErrors = nonCriticalErrors;
	}

	public VaultHook getVaultHandler() {
		return vaultHook;
	}

	public void setVaultHandler(VaultHook vaultHandler) {
		this.vaultHook = vaultHandler;
	}

	public static Economy getEcon() {
		return econ;
	}

	public static void setEcon(Economy econ) {
		PlayerWarpGUI.econ = econ;
	}

	public Permission getPerms() {
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
		return messageHandler;
	}

	public void setMessages(MessageHandler messages) {
		this.messageHandler = messages;
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

	public TeleportHandler getTeleportHandler() {
		return teleportHandler;
	}

	public void setTeleportHandler(TeleportHandler teleportHandler) {
		this.teleportHandler = teleportHandler;
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
