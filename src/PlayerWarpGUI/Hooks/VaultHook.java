package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.config.Config;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import net.milkbowl.vault.economy.Economy;

/**
* Hooks into Vault Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class VaultHook extends HookManager<Object>{

	/**
	 * Hooked plugin name
	 */
	private static String plName = "Vault";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = false;
	/**
	 * Economy variable
	 */
	public Economy econ = null;
	
	/**
	 * Constructor. Pass variables to super.
	 * Check config  set warp cost is above 0, 0 is disabled.
	 */
	public VaultHook() {
		super(plName, configEnabled);
		if((Config.getInstance().getSetWarpCost() > 0) && setupEconomy()) {
			isEnabled = true;
		}
	}
	
	

	/**
	 * Setup economy variable
	 * 
	 * @return true/false
	 */
	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = PlayerWarpGUI.p.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	
	/**
	 * Not used, but has to be overridden
	 * @see PlayerWarpGUI.Hooks.HookManager#warpHookCheck(org.bukkit.entity.Player)
	 */
	@Override
	public String warpHookCheck(Player player) {
		return hookedPluginName;
			}

	/**
	 * Not used, but has to be overridden
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}