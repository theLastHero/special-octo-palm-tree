package PlayerWarpGUI.Handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.earth2me.essentials.Essentials;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Hooks.EssentialsHook;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;

/**
 * TeleportHandler
 * 
 * <P>
 * Checks teleport location, before event occurs.
 * 
 * @author Judgetread
 * @version 1.0
 */

public class TeleportHandler {

	/** Essentails - for /back command */
	public static Essentials ess3 = null;

	/**
	 * Constructor.
	 */
	public TeleportHandler() {
	}

	/** tpQueue - List of player UUIDs and Tasks. */
	private static Map<UUID, BukkitTask> tpQueue = new HashMap<UUID, BukkitTask>();
	/** godModeQueue - List of player in godmode after teleporting */
	@SuppressWarnings("unused")
	private Map<UUID, BukkitTask> godModeQueue = new HashMap<UUID, BukkitTask>();

	/**
	 * starts the teleport process.
	 * 
	 * @param player player teleporting
	 * @param location location teleporting to.
	 */
	public void startTeleport(Player player, Location location) {
		if (Config.getInstance().getTeleportCancel()) {
			TeleportHandler.tpQueue.put(player.getUniqueId(),
					new Teleport(Config.getInstance().getTeleportCancelCooldown(), player.getUniqueId(), location)
							.runTaskTimer(PlayerWarpGUI.p, 0, 20));
		}
	}

	/**
	 * <P>
	 * Starts BukkitRunnable for teleport.
	 * 
	 * @author Judgetread
	 * @version 1.0
	 */
	private class Teleport extends BukkitRunnable {
		int count;
		Player player;
		Location location;

		/**
		 * 
		 * 
		 * @param count countdown timer.
		 * @param player player to teleport.
		 * @param loc location to teleport too.
		 */
		Teleport(int count, UUID player, Location loc) {
			this.count = count;
			this.player = Bukkit.getPlayer(player);
			this.location = loc;
		}

		/**
		 * run part of runnable
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
				if (new EssentialsHook().useBack()) {
					new EssentialsHook().setBackUser(player);
				}
				player.teleport(location);
				tpQueue.remove(player.getUniqueId());
				this.cancel();
				player.sendMessage(
						LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("TELEPORT_COMPLETED"));
				// call godMode Code
				/*
				 * if (PlayerWarpGUI.godModeAfterTP > 0) { godModeQueue.put(player, new
				 * GodMode(PlayerWarpGUI.godModeAfterTP, player)
				 * .runTaskTimer(PlayerWarpGUI.getInstance(), 0, 20)); }
				 */
			}
		}

		/**
		 * teleportCountMessage
		 * Sends countdown message to player.
		 * 
		 * @param player
		 * @param count
		 */
		private void teleportCountMessage(Player player, int count) {
			player.sendMessage(LocaleLoader.getString("TELEPORT_COUNTDOWN", count));
		}

	}

	/**
	 * Check player is in Teleport queue
	 * 
	 * @param player Player to check
	 */
	public static void checkTeleportQueue(Player player) {
		if (tpQueue.containsKey(player.getUniqueId())) {
			cancelTeleportPlayer(player);
		}
	}

	/**
	 * cancelTeleport if movement config enabled.
	 * @param player Player to apply to.
	 */
	protected static void cancelTeleportPlayer(Player player) {
		tpQueue.get(player.getUniqueId()).cancel();
		tpQueue.remove(player.getUniqueId());
		player.sendMessage(
				LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("TELEPORT_CANCEL_MOVEMENT"));
	}

}
