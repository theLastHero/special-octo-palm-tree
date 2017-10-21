package Listeners;

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

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public FactionsListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void lossOfLand(EventFactionsChunksChange e) {

		if (pl.getFactionsHook().isEnabled()) {
			if (pl.getConfig().getBoolean("Factions.delete-warps-if-zone-is-deleted")) {
				for (int i = 0; i < pl.getPlayerWarpObjects().size(); i++) {

					Location loc = pl.getOtherFunctions().str2loc(pl.getPlayerWarpObjects().get(i).getWarpLocation());
					String warpName = pl.getPlayerWarpObjects().get(i).getWarpName();
					UUID playerUUID = Bukkit.getOfflinePlayer(pl.getPlayerWarpObjects().get(i).getPlayerUUID())
							.getUniqueId();
					World world = loc.getWorld();
					// loop through warps
					// is warp in the chunk that just got deleted
					// then delete warp
					//

					for (Entry<PS, Faction> entry : e.getOldChunkFaction().entrySet()) {
						PS chunk = entry.getKey();

						Bukkit.broadcastMessage("warp chunk: " + world.getChunkAt(loc).toString());
						Bukkit.broadcastMessage("deleted chunk: " + chunk.asBukkitChunk());

						if (world.getChunkAt(loc) == chunk.asBukkitChunk()) {
							pl.getPlayerWarpFileHandler().removeSingleWarpValue(
									pl.getPlayerWarpFileHandler().checkWarpsExsits(playerUUID), warpName);

							pl.getPlayerWarpObjects().get(i).removePlayerWarpObject();
							pl.getMessageHandler().sendPlayerMessage(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),
									pl.getLanguageHandler().getMessage("FACTIONS_CLAIM_DELETED", warpName));
						}

					}
				}
			}
		}
	}

}
