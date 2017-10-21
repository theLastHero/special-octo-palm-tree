package Hooks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import Listeners.FactionsListener;
import Listeners.ResidenceListener;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;

public class FactionsHook implements Listener {

	public static PlayerWarpGUI pl;
	public boolean enabled = false;
	public Factions fa;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public FactionsHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		check();
	}

	public void check() {
		if (pl.getConfig().getBoolean("Factions.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("Factions"));
		}
	}

	public Factions getFa() {
		return fa;
	}

	public void setFa(Factions fa) {
		this.fa = fa;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			fa = Factions.get();
			Bukkit.getServer().getPluginManager().registerEvents(new FactionsListener(pl), pl);
		}
	}

	public boolean configEnabled(String configMaker) {
		if (pl.getConfig().getBoolean(configMaker)) {
			return true;
		}
		return false;
	}

	public Faction isInClaim(Location location) {
		Faction faction = null;
		faction = BoardColl.get().getFactionAt(PS.valueOf(location));
		return faction;
	}

	public Response checkWarp(Player player) {


		// if not enabled, or config not enabled or player not in a claim
		if (!isEnabled() || !configEnabled("Factions.enabled")) {
			return new Response(true, "");
		}
		

		MPlayer mplayer;
		mplayer = MPlayer.get(player);

		BoardColl coll = BoardColl.get();
        Faction faction = coll.getFactionAt(PS.valueOf(player.getLocation()));
		
		if (configEnabled("Factions.leader-can-set-warp")) {
			if (faction.getLeader() == mplayer) {
				return new Response(true, "");
			}
		}
		
		if (configEnabled("Factions.leader-can-set-warp")) {
			if (faction.getMPlayers().contains(mplayer)) {
				return new Response(true, "");
			}
		}
		
		if (configEnabled("Factions.in-own-territory-can-set-warp")) {
			if (mplayer.isInOwnTerritory()) {
				return new Response(true, "");
			}
		}
		
		if (configEnabled("Factions.in-wilderness-can-set-warp")) {
			
			if (faction.getId().equalsIgnoreCase(Factions.ID_NONE)) {
				return new Response(true, "");
			}
		}
		
		return new Response(false, "FACTIONS_NO_PERMISSION");

	}

}
