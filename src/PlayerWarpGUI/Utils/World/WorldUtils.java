package PlayerWarpGUI.Utils.World;

import org.bukkit.Location;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.config.Config;

/**
 * Holds methods related to World processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class WorldUtils {
	
	/**
	 * This class instance.
	 */
	private static WorldUtils instance;
	/**
	 * Constructor
	 */
	public WorldUtils() {
	}

	/**
	 * Return this classes instance.
	 * 
	 * @return WorldUtils
	 */
	public static WorldUtils getInstance() {
		if (instance == null) {
			instance = new WorldUtils();
		}
		return instance;
	}
	
	/**
	 * Check is world is on the block worlds list in the config file.<br>
	 * If enabled.
	 * 
	 * @param location
	 * @return true/false
	 */
	public boolean isBlockedWorld(Location location) {
		if(Config.getInstance().getBlockedWorlds().size() > 0) {
		for (int i = 0; i < Config.getInstance().getBlockedWorlds().size(); i++) {
			if (((String) Config.getInstance().getBlockedWorlds().get(i)).equalsIgnoreCase(location.getWorld().getName().toString())) {
				return true;
			}
		}
		}
		return false;	
	}

	
}
