package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;

import Listeners.ResidenceListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;

public class ResidenceHook {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;
	public Residence rs;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ResidenceHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	private void check() {
		if (pl.getConfig().getBoolean("Residence.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("Residence"));
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			rs = Residence.getInstance();
			Bukkit.getServer().getPluginManager().registerEvents(new ResidenceListener(pl), pl);
		}
	}

	public Residence getRs() {
		return rs;
	}

	public void setRs(Residence rs) {
		this.rs = rs;
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public ClaimedResidence isInClaim(Location location) {
		Residence.getInstance().getAPI();
		ClaimedResidence set = ResidenceApi.getResidenceManager().getByLoc(location);
		return set;
	}

	public Response checkWarp(Player player) {

		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !configEnabled("Residence.enabled")) {
			return new Response(true, "");
		}

		if (isInClaim(player.getLocation()) == null) {
			return new Response(false, "RESIDENCE_NOT_IN_A_REGION");
		}

		if (configEnabled("Residence.owner-player-can-set-warp")) {
			if (isInClaim(player.getLocation()).getOwner() == player.getName()) {
				return new Response(true, "");
			}
		}

		if (configEnabled("Residence.build-permission-player-can-set-warp")) {
			FlagPermissions.addFlag("build");
			ResidencePermissions perms = isInClaim(player.getLocation()).getPermissions();

			@SuppressWarnings("deprecation")
			boolean hasPermission = perms.playerHas(player.getName(), "build", false);
			if (hasPermission) {
				return new Response(true, "");
			}
		}

		return new Response(false, "RESIDENCE_NO_PERMISSION");

	}

}
