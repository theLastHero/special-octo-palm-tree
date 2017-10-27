package PlayerWarpGUI.Utils.Player;

import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * Holds  methods related to Player processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class PlayerUtils {
	
	/**
	 * This classes instance.
	 */
	private static PlayerUtils instance;

	/**
	 * Get this classes instance
	 * 
	 * @return PlayerUtils
	 */
	public static PlayerUtils getInstance() {
		if (instance == null) {
			instance = new PlayerUtils();
		}

		return instance;
	}
	
	
	/**
	 * Check if player is a valid player
	 * TODO review this code block
	 * 
	 * @param playerUUID
	 * @return true/false
	 */
	public boolean isValidPlayer(UUID playerUUID) {
		try {
			Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;

	}
	

}
