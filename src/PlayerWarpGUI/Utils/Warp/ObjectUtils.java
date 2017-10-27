package PlayerWarpGUI.Utils.Warp;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;

/**
 * Holds methods related to Warp Object processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class ObjectUtils {

	/**
	 * Hold this classes instance.
	 */
	private static ObjectUtils instance;

	/**
	 * Get this class instance.
	 * 
	 * @return ObjectUtils
	 */
	public static ObjectUtils getInstance() {
		if (instance == null) {
			instance = new ObjectUtils();
		}

		return instance;
	}

	/**
	 * Call PlayerWarpObject constructor to create a new <br>
	 * warp object.
	 * 
	 * @param playerUUID
	 * @param warpName
	 * @param warpLocation
	 * @param title
	 * @param icon
	 * @param loreList
	 * @param banList
	 */
	public void createWarpObjects(UUID playerUUID, String warpName, String warpLocation, String title, String icon,
			ArrayList<String> loreList, ArrayList<String> banList) {
		new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon, loreList, banList, PlayerWarpGUI.p);
	}

	/**
	 * Check if player is on the specified warps banned list.
	 * 
	 * @param banList
	 * @param playerUUIDString
	 * @return true/false
	 */
	public boolean isPlayerOnBannedList(ArrayList<String> banList, String playerUUIDString) {
		for (int i = 0; i < banList.size(); i++) {
			if (banList.get(i).equals(playerUUIDString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get a ArrayList of PlayerWarpObject that belong to Player.
	 * 
	 * @param playerUUID
	 * @return ArrayList of PlayerWarpObject
	 */
	public ArrayList<PlayerWarpObject> getPlayerWarpObjects(UUID playerUUID) {
		ArrayList<PlayerWarpObject> playWarpObjectsList = new ArrayList<PlayerWarpObject>();

		for (PlayerWarpObject pwo : PlayerWarpGUI.getPwoList()) {
			if (pwo.getPlayerUUID().equals(playerUUID)) {
				playWarpObjectsList.add(pwo);
			}
		}
		return playWarpObjectsList;
	}

	/**
	 * Get a single warp object matching a player and warp name.<br>
	 * Return null else.
	 * 
	 * @param playerUUID
	 * @param warpName
	 * @return PlayerWarpObject
	 */
	public PlayerWarpObject getPlayerWarpObject(UUID playerUUID, String warpName) {
		for (PlayerWarpObject pwo : PlayerWarpGUI.getPwoList()) {
			if (pwo.getPlayerUUID().equals(playerUUID) && pwo.getWarpName().equals(warpName)) {
				return pwo;
			}
		}
		return null;
	}

	/**
	 * Get a single warp object matching the uid.<br>
	 * Return null else.
	 * 
	 * @param uid
	 * @return PlayerWarp
	 */
	public static PlayerWarpObject getPlayerWarpObject(int uid) {
		for (PlayerWarpObject n : PlayerWarpGUI.getPwoList()) {
			if (n.getUid() == uid) {
				return n;
			}
		}
		return null;
	}

	/**
	 * return true or false if a warp object exists matching the player and warp name.
	 * 
	 * @param playerUUID
	 * @param warpName
	 * @return true/false
	 */
	public boolean checkPlayerWarpObject(UUID playerUUID, String warpName) {
		if (getPlayerWarpObject(playerUUID, warpName) != null) {
			return true;
		}
		return false;
	}

}
