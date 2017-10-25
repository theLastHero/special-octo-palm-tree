package PlayerWarpGUI.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import PlayerWarpGUI.Handlers.TeleportHandler;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerListener implements Listener {

	private static PlayerWarpGUI p;

	/**
	 * @param pl
	 */
	public PlayerListener(PlayerWarpGUI p) {
		PlayerListener.p = p;
	}

	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		p.getTeleportHandler();
		TeleportHandler.checkTeleport(player);
	}
	


}
