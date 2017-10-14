package Handlers;

import org.bukkit.Bukkit;


import PlayerWarpGUI.PlayerWarpGUI;

public class VaultHandler {
	
	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public VaultHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}
	

	// +-----------------------------------------------------------------------------------
	// | setupVaultHook
	// +-----------------------------------------------------------------------------------
	public void setupVaultHook() {

		String status = pl.getMessageHandler().getMessage("PASSED"); //$NON-NLS-1$
		
		if (pl.getServer().getPluginManager().getPlugin("Vault") == null) { //$NON-NLS-1$
			status = pl.getMessageHandler().getMessage("FAILED"); //$NON-NLS-1$
			pl.errorHandler.addError(pl.getMessageHandler().getMessage("VAULT_HOOK_FAILED")); //$NON-NLS-1$
		}
		
		Bukkit.getLogger().info(pl.getMessageHandler().getMessage("VAULT_HOOK_STATUS") + status); //$NON-NLS-1$
	}

}
