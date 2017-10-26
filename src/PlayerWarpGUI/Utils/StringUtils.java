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

public class StringUtils {
	
	private static StringUtils instance;
	/**
	 * @return
	 */
	public static StringUtils getInstance() {
		if (instance == null) {
			instance = new StringUtils();
		}

		return instance;
	}
	
	public boolean checkArgsString(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length < size) {
			player.sendMessage( "COMMAND_USE_INVALID" + errorMsg);
			return false;
		}
		return true;
	}
	
	public boolean checkArgs(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length != size) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}
	
	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
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

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public String replaceColorVariables(String str) {
		str = ChatColor.translateAlternateColorCodes('&', str);
		return str;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public String replaceHolders(String str, String playerName) {

		// replace [username] variable
		if (!playerName.equals(null)) {
			str = str.replace("[playername]", playerName);
		}
		return str;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
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
	
	@SuppressWarnings("deprecation")
	public String parseStringFromItemStack(ItemStack iStack) {

		int iconID = iStack.getTypeId();
		int iconData = iStack.getData().getData();
		return "" + iconID + ":" + iconData;
	}

	public Location parseLoc(String str) {
		String[] arg = str.split(",");
		double[] parsed = new double[5];
		for (int a = 0; a < 3; a++) {
			parsed[a] = Double.parseDouble(arg[a + 1]);
		}

		Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2],
				(float) parsed[3], (float) parsed[4]);
		return location;
	}

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

	public String loc2str(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":"
				+ (float) loc.getYaw() + ":" + (float) loc.getPitch();
	}
	
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
