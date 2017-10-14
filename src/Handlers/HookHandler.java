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

	  public boolean checkHook(String pluginName)
	  {
	    Plugin pHook = Bukkit.getPluginManager().getPlugin(pluginName);
	    if ((pHook != null) && (pHook.isEnabled())) {
	    	pl.messages.sendConsoleMessage("Hooking "+pluginName+" >> Success");
	    	return true;
	    }
	    pl.messages.sendConsoleMessage("Hooking "+pluginName+" >> Failed");
	    pl.getNonCriticalErrors().add(pluginName+" was not found. Disabling "+pluginName+" support. ");
	    return false;
	  }
	
}
