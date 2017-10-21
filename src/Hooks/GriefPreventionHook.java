package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import Listeners.GriefPreventionListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHook implements Listener {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;
	public GriefPrevention gp;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GriefPreventionHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	public void deletedClaim() {

	}

	public void check() {
		if (pl.getConfig().getBoolean("GriefPrevention.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("GriefPrevention"));
		}
	}

	public GriefPrevention getGp() {
		return gp;
	}

	public void setGp(GriefPrevention gp) {
		this.gp = gp;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			gp = GriefPrevention.instance;
			Bukkit.getServer().getPluginManager().registerEvents(new GriefPreventionListener(pl), pl);
		}
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public Claim isInClaim(Location location) {
		me.ryanhamshire.GriefPrevention.Claim isClaim = this.gp.dataStore.getClaimAt(location, false, null);
		return isClaim;
	}

	public Response checkWarp(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !configEnabled("GriefPrevention.enabled")) {
			return new Response(true, "");
		}

		if (isInClaim(player.getLocation()) == null) {
			return new Response(false, "GRIEFPREVENTION_NOT_IN_A_CLAIM");
		}

		if (configEnabled("GriefPrevention.owner-can-set-warp")
				&& isInClaim(player.getLocation()).getOwnerName().equalsIgnoreCase(player.getName())) {
			return new Response(true, "");

		}

		if (configEnabled("GriefPrevention.trusted-player-can-set-warp")
				&& (isInClaim(player.getLocation()).allowAccess(player) == null)) {
			return new Response(true, "");
		}

		return new Response(false, "GRIEFPREVENTION_NO_PERMISSION");

	}

	

}
