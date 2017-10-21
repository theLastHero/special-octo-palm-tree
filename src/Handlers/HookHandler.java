package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;

public class HookHandler {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public HookHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	public boolean checkHook(String pluginName) {
		Plugin pHook = Bukkit.getPluginManager().getPlugin(pluginName);
		if ((pHook != null) && (pHook.isEnabled())) {
			pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", pluginName,
					pl.getLanguageHandler().getMessage("SUCCESS")));
			return true;
		}
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", pluginName,
				pl.getLanguageHandler().getMessage("FAILED")));
		pl.getNonCriticalErrors().add(pl.getLanguageHandler().getMessage("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		return false;
	}

}
