package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.locale.LocaleLoader;

public abstract class HookManager<OUTPUT> {

	public boolean isEnabled = false;
	public String hookedPluginName;
	private String status = "FAILED";
	private boolean isConfigEnabled;

	public HookManager(String hookedPluginName, boolean configEnabled) {
		this.hookedPluginName = hookedPluginName;
		this.isConfigEnabled = configEnabled;
		setup();

	}


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

	private void addNonCriticalError(String pluginName) {
		if (status == "FAILED") {
			PlayerWarpGUI.getNonCriticalErrors()
					.add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}

	private void consoleStatusMessage(String pluginName) {
		MessageSender.sendConsole("CONSOLE_MSG_HOOK", pluginName, LocaleLoader.getString(status));
	}

	public abstract String warpHookCheck(Player player);

	public abstract OUTPUT getLocationData(Location location);

}
