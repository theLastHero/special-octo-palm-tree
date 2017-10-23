package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import br.net.fabiozumbi12.RedProtect.events.DeleteRegionEvent;
import config.Config;

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
		if(p.getRedProtectHook().isEnabled()) {
		if (Config.getInstance().getRPRemoveOnDelete()) {
			for (int i = 0; i < p.getPlayerWarpObjects().size(); i++) {

				Location loc = p.getOtherFunctions().str2loc(p.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = p.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(p.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (p.getRedProtectHook().getLocationData(loc) != null) {
					Bukkit.broadcastMessage("InClaim");
					p.getPlayerWarpFileHandler().removeSingleWarpValue(
							p.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID), warpName);

					p.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					p.getMsgSend().send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),p.localeLoader.getString("REDPROTECT_CLAIM_DELETED", warpName));
					return;
				}
			}
			}
		}
	}

}
