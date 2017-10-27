package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.Objects.GUIObject;
import PlayerWarpGUI.locale.LocaleLoader;

/**
* Show Command: <br>
* SHow the GUI menu to the player.
* 
* @author Judgetread
* @version 1.0
*/
public class ShowCommand implements CommandExecutor{
	
	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		 
		  final Player player = (Player) sender;
		  

			if (!(args.length == 0)) {
				Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("COMMAND_USE_SHOW"));
				return false;
			}
	        
			new GUIObject().openGUI(player, 0);
		
		return false;
	}

}
