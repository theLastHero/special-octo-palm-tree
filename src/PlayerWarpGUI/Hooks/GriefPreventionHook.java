package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Listeners.GriefPreventionListener;
import config.Config;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionHook implements Listener {

	private PlayerWarpGUI p;
	private boolean enabled = false;
	private GriefPrevention gp;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GriefPreventionHook(PlayerWarpGUI p) {
		this.p = p;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (Config.getInstance().getGPEnabled()) {
			setEnabled(p.getHookHandler().checkHook("GriefPrevention"));
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
			Bukkit.getServer().getPluginManager().registerEvents(new GriefPreventionListener(p), p);
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

		if (!isEnabled() || !Config.getInstance().getGPEnabled()) {
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

}
