package PlayerWarpGUI.Commands.SubCommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Perms.PermUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;

public class SetLoreCommand implements CommandExecutor{

		private String perm = "playerwarpsgui.setlore";
		
		@Override
		public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

			final Player player = (Player) sender;

			if (!StringUtils.getInstance().checkArgsString(player, args, 3, LocaleLoader.getString("COMMAND_USE_LORE"))) {
				return false;
			}
			
			if (!player.hasPermission(perm)) {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_NO_PERMISSION", "COMMAND_USE_LORE"));
				return false;
			}
			

			// update warp file
			if (!ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[1])) {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_DOESNT_EXSISTS_TEXT", args[1]));
				return false;
			}
			
			int LoreRow = 1;
			final String result[] = args[0].split("setlore");
			if (result.length > 0) {
				final String returnValue = result[result.length - 1];
				if (returnValue != null && StringUtils.getInstance().isInt(returnValue)) {
					LoreRow = Integer.parseInt(returnValue);
				}
			}

			// get allowed amount
			if (LoreRow > 1) {
				if (!PermUtils.getInstance().checkPerm(player, "playerwarpsgui.setlore." + LoreRow,
						LocaleLoader.getString("COMMAND_NO_PERMISSION", "playerwarpsgui.setlore." + LoreRow))) {
					return false;
				}
			}

			final StringBuilder sb = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			String loreText = sb.toString();
			loreText = ChatColor.translateAlternateColorCodes('&',loreText);

			// check lenght of text
			if (loreText.length() > Config.getInstance().getMaxLoreSize()) {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_LORE_TOOLONG_TEXT",
						Config.getInstance().getMaxLoreSize()));
				return false;
			}

			// get object
			final PlayerWarpObject pwo = ObjectUtils.getInstance().getPlayerWarpObject(player.getUniqueId(),
					args[1]);

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
			WarpFileUtils.getInstance().setsingleWarpArray(
					WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId()), args[1], "lore",
					pwo.getLoreList());

			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_UPDATE_LORE_COMPLETED_TEXT", args[1], loreText));

		return true;
	}

}
