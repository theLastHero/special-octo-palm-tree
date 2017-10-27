package PlayerWarpGUI.Utils.Perms;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.locale.LocaleLoader;

public class PermUtils {
	
	private static PlayerWarpGUI p;
	private static PermUtils instance;

	/**
	 * @param p
	 */
	
	public PermUtils(PlayerWarpGUI p) {
		PermUtils.p = p;
	}

	/**
	 * @return PermUtils
	 */
	public static PermUtils getInstance() {
		if (instance == null) {
			instance = new PermUtils(p);
		}

		return instance;
	}
	
	public boolean checkPerm(final Player player, final String perm, final String msg) {
		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_NO_PERMISSION", msg));
			return false;
		}
		return true;
	}
	
	public int getLargestPerm(Player player, String perm, String splitter) {
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
