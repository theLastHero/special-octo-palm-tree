package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.locale.LocaleLoader;

public abstract class HookManager<OUTPUT> {

	/**
	 * 
	 */
	public boolean isEnabled = false;
	/**
	 * 
	 */
	public String hookedPluginName;
	/**
	 * 
	 */
	private String status = "FAILED";
	/**
	 * 
	 */
	protected boolean isConfigEnabled;
	private Plugin pHook;

	/**
	 * @param hookedPluginName
	 * @param configEnabled
	 */
	HookManager(String hookedPluginName, boolean configEnabled) {
		this.hookedPluginName = hookedPluginName;
		this.isConfigEnabled = configEnabled;
		setup();

	}

	public void displayStatus() {
		setup();
		if (this.isConfigEnabled) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX") + LocaleLoader.getString("CONSOLE_MSG_HOOK", hookedPluginName, status));
		}
	}

	/**
	 * 
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
	 * @param pluginName
	 */
	private void addNonCriticalError(String pluginName) {
		if (status == "FAILED") {
			PlayerWarpGUI.getNonCriticalErrors()
					.add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}


	/**
	 * @param player
	 * @return
	 */
	public abstract String warpHookCheck(Player player);

	/**
	 * @param location
	 * @return
	 */
	public abstract OUTPUT getLocationData(Location location);

	public Plugin getpHook() {
		return pHook;
	}

}
