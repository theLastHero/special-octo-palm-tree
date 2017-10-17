package Handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpFileHandler {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public PlayerWarpFileHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		checkWarpFolder();
		createAllFromWarpFiles(true);
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------

	public void checkWarpFolder() {
		File directory = new File(pl.getPathWarps());
		if (!directory.exists()) {
			pl.messageHandler.sendConsoleMessage(
					pl.getLanguageHandler().getMessage("CONSOLE_MSG_CREATE_FOLDER", pl.getWarpsName()));
			directory.mkdirs();
		}
	}

	// -----------------------------------------------------
	// loadAllWarpObjects
	// -----------------------------------------------------
	public void createAllFromWarpFiles(boolean verbalise) {
		UUID playerUUID = null;
		int warpFilesCount = 0;
		int warpCount = 0;

		File warpsFolder = new File(pl.getPathWarps());

		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {
;
				if (isValidPlayer(playerUUID = getUUIDFromString(file.getName()))) {
					//increase file count
					warpFilesCount++;

					//increase warpCount & Create warp objects
					warpCount = warpCount + createPlayerWarpsFromFile(file, playerUUID);

				}
			}
		}

		if (verbalise) {
			pl.getMessageHandler().sendConsoleMessage(
					pl.getLanguageHandler().getMessage("CONSOLE_MSG_WARPFILE_COUNT", "" + warpFilesCount));
			pl.getMessageHandler()
					.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_WARPS_COUNT", "" + warpCount));
		}

	}

	public int createPlayerWarpsFromFile(File file, UUID playerUUID) {

		int warpCount = 0;
		FileConfiguration warpsFile = new YamlConfiguration();
		try {
			
			warpsFile.load(file);
			Set<String> keys = warpsFile.getConfigurationSection("warps").getKeys(false);
			
			for (String key : keys) {
				String warpName = key.toString();
				String warpLocation = warpsFile.getString("warps." + key + ".location");
				String warpIcon = warpsFile.getString("warps." + key + ".icon");
				String warpTitle = warpsFile.getString("warps." + key + ".title");
				@SuppressWarnings("unchecked")
				ArrayList<String> loreList = (ArrayList<String>) warpsFile.getList("warps." + key + ".lore");

				try {
					pl.getPlayerWarpObjectHandler().createWarpObjects(playerUUID, warpName, warpLocation, warpTitle, warpIcon,loreList);
					warpCount++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return warpCount;
	}

	public UUID getUUIDFromString(String fileName) {
		// check if file name is a validate UUID
		UUID fileUUID = null;
		try {
			fileUUID = UUID.fromString(fileName.replace(".yml", ""));
		} catch (IllegalArgumentException exception) {
			return null;
		}
		return fileUUID;
	}

	public boolean isValidPlayer(UUID playerUUID) {
		try {
			Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;

	}

	@SuppressWarnings("unchecked")
	public void testFunc() {

		ArrayList<String> loreList;

		// load the configs
		FileConfiguration config = new YamlConfiguration();
		try {

			config.load(pl.getPathWarps() + "a.yml");

			// returns the list of keys at the given configuration section, in this case
			// it'd be test, test2, and test3
			// We're specifing 'false' as the paramater to getKeys() because we don't want a
			// deep search.
			Set<String> keys = config.getConfigurationSection("warps").getKeys(false);

			for (String key : keys) {
				String location = config.getString("warps." + key + ".location");
				String icon = config.getString("warps." + key + ".icon");
				String title = config.getString("warps." + key + ".title");
				loreList = (ArrayList<String>) config.getList("warps." + key + ".lore");

				pl.getMessageHandler().sendConsoleMessage(location);
				pl.getMessageHandler().sendConsoleMessage(icon);
				pl.getMessageHandler().sendConsoleMessage(title);

				for (String x : loreList) {
					pl.getMessageHandler().sendConsoleMessage(x);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public int getWarpAmountAllowed(Player player) {
		int returnMaxWarpsAllowed = 0;

		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {

			if (permission.getPermission().equals("playerwarpgui.set")) {
				returnMaxWarpsAllowed = 1;
			}

			if (permission.getPermission().startsWith("playerwarpgui.set.")) {
				String result[] = permission.getPermission().split("playerwarpgui.set.");
				String returnValue = result[result.length - 1];
				if (returnValue != null && pl.getCalc().isInt(returnValue)) {
					int validInt = Integer.parseInt(returnValue);
					if (validInt > returnMaxWarpsAllowed) {
						returnMaxWarpsAllowed = validInt;
					}
				}

			}

		}

		return returnMaxWarpsAllowed;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public int getWarpCount(Player player) {

		return 0;
	}

}
