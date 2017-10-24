package PlayerWarpGUI.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PWOTeletportEvent extends PlayerTeleportEvent{
	
	private int warpID;
	private Location fromLocation;
	private Location toLocation;

	public PWOTeletportEvent(Player player, Location to, int warpID) {
		super(player, player.getLocation(), to, TeleportCause.COMMAND);
		this.warpID = warpID;
		this.fromLocation = player.getLocation();
		this.toLocation = to;
	}
	
	public int getWarpID() {
		return warpID;
	}

	public Location getFromLocation() {
		return fromLocation;
	}
	
	/**
	 * @return
	 */
	public Location getToLocation() {
		return toLocation;
	}

    /* (non-Javadoc)
	 * @see org.bukkit.event.player.PlayerTeleportEvent#getCause()
	 */
	@Override
	public TeleportCause getCause() {
		// TODO Auto-generated method stub
		return super.getCause();
	}



	/** Rest of file is required boilerplate for custom events **/
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
}
}
