package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import PlayerWarpGUI.config.Config;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

/**
* Hooks into GriefPrevention Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class GriefPreventionHook extends HookManager<Object>{

	/**
	 * Hooked plugin name
	 */
	private static String plName = "GriefPrevention";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = Config.getInstance().getGPEnabled();
	
	/**
	 * Constructor. Pass variables to super.
	 */
	public GriefPreventionHook() {
		super(plName, configEnabled);
	}

	/**
	 * Overridden method. Checks based on is enabled in config.<br>
	 * Claim owner can set warp in claim<br>
	 * Trusted member can set warp in claim<br>
	 * 
	 * @return String representing any errors or else null
	 * @see PlayerWarpGUI.Hooks.HookManager#warpHookCheck(org.bukkit.entity.Player)
	 */
	@Override
	public String warpHookCheck(Player player) {

		if (!this.isEnabled) {
			return null;
		}

		if (getLocationData(player.getLocation()) == null) {
			return "GRIEFPREVENTION_NOT_IN_A_CLAIM";
		}

		if (Config.getInstance().getGPOwnerCan()
				&& getLocationData(player.getLocation()).getOwnerName().equalsIgnoreCase(player.getName())) {
			return null;

		}

		if (Config.getInstance().getGPTrustedCan()
				&& (getLocationData(player.getLocation()).allowAccess(player) == null)) {
			return null;
		}

		return "GRIEFPREVENTION_NO_PERMISSION";

	}

	/**
	 * @return Claim
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public Claim getLocationData(Location location) {
		return GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
	}

	
	
	
	
}
