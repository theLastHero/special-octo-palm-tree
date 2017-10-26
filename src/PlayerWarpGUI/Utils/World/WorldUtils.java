package PlayerWarpGUI.Utils.World;

import org.bukkit.Location;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.config.Config;

public class WorldUtils {
	
	private static PlayerWarpGUI p;
	private static WorldUtils instance;

	/**
	 * @param p
	 */
	
	public WorldUtils(PlayerWarpGUI p) {
		WorldUtils.p = p;
	}

	/**
	 * @return
	 */
	public static WorldUtils getInstance() {
		if (instance == null) {
			instance = new WorldUtils(p);
		}

		return instance;
	}
	
	/**
	 * @param location
	 * @return
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
