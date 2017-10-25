package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Player.PlayerUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.locale.LocaleLoader;

public class BanCommand implements CommandExecutor {

	private String perm = "pwarps.ban";

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!StringUtils.getInstance().checkArgs(player, args, 3, LocaleLoader.getString("COMMAND_USE_BAN"))) {
			return false;
		}

		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("COMMAND_NO_PERMISSION", "COMMAND_USE_BAN"));
			return false;
		}

		// update warp file
		if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
			player.sendMessage(LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
			return false;
		}

		// is player to be ban a valid player?
		if (!PlayerUtils.getInstance().isValidPlayer(Bukkit.getOfflinePlayer(args[2]).getUniqueId())) {
			MessageSender.send(player, "COMMAND_BAN_UNKNOWN_PLAYER");
			return false;
		}

		final PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(), args[1]);
		String error = "";
		if (ObjectUtils.getInstance().isPlayerOnBannedList(pwo.getBanList(),
				Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
			MessageSender.send(player, "COMMAND_BANNED_PLAYER_ALREADY", args[2], args[1]);
			return false;
		}
		pwo.getBanList().add(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString());
		error = "COMMAND_BANNED_PLAYER_COMPLETED";

		// add to file
		WarpFileUtils.getInstance().setsingleWarpArray(
				WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[1], "ban", pwo.getBanList());

		player.sendMessage(LocaleLoader.getString(error) + Bukkit.getOfflinePlayer(args[2]).getName() + args[1]);

		return true;

	}
}
