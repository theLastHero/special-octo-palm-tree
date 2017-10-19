package Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerListener implements Listener {

	public static PlayerWarpGUI pl;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public PlayerListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		pl.getTeleportHandler().checkTeleport(p);
	}
	


}
