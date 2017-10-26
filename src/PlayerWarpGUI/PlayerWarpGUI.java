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
	public static String playerwarpsguiVersion;
	
	// Config Validation Check
	public boolean noErrorsInConfigFiles = true;
	public WarpHandler warpHandler;
	public static HookManager<?> hookManager;
	public TeleportHandler tpHandler;
	public CommandManager commandManager;

	// others
	public boolean startup = true;

	// econ/perms
	public Economy econ = null;

	// error arrays
	public static ArrayList<String> criticalErrorList = new ArrayList<String>();
	public static ArrayList<String> nonCriticalErrorList = new ArrayList<String>();

	
	@Override
	public void onEnable() {

		try {
			p = this;
			PluginManager pluginManager = getServer().getPluginManager();
			setupFilePaths();
			
			warpHandler = new WarpHandler(p);
			WarpFileUtils.getInstance().checkWarpFolder();
			WarpFileUtils.getInstance().createAllWarpsFromFile();
			tpHandler = new TeleportHandler(this);
			commandManager = new CommandManager();
			CommandManager.registerCommands();
			
			
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
		pathLangs = pathMain + File.separator + "languages" + File.separator;
		configName = "config.yml";
		pathConfig = pathMain + File.separator + configName;
		warpsName = "warps";
		pathWarps = pathMain + File.separator + warpsName + File.separator;
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
	
	public boolean isStartup() {
		return startup;
	}
	
	public void setStartup(boolean startup) {
		this.startup = startup;
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

	public Economy getEcon() {
		return econ;
	}

	public void setEcon(Economy econ) {
		this.econ = econ;
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

}
