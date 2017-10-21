package Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import PlayerWarpGUI.PlayerWarpGUI;
import br.net.fabiozumbi12.RedProtect.events.DeleteRegionEvent;

public class RedProtectListener implements Listener {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public RedProtectListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(DeleteRegionEvent e) {
		Bukkit.broadcastMessage("Event Called");
		if(pl.getRedProtectHook().isEnabled()) {
		if (pl.getConfig().getBoolean("RedProtect.delete-warps-if-region-is-deleted")) {
			for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

				Location loc = pl.getOtherFunctions().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (pl.getRedProtectHook().isInClaim(loc) != null) {
					Bukkit.broadcastMessage("InClaim");
					pl.getPlayerWarpFileHandler().deleteSingleWarpFromFile(
							pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(playerUUID), warpName);

					pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					pl.getMessageHandler().sendPlayerMessage(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),pl.getLanguageHandler().getMessage("REDPROTECT_CLAIM_DELETED", warpName));
					return;
				}
				
				Bukkit.broadcastMessage("Not inClaim");
			}
			}
		}
	}

}
