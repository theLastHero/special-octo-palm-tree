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
	
	@SuppressWarnings("unchecked")
	public VaultHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		plName = "Vault";
		checkVault();
	}
	
	  private void checkVault()
	  {
	    Plugin pVT = Bukkit.getPluginManager().getPlugin(plName);
	    if ((pVT != null) && (pVT.isEnabled())) {
	    	pl.messageHandler.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", plName, pl.getLanguageHandler().getMessage("SUCCESS")));
	    	setupEconomy();
	    	setupPermissions();
	    	return;
	    }
	    pl.messageHandler.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", plName, pl.getLanguageHandler().getMessage("FAILED")));
	    pl.getCriticalErrors().add(pl.getLanguageHandler().getMessage("CONSOLE_CRITIAL_ERROR_HOOK", plName));
	  }
	  
	public static Economy getEcon() {
		return econ;
	}

	public static void setEcon(Economy econ) {
		VaultHook.econ = econ;
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
