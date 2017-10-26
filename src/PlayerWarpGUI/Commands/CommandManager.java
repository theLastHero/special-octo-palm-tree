package PlayerWarpGUI.Commands;

import org.bukkit.command.PluginCommand;

import PlayerWarpGUI.PlayerWarpGUI;

public final class CommandManager {

	public CommandManager() {};
	
		
    private static void registerPWOCommand() {
        PluginCommand command = PlayerWarpGUI.p.getCommand("playerwarpsgui");
        //command.setDescription("Commands.Description.Skill");
        //command.setPermission("mcmmo.commands.");
        //command.setPermissionMessage(permissionsMessage);
        //command.setUsage(LocaleLoader.getString("Commands.Usage.0"));
        //command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.2"));
      command.setExecutor(new PWOCommand());
        

}
    
    public static void registerCommands() {
        // Generic Commands
    	registerPWOCommand();
}
}
