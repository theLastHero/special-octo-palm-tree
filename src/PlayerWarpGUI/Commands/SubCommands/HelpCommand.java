package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import PlayerWarpGUI.locale.LocaleLoader;
/**
* Help Command: Shows help menu to the user.
* 
* @author Judgetread
* @version 1.0
*/
public class HelpCommand implements CommandExecutor {

	    /* (non-Javadoc)
	     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	     */
	    @Override
	    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	        
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_SHOW"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_LIST"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_ICON"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_SET"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_DELETE"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_TITLE"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_LORE"));
	                sender.sendMessage(LocaleLoader.getString("COMMAND_USE_BAN"));
	                return true;

	    }
	

}
