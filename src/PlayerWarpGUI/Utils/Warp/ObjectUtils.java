package PlayerWarpGUI.Utils.Warp;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;

public class ObjectUtils {
	
	private static ObjectUtils instance;


	/**
	 * @return
	 */
	public static ObjectUtils getInstance() {
		if (instance == null) {
			instance = new ObjectUtils();
		}

		return instance;
	}
	


	/**
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
	 * @param banList
	 * @param playerUUIDString
	 * @return
	 */
	public boolean isPlayerOnBannedList(ArrayList<String> banList, String playerUUIDString) {
		for (int i = 0; i < banList.size(); i++) {
			if(banList.get(i).equals(playerUUIDString)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param playerUUID
	 * @return
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
	
	public static PlayerWarpObject getPlayerWarpObject(int uid) {
		for (PlayerWarpObject n : PlayerWarpGUI.getPwoList()) {
			if (n.getUid() == uid) {
				return n;
			}
		}
		return null;

	}
	/**
	 * @param playerUUID
	 * @param warpName
	 * @return
	 */
	public boolean checkPlayerWarpObject(UUID playerUUID, String warpName) {
		if(getPlayerWarpObject(playerUUID, warpName) != null) {
			return true;
		}
		return false;
	}
	

}
