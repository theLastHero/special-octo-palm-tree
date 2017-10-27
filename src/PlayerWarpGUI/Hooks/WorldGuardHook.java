package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import PlayerWarpGUI.config.Config;

/**
* Hooks into WorldGuard Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class WorldGuardHook extends HookManager<Object>{

	/**
	 * Hooked plugin name
	 */
	private static String plName = "WorldGuard";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = Config.getInstance().getWGEnabled();
	
	/**
	 * Constructor. Pass variables to super.
	 */
	public WorldGuardHook() {
		super(plName, configEnabled);
	}

	/**
	 * Overridden method. Checks based on is enabled in config.<br>
	 * Only set warps in regions.<br>
	 * Region owner can set warp.<br>
	 * Region member can set warp.<br>
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
			return "WORLGUARD_NOT_IN_A_REGION";
		}

		if (Config.getInstance().getWGOwnerCan()) {
			for (ProtectedRegion r : getLocationData(player.getLocation())) {
				if (r.getOwners().contains(player.getUniqueId())) {
					return null;
				}
			}
		}
		
		if (Config.getInstance().getWGMemberCan()) {
			for (ProtectedRegion r : getLocationData(player.getLocation())) {
				if (r.getMembers().contains(player.getUniqueId())) {
					return null;
				}
			}
		}

		return "WORLDGUARD_NO_PERMISSION";

	}


	/**
	 * @return ApplicableRegionSet
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public ApplicableRegionSet getLocationData(Location location) {
		return WGBukkit.getRegionManager(location.getWorld())
				.getApplicableRegions(location);
	}
	

}
