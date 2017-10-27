package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;

/**
* SetTitle Command: <br>
* Sets the title text shown in the gui menu for the specified warp.
* 
* @author Judgetread
* @version 1.0
*/
public class SetTitleCommand implements CommandExecutor{

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;


		if ((args.length < 2)) {
			Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("COMMAND_USE_TITLE"));
			return false;
		}

		if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[0]));
			return false;
		}

		final StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
		for (int i = 1; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
			sb.append(args[i]); // Adds the argument into the StringBuilder
			sb.append(" "); // Adding a space into the StringBuilder
		}

		String title = sb.toString();
		title = ChatColor.translateAlternateColorCodes('&',title);

		if (title.length() > Config.getInstance().getMaxTitleSize()) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_TITLE_TOOLONG_TEXT",
					"" + Config.getInstance().getMaxTitleSize()));
			return false;
		}

		// update warp file
		final PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(),
				args[0]);

		// update object
		pwo.setTitle(title);

		// update file
		// WarpFileUtils.getInstance().addWarpToPlayerWarpFile((WarpFileUtils.getInstance().checkPlayerWarpsExsits(player.getUniqueId())),
		// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
		WarpFileUtils.getInstance().setSingleWarpValue(
				WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[0], "title", title);
		// chestObject.openGUI(player, 0);

		player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_TITLE_COMPLETED_TEXT", args[0], title));

		return true;
		
		
	}

}
