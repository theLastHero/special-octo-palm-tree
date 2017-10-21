package Handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import PlayerWarpGUI.PlayerWarpGUI;

public class TeleportHandler {

	public static PlayerWarpGUI pl;
	public final Map<UUID, BukkitTask> tpQueue = new HashMap<UUID, BukkitTask>();
	public final Map<UUID, BukkitTask> godModeQueue = new HashMap<UUID, BukkitTask>();

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public TeleportHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	/**
	 * @param player
	 * @param location
	 */
	public void startTeleport(Player player, Location location) {
		if (pl.getConfig().getBoolean("teleport.cancel-on-movement")) {
			tpQueue.put(player.getUniqueId(),
					new Teleport(pl.getConfig().getInt("teleport.movement-cooldown"), player.getUniqueId(), location)
							.runTaskTimer(pl.getPlugin(), 0, 20));
		}
	}

	private class Teleport extends BukkitRunnable {
		int count;
		Player player;
		Location loc;

		/**
		 * @param count
		 * @param player
		 * @param loc
		 */
		Teleport(int count, UUID player, Location loc) {
			this.count = count;
			this.player = Bukkit.getPlayer(player);
			this.loc = loc;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (count > 0) {
				teleportCountMessage(player, count);
				count--;
			} else {
				// Teleport code here
				player.teleport(loc);
				tpQueue.remove(player.getUniqueId());
				this.cancel();
				teleportCompletedMessage();
				// call godMode Code
				/*
				 * if (PlayerWarpGUI.godModeAfterTP > 0) { godModeQueue.put(player, new
				 * GodMode(PlayerWarpGUI.godModeAfterTP, player)
				 * .runTaskTimer(PlayerWarpGUI.getInstance(), 0, 20)); }
				 */
			}
		}

		/**
		 * 
		 */
		private void teleportCompletedMessage() {
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("TELEPORT_COMPLETED"));
		}

		/**
		 * @param player
		 * @param count
		 */
		private void teleportCountMessage(Player player, int count) {
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("TELEPORT_COUNTDOWN", count));
		}
	}

	/**
	 * @param p
	 */
	public void checkTeleport(Player p) {
		if (tpQueue.containsKey(p.getUniqueId())) {
			cancelTeleportPlayer(p);
		}
	}

	/**
	 * @param p
	 */
	private void cancelTeleportPlayer(Player p) {
		tpQueue.get(p.getUniqueId()).cancel();
		tpQueue.remove(p.getUniqueId());
		pl.getMessageHandler().sendPlayerMessage(p, pl.getLanguageHandler().getMessage("TELEPORT_CANCEL_MOVEMENT"));
	}

}
