package PlayerWarpGUI.Commands.SubCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Chat.MessageSender;
import PlayerWarpGUI.Commands.Utils;
import PlayerWarpGUI.Listeners.CommandListener;
import PlayerWarpGUI.Objects.GUIObject;
import PlayerWarpGUI.locale.LocaleLoader;

public class ShowCommand implements CommandExecutor{
	
	private String perm = "pwarps.show";

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		 
		  final Player player = (Player) sender;
		  
			if (!player.hasPermission(perm)) {
				MessageSender.send(player, "COMMAND_NO_PERMISSION", "COMMAND_USE_SHOW");
				return false;
			}
	        
			new GUIObject().openGUI(player, 0);
		
		return false;
	}

}
