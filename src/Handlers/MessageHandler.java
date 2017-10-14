package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import PlayerWarpGUI.PlayerWarpGUI;

public class MessageHandler {

	public static PlayerWarpGUI pl;

	public MessageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		sendTitle();
	}
	
	public void sendConsoleMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[PlayerWarpGUI] " + msg));
	}
	
	public void sendConsoleMessageBare(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',  msg));
	}
	
	public void sendTitle() {
		sendConsoleMessageBare("&4.-. .   .-. . . .-. .-. . . . .-. .-. .-. .-. . . .-. ");
		sendConsoleMessageBare("&4|-' |   |-|  |  |-  |(  | | | |-| |(  |-' |.. | |  |  ");
		sendConsoleMessageBare("&4'   `-' ` '  `  `-' ' ' `.'.' ` ' ' ' '   `-' `-' `-' ");
	}
}
