package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import PlayerWarpGUI.config.Config;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHook extends HookManager<Object>{


	private static String plName = "GriefPrevention";
	private static boolean configEnabled = Config.getInstance().getGPEnabled();
	
	public GriefPreventionHook() {
		super(plName, configEnabled);
	}


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

	@Override
	public Claim getLocationData(Location location) {
		return GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
	}

	
	
	
	
}
