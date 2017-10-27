package PlayerWarpGUI.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.locale.LocaleLoader;

/**
 * Holds some methods used in the commands classes.
 * 
 * @author Judgetread
 * @version 1.0
 */
final class Utils {

	/**
	 * Constructor
	 */
	private Utils() {
	}

	/**
	 * Check sender isn't the console and a instance of player.
	 * 
	 * @param sender Take sender from @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 * @return boolean Returns true/false if sender is a player.
	 */
	public static boolean noConsoleUsage(CommandSender sender) {
		if (sender instanceof Player) {
			return false;
		}
		Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("Commands.NoConsole"));
		return true;
	}
}
