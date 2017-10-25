package OldClasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import PlayerWarpGUI.Listeners.ResidenceListener;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.PlayerWarpGUI;

public class ResidenceHook2 {

	private PlayerWarpGUI pl;
	public boolean enabled = false;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ResidenceHook2(PlayerWarpGUI pl) {
		this.pl = pl;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (Config.getInstance().getRSEnabled()) {
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

		if (!isEnabled() || !Config.getInstance().getRSEnabled()) {
			return null;
		}

		if (getLocationData(player.getLocation()) == null) {
			return "RESIDENCE_NOT_IN_A_REGION";
		}

		if (Config.getInstance().getRSOwnerCan()) {
			if (getLocationData(player.getLocation()).getOwner() == player.getName()) {
				return null;
			}
		}

		if (Config.getInstance().getRSBuilderCan()) {
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
