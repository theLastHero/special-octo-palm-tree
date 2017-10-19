package Listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import Hooks.GriefPreventionHook.Response;
import Hooks.VaultHook;
import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;
import net.milkbowl.vault.economy.EconomyResponse;

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
			if ((args.length >= 2) && (args[1].equalsIgnoreCase("deletewarp"))) {
				cmdDelete(player, cmd, args);
			}

			// set title
			if ((args.length >= 3) && (args[1].equalsIgnoreCase("settitle"))) {
				cmdTitle(player, cmd, args);
			}

			// set icon
			if ((args.length >= 2) && (args[1].equalsIgnoreCase("seticon"))) {
				cmdIcon(player, cmd, args);
			}

			// set lore
			if ((args.length >= 3) && (args[1].equalsIgnoreCase("ban"))) {
				cmdBan(player, cmd, args, true);
			}

			// set lore
			if ((args.length >= 3) && (args[1].equalsIgnoreCase("unban"))) {
				cmdBan(player, cmd, args, false);
			}

			// set lore
			if ((args.length >= 3) && (args[1].contains("setlore"))) {
				cmdLore(player, cmd, args);
			}

			/*
			 * // set lore if ((args.length >= 2) && (args[1].equalsIgnoreCase("setlore")))
			 * { cmdLore(player, cmd, args); }
			 */
		}
		return false;

	}

	private boolean cmdLore(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.setlore", pl.getLanguageHandler().getMessage("COMMAND_USE_LORE"))) {
			if (!checkArgsString(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_LORE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			int LoreRow = 1;
			String result[] = args[1].split("setlore");
			if (result.length > 0) {
				String returnValue = result[result.length - 1];
				if (returnValue != null && pl.getCalc().isInt(returnValue)) {
					LoreRow = Integer.parseInt(returnValue);
				}
			}

			// get allowed amount

			// check lorerow > allowamount
			// checkPerm(Player player, String perm, String msg) {
			if (LoreRow > 1) {
				if (!checkPerm(player, "playerwarpgui.setlore." + LoreRow,
						pl.getLanguageHandler().getMessage("COMMAND_USE_LORE", LoreRow))) {
					return false;
				}
			}

			StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 2; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			// checl lenght of text
			if (sb.toString().length() > pl.getConfig().getInt("settings.max-lore-text-size")) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_LORE_TOOLONG_TEXT"));
				return false;
			}

			// get object
			PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[0]);

			// add lore to object
			ArrayList<String> ll = pwo.getLoreList();

			if (ll.size() < LoreRow) {
				for (int i = ll.size(); i < LoreRow; i++) {
					ll.add(i, "");
				}
			}

			ll.set(LoreRow - 1, sb.toString());
			pwo.setLoreList(ll);
			// set file lore
			pl.getPlayerWarpFileHandler().setStringToArrayWarpFile(
					pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId()), args[0], "lore",
					pwo.getLoreList());

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_LORE_COMPLETED_TEXT", args[0]));

		}
		return true;

	}

	@SuppressWarnings("deprecation")
	private boolean cmdBan(Player player, Command cmd, String[] args, boolean banType) {
		if (checkPerm(player, "playerwarpgui.ban", pl.getLanguageHandler().getMessage("COMMAND_USE_BAN"))) {
			if (!checkArgs(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_BAN"))) {
				return false;
			}

			// does this player have a warp with that name?
			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			// is player to be ban a valid player?
			if (!pl.getPlayerWarpFileHandler().isValidPlayer(Bukkit.getOfflinePlayer(args[2]).getUniqueId())) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_BAN_UNKNOWN_PLAYER"));
				return false;
			}

			PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[0]);
			String error = "";

			if (banType) {
				if (pl.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(),
						Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_ALREADY", args[2], args[0]));
					return false;
				}
				pwo.getBanList().add(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString());
				error = "COMMAND_BANNED_PLAYER_COMPLETED";

			}

			if (!banType) {
				if (!pl.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(),
						Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_NOTBANNED", args[2], args[0]));
					return false;
				}
				pwo.getBanList().remove(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString());
				error = "COMMAND_BANNED_PLAYER_COMPLETED";
			}

			// add to file
			pl.getPlayerWarpFileHandler().setStringToArrayWarpFile(
					pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId()), args[0], "ban",
					pwo.getBanList());

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage(error, Bukkit.getOfflinePlayer(args[2]).getName(), args[0]));

		}
		return true;
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
	public boolean checkArgsString(Player player, String[] args, int size, String errorMsg) {
		if (args.length < size) {
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

			ArrayList<PlayerWarpObject> playerWarpObjects = pl.getPlayerWarpObjectHandler()
					.getPlayerWarpObjects(player.getUniqueId());
			if (playerWarpObjects.size() <= 0) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_TEXT"));
				return true;
			}
			String warpText = "";
			for (int i = 0; i < playerWarpObjects.size(); i++) {
				warpText += playerWarpObjects.get(i).getWarpName();
				if (!(i >= playerWarpObjects.size() - 1)) {
					warpText += ", ";
				}
			}
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_LIST_TEXT") + warpText);

			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdSet(Player player, Command cmd, String[] args) {

		if (checkPerm(player, "playerwarpgui.setwarp", pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);

			// check if at set warp limit

			Hooks.GriefPreventionHook.Response response = pl.getGreifProtectionHook().checkWarp(player);
			if(!response.getResponseBool()) {
				if(response.getErrorMsg().length() > 0) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}
			
			Hooks.WorldGuardHook.Response responseWG = pl.getWorldGuardHook().checkWarp(player);
			if(!responseWG.getResponseBool()) {
				if(responseWG.getErrorMsg().length() > 0) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage(responseWG.getErrorMsg()));
				}
				return false;
			}			
			
			Hooks.RedProtectHook.Response responseRP = pl.getRedProtectHook().checkWarp(player);
			if(!responseRP.getResponseBool()) {
				if(responseRP.getErrorMsg().length() > 0) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage(responseRP.getErrorMsg()));
				}
				return false;
			}
			
			if (pl.getPlayerWarpObjectHandler().getPlayerWarpObjects(player.getUniqueId()).size() >= pl
					.getPlayerWarpObjectHandler().geMaxAmountAllowedFromPerm(player, "playerwarpgui.setwarp", ".")) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_MAX_ALLOWED_TEXT",
								pl.getPlayerWarpObjectHandler().geMaxAmountAllowedFromPerm(player,
										"playerwarpgui.setwarp", ".")));
				return false;
			}

			// check if player has warp named same;
			if (pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_ALLREADY_EXSISTS_TEXT"));
				return false;
			}

			// isafe location
			if (!pl.getWarpHandler().isSafeLocation(player.getLocation())) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_UNSAFE_LOCATION"));
				return false;
			}

			// world blocked
			if (pl.getWarpHandler().isBlockedWorld(player.getLocation())) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_WORLD_BLOCKED"));
				return false;
			}

			// check if player can afford to set a warp.
		/*	if (pl.getConfig().getInt("settings.set-warp-cost") != 0) {
				pl.getVaultHandler();
				EconomyResponse r = VaultHook.econ.withdrawPlayer(player, pl.getConfig().getInt("settings.set-warp-cost"));
				if (!r.transactionSuccess()) {
					pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler().getMessage("COMMAND_SET_NOT_ENOUGH_MONEY"));
					return true;
				} else {
					pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler().getMessage("COMMAND_SET_ENOUGH_MONEY"));
				}
				return false;
			}
			*/
			
			// check for uuid file, if exists than add warp else create file
			// pl.getPlayerWarpFileHandler().checkPlayerWarpsFileExsits(player.getUniqueId());
			pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile(
					(pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId())), player.getLocation(),
					args[1], "", "", new ArrayList<>(Arrays.asList("", "", "")),
					new ArrayList<>(Arrays.asList("", "", "")));
			// create object
			// new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon,
			// loreList);7
			pl.getPlayerWarpObjectHandler().createWarpObjects(player.getUniqueId(), args[1].toString(),
					pl.getOtherFunctions().loc2str(player.getLocation()), "", "",
					new ArrayList<>(Arrays.asList("", "", "")), new ArrayList<>(Arrays.asList("", "", "")));

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_SET_COMPLETED_TEXT"));
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdDelete(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.setwarp", pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			// update warp file
			PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[0]);

			// delete object
			pwo.removePlayerWarpObject();

			// update file
			// pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile((pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId())),
			// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
			pl.getPlayerWarpFileHandler().deleteSingleWarpFromFile(
					pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId()), args[0]);
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_DELETE_WARP_TEXT", args[0]));

			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdTitle(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.settitle", pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
			if (!checkArgsString(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 2; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			String title = sb.toString();

			if (title.length() > pl.getConfig().getInt("settings.max-title-text-size")) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_TOOLONG_TEXT"));
				return false;
			}

			// update warp file
			PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[0]);

			// update object
			pwo.setTitle(title);

			// update file
			// pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile((pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId())),
			// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
			pl.getPlayerWarpFileHandler().updateSingleElementWarpFile(
					pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId()), args[0], "title",
					title);
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_COMPLETED_TEXT", args[0]));

			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public boolean cmdIcon(Player player, Command cmd, String[] args) {
		if (checkPerm(player, "playerwarpgui.icon", pl.getLanguageHandler().getMessage("COMMAND_USE_ICON"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_ICON"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			if (player.getItemInHand().getItemMeta() == null) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_ICON_NOICON_TEXT"));
				return true;
			}

			// update warp file
			PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[0]);

			// update object
			pwo.setIcon(pl.getOtherFunctions().parseStringFromItemStack(player.getItemInHand()));

			pl.getPlayerWarpFileHandler().updateSingleElementWarpFile(
					pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId()), args[0], "icon",
					pl.getOtherFunctions().parseStringFromItemStack(player.getItemInHand()));
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_ICON_COMPLETED_TEXT", args[0]));

			return true;
		}
		return false;

	}

}
