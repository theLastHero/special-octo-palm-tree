package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import PlayerWarpGUI.config.Config;

/**
* Hooks into RedProtectHook Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class RedProtectHook extends HookManager<Object>{

	/**
	 * Hooked plugin name
	 */
	private static String plName = "RedProtect";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = Config.getInstance().getRPEnabled();
	
	/**
	 * Constructor. Pass variables to super.
	 */
	public RedProtectHook() {
		super(plName, configEnabled);
	}

	/**
	 * Overridden method. Checks based on is enabled in config.<br>
	 * Only set warp in a RP region.<br>
	 * Leader of region can set warp<br>
	 * Admin of region can set warp<br>
	 * Member of region can set warp<br>
	 * 
	 * @return String representing any errors or else null
	 * @see PlayerWarpGUI.Hooks.HookManager#warpHookCheck(org.bukkit.entity.Player)
	 */
	@Override
	public String warpHookCheck(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!this.isEnabled) {
			return null;
		}
		
		if (getLocationData(player.getLocation()) == null) {
			return "REDPROTECT_NOT_IN_A_REGION";
		}

		if (Config.getInstance().getRPLeaderCan()) {
			if (getLocationData(player.getLocation()).isLeader(player)) {
				return null;
			}
		}

		if (Config.getInstance().getRPAdminCan()) {
			if (getLocationData(player.getLocation()).isAdmin(player)) {
				return null;
			}
		}

		if (Config.getInstance().getRPMemberCan()) {
			if (getLocationData(player.getLocation()).isMember(player)) {
				return null;
			}
		}

		return "REDPROTECT_NO_PERMISSION";

	}

	/**
	 * @return Region
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}