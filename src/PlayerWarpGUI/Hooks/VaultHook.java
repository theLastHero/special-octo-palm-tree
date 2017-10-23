package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import PlayerWarpGUI.PlayerWarpGUI;
import net.milkbowl.vault.economy.Economy;

public class VaultHook {

	private static PlayerWarpGUI p;
	public String plName;
	public Economy econ = null;

	/**
	 * @param p 
	 * @param playerWarpGUI
	 */
	public VaultHook(PlayerWarpGUI p) {
		VaultHook.p = p;
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
		p.getMsgSend().sendConsole("CONSOLE_MSG_HOOK", plName,
				p.localeLoader.getString("SUCCESS"));
	}

	/**
	 * 
	 */
	private void consoleMsgError(String plName) {
		p.getMsgSend().sendConsole(p.localeLoader.getString("CONSOLE_MSG_HOOK", plName,
				p.localeLoader.getString("FAILED")));
		p.getCriticalErrors().add(p.localeLoader.getString("CONSOLE_CRITIAL_ERROR_HOOK", plName));
	}

	/**
	 * @return
	 */
	private boolean setupEconomy() {
		if (p.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = p.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		p.setEcon(rsp.getProvider());
		return p.getEcon() != null;
	}
	/*
	 * private boolean setupPermissions() { RegisteredServiceProvider<Permission>
	 * rsp = pl.getServer().getServicesManager().getRegistration(Permission.class);
	 * pl.setPerms(rsp.getProvider()); return pl.perms != null; }
	 */

}