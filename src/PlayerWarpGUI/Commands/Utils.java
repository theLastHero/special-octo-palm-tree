package PlayerWarpGUI.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.locale.LocaleLoader;

public final class Utils {

	private Utils() {}
	
	public static boolean noConsoleUsage(CommandSender sender) {
        if (sender instanceof Player) {
            return false;
}
        MessageSender.sendConsole(LocaleLoader.getString("Commands.NoConsole"));
        return true;
}
}
