package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.config.Config;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import net.milkbowl.vault.economy.Economy;

public class VaultHook extends HookManager<Object>{

	private static String plName = "Vault";
	private static boolean configEnabled = false;
	public Economy econ = null;
	
	public VaultHook() {
		super(plName, configEnabled);
		if((Config.getInstance().getSetWarpCost() > 0) && setupEconomy()) {
			isEnabled = true;
		}
	}
	
	

	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = PlayerWarpGUI.p.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	@Override
	public String warpHookCheck(Player player) {
		return hookedPluginName;
			}

	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}