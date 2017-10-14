package Hooks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import PlayerWarpGUI.PlayerWarpGUI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {

	public static PlayerWarpGUI pl;
	
	@SuppressWarnings("unchecked")
	public VaultHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		checkVault();
	}
	
	  private void checkVault()
	  {
	    Plugin pVT = Bukkit.getPluginManager().getPlugin("Vault");
	    if ((pVT != null) && (pVT.isEnabled())) {
	    	pl.messages.sendConsoleMessage("Hooking Vault >> Success");
	    	setupEconomy();
	    	setupPermissions();
	    	return;
	    }
	    pl.messages.sendConsoleMessage("Hooking Vault >> Failed");
	    pl.getCriticalErrors().add("&cVault was not found. ");
	  }
	  
	private boolean setupEconomy() {
		if (pl.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = pl.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		pl.econ = rsp.getProvider();
		return pl.econ != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = pl.getServer().getServicesManager().getRegistration(Permission.class);
		pl.perms = rsp.getProvider();
		return pl.perms != null;
	}	  
	  
	  
}
