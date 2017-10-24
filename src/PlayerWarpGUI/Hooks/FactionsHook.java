package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import PlayerWarpGUI.config.Config;

public class FactionsHook extends HookManager<Object>{
	
	@SuppressWarnings("unused")
	private Factions faction;

	private static String plName = "Factions";
	private static boolean configEnabled = Config.getInstance().getFAEnabled();
	
	public FactionsHook() {
		super(plName, configEnabled);
	}

	@Override
	public String warpHookCheck(Player player) {

		if (!this.isEnabled) {
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
	@Override
	public Faction getLocationData(Location location) {
		return BoardColl.get().getFactionAt(PS.valueOf(location));
	}


}
