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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Commands.CommandManager;
import PlayerWarpGUI.Handlers.HookHandler;
import PlayerWarpGUI.Handlers.TeleportHandler;
import PlayerWarpGUI.Handlers.WarpHandler;
import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
import PlayerWarpGUI.Hooks.HookManager;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Hooks.VaultHook;
import PlayerWarpGUI.Hooks.WorldGuardHook;
import PlayerWarpGUI.Listeners.ChestListener;
import PlayerWarpGUI.Listeners.GriefPreventionListener;
import PlayerWarpGUI.Listeners.PlayerListener;
import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Others.Metrics;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import net.milkbowl.vault.economy.Economy;

public class PlayerWarpGUI extends JavaPlugin {

	// this plugin
	public static PlayerWarpGUI p;

	// objects
	public static ArrayList<PlayerWarpObject> pwoList = new ArrayList<PlayerWarpObject>();;

	// file paths
	public static String pathMain;
	public static String pathLangs;
	public static String configName;
	public static String pathConfig;
	public static String warpsName;
	public static String pathWarps;

	// plugin details
	public static String pWarpsVersion;
	
	// Config Validation Check
	public boolean noErrorsInConfigFiles = true;
	public static HookHandler hookHandler;
	public WarpHandler warpHandler;
	public static HookManager<?> hookManager;
	public TeleportHandler tpHandler;
	public GriefPreventionListener griefpreventionlistener;

	public Locale locale;
	public ResourceBundle resourceBundle;
	public File langanugeFile;
	public ClassLoader classLoader;
	public MessageFormat messageFormat;

	// others
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
	private static ArrayList<String> criticalErrorList = new ArrayList<String>();
	private static ArrayList<String> nonCriticalErrorList = new ArrayList<String>();

	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {

		try {
			p = this;

			PluginManager pluginManager = getServer().getPluginManager();
			setupFilePaths();

			// others
			CommandManager.registerCommands();
			
			warpHandler = new WarpHandler(p);
			WarpFileUtils.getInstance().checkWarpFolder();
			WarpFileUtils.getInstance().createAllWarpsFromFile();
			tpHandler = new TeleportHandler(this);
			hookHandler = new HookHandler(p);
			// hooks

			setupHooks();
			registerEvents();
			useMetrics();

			// console stuff
			MessageSender.sendTitle();
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
		griefPreventionHook = new GriefPreventionHook();
		redProtectHook = new RedProtectHook();
		worldGuardHook = new WorldGuardHook();
		residenceHook = new ResidenceHook();
		factionsHook = new FactionsHook();
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

	
	public void useMetrics() {
		// metrics
		if (Config.getInstance().getMetricsEnabled()) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				MessageSender.sendConsole("CONSOLE_MSG_METRICS", LocaleLoader.getString("SUCCESS"));
			} catch (IOException e) {
				MessageSender.sendConsole("CONSOLE_MSG_METRICS", LocaleLoader.getString("FAILED"));
				getNonCriticalErrors().add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR"));
			}
		}
	}

	public static void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(PlayerWarpGUI.p);
	}

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

	public void setWarpHandler(WarpHandler warpHandler) {
		this.warpHandler = warpHandler;
	}


	public RedProtectHook getRedProtectHook() {
		return redProtectHook;
	}

	public void setRedProtectHook(RedProtectHook redProtectHook) {
		this.redProtectHook = redProtectHook;
	}

	public String getWarpsName() {
		return warpsName;
	}

	public void setWarpsName(String warpsName) {
		PlayerWarpGUI.warpsName = warpsName;
	}

	public String getPathWarps() {
		return pathWarps;
	}

	public void setPathWarps(String pathWarps) {
		PlayerWarpGUI.pathWarps = pathWarps;
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
		PlayerWarpGUI.hookHandler = hookHandler;
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
		PlayerWarpGUI.pwoList = playerWarpObjects;
	}

	public static ArrayList<String> getNonCriticalErrors() {
		return nonCriticalErrorList;
	}

	public void setNonCriticalErrors(ArrayList<String> nonCriticalErrors) {
		PlayerWarpGUI.nonCriticalErrorList = nonCriticalErrors;
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

	public static ArrayList<String> getCriticalErrors() {
		return criticalErrorList;
	}

	public void setCriticalErrors(ArrayList<String> criticalErrors) {
		PlayerWarpGUI.criticalErrorList = criticalErrors;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		PlayerWarpGUI.configName = configName;
	}

	/*
	 * public ConfigHandler getConfigHandler() { return configHandler; }
	 */

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
		PlayerWarpGUI.pWarpsVersion = pWarpsVersion;
	}

	/*
	 * public void setConfigHandler(ConfigHandler configHandler) {
	 * this.configHandler = configHandler; }
	 */
	public String getPathMain() {
		return pathMain;
	}

	public void setPathMain(String pathMain) {
		PlayerWarpGUI.pathMain = pathMain;
	}

	public String getPathLangs() {
		return pathLangs;
	}

	public void setPathLangs(String pathLangs) {
		PlayerWarpGUI.pathLangs = pathLangs;
	}

	public String getPathConfig() {
		return pathConfig;
	}

	public void setPathConfig(String pathConfig) {
		PlayerWarpGUI.pathConfig = pathConfig;
	}

	public String getthisVersion() {
		return pWarpsVersion;

	}

	public void setthisVersion(String thisVersion) {
		PlayerWarpGUI.pWarpsVersion = thisVersion;
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

	public Plugin getInstance() {
		return this;
	}

}
