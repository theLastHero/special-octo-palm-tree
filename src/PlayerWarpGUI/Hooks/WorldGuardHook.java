package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import PlayerWarpGUI.config.Config;

public class WorldGuardHook extends HookManager<Object>{

	private static String plName = "WorldGuard";
	private static boolean configEnabled = Config.getInstance().getWGEnabled();
	
	public WorldGuardHook() {
		super(plName, configEnabled);
	}


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


	@Override
	public ApplicableRegionSet getLocationData(Location location) {
		return WGBukkit.getRegionManager(location.getWorld())
				.getApplicableRegions(location);
	}
	

}
