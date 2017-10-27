package PlayerWarpGUI.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import PlayerWarpGUI.Handlers.TeleportHandler;

/**
 * Listened to PlayerMove Event from bukkit.<br>
 * Send teleport class when a player moves. Used to cancel teleporting<br>
 * if cancelOnMovement enabled in config.
 * 
 * @author Judgetread
 * @version 1.0
 */
public class PlayerListener implements Listener {

	/**
	 * Listener to Player move event.<br>
	 * Send teleport class when a player moves. Used to cancel teleporting<br>
	 * if cancelOnMovement enabled in config.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		TeleportHandler.checkTeleportQueue(player);
	}

}
