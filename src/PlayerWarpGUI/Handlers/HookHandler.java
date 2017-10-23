package PlayerWarpGUI.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import locale.LocaleLoader;

public class HookHandler {

	private static PlayerWarpGUI p;
	private String status = "FAILED";
	
	public HookHandler(PlayerWarpGUI p) {
		HookHandler.p = p;
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
			p.getNonCriticalErrors()
					.add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}

	/**
	 * @param pluginName
	 */
	private void consoleStatusMessage(String pluginName) {
		p.getMsgSend().sendConsole("CONSOLE_MSG_HOOK", pluginName,
				LocaleLoader.getString(status));
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
