package PlayerWarpGUI.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import PlayerWarpGUI.Commands.SubCommands.*;
import PlayerWarpGUI.locale.LocaleLoader;

public class PWOCommand implements TabExecutor {
    private static final List<String> PARTY_SUBCOMMANDS;
    private static final List<String> XPSHARE_COMPLETIONS = ImmutableList.of("none", "equal");
    private static final List<String> ITEMSHARE_COMPLETIONS = ImmutableList.of("none", "equal", "random", "loot", "mining", "herbalism", "woodcutting", "misc");

    static {
        ArrayList<String> subcommands = new ArrayList<String>();

        for (PWOSubCommandType subcommand : PWOSubCommandType.values()) {
            subcommands.add(subcommand.toString());
        }

        Collections.sort(subcommands);
        PARTY_SUBCOMMANDS = ImmutableList.copyOf(subcommands);
    }

    private CommandExecutor helpWarpCommand          = new SetWarpCommand();
    private CommandExecutor setWarpCommand       = new SetWarpCommand();
    private CommandExecutor deleteWarpCommand          = new SetWarpCommand();
    private CommandExecutor setIconWarpCommand       = new DeleteWarpCommand();
    private CommandExecutor setTitleWarpCommand          = new SetWarpCommand();
    private CommandExecutor setLoreWarpCommand       = new DeleteWarpCommand();
    private CommandExecutor banWarpCommand          = new SetWarpCommand();
    private CommandExecutor unbanWarpCommand       = new DeleteWarpCommand();
    private CommandExecutor showCommand          = new ShowCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Utils.noConsoleUsage(sender)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
        	return showCommand.onCommand(sender, command, label, args);
        }

        PWOSubCommandType subcommand = PWOSubCommandType.getSubcommand(args[0]);

        if (subcommand == null) {
            return printUsage(player);
        }

        switch (subcommand) {
            case HELP:
                return setWarpCommand.onCommand(sender, command, label, args);
            case SET:
                return setWarpCommand.onCommand(sender, command, label, args);
            case DELETE:
                return setWarpCommand.onCommand(sender, command, label, args);
            case SETICON:
                return setWarpCommand.onCommand(sender, command, label, args);
            case SETTITLE:
                return setWarpCommand.onCommand(sender, command, label, args);
            case SETLORE:
                return setWarpCommand.onCommand(sender, command, label, args);
            case BAN:
                return setWarpCommand.onCommand(sender, command, label, args);
            case UNBAN:
                return setWarpCommand.onCommand(sender, command, label, args);
            case SHOW:
                return setWarpCommand.onCommand(sender, command, label, args);
            default:
                break;
        }
		return false;
    }

    private boolean printUsage(Player player) {
        player.sendMessage(LocaleLoader.getString("Party.Help.0", "/party join"));
        player.sendMessage(LocaleLoader.getString("Party.Help.1", "/party create"));
        player.sendMessage(LocaleLoader.getString("Party.Help.2", "/party ?"));
        return true;
    }

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}



}