package Listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;

public class CommandListener implements CommandExecutor {

	public static PlayerWarpGUI pl;

	@SuppressWarnings("unchecked")
	public CommandListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			pl.messageHandler
					.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_COMMAND_ERROR", cmd.getName()));
			return false;
		}

		final Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("playerwarps")) {

			// show warps
			if ((args.length == 0) || (args[0].equalsIgnoreCase("show"))) {
				cmdShow(player, cmd, args);
				return true;
			}

			// show warps
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("list"))) {
				cmdList(player, cmd, args);
				return true;
			}

			// set a warp
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("setwarp") || args[0].equalsIgnoreCase("set"))) {
				cmdSet(player, cmd, args);
				return true;
			}

			// delete a warp
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("delete"))) {
				cmdDelete(player, cmd, args);
			}

			// set title
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("title"))) {
				cmdTitle(player, cmd, args);
			}

		}
		return false;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean checkPerm(Player player, String perm, String msg) {
		if (!player.hasPermission(perm)) {
			pl.messageHandler.sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_NO_PERMISSION", msg));
			return false;
		}
		return true;
	}



	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean checkArgs(Player player, String[] args, int size, String errorMsg) {
		if (args.length != size) {
			pl.messageHandler.sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdShow(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.show", pl.getLanguageHandler().getMessage("COMMAND_USE_SHOW"))) {
			pl.getGuiObject().openGUI(player, 0);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdList(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.list", pl.getLanguageHandler().getMessage("COMMAND_USE_LIST"))) {
			if (!checkArgs(player, args, 1, pl.getLanguageHandler().getMessage("COMMAND_USE_LIST"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdSet(Player player, Command cmd, String[] args) {

		if (checkPerm(player, "playerwarpgui.set", pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdDelete(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.set", pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdTitle(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.title", pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);
			return true;
		}
		return false;
	}

}
