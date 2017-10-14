package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;

public class WorldGuardHook {
	
	public static PlayerWarpGUI pl;
	public boolean enabled = false;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public WorldGuardHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}
	
	  private void check()
	  {
		  if(pl.getConfig().getBoolean("WorldGuard.enabled")) {
				setEnabled(pl.hookHandler.checkHook("WorldGuard"));
			}
	  }

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
