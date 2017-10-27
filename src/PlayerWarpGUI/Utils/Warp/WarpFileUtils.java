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
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;
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
	 * 
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
	 * @param file
	 * @param playerUUID
	 * 
	 * @return File
	 */
	public int createWarpFromFile(File file, UUID playerUUID) {
		return loadCreateWarp(file, playerUUID, 0);
	}

	/**
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
	 * @param fileName
	 * 
	 * @return UUID
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
	 * 
	 * @return boolean
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
	 * @param playerDataFile
	 * @param warpName
	 * @return boolean
	 */
	public boolean removeSingleWarpValue(File playerDataFile, String warpName) {
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		warpConfig.set("warps." + warpName, null);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

	public boolean addWarpToPlayerWarpFile(File playerDataFile, Location location, String name, String title,
			String icon, ArrayList<String> lore, ArrayList<String> ban) {

		FileConfiguration warpConfig = setWarpConfigData(playerDataFile, location, name, title, icon, lore, ban);
		return savePlayerDataFile(playerDataFile, warpConfig);
	}

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

	public File createPlayerWarpFile(UUID uuid) {
		File f = new File(PlayerWarpGUI.getPathWarps() + uuid.toString() + ".yml");
		StringUtils.getInstance().copy(PlayerWarpGUI.p.getResource("defaults/" + "defaultWarpConfig.yml"), f);
		return f;
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
	 * @return boolean
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
	 * @param playerUUID
	 * @return ArrayList<PlayerWarpObject>
	 */
	public ArrayList<PlayerWarpObject> getPlayerWarpObjects(UUID playerUUID) {
		ArrayList<PlayerWarpObject> playWarpObjectsList = new ArrayList<PlayerWarpObject>();

		for (PlayerWarpObject pwo : PlayerWarpGUI.p.getPlayerWarpObjects()) {
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
	 * 
	 */
	public PlayerWarpObject getPlayerWarpObject(UUID playerUUID, String warpName) {
		for (PlayerWarpObject pwo : PlayerWarpGUI.p.getPlayerWarpObjects()) {
			if (pwo.getPlayerUUID().equals(playerUUID) && pwo.getWarpName().equals(warpName)) {
				return pwo;
			}
		}
		return null;
	}

	/**
	 * @param playerUUID
	 * @param warpName
	 * @return boolean
	 */
	public boolean checkPlayerWarpObject(UUID playerUUID, String warpName) {
		if (getPlayerWarpObject(playerUUID, warpName) != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 * @param perm
	 * @param splitter
	 * @return integer
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
	 * @return integer
	 */
	private int permSubCount(String perm, String splitter, int returnAllowed, PermissionAttachmentInfo permission) {
		if (permission.getPermission().equals(perm)) {
			returnAllowed = 1;
		}

		if (permission.getPermission().startsWith(perm + splitter)) {
			String result[] = permission.getPermission().split(perm + splitter);
			String returnValue = result[result.length - 1];
			if (returnValue != null && StringUtils.getInstance().isInt(returnValue)) {
				int validInt = Integer.parseInt(returnValue);
				if (validInt > returnAllowed) {
					returnAllowed = validInt;
				}
			}

		}
		return returnAllowed;
	}

}
