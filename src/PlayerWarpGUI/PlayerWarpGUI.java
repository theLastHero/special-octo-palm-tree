package PlayerWarpGUI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Commands.CommandManager;
import PlayerWarpGUI.Handlers.TeleportHandler;
import PlayerWarpGUI.Handlers.WarpHandler;
import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
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

/**
* PlayerWarpGUI
* 
* <P>Bukkit/Spigot plugin. Allowing players to to
* there own warp, and using a gui interface to show warps.
*  
* @author Judgetread
* @version 1.0
*/

public class PlayerWarpGUI extends JavaPlugin {

	 /** Return instance of plugin.  */
	public static PlayerWarpGUI p;

	/** List of all warps objects in an ArrayList.  */
	private static ArrayList<PlayerWarpObject> pwoList = new ArrayList<PlayerWarpObject>();;

	/** Paths. */
	/**========*/
	
	/** Plugin folder path.  */
	private static String pathMain;
	/** Locale folder path.  */
	private static String pathLangs;
	/** Name of config file.  */
	private static String configName;
	/** Path of config file.  */
	private static String pathConfig;
	/** Folder name, where player warp files are located/saved.  */
	private static String warpsName;
	/** Full path of warp Files location.  */
	private static String pathWarps;

	/** Value Contains verison number of the plugin, taken from pugin.yml  */
	public static String playerwarpsguiVersion;
	
	/** Config Validation Check */
	private boolean noErrorsInConfigFiles = true;
	/** Warphandler */
	private WarpHandler warpHandler;
	/** TeleportHandler */
	private TeleportHandler tpHandler;
	/** CommandManager */
	private CommandManager commandManager;

	/** Holds list of critical error at startup */
	private static ArrayList<String> criticalErrorList = new ArrayList<String>();
	/** Holds list of NONcritical error at startup */
	private static ArrayList<String> nonCriticalErrorList = new ArrayList<String>();

	
	@Override
	public void onEnable() {

		try {
			p = this;
			
			getServer().getPluginManager();
			
			setupFilePaths();
			
			setWarpHandler(new WarpHandler());

			setTpHandler(new TeleportHandler());
			setCommandManager(new CommandManager());
			CommandManager.registerCommands();
			
			WarpFileUtils.getInstance().checkCreateWarpFolder();
			WarpFileUtils.getInstance().createAllWarpsFromFile();
			
			
			
			startUpHookTests();
			
			registerEvents();
			
			useMetrics();

			// console stuff
			MessageSender.sendTitle();

		} catch (Throwable t) {

			if (!(t instanceof ExceptionInInitializerError)) {
				t.printStackTrace();
			} else {
				getLogger().info("Please do not replace jar while the server is running.");
				killPlugin();
			}

			killPlugin();
		}

	}
	
	/** Display status of each hook on startup */
	private void startUpHookTests() {
		new GriefPreventionHook().displayStatus();
		new FactionsHook().displayStatus();
		new RedProtectHook().displayStatus();
		new ResidenceHook().displayStatus();
		new WorldGuardHook().displayStatus();
		new VaultHook().displayStatus();
	}

	/** Register Listeners */
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GriefPreventionListener(), this);
	}

	/** setupFilePaths */
	private void setupFilePaths() {
		playerwarpsguiVersion = p.getDescription().getVersion();
		pathMain = p.getDataFolder().toString();
		setPathLangs(pathMain + File.separator + "languages" + File.separator);
		configName = "config.yml";
		setPathConfig(pathMain + File.separator + configName);
		warpsName = "warps";
		setPathWarps(pathMain + File.separator + warpsName + File.separator);
	}

	/** useMetrics */
	private void useMetrics() {
		// metrics
		if (Config.getInstance().getMetricsEnabled()) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_METRICS", LocaleLoader.getString("SUCCESS")));
			} catch (IOException e) {
				MessageSender.sendConsole("CONSOLE_MSG_METRICS", LocaleLoader.getString("FAILED"));
				getNonCriticalErrors().add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR"));
			}
		}
	}

	/** Instantly kill this plugin */
	public static void killPlugin() {
		Bukkit.getPluginManager().disablePlugin(PlayerWarpGUI.p);
	}
	
	/**
	 * Returns complete list of loaded PlayerWarpObjects
	 * 
	 * @return ArrayList of PlayerWarpObject
	 */
	public ArrayList<PlayerWarpObject> getPlayerWarpObjects() {
		return getPwoList();
	}

	/**
	 * Returns list of non critical errors
	 * 
	 * @return ArrayList of String of non critical errors
	 */
	public static ArrayList<String> getNonCriticalErrors() {
		return getNonCriticalErrorList();
	}

	/**
	 * Returns list of critical errors.
	 * 
	 * @return ArrayList of String of critical errors.
	 */
	public static ArrayList<String> getCriticalErrors() {
		return getCriticalErrorList();
	}

	/**
	 * Returns stream from filename.
	 * 
	 * @return InputStreamReader
	 */
	public InputStreamReader getResourceAsReader(String fileName) {
		InputStream in = getResource(fileName);
		return in == null ? null : new InputStreamReader(in, Charsets.UTF_8);
	}

	/**
	 * Returns plugin instance.
	 * 
	 * @return Plugin
	 */
	public Plugin getInstance() {
		return this;
	}

	/**
	 * Returns the location of the config file path
	 * 
	 * @return pathConfig value.
	 */
	public static String getPathConfig() {
		return pathConfig;
	}

	/**
	 * Set the location of the config file.
	 * 
	 * @param pathConfig the pathConfig to set
	 */
	public static void setPathConfig(String pathConfig) {
		PlayerWarpGUI.pathConfig = pathConfig;
	}

	/**
	 * Returns the location of the warps files.
	 * 
	 * @return the pathWarps
	 */
	public static String getPathWarps() {
		return pathWarps;
	}

	/**
	 * Set the path of the warp file location.
	 * 
	 * @param pathWarps the pathWarps to set
	 */
	public static void setPathWarps(String pathWarps) {
		PlayerWarpGUI.pathWarps = pathWarps;
	}

	/**
	 * Returns the location of the locale files.
	 * 
	 * @return the pathLangs
	 */
	public static String getPathLangs() {
		return pathLangs;
	}

	/**
	 * Sets the location of the locale files
	 * 
	 * @param pathLangs the pathLangs to set
	 */
	public static void setPathLangs(String pathLangs) {
		PlayerWarpGUI.pathLangs = pathLangs;
	}

	/**
	 * Returns complete list of loaded PlayerWarpObjects
	 * 
	 * @return ArrayList of PlayerWarpObject
	 */
	public static ArrayList<PlayerWarpObject> getPwoList() {
		return pwoList;
	}

	/**
	 * Returns true/false if any errors are found in config file.
	 * 
	 * @return the noErrorsInConfigFiles
	 */
	public boolean isNoErrorsInConfigFiles() {
		return noErrorsInConfigFiles;
	}

	/**
	 * Set true/false when any errors are found in config file.
	 * 
	 * @param noErrorsInConfigFiles the noErrorsInConfigFiles to set
	 */
	public void setNoErrorsInConfigFiles(boolean noErrorsInConfigFiles) {
		this.noErrorsInConfigFiles = noErrorsInConfigFiles;
	}

	/**
	 * Gets WarpHandler instance.
	 * 
	 * @return the warpHandler
	 */
	public WarpHandler getWarpHandler() {
		return warpHandler;
	}

	/**
	 * Sets WarpHandler instance.
	 * 
	 * @param warpHandler the warpHandler to set
	 */
	public void setWarpHandler(WarpHandler warpHandler) {
		this.warpHandler = warpHandler;
	}

	/**
	 * Gets TeleportHandler instance.
	 * 
	 * @return the tpHandler
	 */
	public TeleportHandler getTpHandler() {
		return tpHandler;
	}

	/**
	 * Sets TeleportHandler instance.
	 * 
	 * @param tpHandler the tpHandler to set
	 */
	public void setTpHandler(TeleportHandler tpHandler) {
		this.tpHandler = tpHandler;
	}

	/**
	 * Gets the CommandManger instance.
	 * 
	 * @return the commandManager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	/**
	 * Sets the CommandManger instance.
	 * 
	 * @param commandManager the commandManager to set
	 */
	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	/**
	 * Returns list of critical errors
	 * 
	 * @return criticalErrorList ArrayList of String
	 */
	public static ArrayList<String> getCriticalErrorList() {
		return criticalErrorList;
	}

	/**
	 * Sets the  list of critical errors
	 * 
	 * @param criticalErrorList ArrayList of String
	 */
	public static void setCriticalErrorList(ArrayList<String> criticalErrorList) {
		PlayerWarpGUI.criticalErrorList = criticalErrorList;
	}

	/**
	 * Returns list of non critical errors
	 * 
	 * @return ArrayList of String of non critical errors
	 */
	public static ArrayList<String> getNonCriticalErrorList() {
		return nonCriticalErrorList;
	}

	/**
	 * Sets list of non critical errors
	 * 
	 * @param nonCriticalErrorList ArrayList of String
	 */
	public static void setNonCriticalErrorList(ArrayList<String> nonCriticalErrorList) {
		PlayerWarpGUI.nonCriticalErrorList = nonCriticalErrorList;
	}

}
