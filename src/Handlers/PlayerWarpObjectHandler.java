package Handlers;

import java.util.ArrayList;
import java.util.UUID;

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
			ArrayList<String> loreList) {
		new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon, loreList);
	}
	
	// ------------------------------------------------------
	// getPlayerWarpObjects - return array of playerworpobjects
	// ------------------------------------------------------
	public static ArrayList<PlayerWarpObject> getPlayerWarpObjects(UUID playerUUID) {
		ArrayList<PlayerWarpObject> playWarpObjectsList = null;
		
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
	public static PlayerWarpObject getPlayerWarpObject(UUID playerUUID, String warpName) {
		for (PlayerWarpObject pwo : pl.getPlayerWarpObjects()) {
			if (pwo.getPlayerUUID().equals(playerUUID) && pwo.getWarpName().equals(warpName)) {
				return pwo;
			}
		}
		return null;
	}
	
	
	

}
