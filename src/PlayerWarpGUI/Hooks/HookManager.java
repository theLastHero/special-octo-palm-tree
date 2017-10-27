package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.locale.LocaleLoader;

/**
* Abstract.framework class for hooked classes.<br>
*  
* @author Judgetread
* @version 1.0
*/
public abstract class HookManager<OUTPUT> {

	/**
	 * is this hook enabled
	 */
	public boolean isEnabled = false;
	/**
	 * this plugin hooks name
	 */
	public String hookedPluginName;
	/**
	 * status String. Failed default.
	 */
	private String status = "FAILED";
	/**
	 * is this hoook enabled in config.
	 */
	protected boolean isConfigEnabled;
	/**
	 * Plugin
	 */
	private Plugin pHook;

	/**
	 * Constructor. Creates theis object.
	 * @param hookedPluginName
	 * @param configEnabled
	 */
	HookManager(String hookedPluginName, boolean configEnabled) {
		this.hookedPluginName = hookedPluginName;
		this.isConfigEnabled = configEnabled;
		setup();

	}

	/**
	 * Prints status of hook attempt to console in enabled in config.
	 */
	public void displayStatus() {
		setup();
		if (this.isConfigEnabled) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX") + LocaleLoader.getString("CONSOLE_MSG_HOOK", hookedPluginName, status));
		}
	}

	/**
	 * Try hooking plugin. Set errors and status messages.
	 * If failed add to NonCritialError list
	 */

	public void setup() {
		if (this.isConfigEnabled) {
			Plugin pHook = Bukkit.getPluginManager().getPlugin(hookedPluginName);

			if ((pHook != null) && (pHook.isEnabled())) {
				status = "SUCCESS";
				this.isEnabled = true;
				this.pHook = pHook;
			} else {
				this.isEnabled = false;
				addNonCriticalError(hookedPluginName);
			}
		}

	}

	/**
	 * Adds error string to NonCritialError list
	 * @param pluginName
	 */
	private void addNonCriticalError(String pluginName) {
		if (status == "FAILED") {
			PlayerWarpGUI.getNonCriticalErrors()
					.add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}


	/**
	 * Provided to be overridden.
	 * 
	 * @param player a Player.
	 * @return String
	 */
	public abstract String warpHookCheck(Player player);

	/**
	 * Provided to be overridden.
	 * 
	 * @param location must be a bukkit location
	 * @return OUTPUT
	 */
	public abstract OUTPUT getLocationData(Location location);

	/**
	 * Returns Plugin instance that is hooked.
	 * 
	 * @return Plugin
	 */
	public Plugin getpHook() {
		return pHook;
	}

}
