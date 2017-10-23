package PlayerWarpGUI.Listeners;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.ps.PS;

import PlayerWarpGUI.PlayerWarpGUI;

public class FactionsListener implements Listener {

	private static PlayerWarpGUI p;
	
	/**
	 * @param pl
	 */
	public FactionsListener(PlayerWarpGUI p) {
		FactionsListener.p = p;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void lossOfLand(EventFactionsChunksChange e) {

		if (p.getFactionsHook().isEnabled()) {
			if (p.getConfig().getBoolean("Factions.delete-warps-if-zone-is-deleted")) {
				for (int i = 0; i < p.getPlayerWarpObjects().size(); i++) {

					Location loc = p.getOtherFunctions().str2loc(p.getPlayerWarpObjects().get(i).getWarpLocation());
					String warpName = p.getPlayerWarpObjects().get(i).getWarpName();
					UUID playerUUID = Bukkit.getOfflinePlayer(p.getPlayerWarpObjects().get(i).getPlayerUUID())
							.getUniqueId();
					World world = loc.getWorld();
					// loop through warps
					// is warp in the chunk that just got deleted
					// then delete warp
					//

					for (Entry<PS, Faction> entry : e.getOldChunkFaction().entrySet()) {
						PS chunk = entry.getKey();

						if (world.getChunkAt(loc) == chunk.asBukkitChunk()) {
							p.getPlayerWarpFileHandler().removeSingleWarpValue(
									p.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID), warpName);

							p.getPlayerWarpObjects().get(i).removePlayerWarpObject();
							p.getMsgSend().send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),
									p.localeLoader.getString("FACTIONS_CLAIM_DELETED", warpName));
						}

					}
				}
			}
		}
	}

}