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
import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;

public class FactionsListener implements Listener {

	/**
	 * @param pl
	 */
	public FactionsListener(PlayerWarpGUI p) {
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void lossOfLand(EventFactionsChunksChange e) {

		if (Config.getInstance().getFAEnabled()) {
			if (Config.getInstance().getFARemoveOnDelete()) {
				for (int i = 0; i < PlayerWarpGUI.getPwoList().size(); i++) {

					Location loc = StringUtils.getInstance().str2loc(PlayerWarpGUI.getPwoList().get(i).getWarpLocation());
					String warpName = PlayerWarpGUI.getPwoList().get(i).getWarpName();
					UUID playerUUID = Bukkit.getOfflinePlayer(PlayerWarpGUI.getPwoList().get(i).getPlayerUUID())
							.getUniqueId();
					World world = loc.getWorld();
					// loop through warps
					// is warp in the chunk that just got deleted
					// then delete warp
					//
					for (Entry<PS, Faction> entry : e.getOldChunkFaction().entrySet()) {
						PS chunk = entry.getKey();

						if (world.getChunkAt(loc) == chunk.asBukkitChunk()) {
							WarpFileUtils.getInstance().removeSingleWarpValue(
									WarpFileUtils.getInstance().checkWarpsExsits(playerUUID), warpName);

							PlayerWarpGUI.getPwoList().get(i).removePlayerWarpObject();
							MessageSender.send(Bukkit.getOfflinePlayer(playerUUID).getPlayer(),
									LocaleLoader.getString("FACTIONS_CLAIM_DELETED", warpName));
						}

					}
				}
			}
		}
	}

}
