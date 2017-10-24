package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import PlayerWarpGUI.config.Config;

public class ResidenceHook extends HookManager<Object>{

	private static String plName = "Residence";
	private static boolean configEnabled = Config.getInstance().getRSEnabled();
	
	public ResidenceHook() {
		super(plName, configEnabled);
	}
	

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

	@Override
	public ClaimedResidence getLocationData(Location location) {
		return ResidenceApi.getResidenceManager().getByLoc(location);
	}

	
	
	

}
