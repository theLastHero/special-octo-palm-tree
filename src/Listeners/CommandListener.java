package Listeners;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Response;

public class CommandListener implements CommandExecutor {

	public static PlayerWarpGUI pl;

	public CommandListener(final PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		if ((args.length >= 1) && (args[0].equalsIgnoreCase("test"))) {
			cmdtest(sender, cmd, args);
		}

		if (!(sender instanceof Player)) {
			pl.getMessageHandler()
					.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_COMMAND_ERROR", cmd.getName()));
			return false;
		}

		final Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("pwarps")) {

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
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("deletewarp") || args[0].equalsIgnoreCase("delete")|| args[0].equalsIgnoreCase("warpdelete"))) {
				cmdDelete(player, cmd, args);
			}

			// set title
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("settitle")|| args[0].equalsIgnoreCase("title")|| args[0].equalsIgnoreCase("titleset"))) {
				cmdTitle(player, cmd, args);
			}

			// set icon
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("seticon")|| args[0].equalsIgnoreCase("icon")|| args[0].equalsIgnoreCase("iconset"))) {
				cmdIcon(player, cmd, args);
			}

			// set lore
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("ban"))) {
				cmdBan(player, cmd, args, true);
			}

			// set lore
			if ((args.length >= 1) && (args[0].equalsIgnoreCase("unban"))) {
				cmdBan(player, cmd, args, false);
			}

			// set lore
			if ((args.length >= 1) && (args[0].contains("setlore")|| args[0].equalsIgnoreCase("lore")|| args[0].equalsIgnoreCase("loreset"))) {
				cmdLore(player, cmd, args);
			}

			// reload language
			if ((args.length == 1) && (args[0].contains("langreload"))) {
				cmdLangReload(player, cmd, args);
			}
			// getLanguageHandler().setupLocale(getConfig().getString("language"));
		}
		return false;

	}

	private boolean cmdLangReload(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.langreload", pl.getLanguageHandler().getMessage("COMMAND_USE_LANGUAGE_RELOAD"))) {
			if (!checkArgs(player, args, 1, pl.getLanguageHandler().getMessage("COMMAND_USE_LANGUAGE_RELOAD"))) {
				return false;
			}

		//	pl.getLanguageHandler().setupLocaleReload(pl.getConfig().getString("language"));
		//	pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler()
		//			.getMessage("COMMAND_LANGUAGE_RELOADED", pl.getConfig().getString("language")));
		}
		return false;
	}

	private boolean cmdLore(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.setlore", pl.getLanguageHandler().getMessage("COMMAND_USE_LORE"))) {
			if (!checkArgsString(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_LORE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
				return false;
			}

			int LoreRow = 1;
			final String result[] = args[0].split("setlore");
			if (result.length > 0) {
				final String returnValue = result[result.length - 1];
				if (returnValue != null && pl.getCalc().isInt(returnValue)) {
					LoreRow = Integer.parseInt(returnValue);
				}
			}

			// get allowed amount

			// check lorerow > allowamount
			// checkPerm(Player player, String perm, String msg) {
			if (LoreRow > 1) {
				if (!checkPerm(player, "pwarps.setlore." + LoreRow,
						pl.getLanguageHandler().getMessage("COMMAND_NO_PERMISSION", "pwarps.setlore."+LoreRow))) {
					return false;
				}
			}

			final StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 2; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}
			String loreText = sb.toString();
			loreText = pl.getMessageHandler().ColorVariables(player, loreText);

			// checl lenght of text
			if (loreText.length() > pl.getConfig().getInt("settings.max-lore-text-size")) {
				pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler().getMessage(
						"COMMAND_UPDATE_LORE_TOOLONG_TEXT", pl.getConfig().getInt("settings.max-lore-text-size")));
				return false;
			}

			// get object
			final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[1]);

			// add lore to object
			final ArrayList<String> ll = pwo.getLoreList();

			if (ll.size() < LoreRow) {
				for (int i = ll.size(); i < LoreRow; i++) {
					ll.add(i, "");
				}
			}

			ll.set(LoreRow - 1, loreText);
			pwo.setLoreList(ll);
			// set file lore
			pl.getPlayerWarpFileHandler().setsingleWarpArray(
					pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId()), args[1], "lore",
					pwo.getLoreList());

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_LORE_COMPLETED_TEXT", args[1], loreText));

		}
		return true;

	}

	@SuppressWarnings("deprecation")
	private boolean cmdBan(final Player player, final Command cmd, final String[] args, final boolean banType) {
		if (checkPerm(player, "pwarps.ban", pl.getLanguageHandler().getMessage("COMMAND_USE_BAN"))) {
			if (!checkArgs(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_BAN"))) {
				return false;
			}

			// does this player have a warp with that name?
			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
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

			final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[1]);
			String error = "";

			if (banType) {
				if (pl.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(),
						Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_ALREADY", args[2], args[1]));
					return false;
				}
				pwo.getBanList().add(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString());
				error = "COMMAND_BANNED_PLAYER_COMPLETED";

			}

			if (!banType) {
				if (!pl.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(),
						Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_NOTBANNED", args[2], args[1]));
					return false;
				}
				pwo.getBanList().remove(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString());
				error = "COMMAND_BANNED_PLAYER_COMPLETED";
			}

			// add to file
			pl.getPlayerWarpFileHandler().setsingleWarpArray(
					pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId()), args[1], "ban",
					pwo.getBanList());

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage(error, Bukkit.getOfflinePlayer(args[2]).getName(), args[1]));

		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean checkPerm(final Player player, final String perm, final String msg) {
		if (!player.hasPermission(perm)) {
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_NO_PERMISSION", msg));
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean checkArgs(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length != size) {
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean checkArgsString(final Player player, final String[] args, final int size, final String errorMsg) {
		if (args.length < size) {
			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_USE_INVALID") + errorMsg);
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdShow(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.show", pl.getLanguageHandler().getMessage("COMMAND_USE_SHOW"))) {
			pl.getGuiObject().openGUI(player, 0);
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdList(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.list", pl.getLanguageHandler().getMessage("COMMAND_USE_LIST"))) {
			// if (!checkArgs(player, args, 2,
			// pl.getLanguageHandler().getMessage("COMMAND_USE_LIST"))) {
			// return false;
			// }

			final ArrayList<PlayerWarpObject> playerWarpObjects = pl.getPlayerWarpObjectHandler()
					.getPlayerWarpObjects(player.getUniqueId());
			if (playerWarpObjects.size() <= 0) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARPS_TITLE"));
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_NONE_TEXT"));
				return true;
			}

			if (args.length == 1) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARPS_TITLE"));

				for (int i = 0; i < playerWarpObjects.size(); i++) {
					final String warpText = playerWarpObjects.get(i).getWarpName();
					final Location warpLocation = pl.getOtherFunctions().str2loc(playerWarpObjects.get(i).getWarpLocation());
					final String warpWorld = warpLocation.getWorld().getName().toString();
					final String warpXpos = String.valueOf(warpLocation.getX()).split("\\.")[0];
					final String warpYpos = String.valueOf(warpLocation.getY()).split("\\.")[0];
					final String warpZpos = String.valueOf(warpLocation.getZ()).split("\\.")[0];
					if (!(i >= playerWarpObjects.size())) { // COMMAND_LIST_PRETEXT
						pl.getMessageHandler().sendPlayerMessage(player,
								pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP", "" + (i + 1), warpText,
										warpWorld, warpXpos, warpYpos, warpZpos));
					}
				}
			}

			if (args.length == 2) {
				final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(),
						args[1]);
				if (pwo == null) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
					return false;
				}

				final String warpText = pwo.getWarpName();
				final Location warpLocation = pl.getOtherFunctions().str2loc(pwo.getWarpLocation());
				final String warpWorld = warpLocation.getWorld().getName().toString();
				final String warpXpos = String.valueOf(warpLocation.getX()).split("\\.")[0];
				final String warpYpos = String.valueOf(warpLocation.getY()).split("\\.")[0];
				final String warpZpos = String.valueOf(warpLocation.getZ()).split("\\.")[0];

				final String warpTitle = pwo.getTitle();
				final String warpIcon = pwo.getIcon();
				final ArrayList<String> warpLore = pwo.getLoreList();
				final ArrayList<String> warpBan = pwo.getBanList();

				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARPS_DETAILS_TITLE", warpText));
				// title
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_TITLE", warpTitle));
				// Location
				pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler()
						.getMessage("COMMAND_LIST_WARP_LOCATION", warpWorld, warpXpos, warpYpos, warpZpos));
				// ICON
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_ICON", warpIcon));
				// LOREMAIN
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_LORE_MAIN"));
				// LORE LOOP
				int i = 0;
				for (final String lore : warpLore) {
					i++;
					if (lore.length() > 0) {
						pl.getMessageHandler().sendPlayerMessage(player,
								pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_LORE",lore, i));
					}
				}
				// BAN MAIN
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_BAN_MAIN"));
				// BAN LOOP
				for (final String ban : warpBan) {
					if (ban.length() > 0) {
						pl.getMessageHandler().sendPlayerMessage(player,
								pl.getLanguageHandler().getMessage("COMMAND_LIST_WARP_BAN", ban));
					}
				}

			}

			return true;
		}
		return false;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdSet(final Player player, final Command cmd, final String[] args) {

		if (checkPerm(player, "pwarps.setwarp", pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_SET"))) {
				return false;
			}
			// chestObject.openGUI(player, 0);

			// check if at set warp limit

			Response response = pl.getGriefPreventionHook().checkWarp(player);
			if (!response.getResponseBool()) {
				if (response.getErrorMsg().length() > 0) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}

			response = pl.getWorldGuardHook().checkWarp(player);
			if (!response.getResponseBool()) {
				if (response.getErrorMsg().length() > 0) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}

			response = pl.getRedProtectHook().checkWarp(player);
			if (!response.getResponseBool()) {
				if (response.getErrorMsg().length() > 0) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}

			response = pl.getFactionsHook().checkWarp(player);
			if (!response.getResponseBool()) {
				if (response.getErrorMsg().length() > 0) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}

			response = pl.getResidenceHook().checkWarp(player);
			if (!response.getResponseBool()) {
				if (response.getErrorMsg().length() > 0) {
					pl.getMessageHandler().sendPlayerMessage(player,
							pl.getLanguageHandler().getMessage(response.getErrorMsg()));
				}
				return false;
			}

			final int maxSizeAllowed = pl.getPlayerWarpObjectHandler().geMaxAmountAllowedFromPerm(player, "pwarps.setwarp",
					".");
			final int currentSize = pl.getPlayerWarpObjectHandler().getPlayerWarpObjects(player.getUniqueId()).size();
			if (currentSize >= maxSizeAllowed) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_MAX_ALLOWED_TEXT", maxSizeAllowed));
				return false;
			}

			// check if player has warp named same;
			if (pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_ALLREADY_EXSISTS_TEXT", args[1]));
				return false;
			}

			// isafe location
			if (!pl.getWarpHandler().isSafeLocation(player.getLocation())) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_UNSAFE_LOCATION", args[1]));
				return false;
			}

			// world blocked
			if (pl.getWarpHandler().isBlockedWorld(player.getLocation())) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_WORLD_BLOCKED", args[1]));
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
			pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile(
					(pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId())), player.getLocation(),
					args[1], "", "", new ArrayList<>(Arrays.asList("", "", "")),
					new ArrayList<>(Arrays.asList("", "", "")));
			// create object
			// new PlayerWarpObject(playerUUID, warpName, warpLocation, title, icon,
			// loreList);7
			pl.getPlayerWarpObjectHandler().createWarpObjects(player.getUniqueId(), args[1].toString(),
					pl.getOtherFunctions().loc2str(player.getLocation()), "", "", new ArrayList<>(Arrays.asList("")),
					new ArrayList<>(Arrays.asList("")));

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_SET_COMPLETED_TEXT", args[1]));
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdDelete(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.setwarp", pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT"));
				return false;
			}

			// update warp file
			final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[1]);

			// delete object
			pwo.removePlayerWarpObject();

			// update file
			// pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile((pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId())),
			// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
			pl.getPlayerWarpFileHandler().removeSingleWarpValue(
					pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId()), args[1]);
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_DELETE_WARP_TEXT", args[1]));

			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean cmdTitle(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.settitle", pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
			if (!checkArgsString(player, args, 3, pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
				return false;
			}

			final StringBuilder sb = new StringBuilder(); // Creating a new instance of StringBuilder
			for (int i = 2; i < args.length; i++) { // Basic for loop, going through the arguments starting from 1
				sb.append(args[i]); // Adds the argument into the StringBuilder
				sb.append(" "); // Adding a space into the StringBuilder
			}

			String title = sb.toString();
			title = pl.getMessageHandler().ColorVariables(player, title);

			if (title.length() > pl.getConfig().getInt("settings.max-title-text-size")) {
				pl.getMessageHandler().sendPlayerMessage(player,
						pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_TOOLONG_TEXT",
								"" + pl.getConfig().getInt("settings.max-title-text-size")));
				return false;
			}

			// update warp file
			final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[1]);

			// update object
			pwo.setTitle(title);

			// update file
			// pl.getPlayerWarpFileHandler().addWarpToPlayerWarpFile((pl.getPlayerWarpFileHandler().checkPlayerWarpsExsits(player.getUniqueId())),
			// player.getLocation(), args[0], title,pwo.getIcon(),pwo.getLoreList());
			pl.getPlayerWarpFileHandler().setSingleWarpValue(
					pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId()), args[1], "title",
					title);
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_COMPLETED_TEXT", args[1], title));

			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public boolean cmdIcon(final Player player, final Command cmd, final String[] args) {
		if (checkPerm(player, "pwarps.icon", pl.getLanguageHandler().getMessage("COMMAND_USE_ICON"))) {
			if (!checkArgs(player, args, 2, pl.getLanguageHandler().getMessage("COMMAND_USE_ICON"))) {
				return false;
			}

			if (!pl.getPlayerWarpObjectHandler().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
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
			final PlayerWarpObject pwo = pl.getPlayerWarpObjectHandler().getPlayerWarpObject(player.getUniqueId(), args[1]);

			// update object
			pwo.setIcon(pl.getOtherFunctions().parseStringFromItemStack(player.getItemInHand()));

			pl.getPlayerWarpFileHandler().setSingleWarpValue(
					pl.getPlayerWarpFileHandler().checkWarpsExsits(player.getUniqueId()), args[1], "icon",
					pl.getOtherFunctions().parseStringFromItemStack(player.getItemInHand()));
			// chestObject.openGUI(player, 0);

			pl.getMessageHandler().sendPlayerMessage(player,
					pl.getLanguageHandler().getMessage("COMMAND_UPDATE_ICON_COMPLETED_TEXT", args[1]));

			return true;
		}

		return false;

	}

	private void cmdtest(final CommandSender sender, final Command cmd, final String[] args) {
		if ((sender instanceof Player)) {
			pl.getMessageHandler().sendPlayerMessage((Player) sender, pl.getLanguageHandler().getMessage(
					"COMMAND_NO_PERMISSION", pl.getLanguageHandler().getMessage("COMMAND_USE_SET", "arg1", "arg2")));
		}
		pl.getMessageHandler().sendConsoleMessage("&5[&6&l&nOWNER&5] ");
		pl.getMessageHandler().sendConsoleMessage("&4&l[&c&l&oOwner&4&l] &c&o[username]&4: &b<message>");
		pl.getMessageHandler().sendConsoleMessage("&e&l<<&2&lO&a&lwner&e&l>> ");
		pl.getMessageHandler().sendConsoleMessage("&e---< &a sometext &e >---");
		pl.getMessageHandler().sendConsoleMessage("&e-----< &6sometext &e>-----");
		pl.getMessageHandler().sendConsoleMessage("&e---- &6Market List &e----");
		pl.getMessageHandler().sendConsoleMessage("&eUsage: &6/res compass <residence>");
		pl.getMessageHandler().sendConsoleMessage("&b[&cRed&4Protect&b]&r");
		pl.getMessageHandler().sendConsoleMessage("&3/rp {cmd}|{alias} &r- &bSee a list of near regions.");
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("MESSAGE_PREFIX", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_INVALID", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_TOOMANY", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_SHOW", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_LIST", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_ICON", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_SET", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_DELETE", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_TITLE", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_LORE", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_USE_BAN", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_LIST_TEXT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_LIST_NONE_TEXT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_SET_COMPLETED_TEXT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_SET_ENOUGH_MONEY", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_SET_NOT_ENOUGH_MONEY", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_SET_ALLREADY_EXSISTS_TEXT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_SET_MAX_ALLOWED_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_UNSAFE_LOCATION", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_SET_CANCEL_WORLD_BLOCKED", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_COMPLETED_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_TITLE_TOOLONG_TEXT", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_LORE_COMPLETED_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_LORE_TOOLONG_TEXT", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_ICON_COMPLETED_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_ICON_NOICON_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("GRIEFPREVENTION_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("GRIEFPREVENTION_NOT_IN_A_CLAIM", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("GRIEFPREVENTION_CLAIM_DELETED", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("WORLDGUARD_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("WORLGUARD_NOT_IN_A_REGION", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("REDPROTECT_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("REDPROTECT_NOT_IN_A_REGION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("REDPROTECT_CLAIM_DELETED", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("RESIDENCE_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("RESIDENCE_NOT_IN_A_REGION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("RESIDENCE_CLAIM_DELETED", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("FACTIONS_NO_PERMISSION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("FACTIONS_NOT_IN_A_REGION", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("FACTIONS_CLAIM_DELETED", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_BAN_UNKNOWN_PLAYER", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_ALREADY", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_NOTBANNED", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_COMPLETED", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER_UNBANNED_COMPLETED", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("COMMAND_DELETE_WARP_TEXT", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("TELEPORT_CANCEL_MOVEMENT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("TELEPORT_CANCEL_INVALID_LOCATION", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("TELEPORT_CANCEL_UNSAFE_LOCATION", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("TELEPORT_CANCEL_WORLD_BLOCKED", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("TELEPORT_COMPLETED", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("TELEPORT_COUNTDOWN", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("NEXTPAGE_TEXT", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("WARP_ID_TEXT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("WARP_OWNER_TEXT", "arg1", "arg2"));

		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_COMMAND_ERROR", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_METRICS", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_HOOK", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_LANGUAGE_FILE", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_CONFIG_FILE", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_WARPFILE_COUNT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_WARPS_COUNT", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_PREFIX", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_FINAL", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("CONSOLE_MSG_CRITIAL_ERROR_PREFIX", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("CONSOLE_MSG_NONCRITIAL_ERROR_PREFIX", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_CREATE_FOLDER", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("CONSOLE_ERROR_CRITIAL_FILENOTFOUND", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_NONCRITIAL_ERROR", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(
				pl.getLanguageHandler().getMessage("CONSOLE_NONCRITIAL_ERROR_HOOK", "arg1", "arg2"));
		pl.getMessageHandler()
				.sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_CRITIAL_ERROR_HOOK", "arg1", "arg2"));

		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("SUCCESS", "arg1", "arg2"));
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("FAILED", "arg1", "arg2"));

	}

}
