package Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;

import PlayerWarpGUI.PlayerWarpGUI;

public class ResidenceListener implements Listener {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ResidenceListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@EventHandler
	public void deleteWarpOnDeleteClaim(ResidenceDeleteEvent e) {
		Bukkit.broadcastMessage("Event Called");
		if(pl.getResidenceHook().isEnabled()) {
		
		if (pl.getConfig().getBoolean("Residence.delete-warps-if-region-is-deleted")) {
			for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

				Location loc = pl.getOtherFunctions().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (pl.getResidenceHook().isInClaim(loc) != null) {
					pl.getPlayerWarpFileHandler().removeSingleWarpValue(
							pl.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID), warpName);

					pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					pl.getMessageHandler().sendPlayerMessage(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),pl.getLanguageHandler().getMessage("RESIDENCE_CLAIM_DELETED", warpName));
				}
			}
			}
		}
	}

}
