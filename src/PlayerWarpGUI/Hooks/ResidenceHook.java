package PlayerWarpGUI.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Listeners.ResidenceListener;

public class ResidenceHook {

	private PlayerWarpGUI pl;
	public boolean enabled = false;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ResidenceHook(PlayerWarpGUI pl) {
		this.pl = pl;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (pl.getConfig().getBoolean("Residence.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("Residence"));
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
		if (enabled) {
			Bukkit.getServer().getPluginManager().registerEvents(new ResidenceListener(pl), pl);
		}
	}

	/**
	 * @param location
	 * @return
	 */
	public ClaimedResidence getLocationData(Location location) {
		return ResidenceApi.getResidenceManager().getByLoc(location);
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		if (!isEnabled() || !pl.getConfig().getBoolean("Residence.enabled")) {
			return null;
		}

		if (getLocationData(player.getLocation()) == null) {
			return "RESIDENCE_NOT_IN_A_REGION";
		}

		if (pl.getConfig().getBoolean("Residence.owner-player-can-set-warp")) {
			if (getLocationData(player.getLocation()).getOwner() == player.getName()) {
				return null;
			}
		}

		if (pl.getConfig().getBoolean("Residence.build-permission-player-can-set-warp")) {
			FlagPermissions.addFlag("build");
			ResidencePermissions perms = getLocationData(player.getLocation()).getPermissions();

			@SuppressWarnings("deprecation")
			boolean hasPermission = perms.playerHas(player.getName(), "build", false);
			if (hasPermission) {
				return null;
			}
		}

		return "RESIDENCE_NO_PERMISSION";

	}

}
