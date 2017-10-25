package PlayerWarpGUI.Utils.Player;

import java.util.UUID;

import org.bukkit.Bukkit;

public class PlayerUtils {
	
	private static PlayerUtils instance;

	/**
	 * @return
	 */
	public static PlayerUtils getInstance() {
		if (instance == null) {
			instance = new PlayerUtils();
		}

		return instance;
	}
	
	public boolean isValidPlayer(UUID playerUUID) {
		try {
			Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;

	}
	

}
