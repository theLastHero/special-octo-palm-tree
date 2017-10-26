package PlayerWarpGUI.Commands;

import org.bukkit.command.PluginCommand;

import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

public final class CommandManager {

	public CommandManager() {}
	
		
    private static void registerPWOCommand() {
        PluginCommand command = PlayerWarpGUI.p.getCommand("pwarps");

        command.setDescription(LocaleLoader.getString("Commands.Description.mcmmo"));
        command.setPermission("");
        //command.setPermissionMessage(permissionsMessage);
       // command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcmmo"));
        //command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "mcmmo", "help"));
        command.setExecutor(new PWOCommand());
}
    
    public static void registerCommands() {
        // Generic Commands
    	registerPWOCommand();
}
}
