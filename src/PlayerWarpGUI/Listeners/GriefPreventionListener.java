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

/**
 * Listened to GriefPrevention Plugin to deleteclaim event.<br>
 * If config enabled, remove any warps when a claim isdeleted.
 * 
 * @author Judgetread
 * @version 1.0
 */
public class GriefPreventionListener implements Listener {

	/**
	 * Constructor
	 */
	public GriefPreventionListener() {
	}

	/**
	 * Catches when a griefprevention claim is deleted. Deletes any warps found in
	 * the deleted claim.
	 * 
	 * @param e
	 */
	@EventHandler
	public void deleteWarpOnDeleteClaim(ClaimDeletedEvent e) {

		if (Config.getInstance().getGPEnabled()) {
			if (Config.getInstance().getGPRemoveOnDelete()) {
				for (int i = 0; i < PlayerWarpGUI.getPwoList().size(); i++) {

					Location location = StringUtils.getInstance()
							.str2loc(PlayerWarpGUI.getPwoList().get(i).getWarpLocation());
					String warpName = PlayerWarpGUI.getPwoList().get(i).getWarpName();
					UUID playerUUID = Bukkit.getOfflinePlayer(PlayerWarpGUI.getPwoList().get(i).getPlayerUUID())
							.getUniqueId();

					if (e.getClaim().contains(location, false, false)) {
						WarpFileUtils.getInstance().removeSingleWarpValue(
								WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

						PlayerWarpGUI.p.getPlayerWarpObjects().get(i).removePlayerWarpObject();
						Bukkit.getOfflinePlayer(playerUUID).getPlayer()
								.sendMessage(LocaleLoader.getString("GRIEFPREVENTION_CLAIM_DELETED", warpName));

					}

				}
			}
		}
	}

}
