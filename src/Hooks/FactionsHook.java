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
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;

public class FactionsHook implements Listener {

	private static PlayerWarpGUI pl;
	protected boolean enabled = false;
	private Factions faction;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public FactionsHook(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (pl.getConfig().getBoolean("Factions.enabled")) {
			setEnabled(pl.getHookHandler().checkHook("Factions"));
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
	 * @param enabled
	 */
	private void registerListener() {
		if (this.enabled) {
			faction = Factions.get();
			Bukkit.getServer().getPluginManager().registerEvents(new FactionsListener(pl), pl);
		}
	}

	/**
	 * @param location
	 * @return
	 */
	private Faction getLocationData(Location location) {
		return BoardColl.get().getFactionAt(PS.valueOf(location));
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		if (!isEnabled() || !pl.getConfig().getBoolean("Factions.enabled")) {
			return null;
		}
		
		MPlayer mplayer = MPlayer.get(player);
		BoardColl coll = BoardColl.get();
        Faction faction = coll.getFactionAt(PS.valueOf(player.getLocation()));
		
		if (pl.getConfig().getBoolean("Factions.leader-can-set-warp")) {
			if (faction.getLeader() == mplayer) {
				return null;
			}
		}
		
		if (pl.getConfig().getBoolean("Factions.leader-can-set-warp")) {
			if (faction.getMPlayers().contains(mplayer)) {
				return null;
			}
		}
		
		if (pl.getConfig().getBoolean("Factions.in-own-territory-can-set-warp")) {
			if (mplayer.isInOwnTerritory()) {
				return null;
			}
		}
		
		if (pl.getConfig().getBoolean("Factions.in-wilderness-can-set-warp")) {
			
			if (faction.getId().equalsIgnoreCase(Factions.ID_NONE)) {
				return null;
			}
		}
		
		return "FACTIONS_NO_PERMISSION";

	}

}
