package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import PlayerWarpGUI.config.Config;

public class RedProtectHook extends HookManager<Object>{

	private static String plName = "RedProtect";
	private static boolean configEnabled = Config.getInstance().getRPEnabled();
	
	public RedProtectHook() {
		super(plName, configEnabled);
	}


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

	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}