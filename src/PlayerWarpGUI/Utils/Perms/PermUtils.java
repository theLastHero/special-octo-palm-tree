package PlayerWarpGUI.Utils.Perms;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.locale.LocaleLoader;

/**
 * Holds  methods related to Permissions processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class PermUtils {
	
	/**
	 * Plugin instance
	 */
	private static PlayerWarpGUI p;
	/**
	 * This class instance
	 */
	private static PermUtils instance;

	/**
	 * Constructor
	 * 
	 * @param p plugin reference
	 */
	
	public PermUtils(PlayerWarpGUI p) {
		PermUtils.p = p;
	}

	/**
	 * Get this classes instance
	 * 
	 * @return PermUtils
	 */
	public static PermUtils getInstance() {
		if (instance == null) {
			instance = new PermUtils(p);
		}

		return instance;
	}
	
	/**
	 * Check player has permission, else send a no permission message.
	 * 
	 * @param player
	 * @param perm
	 * @param msg
	 * @return
	 */
	public boolean checkPerm(final Player player, final String perm, final String msg) {
		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_NO_PERMISSION", msg));
			return false;
		}
		return true;
	}
	
	/**
	 * Get largest permission value of a group.
	 * eg:
	 * playerwarp.setlore.1
	 * playerwarp.setlore.2
	 * playerwarp.setlore.3
	 * 
	 * Method will return 3 as it is the largest permission in that group.
	 * 
	 * @param player
	 * @param perm perm
	 * @param splitter splitter
	 * @return integer integer
	 */
	public int getLargestPerm(Player player, String perm, String splitter) {
		int maxAllowed = 1;
		for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
			maxAllowed = permSubCount(perm, splitter, maxAllowed, permission);
		}
		return maxAllowed;
	}

	/**
	 * Break down a group of permissions and find the largest value.
	 * eg:
	 * playerwarp.setlore.1
	 * playerwarp.setlore.2
	 * playerwarp.setlore.3
	 * 
	 * Method will return 3 as it is the largest permission in that group.
	 * 
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
