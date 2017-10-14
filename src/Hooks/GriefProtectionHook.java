package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;

public class GriefProtectionHook {
	
	public static PlayerWarpGUI pl;
	public boolean enabled = false;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GriefProtectionHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	public void check() {
		if(pl.getConfig().getBoolean("GriefProtection.enabled")) {
			setEnabled(pl.hookHandler.checkHook("GriefProtection"));
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
