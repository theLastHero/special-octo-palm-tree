package PlayerWarpGUI.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;

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
		if(p.getResidenceHook().isEnabled()) {
		
		if (p.getConfig().getBoolean("Residence.delete-warps-if-region-is-deleted")) {
			for (int i = 0; i < p.getPlayerWarpObjects().size(); i++) {

				Location loc = p.getOtherFunctions().str2loc(p.getPlayerWarpObjects().get(i).getWarpLocation());
				String warpName = p.getPlayerWarpObjects().get(i).getWarpName();
				UUID playerUUID = Bukkit.getOfflinePlayer(p.getPlayerWarpObjects().get(i).getPlayerUUID())
						.getUniqueId();

				if (p.getResidenceHook().getLocationData(loc) != null) {
					p.getPlayerWarpFileHandler().removeSingleWarpValue(
							p.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID), warpName);

					p.getPlayerWarpObjects().get(i).removePlayerWarpObject();
					p.getMsgSend().send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),p.localeLoader.getString("RESIDENCE_CLAIM_DELETED", warpName));
				}
			}
			}
		}
	}

}
