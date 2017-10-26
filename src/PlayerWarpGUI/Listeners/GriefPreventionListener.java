package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
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
				for (int i = 0; i < PlayerWarpGUI.pwoList.size(); i++) {

					Location location = StringUtils.getInstance().str2loc(PlayerWarpGUI.pwoList.get(i).getWarpLocation());
					String warpName = PlayerWarpGUI.pwoList.get(i).getWarpName();
					UUID playerUUID = Bukkit.getOfflinePlayer(PlayerWarpGUI.pwoList.get(i).getPlayerUUID()).getUniqueId();

					if(e.getClaim().contains(location, false, false)) {
						WarpFileUtils.getInstance().removeSingleWarpValue(WarpFileUtils.getInstance().checkWarpsExsits(playerUUID),
								warpName);

						pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
						Bukkit.getOfflinePlayer(playerUUID).getPlayer().sendMessage(LocaleLoader.getString("GRIEFPREVENTION_CLAIM_DELETED", warpName));

					}
					
					
				}
			}
		}
	}

}
