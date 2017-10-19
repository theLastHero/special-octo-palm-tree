package Hooks;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import Hooks.GriefPreventionHook.Response;
import PlayerWarpGUI.PlayerWarpGUI;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class WorldGuardHook {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;
	public WorldGuardPlugin wg;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public WorldGuardHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	private void check() {
		if (pl.getConfig().getBoolean("WorldGuard.enabled")) {
			setEnabled(pl.hookHandler.checkHook("WorldGuard"));
		}
	}

	public WorldGuardPlugin getWg() {
		return wg;
	}

	public void setWg(WorldGuardPlugin wg) {
		this.wg = wg;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			wg = WGBukkit.getPlugin();
		}
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public ApplicableRegionSet isInClaim(Player player) {
		ApplicableRegionSet set = getWg().getRegionManager(player.getLocation().getWorld())
				.getApplicableRegions(player.getLocation());
		return set;
	}

	public Response checkWarp(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !configEnabled("WorldGuard.enabled")) {
			return new Response(true, "");
		}

		if (isInClaim(player) == null) {
			return new Response(false, "WORLGUARD_NOT_IN_A_REGION");
		}

		if (configEnabled("WorldGuard.owner-can-set-warp")) {
			for (ProtectedRegion r : isInClaim(player)) {
				if (r.getOwners().contains(player.getUniqueId())) {
					return new Response(true, "");
				}
			}
		}
		
		if (configEnabled("WorldGuard.member-player-can-set-warp")) {
			for (ProtectedRegion r : isInClaim(player)) {
				if (r.getMembers().contains(player.getUniqueId())) {
					return new Response(true, "");
				}
			}
		}

		return new Response(false, "WORLDGUARD_NO_PERMISSION");

	}

	public class Response {

		private String errorMsg;
		private boolean responseBool;

		public Response(boolean showError, String errorMsg) {

			this.responseBool = showError;
			this.errorMsg = errorMsg;

		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		public boolean getResponseBool() {
			return responseBool;
		}

		public void setresponseBool(boolean responseBool) {
			this.responseBool = responseBool;
		}

	}
}
