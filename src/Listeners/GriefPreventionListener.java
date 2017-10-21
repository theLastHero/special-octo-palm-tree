package Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;

public class GriefPreventionListener implements Listener {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GriefPreventionListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(ClaimDeletedEvent e) {

		if(pl.getGriefPreventionHook().isEnabled()) {
		if (pl.getConfig().getBoolean("GriefPrevention.delete-warps-if-claim-is-deleted")) {
			for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

				Location loc = pl.getOtherFunctions().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID()).getUniqueId();

				if (pl.getGriefPreventionHook().isInClaim(loc) != null) {
					pl.getPlayerWarpFileHandler().removeSingleWarpValue(pl.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID),
							warpName);

					pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					pl.getMessageHandler().sendPlayerMessage(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),pl.getLanguageHandler().getMessage("GRIEFPREVENTION_CLAIM_DELETED", warpName));
					
				}
			}
			}
		}
	}

}
