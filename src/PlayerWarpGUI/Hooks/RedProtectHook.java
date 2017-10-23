package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Listeners.RedProtectListener;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;
import config.Config;

public class RedProtectHook {

	private PlayerWarpGUI pl;
	private boolean enabled = false;
	@SuppressWarnings("unused")
	private Plugin rp;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public RedProtectHook(PlayerWarpGUI pl) {
		this.pl = pl;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (Config.getInstance().getRPEnabled()) {
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
			Bukkit.getServer().getPluginManager().registerEvents(new RedProtectListener(this.pl), pl);
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
		if (!isEnabled() || !Config.getInstance().getRPEnabled()) {
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

}
