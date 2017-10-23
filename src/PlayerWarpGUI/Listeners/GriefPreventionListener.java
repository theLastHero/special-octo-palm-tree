package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import config.Config;
import locale.LocaleLoader;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;

public class GriefPreventionListener implements Listener {

	public PlayerWarpGUI pl;
	

	/**
	 * @param pl
	 */
	public GriefPreventionListener(PlayerWarpGUI pl) {
		this.pl = pl;
	}


	@EventHandler
	public void deleteWarpOnDeleteClaim(ClaimDeletedEvent e) {

		if(pl.getGriefPreventionHook().isEnabled()) {
		if (Config.getInstance().getGPRemoveOnDelete()) {
			for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

				Location loc = pl.getOtherFunctions().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID()).getUniqueId();

				if (pl.getGriefPreventionHook().getLocationData(loc) != null) {
					pl.getPlayerWarpFileHandler().removeSingleWarpValue(pl.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID),
							warpName);

					pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					pl.getMsgSend().send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(), LocaleLoader.getString("GRIEFPREVENTION_CLAIM_DELETED", warpName));
					
				}
			}
			}
		}
	}

}