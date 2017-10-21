package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;

public class HookHandler {

	private static PlayerWarpGUI pl;
	private String status = "FAILED";

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public HookHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	public boolean checkHook(String pluginName) {
		Plugin pHook = Bukkit.getPluginManager().getPlugin(pluginName);

		if (validateHook(pluginName, pHook)) {
			return true;
		}

		consoleStatusMessage(pluginName);
		addNonCriticalError(pluginName);
		
		return false;
	}

	/**
	 * @param pluginName
	 */
	private void addNonCriticalError(String pluginName) {
		if (status == "FAILED") {
			pl.getNonCriticalErrors()
					.add(pl.getLanguageHandler().getMessage("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}

	/**
	 * @param pluginName
	 */
	private void consoleStatusMessage(String pluginName) {
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", pluginName,
				pl.getLanguageHandler().getMessage(status)));
	}

	/**
	 * @param pluginName
	 * @param pHook
	 */
	private boolean validateHook(String pluginName, Plugin pHook) {
		if ((pHook != null) && (pHook.isEnabled())) {
			status = "SUCCESS";
			return true;
		}
		return false;
	}

}
