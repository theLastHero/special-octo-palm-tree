package PlayerWarpGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import Hooks.FactionsHook;
import Hooks.GriefPreventionHook;
import Hooks.RedProtectHook;
import Hooks.ResidenceHook;
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

	// this plugin
	public static PlayerWarpGUI plugin;

	// objects
	public static ArrayList<PlayerWarpObject> playerWarpObjects = new ArrayList<PlayerWarpObject>();
	
	// file paths
	public String pathMain = this.getDataFolder().toString();
	public String pathLangs = pathMain + File.separator + "languages" + File.separator;
	public String configName = "config.yml";
	public String pathConfig = pathMain + File.separator + configName;
	public String warpsName = "warps";
	public String pathWarps = pathMain + File.separator + warpsName + File.separator;

	// plugin details
	public PluginDescriptionFile pdf = this.getDescription(); // Gets plugin.yml
	public String playerWarpGUIVersion = pdf.getVersion();
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
	public boolean startup = true;

	// hooks
	public VaultHook vaultHook;
	public WorldGuardHook worldGuardHook;
	public GriefPreventionHook griefPreventionHook;
	public RedProtectHook redProtectHook;
	public ResidenceHook residenceHook;
	public FactionsHook factionsHook;

	// locales
	public Locale locale;
	public ResourceBundle resourceBundle;
	public File langanugeFile;
	public ClassLoader classLoader;
	public MessageFormat languageFormat;
	public LanguageHandler languageHandler;

	// econ/perms
	public static Economy econ = null;
	public static Permission perms = null;

	// error arrays
	public ArrayList<String> criticalErrors = new ArrayList<String>();
	public ArrayList<String> nonCriticalErrors = new ArrayList<String>();

	// +------------------------------------------------------------------------------------
	// | onEnable()
	// +-----------------------------------------------------------------------------------
	@Override
	public void onEnable() {

		// this plugin
		setPlugin(this);

		// listeners
		this.getCommand("pwarps").setExecutor(new CommandListener(this));
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

		// handlers
		languageHandler = new LanguageHandler(this);
		messageHandler = new MessageHandler(this);
		configHandler = new ConfigHandler(this);

		//create language files
		getLanguageHandler().checkLanguageFileExsists("en_US");

		configHandler.setUp();
		languageHandler.loadLanguageFile(getConfig().getString("language"));
		configHandler.msg();
		
		// handlers
		playerWarpObjectHandler = new PlayerWarpObjectHandler(this);
		playerWarpFileHandler = new PlayerWarpFileHandler(this);
		playerWarpFileHandler.checkWarpFolder();
		playerWarpFileHandler.createAllWarpsFromFile();
		
		// hooks
		vaultHook = new VaultHook(this);
		griefPreventionHook = new GriefPreventionHook(this);
		redProtectHook = new RedProtectHook(this);
		worldGuardHook = new WorldGuardHook(this);
		residenceHook = new ResidenceHook(this);
		factionsHook = new FactionsHook(this);

		// metrics
		useMetrics();

		// console stuff
		messageHandler.sendTitle();
		setStartup(false);

	}

	// +-----------------------------------------------------------------------------------
	// | useMetrics()
	// +-----------------------------------------------------------------------------------
	private void useMetrics() {
		// metrics
		if (getConfig().getBoolean("metrics.enabled") == true) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				messageHandler.sendConsoleMessage(
						languageHandler.getMessage("CONSOLE_MSG_METRICS", languageHandler.getMessage("SUCCESS")));
			} catch (IOException e) {
				messageHandler.sendConsoleMessage(
						languageHandler.getMessage("CONSOLE_MSG_METRICS", languageHandler.getMessage("FAILED")));
				getNonCriticalErrors().add(languageHandler.getMessage("CONSOLE_NONCRITIAL_ERROR"));
			}
		}
	}
	
	// +-----------------------------------------------------------------------------------
	// | killPlugin()
	// +-----------------------------------------------------------------------------------
	public void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	// +-----------------------------------------------------------------------------------
	// | Getters/Setters
	// +-----------------------------------------------------------------------------------


	/**
	 * @return the startup
	 */
	public boolean isStartup() {
		return startup;
	}

	/**
	 * @param startup the startup to set
	 */
	public void setStartup(boolean startup) {
		this.startup = startup;
	}
	
	/**
	 * @param pdf the pdf to set
	 */
	public void setPdf(PluginDescriptionFile pdf) {
		this.pdf = pdf;
	}

	/**
	 * @param playerWarpFileHandler the playerWarpFileHandler to set
	 */
	public void setPlayerWarpFileHandler(PlayerWarpFileHandler playerWarpFileHandler) {
		this.playerWarpFileHandler = playerWarpFileHandler;
	}

	/**
	 * @param residenceHook the residenceHook to set
	 */
	public void setResidenceHook(ResidenceHook residenceHook) {
		this.residenceHook = residenceHook;
	}

	/**
	 * @param factionsHook the factionsHook to set
	 */
	public void setFactionsHook(FactionsHook factionsHook) {
		this.factionsHook = factionsHook;
	}

	public FactionsHook getFactionsHook() {
		return factionsHook;
	}

	public GriefPreventionHook getGriefPreventionHook() {
		return griefPreventionHook;
	}

	public void setGriefPreventionHook(GriefPreventionHook griefPreventionHook) {
		this.griefPreventionHook = griefPreventionHook;
	}

	public ResidenceHook getResidenceHook() {
		return residenceHook;
	}

	public PlayerWarpFileHandler getPlayerWarpFileHandler() {
		return playerWarpFileHandler;
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

	public void setEcon(Economy econ) {
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

	public String getPlayerWarpGUIVersion() {
		return playerWarpGUIVersion;
	}

	public void setPlayerWarpGUIVersion(String playerWarpGUIVersion) {
		this.playerWarpGUIVersion = playerWarpGUIVersion;
	}

	public TeleportHandler getTeleportHandler() {
		return teleportHandler;
	}

	public void setTeleportHandler(TeleportHandler teleportHandler) {
		this.teleportHandler = teleportHandler;
	}

	public PlayerWarpGUI getPlugin() {
		return plugin;
	}

	public void setPlugin(PlayerWarpGUI plugin) {
		PlayerWarpGUI.plugin = plugin;
	}

}
