package Handlers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpObjectHandler {
	
	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public PlayerWarpObjectHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}
	

	
	// -----------------------------------------------------
	// createWarpObjects
	// -----------------------------------------------------
	public void createWarpObjects(UUID playerUUID, String warpName, String warpLocation, String title, String icon,
			ArrayList<String> loreList, ArrayList<String> banList) {
		new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon, loreList, banList);
	}
	
	public boolean isPlayerOnBannedList(ArrayList<String> banList, String playerUUIDString) {
		for (int i = 0; i < banList.size(); i++) {
			if(banList.get(i).equals(playerUUIDString)) {
				return true;
			}
		}
		return false;
	}
	
	// ------------------------------------------------------
	// getPlayerWarpObjects - return array of playerworpobjects
	// ------------------------------------------------------
	public ArrayList<PlayerWarpObject> getPlayerWarpObjects(UUID playerUUID) {
		ArrayList<PlayerWarpObject> playWarpObjectsList = new ArrayList<PlayerWarpObject>();
		
		for (PlayerWarpObject pwo : pl.getPlayerWarpObjects()) {
			if (pwo.getPlayerUUID().equals(playerUUID)) {
				playWarpObjectsList.add(pwo);
			}
		}
		return playWarpObjectsList;
	}
	
	// ------------------------------------------------------
	// getPlayerWarpObject - returns single object
	// ------------------------------------------------------
	public PlayerWarpObject getPlayerWarpObject(UUID playerUUID, String warpName) {
		for (PlayerWarpObject pwo : pl.getPlayerWarpObjects()) {
			if (pwo.getPlayerUUID().equals(playerUUID) && pwo.getWarpName().equals(warpName)) {
				return pwo;
			}
		}
		return null;
	}
	
	public boolean checkPlayerWarpObject(UUID playerUUID, String warpName) {
		if(getPlayerWarpObject(playerUUID, warpName) != null) {
			return true;
		}
		return false;
	}
	
	
	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public int geMaxAmountAllowedFromPerm(Player player, String perm, String splitter) {
		int returnAllowed = 1;

		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

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

		}

		return returnAllowed;
	}
}
