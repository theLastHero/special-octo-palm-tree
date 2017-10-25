package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;
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

		if(Config.getInstance().getGPEnabled()) {
		if (Config.getInstance().getGPRemoveOnDelete()) {
			for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

				Location loc = StringUtils.getInstance().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID()).getUniqueId();

				if (pl.getGriefPreventionHook().getLocationData(loc) != null) {
					pl.getPlayerWarpFileHandler().removeSingleWarpValue(pl.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID),
							warpName);

					pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(), LocaleLoader.getString("GRIEFPREVENTION_CLAIM_DELETED", warpName));
					
				}
			}
			}
		}
	}

}
