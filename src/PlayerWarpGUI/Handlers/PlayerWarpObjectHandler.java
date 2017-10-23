package PlayerWarpGUI.Handlers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;

public class PlayerWarpObjectHandler {
	
	public PlayerWarpGUI pl;
		
	/**
	 * @param pl
	 */
	public PlayerWarpObjectHandler(PlayerWarpGUI pl) {
		this.pl = pl;
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
		new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon, loreList, banList, this.pl);
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
		
		for (PlayerWarpObject pwo : pl.getPlayerWarpObjects()) {
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
		for (PlayerWarpObject pwo : pl.getPlayerWarpObjects()) {
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
	
	/**
	 * @param player
	 * @param perm
	 * @param splitter
	 * @return
	 */
	public int geMaxAmountAllowedFromPerm(Player player, String perm, String splitter) {
		int maxAllowed = 1;
		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
			maxAllowed = permSubCount(perm, splitter, maxAllowed, permission);
		}
		return maxAllowed;
	}

	/**
	 * @param perm
	 * @param splitter
	 * @param returnAllowed
	 * @param permission
	 * @return
	 */
	private int permSubCount(String perm, String splitter, int returnAllowed, PermissionAttachmentInfo permission) {
		if (permission.getPermission().equals(perm)) {
			returnAllowed = 1;
		}

		if (permission.getPermission().startsWith(perm+splitter)) {
			String result[] = permission.getPermission().split(perm+splitter);
			String returnValue = result[result.length - 1];
			if (returnValue != null && pl.getCalc().isInt(returnValue)) {
				int validInt = Integer.parseInt(returnValue);
				if (validInt > returnAllowed) {
					returnAllowed = validInt;
				}
			}

		}
		return returnAllowed;
	}
}
