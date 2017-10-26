package PlayerWarpGUI.Handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.earth2me.essentials.Essentials;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Hooks.EssentialsHook;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;


public class TeleportHandler {


	private static PlayerWarpGUI p;
	public static Essentials ess3 = null;

	/**
	 * @param pl
	 */
	public TeleportHandler(PlayerWarpGUI p) {
		TeleportHandler.p = p;
	}
	
	private static  Map<UUID, BukkitTask> tpQueue = new HashMap<UUID, BukkitTask>();
	@SuppressWarnings("unused")
	private  Map<UUID, BukkitTask> godModeQueue = new HashMap<UUID, BukkitTask>();
	
	/**
	 * @param player
	 * @param location
	 */
	public void startTeleport(Player player, Location location) {
		if (Config.getInstance().getTeleportCancel()) {
			TeleportHandler.tpQueue.put(player.getUniqueId(),
					new Teleport(Config.getInstance().getTeleportCancelCooldown(), player.getUniqueId(), location)
							.runTaskTimer(p, 0, 20));
		}
	}
	
	public Plugin a() {
		return p;
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
				if (new EssentialsHook().useBack()){
					new EssentialsHook().setBackUser(player);
					}
				player.teleport(loc);
				tpQueue.remove(player.getUniqueId());
				this.cancel();
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("TELEPORT_COMPLETED"));
				// call godMode Code
				/*
				 * if (PlayerWarpGUI.godModeAfterTP > 0) { godModeQueue.put(player, new
				 * GodMode(PlayerWarpGUI.godModeAfterTP, player)
				 * .runTaskTimer(PlayerWarpGUI.getInstance(), 0, 20)); }
				 */
			}
		}

		/**
		 * @param player
		 * @param count
		 */
		private void teleportCountMessage(Player player, int count) {
			player.sendMessage(
					LocaleLoader.getString("TELEPORT_COUNTDOWN", count));
		}
	}

	/**
	 * @param playerp
	 */
	public static void checkTeleport(Player playerp) {
		if (tpQueue.containsKey(playerp.getUniqueId())) {
			cancelTeleportPlayer(playerp);
		}
	}

	/**
	 * @param p
	 */
	
	protected static void cancelTeleportPlayer(Player player) {
		tpQueue.get(player.getUniqueId()).cancel();
		tpQueue.remove(player.getUniqueId());
		player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("TELEPORT_CANCEL_MOVEMENT"));
	}

}
