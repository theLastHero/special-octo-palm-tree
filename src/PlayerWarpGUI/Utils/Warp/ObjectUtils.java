package PlayerWarpGUI.Utils.Warp;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;

public class ObjectUtils {
	
	private static PlayerWarpGUI p;
	private static ObjectUtils instance;

	/**
	 * @param p
	 */
	public ObjectUtils(PlayerWarpGUI p) {
		ObjectUtils.p = p;
	}

	/**
	 * @return
	 */
	public static ObjectUtils getInstance() {
		if (instance == null) {
			instance = new ObjectUtils(p);
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
		
		for (PlayerWarpObject pwo : PlayerWarpGUI.pwoList) {
			if (pwo.getPlayerUUID().equals(playerUUID)) {
				playWarpObjectsList.add(pwo);
			}
		}
		return playWarpObjectsList;
	}
	
	/**
	 * @param playerUUID
	 * @param warpName
	 * @return
	 */
	public PlayerWarpObject getPlayerWarpObject(UUID playerUUID, String warpName) {
		for (PlayerWarpObject pwo : PlayerWarpGUI.pwoList) {
			if (pwo.getPlayerUUID().equals(playerUUID) && pwo.getWarpName().equals(warpName)) {
				return pwo;
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
