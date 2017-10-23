package PlayerWarpGUI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import PlayerWarpGUI.Handlers.ConfigHandler;
import PlayerWarpGUI.Handlers.HookHandler;
import PlayerWarpGUI.Handlers.PlayerWarpFileHandler;
import PlayerWarpGUI.Handlers.PlayerWarpObjectHandler;
import PlayerWarpGUI.Handlers.TeleportHandler;
import PlayerWarpGUI.Handlers.WarpHandler;
import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Hooks.VaultHook;
import PlayerWarpGUI.Hooks.WorldGuardHook;
import PlayerWarpGUI.Listeners.ChestListener;
import PlayerWarpGUI.Listeners.CommandListener;
import PlayerWarpGUI.Listeners.PlayerListener;
import PlayerWarpGUI.Objects.GUIObject;
import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Objects.msgSender;
import PlayerWarpGUI.Others.Conversions;
import PlayerWarpGUI.Others.Metrics;
import config.Config;
import locale.LocaleLoader;
import net.milkbowl.vault.economy.Economy;
import com.google.common.base.Charsets;

public class PlayerWarpGUI extends JavaPlugin {

	// this plugin
	public static PlayerWarpGUI p;

	// objects
	public ArrayList<PlayerWarpObject> pwoList = new ArrayList<PlayerWarpObject>();;

	// file paths
	public String pathMain;
	public String pathLangs;
	public String configName;
	public String pathConfig;
	public String warpsName;
	public String pathWarps;

	// plugin details
	public String pWarpsVersion;
	// Config Validation Check
    public boolean noErrorsInConfigFiles = true;
	// handlers
	public LocaleLoader localeLoader;
	public msgSender msgSend;
	//public ConfigHandler configHandler;
	// public MessageHandler msgHandler;
	public HookHandler hookHandler;
	public PlayerWarpObjectHandler pwoHandler;
	public WarpHandler warpHandler;
	public TeleportHandler tpHandler;
	public PlayerWarpFileHandler warpFileHandler;

	public Locale locale;
	public ResourceBundle resourceBundle;
	public File langanugeFile;
	public ClassLoader classLoader;
	public MessageFormat messageFormat;

	// others
	public Conversions conversions;
	public GUIObject guiObject;
	public boolean startup = true;

	// hooks
	public VaultHook vaultHook;
	public WorldGuardHook worldGuardHook;
	public GriefPreventionHook griefPreventionHook;
	public RedProtectHook redProtectHook;
	public ResidenceHook residenceHook;
	public FactionsHook factionsHook;

	// locales

	// econ/perms
	public Economy econ = null;

	// error arrays
	public ArrayList<String> criticalErrorList = new ArrayList<String>();
	public ArrayList<String> nonCriticalErrorList = new ArrayList<String>();

	// +------------------------------------------------------------------------------------
	// | onEnable()
	// +-----------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {

		try {
			p = this;

			PluginManager pluginManager = getServer().getPluginManager();
			setupFilePaths();

			// others
			conversions = new Conversions();
			//localeLoader = new LocaleLoader(this);
			// handlers
			//messageFormat = new MessageFormat("",new Locale("en", "US"));
			//languageHandler = new LanguageHandler(p);
			//languageHandler.loadLanguageFile("en_US");
			// msgHandler = new MessageHandler(p);
			msgSend = new msgSender(p);
			//configHandler = new ConfigHandler(p);

			// create language files
			//languageHandler.checkLanguageFileExsists("en_US");

			//configHandler.setUp();
			//configHandler.msg();

			// handlers
			pwoHandler = new PlayerWarpObjectHandler(p);
			warpFileHandler = new PlayerWarpFileHandler(p);
			warpFileHandler.checkWarpFolder();
			warpFileHandler.createAllWarpsFromFile();
			warpHandler = new WarpHandler(p);
			tpHandler = new TeleportHandler(this);
			hookHandler = new HookHandler(p);
			// hooks

			setupHooks();

			guiObject = new GUIObject(p);

			// listeners
			this.getCommand("pwarps").setExecutor(new CommandListener(p));
			registerEvents();

			// metrics
			useMetrics();

			// console stuff
			p.getMsgSend().sendTitle();
			setStartup(false);
		} catch (Throwable t) {

			if (!(t instanceof ExceptionInInitializerError)) {
				t.printStackTrace();
			} else {
				getLogger().info("Please do not replace jar while the server is running.");
			}

			getServer().getPluginManager().disablePlugin(this);
		}

	}

	/**
	 * 
	 */
	private void setupHooks() {
		vaultHook = new VaultHook(p);
		griefPreventionHook = new GriefPreventionHook(p);
		redProtectHook = new RedProtectHook(p);
		worldGuardHook = new WorldGuardHook(p);
		residenceHook = new ResidenceHook(p);
		factionsHook = new FactionsHook(p);
	}

	/**
	 * 
	 */
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	/**
	 * 
	 */
	private void setupFilePaths() {
		pWarpsVersion = p.getDescription().getVersion();
		pathMain = p.getDataFolder().toString();
		pathLangs = pathMain + File.separator + "languages" + File.separator;
		configName = "config.yml";
		pathConfig = pathMain + File.separator + configName;
		warpsName = "warps";
		pathWarps = pathMain + File.separator + warpsName + File.separator;
	}

	/**
	 * @return the pwoList
	 */
	public ArrayList<PlayerWarpObject> getPwoList() {
		return pwoList;
	}

	/**
	 * @param pwoList
	 *            the pwoList to set
	 */
	public void setPwoList(ArrayList<PlayerWarpObject> pwoList) {
		this.pwoList = pwoList;
	}

	/**
	 * @return the pwarpsVersion
	 */
	public String getPwarpsVersion() {
		return pWarpsVersion;
	}

	/**
	 * @param pwarpsVersion
	 *            the pwarpsVersion to set
	 */
	public void setPwarpsVersion(String pwarpsVersion) {
		this.pWarpsVersion = pwarpsVersion;
	}

	/**
	 * @return the pwoHandler
	 */
	public PlayerWarpObjectHandler getPwoHandler() {
		return pwoHandler;
	}

	/**
	 * @param pwoHandler
	 *            the pwoHandler to set
	 */
	public void setPwoHandler(PlayerWarpObjectHandler pwoHandler) {
		this.pwoHandler = pwoHandler;
	}

	/**
	 * @return the tpHandler
	 */
	public TeleportHandler getTpHandler() {
		return tpHandler;
	}

	/**
	 * @param tpHandler
	 *            the tpHandler to set
	 */
	public void setTpHandler(TeleportHandler tpHandler) {
		this.tpHandler = tpHandler;
	}

	/**
	 * @return the warpFileHandler
	 */
	public PlayerWarpFileHandler getWarpFileHandler() {
		return warpFileHandler;
	}

	/**
	 * @param warpFileHandler
	 *            the warpFileHandler to set
	 */
	public void setWarpFileHandler(PlayerWarpFileHandler warpFileHandler) {
		this.warpFileHandler = warpFileHandler;
	}

	/**
	 * @return the conversions
	 */
	public Conversions getConversions() {
		return conversions;
	}

	/**
	 * @param conversions
	 *            the conversions to set
	 */
	public void setConversions(Conversions conversions) {
		this.conversions = conversions;
	}

	/**
	 * @return the criticalErrorList
	 */
	public ArrayList<String> getCriticalErrorList() {
		return criticalErrorList;
	}

	/**
	 * @param criticalErrorList
	 *            the criticalErrorList to set
	 */
	public void setCriticalErrorList(ArrayList<String> criticalErrorList) {
		this.criticalErrorList = criticalErrorList;
	}

	/**
	 * @return the nonCriticalErrorList
	 */
	public ArrayList<String> getNonCriticalErrorList() {
		return nonCriticalErrorList;
	}

	/**
	 * @param nonCriticalErrorList
	 *            the nonCriticalErrorList to set
	 */
	public void setNonCriticalErrorList(ArrayList<String> nonCriticalErrorList) {
		this.nonCriticalErrorList = nonCriticalErrorList;
	}

	// +-----------------------------------------------------------------------------------
	// | useMetrics()
	// +-----------------------------------------------------------------------------------
	public void useMetrics() {
		// metrics
		if (Config.getInstance().getMetricsEnabled()) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				p.getMsgSend().sendConsole("CONSOLE_MSG_METRICS", LocaleLoader.getString("SUCCESS"));
			} catch (IOException e) {
				p.getMsgSend().sendConsole("CONSOLE_MSG_METRICS", LocaleLoader.getString("FAILED"));
				getNonCriticalErrors().add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR"));
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
	 * @param startup
	 *            the startup to set
	 */
	public void setStartup(boolean startup) {
		this.startup = startup;
	}

	/**
	 * @param playerWarpFileHandler
	 *            the playerWarpFileHandler to set
	 */
	public void setPlayerWarpFileHandler(PlayerWarpFileHandler playerWarpFileHandler) {
		this.warpFileHandler = playerWarpFileHandler;
	}

	/**
	 * @param residenceHook
	 *            the residenceHook to set
	 */
	public void setResidenceHook(ResidenceHook residenceHook) {
		this.residenceHook = residenceHook;
	}

	/**
	 * @param factionsHook
	 *            the factionsHook to set
	 */
	public void setFactionsHook(FactionsHook factionsHook) {
		this.factionsHook = factionsHook;
	}

	/**
	 * @return
	 */
	public FactionsHook getFactionsHook() {
		return factionsHook;
	}

	/**
	 * @return
	 */
	public GriefPreventionHook getGriefPreventionHook() {
		return griefPreventionHook;
	}

	/**
	 * @param griefPreventionHook
	 */
	public void setGriefPreventionHook(GriefPreventionHook griefPreventionHook) {
		this.griefPreventionHook = griefPreventionHook;
	}

	/**
	 * @return
	 */
	public ResidenceHook getResidenceHook() {
		return residenceHook;
	}

	/**
	 * @return
	 */
	public PlayerWarpFileHandler getPlayerWarpFileHandler() {
		return warpFileHandler;
	}

	/**
	 * @return
	 */
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
		return pwoHandler;
	}

	public void setPlayerWarpObjectHandler(PlayerWarpObjectHandler playerWarpObjectHandler) {
		this.pwoHandler = playerWarpObjectHandler;
	}

	public Conversions getOtherFunctions() {
		return this.conversions;
	}

	public void setOtherFunctions(Conversions otherFunctions) {
		this.conversions = otherFunctions;
	}

	public RedProtectHook getRedProtectHook() {
		return redProtectHook;
	}

	public void setRedProtectHook(RedProtectHook redProtectHook) {
		this.redProtectHook = redProtectHook;
	}

	public Conversions getCalc() {
		return conversions;
	}

	public void setCalc(Conversions calc) {
		this.conversions = calc;
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

	/*
	 * public MessageHandler getMessageHandler() { return msgHandler; }
	 * 
	 * public void setMessageHandler(MessageHandler messageHandler) {
	 * this.msgHandler = messageHandler; }
	 */
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
		return pwoList;
	}

	public void setPlayerWarpObjects(ArrayList<PlayerWarpObject> playerWarpObjects) {
		this.pwoList = playerWarpObjects;
	}

	public ArrayList<String> getNonCriticalErrors() {
		return nonCriticalErrorList;
	}

	public void setNonCriticalErrors(ArrayList<String> nonCriticalErrors) {
		this.nonCriticalErrorList = nonCriticalErrors;
	}

	public VaultHook getVaultHandler() {
		return vaultHook;
	}

	public void setVaultHandler(VaultHook vaultHandler) {
		this.vaultHook = vaultHandler;
	}

	public Economy getEcon() {
		return econ;
	}

	public void setEcon(Economy econ) {
		this.econ = econ;
	}

	public ArrayList<String> getCriticalErrors() {
		return criticalErrorList;
	}

	public void setCriticalErrors(ArrayList<String> criticalErrors) {
		this.criticalErrorList = criticalErrors;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

/*	public ConfigHandler getConfigHandler() {
		return configHandler;
	}*/

	/**
	 * @return the pWarpsVersion
	 */
	public String getpWarpsVersion() {
		return pWarpsVersion;
	}

	/**
	 * @param pWarpsVersion
	 *            the pWarpsVersion to set
	 */
	public void setpWarpsVersion(String pWarpsVersion) {
		this.pWarpsVersion = pWarpsVersion;
	}

	/**
	 * @return the msgSend
	 */
	public msgSender getMsgSend() {
		return this.msgSend;
	}

	/**
	 * @param msgSend
	 *            the msgSend to set
	 */
	public void setMsgSend(msgSender msgSend) {
		this.msgSend = msgSend;
	}

/*	public void setConfigHandler(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}
*/
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

	public String getthisVersion() {
		return pWarpsVersion;

	}

	public void setthisVersion(String thisVersion) {
		this.pWarpsVersion = thisVersion;
	}

	public TeleportHandler getTeleportHandler() {
		return tpHandler;
	}

	public void setTeleportHandler(TeleportHandler teleportHandler) {
		this.tpHandler = teleportHandler;
	}

	/**
	 * @return the languageFormat
	 */
	public MessageFormat getLanguageFormat() {
		return messageFormat;
	}

	/**
	 * @param languageFormat
	 *            the languageFormat to set
	 */
	public void setLanguageFormat(MessageFormat languageFormat) {
		this.messageFormat = languageFormat;
	}

    public InputStreamReader getResourceAsReader(String fileName) {
        InputStream in = getResource(fileName);
        return in == null ? null : new InputStreamReader(in, Charsets.UTF_8);
}
}
