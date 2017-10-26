package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.locale.LocaleLoader;

public class SetIconCommand implements CommandExecutor{
	
	private String perm = "playerwarpsgui.seticon";

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!StringUtils.getInstance().checkArgs(player, args, 2, LocaleLoader.getString("COMMAND_USE_ICON"))) {
			return false;
		}
		
		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_NO_PERMISSION", "COMMAND_USE_ICON"));
			return false;
		}
		

		if (player.getItemInHand().getItemMeta() == null) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_ICON_NOICON_TEXT"));
			return true;
		}
		
		if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
			return false;
		}

		// update warp file
		PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(),
				args[1]);

		// update object
		pwo.setIcon(StringUtils.getInstance().parseStringFromItemStack(player.getItemInHand()));

		WarpFileUtils.getInstance().setSingleWarpValue(
				WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[1], "icon",
				StringUtils.getInstance().parseStringFromItemStack(player.getItemInHand()));
		// chestObject.openGUI(player, 0);

		player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_ICON_COMPLETED_TEXT", args[1]));

		return true;
	

	}

}
