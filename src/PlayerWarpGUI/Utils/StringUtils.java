package PlayerWarpGUI.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import PlayerWarpGUI.locale.LocaleLoader;

/**
 * Holds methods related to String processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class StringUtils {
	
	/**
	 * Holds this classes instance.
	 */
	private static StringUtils instance;
	/**
	 * create instance if null
	 */
	public static StringUtils getInstance() {
		if (instance == null) {
			instance = new StringUtils();
		}

		return instance;
	}
	
	/**
	 * Checks if list is less than the size of size.
	 * 
	 * @param player
	 * @param args
	 * @param size
	 * @param errorMsg
	 * @return true/false
	 */
	public boolean checkArgsString(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length < size) {
			player.sendMessage( "COMMAND_USE_INVALID" + errorMsg);
			return false;
		}
		return true;
	}
	

	/**
	 * Checks if list is equale to the size of size.
	 * 
	 * @param player
	 * @param args
	 * @param size
	 * @param errorMsg
	 * @return true/false
	 */
	public boolean checkArgs(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length != size) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}
	
	
	/**
	 * Check if a string is a int.
	 * 
	 * @param s
	 * @return true/false
	 */
	public boolean isInt(String s) // assuming integer is in decimal number system
	{
		for (int a = 0; a < s.length(); a++) {
			if (a == 0 && s.charAt(a) == '-')
				continue;
			if (!Character.isDigit(s.charAt(a)))
				return false;
		}
		return true;
	}

	
	/**
	 * Transform &color symbols to bukkit.chatcolor
	 * 
	 * @param str
	 * @return String
	 */
	public String replaceColorVariables(String str) {
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;

	}

	
	/**
	 * Replace holders for match string.
	 * [username] replace with players name.
	 * 
	 * @param str
	 * @param playerName
	 * @return String
	 */
	public String replaceHolders(String str, String playerName) {

		// replace [username] variable
		if (!playerName.equals(null)) {
			str = str.replace("[playername]", playerName);
		}
		return str;
	}

	
	/**
	 * Create a ItemStack from a item id.
	 * 
	 * @param itemId
	 * @return ItemStack
	 */
	@SuppressWarnings("deprecation")
	public ItemStack parseItemStackFromString(String itemId) {
		String[] parts = itemId.split(":");
		int matId = Integer.parseInt(parts[0]);
		if (parts.length == 2) {
			short data = Short.parseShort(parts[1]);
			return new ItemStack(Material.getMaterial(matId), 1, data);
		}
		return new ItemStack(Material.getMaterial(matId));
	}
	
	
	/**
	 * Get item ID from a ItemStack.
	 * 
	 * @param iStack
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public String parseStringFromItemStack(ItemStack iStack) {

		int iconID = iStack.getTypeId();
		int iconData = iStack.getData().getData();
		return "" + iconID + ":" + iconData;
	}

	

	/**
	 * Convert a string into a bukkit location. Used <br>
	 * to convert warp locations in player warp files into<br>
	 * a usable format. 
	 * 
	 * @param str
	 * @return Location
	 */
	public Location str2loc(String str) {

		String str2loc[] = str.split("\\:");
		Location loc;
			try {
				loc = new Location(Bukkit.getServer().getWorld(str2loc[0]), 0, 0, 0, 0, 0);
				loc.setX(Double.parseDouble(str2loc[1]));
				loc.setY(Double.parseDouble(str2loc[2]));
				loc.setZ(Double.parseDouble(str2loc[3]));
				loc.setYaw((float) Double.parseDouble(str2loc[4]));
				loc.setPitch((float) Double.parseDouble(str2loc[5]));
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				return loc = null;
			}
		
		return loc;
	}

	

	/**
	 * Convert a bukkit location into a string format we can save<br>
	 * in player warp files.
	 * 
	 * @param loc
	 * @return String
	 */
	public String loc2str(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":"
				+ (float) loc.getYaw() + ":" + (float) loc.getPitch();
	}
	
	/**
	 * Copy a file from resource to a file.
	 * 
	 * @param in
	 * @param file
	 */
	public void copy(InputStream in, File file) {

		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
