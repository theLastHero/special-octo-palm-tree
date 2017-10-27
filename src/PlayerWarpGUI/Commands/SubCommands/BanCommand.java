package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.Player.PlayerUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.locale.LocaleLoader;

/**
* Command: Add players to the ban list of a specific warp.
* Stores banned players UUID in a list of the warp file
* they are banned for.
*  
* @author Judgetread
* @version 1.0
*/
public class BanCommand implements CommandExecutor {

	
	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!(args.length == 2)) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("COMMAND_USE_BAN"));
			return false;
		}

		// update warp file
		if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[0]));
			return false;
		}

		// is player to be ban a valid player?
		if (!PlayerUtils.getInstance().isValidPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_BAN_UNKNOWN_PLAYER"));
			return false;
		}

		final PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(), args[0]);
		String error = "";
		if (ObjectUtils.getInstance().isPlayerOnBannedList(pwo.getBanList(),
				Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString())) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_BANNED_PLAYER_ALREADY", args[1], args[0]));
			return false;
		}
		pwo.getBanList().add(Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString());
		error = "COMMAND_BANNED_PLAYER_COMPLETED";

		// add to file
		WarpFileUtils.getInstance().setsingleWarpArray(
				WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[0], "ban", pwo.getBanList());

		player.sendMessage(LocaleLoader.getString(error) + Bukkit.getOfflinePlayer(args[1]).getName() + args[0]);

		return true;

	}
}
