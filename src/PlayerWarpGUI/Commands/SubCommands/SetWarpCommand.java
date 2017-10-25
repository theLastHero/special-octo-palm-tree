package PlayerWarpGUI.Commands.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Hooks.WorldGuardHook;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Location.LocUtils;
import PlayerWarpGUI.Utils.Perms.PermUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.Utils.World.WorldUtils;
import PlayerWarpGUI.locale.LocaleLoader;

public class SetWarpCommand implements CommandExecutor {

	private String perm = "pwarps.setwarp";

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!checkArgs(player, args, 2, LocaleLoader.getString("COMMAND_USE_SET"))) {
			return false;
		}
		
		if (!player.hasPermission(perm)) {
			MessageSender.send(player, "COMMAND_NO_PERMISSION", "COMMAND_USE_SET");
			return false;
		}

		// GP check
		if (!checkCanSetWarp(player, new GriefPreventionHook().warpHookCheck(player))) {
			return false;
		}
		// WG check
		if (!checkCanSetWarp(player, new WorldGuardHook().warpHookCheck(player))) {
			return false;
		}
		// RP check
		if (!checkCanSetWarp(player, new RedProtectHook().warpHookCheck(player))) {
			return false;
		}
		// FA check
		if (!checkCanSetWarp(player, new FactionsHook().warpHookCheck(player))) {
			return false;
		}
		// RS check
		if (!checkCanSetWarp(player, new ResidenceHook().warpHookCheck(player))) {
			return false;
		}

		int maxSizeAllowed = PermUtils.getInstance().getLargestPerm(player, "pwarps.setwarp", ".");
		int currentSize = 0;
				if(!(ObjectUtils.getInstance().getPlayerWarpObjects(player.getUniqueId()) == null)) {
					currentSize = ObjectUtils.getInstance().getPlayerWarpObjects(player.getUniqueId()).size();
				}
		
		if (currentSize >= maxSizeAllowed) {
			player.sendMessage(LocaleLoader.getString("COMMAND_SET_MAX_ALLOWED_TEXT", maxSizeAllowed));
			return false;
		}

		// check if player has warp named same;
		if (ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
			player.sendMessage(LocaleLoader.getString("COMMAND_SET_ALLREADY_EXSISTS_TEXT", args[1]));
			return false;
		}

		// isafe location
		if (!LocUtils.getInstance().isSafeLocation(player.getLocation())) {
			player.sendMessage(LocaleLoader.getString("COMMAND_SET_CANCEL_UNSAFE_LOCATION", args[1]));
			return false;
		}

		// world blocked
		if (WorldUtils.getInstance().isBlockedWorld(player.getLocation())) {
			player.sendMessage(LocaleLoader.getString("COMMAND_SET_CANCEL_WORLD_BLOCKED", args[1]));
			return false;
		}

		// check if player can afford to set a warp.
		/*
		 * if (pl.getConfig().getInt("settings.set-warp-cost") != 0) {
		 * pl.getVaultHandler(); EconomyResponse r =
		 * VaultHook.econ.withdrawPlayer(player,
		 * pl.getConfig().getInt("settings.set-warp-cost")); if
		 * (!r.transactionSuccess()) { pl.getMessageHandler().sendPlayerMessage(player,
		 * pl.getLanguageHandler().getMessage("COMMAND_SET_NOT_ENOUGH_MONEY")); return
		 * true; } else { pl.getMessageHandler().sendPlayerMessage(player,
		 * pl.getLanguageHandler().getMessage("COMMAND_SET_ENOUGH_MONEY")); } return
		 * false; }
		 */

		WarpFileUtils.getInstance().addWarpToPlayerWarpFile(
				(WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId())), player.getLocation(),
				args[1], "", "", new ArrayList<>(Arrays.asList("", "", "")),
				new ArrayList<>(Arrays.asList("", "", "")));
		
		ObjectUtils.getInstance().createWarpObjects(player.getUniqueId(), args[1].toString(),
				StringUtils.getInstance().loc2str(player.getLocation()), "", "", new ArrayList<>(Arrays.asList("")),
				new ArrayList<>(Arrays.asList("")));

		player.sendMessage(LocaleLoader.getString("COMMAND_SET_COMPLETED_TEXT", args[1]));
		return true;
	}

	private boolean checkCanSetWarp(Player player, String checkCanSetWarp) {
		if (!(checkCanSetWarp == (null))) {
			player.sendMessage(LocaleLoader.getString(checkCanSetWarp));
			return false;
		}
		return true;
	}
	
	public boolean checkArgs(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length != size) {
			player.sendMessage(LocaleLoader.getString("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}


}