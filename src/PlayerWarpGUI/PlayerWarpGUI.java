package PlayerWarpGUI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Commands.CommandManager;
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

	/** Contains verison number of the plugin, taken from pugin.yml  */
	public static String playerwarpsguiVersion;
	
	/** Config Validation Check */
	private boolean noErrorsInConfigFiles = true;
	private WarpHandler warpHandler;
	private TeleportHandler tpHandler;
	public CommandManager commandManager;

	// error arrays
	public static ArrayList<String> criticalErrorList = new ArrayList<String>();
	public static ArrayList<String> nonCriticalErrorList = new ArrayList<String>();

	
	@Override
	public void onEnable() {

		try {
			p = this;
			getServer().getPluginManager();
			setupFilePaths();
			
			setWarpHandler(new WarpHandler(p));
			
			
			WarpFileUtils.getInstance().checkWarpFolder();
			WarpFileUtils.getInstance().createAllWarpsFromFile();
			
			setTpHandler(new TeleportHandler(this));
			commandManager = new CommandManager();
			CommandManager.registerCommands();
			
			
			setupHooks();
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
	
	public void setupHooks() {
		new GriefPreventionHook().displayStatus();
		new FactionsHook().displayStatus();
		new RedProtectHook().displayStatus();
		new ResidenceHook().displayStatus();
		new WorldGuardHook().displayStatus();
		new VaultHook().displayStatus();
	}

	/**
	 * 
	 */
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GriefPreventionListener(this), this);
	}

	/**
	 * 
	 */
	private void setupFilePaths() {
		playerwarpsguiVersion = p.getDescription().getVersion();
		pathMain = p.getDataFolder().toString();
		setPathLangs(pathMain + File.separator + "languages" + File.separator);
		configName = "config.yml";
		setPathConfig(pathMain + File.separator + configName);
		warpsName = "warps";
		setPathWarps(pathMain + File.separator + warpsName + File.separator);
	}

	
	private void useMetrics() {
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
	
	public ArrayList<PlayerWarpObject> getPlayerWarpObjects() {
		return getPwoList();
	}

	public void setPlayerWarpObjects(ArrayList<PlayerWarpObject> playerWarpObjects) {
		PlayerWarpGUI.setPwoList(playerWarpObjects);
	}

	public static ArrayList<String> getNonCriticalErrors() {
		return nonCriticalErrorList;
	}


	public static ArrayList<String> getCriticalErrors() {
		return criticalErrorList;
	}

	public InputStreamReader getResourceAsReader(String fileName) {
		InputStream in = getResource(fileName);
		return in == null ? null : new InputStreamReader(in, Charsets.UTF_8);
	}

	public Plugin getInstance() {
		return this;
	}

	/**
	 * @return the pathConfig
	 */
	public static String getPathConfig() {
		return pathConfig;
	}

	/**
	 * @param pathConfig the pathConfig to set
	 */
	public static void setPathConfig(String pathConfig) {
		PlayerWarpGUI.pathConfig = pathConfig;
	}

	/**
	 * @return the pathWarps
	 */
	public static String getPathWarps() {
		return pathWarps;
	}

	/**
	 * @param pathWarps the pathWarps to set
	 */
	public static void setPathWarps(String pathWarps) {
		PlayerWarpGUI.pathWarps = pathWarps;
	}

	/**
	 * @return the pathLangs
	 */
	public static String getPathLangs() {
		return pathLangs;
	}

	/**
	 * @param pathLangs the pathLangs to set
	 */
	public static void setPathLangs(String pathLangs) {
		PlayerWarpGUI.pathLangs = pathLangs;
	}

	/**
	 * @return the pwoList
	 */
	public static ArrayList<PlayerWarpObject> getPwoList() {
		return pwoList;
	}

	/**
	 * @param pwoList the pwoList to set
	 */
	public static void setPwoList(ArrayList<PlayerWarpObject> pwoList) {
		PlayerWarpGUI.pwoList = pwoList;
	}

	/**
	 * @return the noErrorsInConfigFiles
	 */
	public boolean isNoErrorsInConfigFiles() {
		return noErrorsInConfigFiles;
	}

	/**
	 * @param noErrorsInConfigFiles the noErrorsInConfigFiles to set
	 */
	public void setNoErrorsInConfigFiles(boolean noErrorsInConfigFiles) {
		this.noErrorsInConfigFiles = noErrorsInConfigFiles;
	}

	/**
	 * @return the warpHandler
	 */
	public WarpHandler getWarpHandler() {
		return warpHandler;
	}

	/**
	 * @param warpHandler the warpHandler to set
	 */
	public void setWarpHandler(WarpHandler warpHandler) {
		this.warpHandler = warpHandler;
	}

	/**
	 * @return the tpHandler
	 */
	public TeleportHandler getTpHandler() {
		return tpHandler;
	}

	/**
	 * @param tpHandler the tpHandler to set
	 */
	public void setTpHandler(TeleportHandler tpHandler) {
		this.tpHandler = tpHandler;
	}

}
