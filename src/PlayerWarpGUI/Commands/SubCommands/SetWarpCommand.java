package PlayerWarpGUI.Commands.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Hooks.FactionsHook;
import PlayerWarpGUI.Hooks.GriefPreventionHook;
import PlayerWarpGUI.Hooks.RedProtectHook;
import PlayerWarpGUI.Hooks.ResidenceHook;
import PlayerWarpGUI.Hooks.VaultHook;
import PlayerWarpGUI.Hooks.WorldGuardHook;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Location.LocUtils;
import PlayerWarpGUI.Utils.Perms.PermUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.Utils.Warp.WarpFileUtils;
import PlayerWarpGUI.Utils.World.WorldUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import net.milkbowl.vault.economy.EconomyResponse;

/**
* Setwarp Command: <br>
* Sets a warp at the current players location for that player.
* 
* @author Judgetread
* @version 1.0
*/
public class SetWarpCommand implements CommandExecutor {

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

		final Player player = (Player) sender;

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

		int maxSizeAllowed = PermUtils.getInstance().getLargestPerm(player, "playerwarpsgui.setwarp", ".");
		int currentSize = 0;
		if (!(ObjectUtils.getInstance().getPlayerWarpObjects(player.getUniqueId()) == null)) {
			currentSize = ObjectUtils.getInstance().getPlayerWarpObjects(player.getUniqueId()).size();
		}

		if (currentSize >= maxSizeAllowed) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_MAX_ALLOWED_TEXT", maxSizeAllowed));
			return false;
		}

		// check if player has warp named same;
		if (ObjectUtils.getInstance().checkPlayerWarpObject(player.getUniqueId(), args[0])) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_ALLREADY_EXSISTS_TEXT", args[0]));
			return false;
		}

		// isafe location
		if (!LocUtils.getInstance().isSafeLocation(player.getLocation())) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_CANCEL_UNSAFE_LOCATION", args[0]));
			return false;
		}

		// world blocked
		if (WorldUtils.getInstance().isBlockedWorld(player.getLocation())) {
			player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_CANCEL_WORLD_BLOCKED", args[0]));
			return false;
		}

		// check if player can afford to set a warp.
		VaultHook vh = new VaultHook();
		if (vh.isEnabled) {

			EconomyResponse r = vh.econ.withdrawPlayer(player, Config.getInstance().getSetWarpCost());

			if (!r.transactionSuccess()) {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_NOT_ENOUGH_MONEY"));
				return false;
			} else {
				player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_ENOUGH_MONEY",  Config.getInstance().getSetWarpCost()));
			}
		
		}


	WarpFileUtils.getInstance().addWarpToPlayerWarpFile((WarpFileUtils.getInstance().checkWarpsExsits(player.getUniqueId())),player.getLocation(),args[0],"","",new ArrayList<>(Arrays.asList("","","")),new ArrayList<>(Arrays.asList("","","")));

	ObjectUtils.getInstance().createWarpObjects(player.getUniqueId(),args[0].toString(),StringUtils.getInstance().loc2str(player.getLocation()),Config.getInstance().getDefaultTitle(),Config.getInstance().getDefaultIcon(),new ArrayList<>(Arrays.asList("")),new ArrayList<>(Arrays.asList("")));

	player.sendMessage(LocaleLoader.getString("MESSAGE_PREFIX") + LocaleLoader.getString("COMMAND_SET_COMPLETED_TEXT",args[0]));return true;

	}

	private boolean checkCanSetWarp(Player player, String checkCanSetWarp) {
		if ((checkCanSetWarp != (null))) {
			player.sendMessage(LocaleLoader.getString(checkCanSetWarp));
			return false;
		}
		return true;
	}


}
