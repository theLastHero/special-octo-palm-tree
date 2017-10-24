package OldClasses;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.config.Config;

public class WorldGuardHook2 {

	private PlayerWarpGUI pl;
	public boolean enabled = false;
	public WorldGuardPlugin wg;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public WorldGuardHook2(PlayerWarpGUI pl) {
		this.pl = pl;
		setEnabled();
		//registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (Config.getInstance().getWGEnabled()) {
			setEnabled(pl.getHookHandler().checkHook("WorldGuard"));
		}
	}
	
	/**
	 * @param enabled
	 */
	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 */
	private boolean isEnabled() {
		return enabled;
	}


	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private void registerListener() {
		if (this.enabled) {
			wg = WGBukkit.getPlugin();
			//Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardListener(pl), pl);
		}
	}

	
	/**
	 * @param player
	 * @return
	 */
	public ApplicableRegionSet getLocationData(Player player) {
		return wg.getRegionManager(player.getLocation().getWorld())
				.getApplicableRegions(player.getLocation());
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !Config.getInstance().getWGEnabled()) {
			return null;
		}

		if (getLocationData(player) == null) {
			return "WORLGUARD_NOT_IN_A_REGION";
		}

		if (Config.getInstance().getWGOwnerCan()) {
			for (ProtectedRegion r : getLocationData(player)) {
				if (r.getOwners().contains(player.getUniqueId())) {
					return null;
				}
			}
		}
		
		if (Config.getInstance().getWGMemberCan()) {
			for (ProtectedRegion r : getLocationData(player)) {
				if (r.getMembers().contains(player.getUniqueId())) {
					return null;
				}
			}
		}

		return "WORLDGUARD_NO_PERMISSION";

	}

}
