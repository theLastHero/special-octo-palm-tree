package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import br.net.fabiozumbi12.RedProtect.events.DeleteRegionEvent;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

/**
 * Listened to RedProtect Plugin to delete region event.<br>
 * If config enabled, remove any warps when a region is deleted.
 * 
 * @author Judgetread
 * @version 1.0
 */
public class RedProtectListener implements Listener {

	/**
	 * Constructor
	 * @param p
	 */
	public RedProtectListener(PlayerWarpGUI p) {
	}

	/**
	 * Catches when a RedProtect region is deleted. Deletes any warps found in
	 * the deleted region.
	 * 
	 * @param e
	 */
	@EventHandler
	public void deleteWarpOnDeleteClaim(DeleteRegionEvent e) {
		if(Config.getInstance().getRPEnabled()) {
		if (Config.getInstance().getRPRemoveOnDelete()) {
			for (int i = 0; i < PlayerWarpGUI.getPwoList().size(); i++) {

				Location loc = StringUtils.getInstance().str2loc(PlayerWarpGUI.getPwoList().get(i).getWarpLocation());
				String warpName = PlayerWarpGUI.getPwoList().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(PlayerWarpGUI.getPwoList().get(i).getPlayerUUID())
						.getUniqueId();
				
				if (new RedProtectHook().getLocationData(loc) != null) {
					Bukkit.broadcastMessage("InClaim");
					WarpFileUtils.getInstance().removeSingleWarpValue(
							WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

					PlayerWarpGUI.getPwoList().get(i).removePlayerWarpObject();
					MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),LocaleLoader.getString("REDPROTECT_CLAIM_DELETED", warpName));
					return;
				}
			}
			}
		}
	}

}
