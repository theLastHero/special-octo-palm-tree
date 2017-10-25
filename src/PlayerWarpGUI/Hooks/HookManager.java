package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

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
	private boolean isConfigEnabled;

	/**
	 * @param hookedPluginName
	 * @param configEnabled
	 */
	public HookManager(String hookedPluginName, boolean configEnabled) {
		this.hookedPluginName = hookedPluginName;
		this.isConfigEnabled = configEnabled;
		setup();

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
			} else {
				this.isEnabled = false;
				addNonCriticalError(hookedPluginName);
			}
			consoleStatusMessage(hookedPluginName);
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
	 * @param pluginName
	 */
	private void consoleStatusMessage(String pluginName) {
		MessageSender.sendConsole("CONSOLE_MSG_HOOK", pluginName, LocaleLoader.getString(status));
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

}
