package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

public class ResidenceListener implements Listener {

	/**
	 * @param p
	 */
	public ResidenceListener(PlayerWarpGUI p) {
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(ResidenceDeleteEvent e) {
		Bukkit.broadcastMessage("Event Called");
		if(Config.getInstance().getRSEnabled()) {
		
		if (Config.getInstance().getRSemoveOnDelete()) {
			for (int i = 0; i < PlayerWarpGUI.getPwoList().size(); i++) {

				Location loc = StringUtils.getInstance().str2loc(PlayerWarpGUI.getPwoList().get(i).getWarpLocation());
				String warpName = PlayerWarpGUI.getPwoList().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(PlayerWarpGUI.getPwoList().get(i).getPlayerUUID())
						.getUniqueId();

				
				if (e.getResidence().containsLoc(loc)) {
					WarpFileUtils.getInstance().removeSingleWarpValue(
							WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

					PlayerWarpGUI.getPwoList().get(i).removePlayerWarpObject();
					MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),LocaleLoader.getString("RESIDENCE_CLAIM_DELETED", warpName));
				}
			}
			}
		}
	}

}
