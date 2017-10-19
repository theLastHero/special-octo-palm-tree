package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WGBukkit;

import PlayerWarpGUI.PlayerWarpGUI;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;

public class GriefPreventionHook {

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

	public void check() {
		if (pl.getConfig().getBoolean("GriefPrevention.enabled")) {
			setEnabled(pl.hookHandler.checkHook("GriefPrevention"));
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
		}
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public Claim isInClaim(Player player) {
		me.ryanhamshire.GriefPrevention.Claim isClaim = this.gp.dataStore.getClaimAt(player.getLocation(), false, null);
		return isClaim;
	}

	public Response checkWarp(Player player) {
		
			// if not enabled, or config not enabled or player not in a claim
			if (!isEnabled()  || !configEnabled("GriefPrevention.enabled")) {
				return new Response(true,"");
			}
			
			if(isInClaim(player) == null) {
				return new Response(false,"GRIEFPREVETION_NOT_IN_A_CLAIM");
			}

			if (configEnabled("GriefPrevention.owner-can-set-warp")
					&& isInClaim(player).getOwnerName().equalsIgnoreCase(player.getName())) {
				return new Response(true,"");

			}

			if (configEnabled("GriefPrevention.trusted-player-can-set-warp")
					&& (isInClaim(player).allowAccess(player) == null)) {
				return new Response(true,"");
			}

	
		return new Response(false,"GRIEFPREVETION_NO_PERMISSION");

	}
	
	public class Response {

	    private String errorMsg;
		private boolean responseBool;

	    public Response( boolean showError, String errorMsg ) {

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
