package PlayerWarpGUI.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import PlayerWarpGUI.Handlers.TeleportHandler;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerListener implements Listener {

	
	public PlayerListener(PlayerWarpGUI playerWarpGUI) {
		// TODO Auto-generated constructor stub
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		TeleportHandler.checkTeleport(player);
	}
	


}
