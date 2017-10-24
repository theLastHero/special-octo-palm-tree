package OldClasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Listeners.FactionsListener;
import PlayerWarpGUI.config.Config;

public class FactionsHook2 implements Listener {

	private PlayerWarpGUI pl;
	protected boolean enabled = false;
	@SuppressWarnings("unused")
	private Factions faction;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public FactionsHook2(PlayerWarpGUI pl) {
		this.pl = pl;
		setEnabled();
		registerListener();
	}

	/**
	 * 
	 */
	private void setEnabled() {
		if (Config.getInstance().getFAEnabled()) {
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
	public Faction getLocationData(Location location) {
		return BoardColl.get().getFactionAt(PS.valueOf(location));
	}

	/**
	 * @param player
	 * @return
	 */
	public String warpHookCheck(Player player) {

		if (!isEnabled() || !Config.getInstance().getFAEnabled()) {
			return null;
		}
		
		MPlayer mplayer = MPlayer.get(player);
		BoardColl coll = BoardColl.get();
        Faction faction = coll.getFactionAt(PS.valueOf(player.getLocation()));
		
		if (Config.getInstance().getFALeaderCan()) {
			if (faction.getLeader() == mplayer) {
				return null;
			}
		}
		/*
		if (Config.getInstance().getFALeaderCan()) {
			if (faction.getMPlayers().contains(mplayer)) {
				return null;
			}
		}
		*/
		if (Config.getInstance().getFAOwnTerritoryCan()) {
			if (mplayer.isInOwnTerritory()) {
				return null;
			}
		}
		
		if (Config.getInstance().getFAWildernessrCan()) {
			
			if (faction.getId().equalsIgnoreCase(Factions.ID_NONE)) {
				return null;
			}
		}
		
		return "FACTIONS_NO_PERMISSION";

	}

}
