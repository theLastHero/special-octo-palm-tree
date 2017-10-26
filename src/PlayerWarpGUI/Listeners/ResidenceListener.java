package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

public class ResidenceListener implements Listener {

	private static PlayerWarpGUI p;
	
	/**
	 * @param p
	 */
	public ResidenceListener(PlayerWarpGUI p) {
		ResidenceListener.p = p;
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(ResidenceDeleteEvent e) {
		Bukkit.broadcastMessage("Event Called");
		if(Config.getInstance().getRSEnabled()) {
		
		if (Config.getInstance().getRSemoveOnDelete()) {
			for (int i = 0; i < p.getPlayerWarpObjects().size(); i++) {

				Location loc = StringUtils.getInstance().str2loc(p.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = p.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(p.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (new ResidenceHook().getLocationData(loc) != null) {
					WarpFileUtils.getInstance().removeSingleWarpValue(
							WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

					p.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),LocaleLoader.getString("RESIDENCE_CLAIM_DELETED", warpName));
				}
			}
			}
		}
	}

}
