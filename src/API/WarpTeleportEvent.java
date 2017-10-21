package API;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarpTeleportEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Location location;
	private Player player;
	private boolean timerCancelled = false;
	private boolean cancelled = false;

	public WarpTeleportEvent(Player p, Location location) {
		this.player = p;
		this.location = location;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isTimerCancelled() {
		return timerCancelled;
	}

	public void setTimerCancelled(boolean timerCancelled) {
		this.timerCancelled = timerCancelled;
	}
}
