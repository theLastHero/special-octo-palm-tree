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

public class DeleteWarpCommand implements CommandExecutor{

	private String perm = "pwarps.setwarp";
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!StringUtils.getInstance().checkArgs(player, args, 2, LocaleLoader.getString("COMMAND_USE_DELETE"))) {
			return false;
		}
		
		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("COMMAND_NO_PERMISSION", "COMMAND_USE_DELETE"));
			return false;
		}
		

		// update warp file
		if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
			player.sendMessage(LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
			return false;
		}
		
		PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(),
				args[1]);

		// delete object
		pwo.removePlayerWarpObject();

		// update file
		// WarpFileUtils.getInstance().addWarpToPlayerWarpFile((WarpFileUtils.getInstance().checkPlayerWarpsExsits(player.getUniqueId())),
		// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
		WarpFileUtils.getInstance().removeSingleWarpValue(
				WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[1]);
		// chestObject.openGUI(player, 0);

		player.sendMessage(LocaleLoader.getString("COMMAND_DELETE_WARP_TEXT", args[1]));

		return true;
	}
		
	

}
