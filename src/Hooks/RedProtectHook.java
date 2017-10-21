package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Listeners.GriefPreventionListener;
import Listeners.RedProtectListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class RedProtectHook {

	private static PlayerWarpGUI pl;
	private boolean enabled = false;
	private Plugin rp;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public RedProtectHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (pl.getConfig().getBoolean("RedProtect.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("RedProtect"));
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
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * 
	 */
	private void registerListener() {
		if (this.enabled) {
			rp = Bukkit.getPluginManager().getPlugin("RedProtect");
			Bukkit.getServer().getPluginManager().registerEvents(new RedProtectListener(pl), pl);
		}
	}

	/**
	 * @param location
	 * @return
	 */
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !pl.getConfig().getBoolean("RedProtect.enabled")) {
			return null;
		}
		
		if (getLocationData(player.getLocation()) == null) {
			return "REDPROTECT_NOT_IN_A_REGION";
		}

		if (pl.getConfig().getBoolean("RedProtect.leader-player-can-set-warp")) {
			if (getLocationData(player.getLocation()).isLeader(player)) {
				return null;
			}
		}

		if (pl.getConfig().getBoolean("RedProtect.admin-player-can-set-warp")) {
			if (getLocationData(player.getLocation()).isAdmin(player)) {
				return null;
			}
		}

		if (pl.getConfig().getBoolean("RedProtect.member-player-can-set-warp")) {
			if (getLocationData(player.getLocation()).isMember(player)) {
				return null;
			}
		}

		return "REDPROTECT_NO_PERMISSION";

	}

}
