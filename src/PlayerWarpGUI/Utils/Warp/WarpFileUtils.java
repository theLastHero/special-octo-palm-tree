package PlayerWarpGUI.Utils.Warp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;

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
		File warpsFolder = new File(PlayerWarpGUI.p.getPathWarps());
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
		File f = new File(PlayerWarpGUI.p.getPathWarps() + uuid.toString() + ".yml");
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
