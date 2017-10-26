package PlayerWarpGUI.Commands.SubCommands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.locale.LocaleLoader;

public class ListCommand implements CommandExecutor{

	private String perm = "playerwarpsgui.list";

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

		/*if (!StringUtils.getInstance().checkArgs(player, args, 3, LocaleLoader.getString("COMMAND_USE_LIST"))) {
			return false;
		}*/

		if (!player.hasPermission(perm)) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_NO_PERMISSION", "COMMAND_USE_LIST"));
			return false;
		}
		

		final ArrayList<PlayerWarpObject> playerWarpObjects = ObjectUtils.getInstance().getPlayerWarpObjects(player.getUniqueId());

		if (playerWarpObjects.size() <= 0) {

			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARPS_TITLE"));
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_NONE_TEXT"));
			return true;
		}

		if (args.length == 1) {

			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARPS_TITLE"));

			for (int i = 0; i < playerWarpObjects.size(); i++) {
				final String warpText = playerWarpObjects.get(i).getWarpName();
				final Location warpLocation = StringUtils.getInstance()
						.str2loc(playerWarpObjects.get(i).getWarpLocation());
				final String warpWorld = warpLocation.getWorld().getName().toString();
				final String warpXpos = String.valueOf(warpLocation.getX()).split("\\.")[0];
				final String warpYpos = String.valueOf(warpLocation.getY()).split("\\.")[0];
				final String warpZpos = String.valueOf(warpLocation.getZ()).split("\\.")[0];
				if (!(i >= playerWarpObjects.size())) {

					player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP", new Object[] { String.valueOf(i + 1), warpText,
							warpWorld, warpXpos, warpYpos, warpZpos }));
				}
			}
		}

		if (args.length == 2) {
			final PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(),
					args[1]);
			if (pwo == null) {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
				return false;
			}

			final String warpText = pwo.getWarpName();
			final Location warpLocation = StringUtils.getInstance().str2loc(pwo.getWarpLocation());
			final String warpWorld = warpLocation.getWorld().getName().toString();
			final String warpXpos = String.valueOf(warpLocation.getX()).split("\\.")[0];
			final String warpYpos = String.valueOf(warpLocation.getY()).split("\\.")[0];
			final String warpZpos = String.valueOf(warpLocation.getZ()).split("\\.")[0];

			final String warpTitle = pwo.getTitle();
			final String warpIcon = pwo.getIcon();
			final ArrayList<String> warpLore = pwo.getLoreList();
			final ArrayList<String> warpBan = pwo.getBanList();

			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARPS_DETAILS_TITLE", warpText));
			// title
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_TITLE", warpTitle));
			// Location
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_LOCATION", warpWorld, warpXpos, warpYpos, warpZpos));
			// ICON
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_ICON", warpIcon));
			// LOREMAIN
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_LORE_MAIN"));
			// LORE LOOP
			int i = 0;
			for (final String lore : warpLore) {
				i++;
				if (lore.length() > 0) {
					player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_LORE", lore, i));
				}
			}
			// BAN MAIN
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_BAN_MAIN"));
			// BAN LOOP
			for (final String ban : warpBan) {
				if (ban.length() > 0) {
					player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_LIST_WARP_BAN", ban));
				}
			}

		}

		return true;
	}
}