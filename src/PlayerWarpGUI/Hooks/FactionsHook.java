package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import PlayerWarpGUI.config.Config;

/**
* Hooks into Factions Plugin.<br>
* Performs various actions depending on config settings.
*  
* @author Judgetread
* @version 1.0
*/
public class FactionsHook extends HookManager<Object>{
	
	/**
	 * Hooked plugin name
	 */
	private static String plName = "Factions";
	/**
	 * Holds if plugin is enabled in config.
	 */
	private static boolean configEnabled = Config.getInstance().getFAEnabled();
	
	/**
	 * Constructor. Pass variables to super.
	 */
	public FactionsHook() {
		super(plName, configEnabled);
	}

	/**
	 * Overridden method. Checks based on is enabled in config.<br>
	 * Faction Leader can set warp in faction land<br>
	 * Allow player to set warp in own territory<br>
	 * Allow player to set warp in wilderness<br>
	 * 
	 * @return String representing any errors or else null
	 * @see PlayerWarpGUI.Hooks.HookManager#warpHookCheck(org.bukkit.entity.Player)
	 */
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
	
	
	/**
	 * Check what faction owns this location the player is at.
	 * 
	 * @return Faction
	 * @see PlayerWarpGUI.Hooks.HookManager#getLocationData(org.bukkit.Location)
	 */
	@Override
	public Faction getLocationData(Location location) {
		return BoardColl.get().getFactionAt(PS.valueOf(location));
	}


}
