package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import br.net.fabiozumbi12.RedProtect.events.DeleteRegionEvent;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

public class RedProtectListener implements Listener {

	private static PlayerWarpGUI p;

	/**
	 * @param p
	 */
	public RedProtectListener(PlayerWarpGUI p) {
		RedProtectListener.p = p;
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(DeleteRegionEvent e) {
		if(Config.getInstance().getRPEnabled()) {
		if (Config.getInstance().getRPRemoveOnDelete()) {
			for (int i = 0; i < p.getPlayerWarpObjects().size(); i++) {

				Location loc = StringUtils.getInstance().str2loc(p.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = p.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(p.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (p.getRedProtectHook().getLocationData(loc) != null) {
					Bukkit.broadcastMessage("InClaim");
					WarpFileUtils.getInstance().removeSingleWarpValue(
							WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

					PlayerWarpGUI.pwoList.get(i).removePlayerWarpObject();
					MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),LocaleLoader.getString("REDPROTECT_CLAIM_DELETED", warpName));
					return;
				}
			}
			}
		}
	}

}
