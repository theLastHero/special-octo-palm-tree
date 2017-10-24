package PlayerWarpGUI.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.locale.LocaleLoader;

public class HookHandler {

	private static PlayerWarpGUI p;
	private String status = "FAILED";
	
	public HookHandler(PlayerWarpGUI pl) {
		HookHandler.p = pl;
		//ignore error trash line
		p.getEcon();
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
			PlayerWarpGUI.getNonCriticalErrors()
					.add(LocaleLoader.getString("CONSOLE_NONCRITIAL_ERROR_HOOK", pluginName));
		}
	}

	/**
	 * @param pluginName
	 */
	private void consoleStatusMessage(String pluginName) {
		MessageSender.sendConsole("CONSOLE_MSG_HOOK", pluginName,
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
