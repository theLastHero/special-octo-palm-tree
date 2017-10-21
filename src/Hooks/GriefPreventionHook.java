package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Factions;

import Listeners.FactionsListener;
import Listeners.GriefPreventionListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHook implements Listener {

	private static PlayerWarpGUI pl;
	private boolean enabled = false;
	private GriefPrevention gp;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GriefPreventionHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (pl.getConfig().getBoolean("GriefPrevention.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("GriefPrevention"));
		}
	}

	/**
	 * @param enabled
	 */
	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	private void registerListener() {
		if (this.enabled) {
			gp = GriefPrevention.instance;
			Bukkit.getServer().getPluginManager().registerEvents(new GriefPreventionListener(pl), pl);
		}
	}

	/**
	 * @param location
	 * @return
	 */
	public Claim getLocationData(Location location) {
		return this.gp.dataStore.getClaimAt(location, false, null);
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		if (!isEnabled() || !pl.getConfig().getBoolean("GriefPrevention.enabled")) {
			return null;
		}

		if (getLocationData(player.getLocation()) == null) {
			return "GRIEFPREVENTION_NOT_IN_A_CLAIM";
		}

		if (pl.getConfig().getBoolean("GriefPrevention.owner-can-set-warp")
				&& getLocationData(player.getLocation()).getOwnerName().equalsIgnoreCase(player.getName())) {
			return null;

		}

		if (pl.getConfig().getBoolean("GriefPrevention.trusted-player-can-set-warp")
				&& (getLocationData(player.getLocation()).allowAccess(player) == null)) {
			return null;
		}

		return "GRIEFPREVENTION_NO_PERMISSION";

	}

}
