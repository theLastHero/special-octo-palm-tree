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

public class WarpFileUtils {
	private static WarpFileUtils instance;

	/**
	 * @return
	 */
	public static WarpFileUtils getInstance() {
		if (instance == null) {
			instance = new WarpFileUtils();
		}

		return instance;
	}

	public void checkWarpFolder() {
		File directory = new File(PlayerWarpGUI.pathWarps);
		if (!directory.exists()) {
			Bukkit.getConsoleSender()
					.sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX") + LocaleLoader.getString("CONSOLE_MSG_CREATE_FOLDER", PlayerWarpGUI.pathWarps));
			directory.mkdirs();
		}
	}

	/**
	 * 
	 */
	public void createAllWarpsFromFile() {
		int warpFilesCount = 0;
		int warpCount = 0;

		File warpsFolder = new File(PlayerWarpGUI.pathWarps);

		if (!(new File(PlayerWarpGUI.pathWarps).listFiles() == null)) {
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
		if (PlayerWarpGUI.p.startup) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX")
					+ LocaleLoader.getString("CONSOLE_MSG_WARPFILE_COUNT", "" + warpFilesCount));
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX")
					+ LocaleLoader.getString("CONSOLE_MSG_WARPS_COUNT", "" + warpCount));
		}
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
	 * @param file
	 * @param playerUUID
	 * 
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
	 * @return
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
	 * 
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
		File warpsFolder = new File(PlayerWarpGUI.pathWarps);
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
		File f = new File(PlayerWarpGUI.pathWarps + uuid.toString() + ".yml");
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
