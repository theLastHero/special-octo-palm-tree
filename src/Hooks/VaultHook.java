package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import PlayerWarpGUI.PlayerWarpGUI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {

	public String plName;
	public static PlayerWarpGUI pl;
	public static Economy econ = null;

	/**
	 * @param playerWarpGUI
	 */
	public VaultHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		plName = "Vault";
		checkVault();
	}

	/**
	 * 
	 */
	private void checkVault() {
		Plugin pVT = Bukkit.getPluginManager().getPlugin(plName);
		if ((pVT != null) && (pVT.isEnabled())) {
			setupEconomy();
			consoleMsgPassed(plName);
			return;
		}
		consoleMsgError(plName);
	}

	/**
	 * 
	 */
	private void consoleMsgPassed(String plName) {
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", plName,
				pl.getLanguageHandler().getMessage("SUCCESS")));
	}

	/**
	 * 
	 */
	private void consoleMsgError(String plName) {
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", plName,
				pl.getLanguageHandler().getMessage("FAILED")));
		pl.getCriticalErrors().add(pl.getLanguageHandler().getMessage("CONSOLE_CRITIAL_ERROR_HOOK", plName));
	}

	/**
	 * @return
	 */
	private boolean setupEconomy() {
		if (pl.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = pl.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		pl.setEcon(rsp.getProvider());
		return pl.getEcon() != null;
	}
	/*
	 * private boolean setupPermissions() { RegisteredServiceProvider<Permission>
	 * rsp = pl.getServer().getServicesManager().getRegistration(Permission.class);
	 * pl.setPerms(rsp.getProvider()); return pl.perms != null; }
	 */

}
