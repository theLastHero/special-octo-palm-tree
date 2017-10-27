package PlayerWarpGUI.Handlers;

import org.bukkit.Location;

import PlayerWarpGUI.Utils.Location.LocUtils;
import PlayerWarpGUI.Utils.World.WorldUtils;
import PlayerWarpGUI.locale.LocaleLoader;

/**
 * WarpHandler
 * 
 * <P>
 * Checks teleport location, before event occurs.
 * 
 * @author Judgetread
 * @version 1.0
 */

public class WarpHandler {
	/*
	 * Constructor.
	 */
	public WarpHandler() {
		
	}

	/**
	 * Checks if location is safe to teleport too.
	 * Checks before player is teleported. 
	 * Returns either string representing error or null.
	 * 
	 * @param player Player that is teleporting.
	 * @param location Location to check.
	 * @return error String representing the error else null
	 */
	public String canTeleport(Location location) {

		if (location == null) {
			return LocaleLoader.getString("TELEPORT_CANCEL_INVALID_LOCATION");
		}

		if (!LocUtils.getInstance().isSafeLocation((location))) {
			return LocaleLoader.getString("TELEPORT_CANCEL_UNSAFE_LOCATION");
		}

		if (WorldUtils.getInstance().isBlockedWorld(location)) {
			return LocaleLoader.getString("TELEPORT_CANCEL_WORLD_BLOCKED");
		}

		return null;

	}

}
