package PlayerWarpGUI.Commands.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Hooks.WorldGuardHook;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class SetWarpCommand implements CommandExecutor {

	private PlayerWarpGUI p;
	private String perm = "pwarps.setwarp";

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		if (!player.hasPermission(perm)) {
			MessageSender.send(player, "COMMAND_NO_PERMISSION", "COMMAND_USE_SET");
			return false;
		}

		p = PlayerWarpGUI.p;
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

		final int maxSizeAllowed = p.getPlayerWarpObjectHandler().geMaxAmountAllowedFromPerm(player, "pwarps.setwarp",
				".");
		final int currentSize = p.getPlayerWarpObjectHandler().getPlayerWarpObjects(player.getUniqueId()).size();
		if (currentSize >= maxSizeAllowed) {
			MessageSender.send(player, "COMMAND_SET_MAX_ALLOWED_TEXT", maxSizeAllowed);
			return false;
		}

		// check if player has warp named same;
		if (p.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
			MessageSender.send(player, "COMMAND_SET_ALLREADY_EXSISTS_TEXT", args[1]);
			return false;
		}

		// isafe location
		if (!p.getWarpHandler().isSafeLocation(player.getLocation())) {
			MessageSender.send(player, "COMMAND_SET_CANCEL_UNSAFE_LOCATION", args[1]);
			return false;
		}

		// world blocked
		if (p.getWarpHandler().isBlockedWorld(player.getLocation())) {
			MessageSender.send(player, "COMMAND_SET_CANCEL_WORLD_BLOCKED", args[1]);
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

		// check for uuid file, if exists than add warp else create file
		// pl.getPlayerWarpFileHandler().checkPlayerWarpsFileExsits(player.getUniqueId());
		p.getPlayerWarpFileHandler().addWarpToPlayerWarpFile(
				(p.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId())), player.getLocation(), args[1],
				"", "", new ArrayList<>(Arrays.asList("", "", "")), new ArrayList<>(Arrays.asList("", "", "")));
		// create object
		// new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon,
		// loreList);7
		p.getPlayerWarpObjectHandler().createWarpObjects(player.getUniqueId(), args[1].toString(),
				p.getOtherFunctions().loc2str(player.getLocation()), "", "", new ArrayList<>(Arrays.asList("")),
				new ArrayList<>(Arrays.asList("")));

		MessageSender.send(player, "COMMAND_SET_COMPLETED_TEXT", args[1]);
		return true;

	}

	private boolean checkCanSetWarp(Player player, String checkCanSetWarp) {
		if (!(checkCanSetWarp == (null))) {
			// msgPlayerCheckCanSetWarp(player, checkCanSetWarp);
			return false;
		}
		return true;
	}

}
