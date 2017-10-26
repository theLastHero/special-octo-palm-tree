package PlayerWarpGUI.Commands;

import org.bukkit.command.PluginCommand;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Commands.SubCommands.BanCommand;
import PlayerWarpGUI.Commands.SubCommands.DeleteWarpCommand;
import PlayerWarpGUI.Commands.SubCommands.HelpCommand;
import PlayerWarpGUI.Commands.SubCommands.ListCommand;
import PlayerWarpGUI.Commands.SubCommands.SetIconCommand;
import PlayerWarpGUI.Commands.SubCommands.SetLoreCommand;
import PlayerWarpGUI.Commands.SubCommands.SetTitleCommand;
import PlayerWarpGUI.Commands.SubCommands.SetWarpCommand;
import PlayerWarpGUI.Commands.SubCommands.ShowCommand;
import PlayerWarpGUI.Commands.SubCommands.UnbanCommand;
import PlayerWarpGUI.locale.LocaleLoader;

public final class CommandManager {
	
	public CommandManager() {};
	
	 private static String permissionsMessage = LocaleLoader.getString("COMMAND_NO_PERMISSION");
		
    private static void registerPWOCommand() {
       PluginCommand command = PlayerWarpGUI.p.getCommand("playerwarpsgui");
       command.setPermission("playerwarpgui.show");
       command.setPermissionMessage(permissionsMessage);
       command.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_SHOW"));
       command.setExecutor(new ShowCommand());
      
      PluginCommand setpwarpCommand = PlayerWarpGUI.p.getCommand("setpwarp");
      setpwarpCommand.setPermission("playerwarpgui.setwarp");
      setpwarpCommand.setPermissionMessage(permissionsMessage);
      setpwarpCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_SET"));
      setpwarpCommand.setExecutor(new SetWarpCommand());
        
      PluginCommand deletepwarpCommand = PlayerWarpGUI.p.getCommand("deletepwarp");
      deletepwarpCommand.setPermission("playerwarpgui.setwarp");
      deletepwarpCommand.setPermissionMessage(permissionsMessage);
      deletepwarpCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_DELETE"));
      deletepwarpCommand.setExecutor(new DeleteWarpCommand());
        
      PluginCommand banCommand = PlayerWarpGUI.p.getCommand("pwarpban");
      banCommand.setPermission("playerwarpgui.ban");
      banCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_BAN"));
      banCommand.setPermissionMessage(permissionsMessage);
      banCommand.setExecutor(new BanCommand());
      
      PluginCommand unbanCommand = PlayerWarpGUI.p.getCommand("pwarpunban");
      unbanCommand.setPermission("playerwarpgui.ban");
      unbanCommand.setPermissionMessage(permissionsMessage);
      unbanCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_BAN"));
      unbanCommand.setExecutor(new UnbanCommand());
      
      PluginCommand helpCommand = PlayerWarpGUI.p.getCommand("pwarphelp");
      helpCommand.setPermission("playerwarpgui.help");
      helpCommand.setPermissionMessage(permissionsMessage);
      helpCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_HELP"));
      helpCommand.setExecutor(new HelpCommand());
      
      PluginCommand listCommand = PlayerWarpGUI.p.getCommand("pwarplist");
      listCommand.setPermission("playerwarpgui.list");
      listCommand.setPermissionMessage(permissionsMessage);
      listCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_LIST"));
      listCommand.setExecutor(new ListCommand());
      
      PluginCommand seticonCommand = PlayerWarpGUI.p.getCommand("pwarpicon");
      seticonCommand.setPermission("playerwarpgui.icon");
      seticonCommand.setPermissionMessage(permissionsMessage);
      seticonCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_ICON"));
      seticonCommand.setExecutor(new SetIconCommand());
      
      PluginCommand setloreCommand = PlayerWarpGUI.p.getCommand("pwarplore");
      setloreCommand.setPermission("playerwarpgui.lore");
      setloreCommand.setPermissionMessage(permissionsMessage);
      setloreCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_LORE"));
      setloreCommand.setExecutor(new SetLoreCommand());
      
      PluginCommand showCommand = PlayerWarpGUI.p.getCommand("pwarpshow");
      showCommand.setPermission("playerwarpgui.show");
      showCommand.setPermissionMessage(permissionsMessage);
      showCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_SHOW"));
      showCommand.setExecutor(new ShowCommand());
      
      PluginCommand titleCommand = PlayerWarpGUI.p.getCommand("pwarptitle");
      titleCommand.setPermission("playerwarpgui.title");
      titleCommand.setPermissionMessage(permissionsMessage);
      titleCommand.setUsage(LocaleLoader.getString("COMMAND_USE_INVALID") + LocaleLoader.getString("COMMAND_USE_TITLE"));
      titleCommand.setExecutor(new SetTitleCommand());
      
}
    
    public static void registerCommands() {
        // Generic Commands
    	registerPWOCommand();
}
}
