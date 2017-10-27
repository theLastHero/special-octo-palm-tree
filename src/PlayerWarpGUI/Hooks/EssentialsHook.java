package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import PlayerWarpGUI.config.Config;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;

/**
* Hooks into Essentials, used to be able to use
* essentials set /back command after teleporting.
*  
* @author Judgetread
* @version 1.0
*/
public class EssentialsHook extends HookManager<Object>{

	/** Name of plugin to attempt to hook into*/
	private static String plName = "Essentials";
	/** Is the hook plugin enabled in config */
	private static boolean configEnabled = Config.getInstance().getESEnabled();
	/** essentials reference */
	public static Essentials ess3 = null;
	
	
	/**
	 * Constructor
	 */
	public EssentialsHook() {
		super(plName, configEnabled);
	}

	/**
	 * When teleported, sets the user to be able to use<br>
	 * Essentials /back command
	 * 
	 * @param player Player to set .back command in essentials to.
	 */
	public void setBackUser(Player player) {
		ess3 = (Essentials) getpHook();
		ess3.getUser(player).setLastLocation();
	}
	
	
	/**
	 * Check use /back is enabled in config and essentials is installed and hooked.
	 * 
	 * @return boolean true/false
	 */
	public boolean useBack() {
		if(this.isEnabled) {
			if(Config.getInstance().getESEnabled()) {
				if(Config.getInstance().getESBack()) {
					return true;
				}
			}
		}	
		return false;
	}

	/**
	 * Not used, but has to be overridden
	 * 
	 * @see PlayerWarpGUI.Hooks.HookManager#warpHookCheck(org.bukkit.entity.Player)
	 */
	@Override
	public String warpHookCheck(Player player) {
		return hookedPluginName;
			}

	
	/**
	 * Not used, but has to be overridden
	 * 
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}