package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Listeners.RedProtectListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;

public class RedProtectHook {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;
	public Plugin rp;

	public Plugin getRp() {
		return rp;
	}

	public void setRp(Plugin rp) {
		this.rp = rp;
	}

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public RedProtectHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	private void check() {
		if (pl.getConfig().getBoolean("RedProtect.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("RedProtect"));
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			rp = Bukkit.getPluginManager().getPlugin("RedProtect");
			Bukkit.getServer().getPluginManager().registerEvents(new RedProtectListener(pl), pl);
		}
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public Region isInClaim(Location location) {
		Region r = RedProtectAPI.getRegion(location);
		return r;
	}

	public Response checkWarp(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !configEnabled("RedProtect.enabled")) {
			return new Response(true, "");
		}
		
		if (isInClaim(player.getLocation()) == null) {
			return new Response(false, "REDPROTECT_NOT_IN_A_REGION");
		}

		if (configEnabled("RedProtect.leader-player-can-set-warp")) {
			if (isInClaim(player.getLocation()).isLeader(player)) {
				return new Response(true, "");
			}
		}

		if (configEnabled("RedProtect.admin-player-can-set-warp")) {
			if (isInClaim(player.getLocation()).isAdmin(player)) {
				return new Response(true, "");
			}
		}

		if (configEnabled("RedProtect.member-player-can-set-warp")) {
			if (isInClaim(player.getLocation()).isMember(player)) {
				return new Response(true, "");
			}
		}

		return new Response(false, "REDPROTECT_NO_PERMISSION");

	}

}