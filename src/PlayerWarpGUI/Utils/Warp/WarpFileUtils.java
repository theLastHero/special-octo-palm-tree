package PlayerWarpGUI.Utils.Warp;

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
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.locale.LocaleLoader;

/**
 * WarpFileUtils
 * 
 * <P> Contains methods used in reading from and writing to warp files.
 * 
 * @author Judgetread
 * @version 1.0
 */

/**
 * @author Tony
 *
 */
public class WarpFileUtils {
	private static WarpFileUtils instance;

	/**
	 * Return instance of this class.
	 * 
	 * @return instance
	 */
	public static WarpFileUtils getInstance() {
		if (instance == null) {
			instance = new WarpFileUtils();
		}

		return instance;
	}

	
	/**
	 * Check to see if warp folder exists. If not creates warp folder.
	 * And send message to console.
	 */
	public void checkCreateWarpFolder() {
		File directory = new File(PlayerWarpGUI.getPathWarps());
		if (!directory.exists()) {
			Bukkit.getConsoleSender()
					.sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX") + LocaleLoader.getString("CONSOLE_MSG_CREATE_FOLDER", PlayerWarpGUI.getPathWarps()));
			directory.mkdirs();
		}
	}
	
	/**
	 * Loop through all warp file and through all warps in<br>
	 * each file and create warp object from that data.
	 */
	public void createAllWarpsFromFile() {
		int warpFilesCount = 0;
		int warpCount = 0;

		File warpsFolder = new File(PlayerWarpGUI.getPathWarps());

		if (!(new File(PlayerWarpGUI.getPathWarps()).listFiles() == null)) {
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
	 * Display console message with warp file count and total number of warps.
	 * 
	 * @param warpFilesCount
	 * @param warpCount
	 */

	public void consoleMsgWarpCount(int warpFilesCount, int warpCount) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX")
					+ LocaleLoader.getString("CONSOLE_MSG_WARPFILE_COUNT", "" + warpFilesCount));
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX")
					+ LocaleLoader.getString("CONSOLE_MSG_WARPS_COUNT", "" + warpCount));
	}

	/**
	 * Set an ArrayList in a warp file.<br>
	 * This can be used for any array in a warpfile. such as banlist, lorelist etc.
	 * 
	 * @param playerDataFile
	 * @param warpName
	 * @param arrayName
	 * @param aArray
	 * @return File
	 */
	public boolean setsingleWarpArray(File playerDataFile, String warpName, String arrayName,
			ArrayList<String> aArray) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName + "." + arrayName, aArray);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * public access to create warp from file.
	 * 
	 * @param file
	 * @param playerUUID
	 * 
	 * @return integer
	 */
	public int createWarpFromFile(File file, UUID playerUUID) {
		return loadCreateWarp(file, playerUUID, 0);
	}

	/**
	 * Grab all the data from a warp ina  warp file.
	 * 
	 * @param file
	 * @param playerUUID
	 * @param warpCount
	 * @return integer
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
			// e.printStackTrace();
		}
		return warpCount;
	}

	/**
	 * Create warp object???
	 * TODO is this double method?
	 * 
	 * @param playerUUID
	 * @param warpCount
	 * @param warpName
	 * @param warpLocation
	 * @param warpIcon
	 * @param warpTitle
	 * @param loreList
	 * @param banList
	 * @return integer
	 */
	private int createWarp(UUID playerUUID, int warpCount, String warpName, String warpLocation, String warpIcon,
			String warpTitle, ArrayList<String> loreList, ArrayList<String> banList) {
		try {
			ObjectUtils.getInstance().createWarpObjects(playerUUID, warpName, warpLocation, warpTitle, warpIcon,
					loreList, banList);
			warpCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return warpCount;
	}

	/**
	 * Get the UUID from a filename.<br>
	 * If not valid return null.
	 * 
	 * @param fileName
	 * @return UUID/null
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
	 * Check if player is valid.
	 * 
	 * @param playerUUID
	 * @return true/false
	 */
	public boolean isValidPlayer(UUID playerUUID) {
		try {
			Bukkit.getOfflinePlayer(playerUUID).hasPlayedBefore();
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;

	}

	/**
	 * Set one value in a warp file to String value.<br>
	 * Such as title, icon.
	 * 
	 * @param playerDataFile
	 * @param warpName
	 * @param subName
	 * @param value
	 * @return boolean
	 */
	public boolean setSingleWarpValue(File playerDataFile, String warpName, String subName, String value) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName + "." + subName, value);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	/**
	 * Set one value in a warp file to null.<br>
	 * Such as title, icon.
	 * 
	 * @param playerDataFile
	 * @param warpName
	 * @return true/false
	 */
	public boolean removeSingleWarpValue(File playerDataFile, String warpName) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName, null);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	
	/**
	 * Add a new warp to a player warp file.
	 * 
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
	 * Set the complete data of a warp in a players warp file by name.
	 * 
	 * @param playerDataFile
	 * @param location
	 * @param name
	 * @param title
	 * @param icon
	 * @param lore
	 * @param ban
	 * @return {@link FileConfiguration}
	 */
	private FileConfiguration setWarpConfigData(File playerDataFile, Location location, String name, String title,
			String icon, ArrayList<String> lore, ArrayList<String> ban) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + name, "");
		warpConfig.set("warps." + name + ".location", StringUtils.getInstance().loc2str(location));
		warpConfig.set("warps." + name + ".title", title);
		warpConfig.set("warps." + name + ".icon", icon);
		warpConfig.set("warps." + name + ".lore", lore);
		warpConfig.set("warps." + name + ".ban", ban);
		return warpConfig;
	}

	/**
	 * Save the changes to a players warp file.
	 * 
	 * @param playerDataFile
	 * @param warpConfig
	 * @return true/false
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
	 * Check if a player warps file exists.
	 * 
	 * @param uuid
	 * @return File
	 */
	public File checkWarpsExsits(UUID uuid) {
		File warpsFolder = new File(PlayerWarpGUI.getPathWarps());
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
	 * Create a new empty players warp file.
	 * 
	 * @param uuid
	 * @return File
	 */
	public File createPlayerWarpFile(UUID uuid) {
		File f = new File(PlayerWarpGUI.getPathWarps() + uuid.toString() + ".yml");
		StringUtils.getInstance().copy(PlayerWarpGUI.p.getResource("defaults/" + "defaultWarpConfig.yml"), f);
		return f;
	}


}
