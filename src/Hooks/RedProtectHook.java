package Hooks;

import PlayerWarpGUI.PlayerWarpGUI;

public class RedProtectHook {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public RedProtectHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	private void check() {
		if (pl.getConfig().getBoolean("RedProtect.enabled")) {
			setEnabled(pl.hookHandler.checkHook("RedProtect"));
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
