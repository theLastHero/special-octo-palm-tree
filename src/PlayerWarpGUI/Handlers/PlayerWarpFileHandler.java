package PlayerWarpGUI.Handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Chat.MessageSender;

public class PlayerWarpFileHandler {

	private static PlayerWarpGUI p;

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------

	public PlayerWarpFileHandler(PlayerWarpGUI p) {
		PlayerWarpFileHandler.p = p;
	}

	public void checkWarpFolder() {
		File directory = new File(p.getPathWarps());
		if (!directory.exists()) {
			MessageSender.sendConsole("CONSOLE_MSG_CREATE_FOLDER", p.getWarpsName());
			directory.mkdirs();
		}
	}

	/**
	 * 
	 */
	public void createAllWarpsFromFile() {
		int warpFilesCount = 0;
		int warpCount = 0;

		File warpsFolder = new File(p.getPathWarps());

		if (!(new File(p.getPathWarps()).listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {
				if (isValidPlayer(getUUIDFromString(file.getName()))) {
					// increase file count
					warpFilesCount++;
					// increase warpCount & Create warp objects
					warpCount = warpCount + createWarpFromFile(file, getUUIDFromString(file.getName()));
				}
			}
		}
		consoleMsgWarpCount(warpFilesCount, warpCount);
	}

	/**
	 * @param warpFilesCount
	 * @param warpCount
	 */
	public void consoleMsgWarpCount(int warpFilesCount, int warpCount) {
		if (p.isStartup()) {
			MessageSender.sendConsole("CONSOLE_MSG_WARPFILE_COUNT", "" + warpFilesCount);
			MessageSender.sendConsole("CONSOLE_MSG_WARPS_COUNT", "" + warpCount);
		}
	}

	/**
	 * @param uuid
	 * @return
	 */
	public File createPlayerWarpFile(UUID uuid) {
		File f = new File(p.getPathWarps() + uuid.toString() + ".yml");
		p.getOtherFunctions().copy(p.getResource("defaults/" + "defaultWarpConfig.yml"), f);
		return f;
	}

	/**
	 * @param playerDataFile
	 * @param location
	 * @param name
	 * @param title
	 * @param icon
	 * @param lore
	 * @param ban
	 * @return
	 */
	public boolean addWarpToPlayerWarpFile(File playerDataFile, Location location, String name, String title,
			String icon, ArrayList<String> lore, ArrayList<String> ban) {

		FileConfiguration warpConfig = setWarpConfigData(playerDataFile, location, name, title, icon, lore, ban);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * @param playerDataFile
	 * @param location
	 * @param name
	 * @param title
	 * @param icon
	 * @param lore
	 * @param ban
	 * @return
	 */
	private FileConfiguration setWarpConfigData(File playerDataFile, Location location, String name, String title,
			String icon, ArrayList<String> lore, ArrayList<String> ban) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + name, "");
		warpConfig.set("warps." + name + ".location", p.getOtherFunctions().loc2str(location));
		warpConfig.set("warps." + name + ".title", title);
		warpConfig.set("warps." + name + ".icon", icon);
		warpConfig.set("warps." + name + ".lore", lore);
		warpConfig.set("warps." + name + ".ban", ban);
		return warpConfig;
	}

	/**
	 * @param playerDataFile
	 * @param warpConfig
	 */
	private boolean savePlayerDataFile(File playerDataFile, FileConfiguration warpConfig) {
		try {
			warpConfig.save(playerDataFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param playerDataFile
	 * @param warpName
	 * @param subName
	 * @param value
	 * @return
	 */
	public boolean setSingleWarpValue(File playerDataFile, String warpName, String subName, String value) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName + "." + subName, value);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * @param playerDataFile
	 * @param warpName
	 * @return
	 */
	public boolean removeSingleWarpValue(File playerDataFile, String warpName) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName, null);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * @param playerDataFile
	 * @param warpName
	 * @param arrayName
	 * @param aArray
	 * @return
	 */
	public boolean setsingleWarpArray(File playerDataFile, String warpName, String arrayName,
			ArrayList<String> aArray) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName + "." + arrayName, aArray);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * @param uuid
	 * @return
	 */
	public File checkWarpsExsits(UUID uuid) {
		File warpsFolder = new File(p.getPathWarps());
		if (!(warpsFolder.listFiles() == null)) {
			for (File file : warpsFolder.listFiles()) {
				if (file.getName().equals(uuid.toString() + ".yml")) {
					return file;
				}
			}
		}
		return createPlayerWarpFile(uuid);
	}

	/**
	 * @param file
	 * @param playerUUID
	 * @return
	 */
	public int createWarpFromFile(File file, UUID playerUUID) {
		return loadCreateWarp(file, playerUUID, 0);
	}

	/**
	 * @param file
	 * @param playerUUID
	 * @param warpCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int loadCreateWarp(File file, UUID playerUUID, int warpCount) {
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

				warpCount = createWarp(playerUUID, warpCount, warpName, warpLocation, warpIcon, warpTitle, loreList,
						banList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return warpCount;
	}

	/**
	 * @param playerUUID
	 * @param warpCount
	 * @param warpName
	 * @param warpLocation
	 * @param warpIcon
	 * @param warpTitle
	 * @param loreList
	 * @param banList
	 * @return
	 */
	private int createWarp(UUID playerUUID, int warpCount, String warpName, String warpLocation, String warpIcon,
			String warpTitle, ArrayList<String> loreList, ArrayList<String> banList) {
		try {
			p.getPlayerWarpObjectHandler().createWarpObjects(playerUUID, warpName, warpLocation, warpTitle, warpIcon,
					loreList, banList);
			warpCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return warpCount;
	}

	/**
	 * @param fileName
	 * @return
	 */
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

	
	/**
	 * @param playerUUID
	 * @return
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
