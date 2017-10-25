package PlayerWarpGUI.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Commands.SubCommands.BanCommand;
import PlayerWarpGUI.Commands.SubCommands.DeleteWarpCommand;
import PlayerWarpGUI.Commands.SubCommands.HelpCommand;
import PlayerWarpGUI.Commands.SubCommands.SetIconCommand;
import PlayerWarpGUI.Commands.SubCommands.SetLoreCommand;
import PlayerWarpGUI.Commands.SubCommands.SetTitleCommand;
import PlayerWarpGUI.Commands.SubCommands.SetWarpCommand;
import PlayerWarpGUI.Commands.SubCommands.ShowCommand;
import PlayerWarpGUI.Commands.SubCommands.UnbanCommand;
import PlayerWarpGUI.Commands.SubCommands.ListCommand;
import PlayerWarpGUI.locale.LocaleLoader;

public class PWOCommand implements TabExecutor {
	
   

    private CommandExecutor helpWarpCommand          = new HelpCommand();
    private CommandExecutor setWarpCommand       = new SetWarpCommand();
    private CommandExecutor deleteWarpCommand          = new DeleteWarpCommand();
    private CommandExecutor setIconWarpCommand       = new SetIconCommand();
    private CommandExecutor setTitleWarpCommand          = new SetTitleCommand();
    private CommandExecutor setLoreWarpCommand       = new SetLoreCommand();
    private CommandExecutor banWarpCommand          = new BanCommand();
    private CommandExecutor unbanWarpCommand       = new UnbanCommand();
    private CommandExecutor showCommand          = new ShowCommand();
    private CommandExecutor listCommand          = new ListCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Utils.noConsoleUsage(sender)) {
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
        	return showCommand.onCommand(sender, command, label, args);
        }
        
        if (args[0].contains("setlore")) {
        	return setLoreWarpCommand.onCommand(sender, command, label, args);
        }

        PWOSubCommandType subcommand = PWOSubCommandType.getSubcommand(args[0]);

        if (subcommand == null) {
            return printUsage(player);
        }

        switch (subcommand) {
            case HELP:
                return helpWarpCommand.onCommand(sender, command, label, args);
            case SET:
                return setWarpCommand.onCommand(sender, command, label, args);
            case DELETE:
                return deleteWarpCommand.onCommand(sender, command, label, args);
            case SETICON:
                return setIconWarpCommand.onCommand(sender, command, label, args);
            case SETTITLE:
                return setTitleWarpCommand.onCommand(sender, command, label, args);
            case SETLORE:
                return setLoreWarpCommand.onCommand(sender, command, label, args);
            case BAN:
                return banWarpCommand.onCommand(sender, command, label, args);
            case UNBAN:
                return unbanWarpCommand.onCommand(sender, command, label, args);
            case SHOW:
                return showCommand.onCommand(sender, command, label, args);
            case LIST:
                return listCommand.onCommand(sender, command, label, args);
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