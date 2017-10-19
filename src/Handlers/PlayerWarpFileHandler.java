package Handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpFileHandler {

	public static PlayerWarpGUI pl;

	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public PlayerWarpFileHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
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
					// increase file count
					warpFilesCount++;

					// increase warpCount & Create warp objects
					warpCount = warpCount + createPlayerWarpObjectFromFile(file, playerUUID);

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

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public File createPlayerWarpFile(UUID uuid) {
		File f = new File(pl.getPathWarps() + uuid.toString() + ".yml");
		pl.getOtherFunctions().copy(pl.getResource("defaults/" + "defaultWarpConfig.yml"), f);
		return f;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean addWarpToPlayerWarpFile(File playerDataFile, Location location, String name, String title,
			String icon, ArrayList<String> lore, ArrayList<String> ban) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);

		warpConfig.set("warps." + name, "");
		warpConfig.set("warps." + name + ".location", pl.getOtherFunctions().loc2str(location));
		warpConfig.set("warps." + name + ".title", title);
		warpConfig.set("warps." + name + ".icon", icon);
		warpConfig.set("warps." + name + ".lore", lore);
		warpConfig.set("warps." + name + ".ban", ban);
		try {
			warpConfig.save(playerDataFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean updateSingleElementWarpFile(File playerDataFile, String warpName, String subName, String value) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);

		warpConfig.set("warps." + warpName + "." + subName, value);
		try {
			warpConfig.save(playerDataFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean deleteSingleWarpFromFile(File playerDataFile, String warpName) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);

		warpConfig.set("warps." + warpName, null);
		try {
			warpConfig.save(playerDataFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean setStringToArrayWarpFile(File playerDataFile, String warpName, String arrayName,
			ArrayList<String> aArray) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);

		// aArray.add(newElement);
		warpConfig.set("warps." + warpName + "." + arrayName, aArray);

		try {
			warpConfig.save(playerDataFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public File checkPlayerWarpsExsits(UUID uuid) {
		File warpsFolder = new File(pl.getPathWarps());

		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {
				if (file.getName().equals(uuid.toString() + ".yml")) {
					return file;
				}
			}
		}
		return createPlayerWarpFile(uuid);
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public int createPlayerWarpObjectFromFile(File file, UUID playerUUID) {

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
				ArrayList<String> loreList = (ArrayList<String>) warpsFile.getList("warps." + key + ".lore");
				ArrayList<String> banList = (ArrayList<String>) warpsFile.getList("warps." + key + ".ban");

				try {
					pl.getPlayerWarpObjectHandler().createWarpObjects(playerUUID, warpName, warpLocation, warpTitle,
							warpIcon, loreList, banList);
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

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
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

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean isValidPlayer(UUID playerUUID) {
		try {
			Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;

	}

}
