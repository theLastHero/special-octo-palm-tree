package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import PlayerWarpGUI.config.Config;

/**
* Hooks into Residence Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class ResidenceHook extends HookManager<Object>{

	/**
	 * Hooked plugin name
	 */
	private static String plName = "Residence";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = Config.getInstance().getRSEnabled();
	
	/**
	 * Constructor. Pass variables to super.
	 */
	public ResidenceHook() {
		super(plName, configEnabled);
	}
	
	/**
	 * Overridden method. Checks based on is enabled in config.<br>
	 * Only set warp in a region
	 * Region owner can set warp in claim<br>
	 * Builder can set warp in claim<br>
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
			return "RESIDENCE_NOT_IN_A_REGION";
		}

		if (Config.getInstance().getRSOwnerCan()) {
			if (getLocationData(player.getLocation()).getOwner() == player.getName()) {
				return null;
			}
		}

		if (Config.getInstance().getRSBuilderCan()) {
			FlagPermissions.addFlag("build");
			ResidencePermissions perms = getLocationData(player.getLocation()).getPermissions();

			@SuppressWarnings("deprecation")
			boolean hasPermission = perms.playerHas(player.getName(), "build", false);
			if (hasPermission) {
				return null;
			}
		}

		return "RESIDENCE_NO_PERMISSION";

	}

	/**
	 * @return ClaimedResidence
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public ClaimedResidence getLocationData(Location location) {
		return ResidenceApi.getResidenceManager().getByLoc(location);
	}

	
	
	

}
