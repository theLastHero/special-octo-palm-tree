package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import PlayerWarpGUI.PlayerWarpGUI;

public class MessageHandler {

	public static PlayerWarpGUI pl;

	public MessageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		
	}
	
	public void sendConsoleMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[PlayerWarpGUI] " + msg));
	}
	
	public void sendConsoleMessageBare(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',  msg));
	}
	
	public void sendTitle() {
		sendConsoleMessageBare("&b.-. .   .-. . . .-. .-. &3. . . .-. .-. .-. &7.-. . . .-. ");
		sendConsoleMessageBare("&b|-' |   |-|  |  |-  |(  &3| | | |-| |(  |-' &7|.. | |  |  ");
		sendConsoleMessageBare("&b'   `-' ` '  `  `-' ' ' &3`.'.' ` ' ' ' '   &7`-' `-' `-' ");
		startupStatus();
		showErrors();
	}
	
	public void showErrors() {
		
		if (!pl.getNonCriticalErrors().isEmpty()) {
			for (String error : pl.getNonCriticalErrors()) {
				sendConsoleMessage("&4[NON CRITICAL ERROR] " + error);
			}
		}
			
		if (!pl.getCriticalErrors().isEmpty()) {
			for (String error : pl.getCriticalErrors()) {
				sendConsoleMessage("&4[CRITICAL ERROR] " + error);
			}
			pl.killPlugin();
		}
		
		
	}
	
	public void startupStatus() {
		if(pl.getCriticalErrors().isEmpty()) {
			sendConsoleMessageBare("&aPlayerWarpGUI v " + pl.getPlayerWarpGUIVersion() + " >> SUCCESSFUL.");
			return;
		} 
		sendConsoleMessageBare("&cPlayerWarpGUI v " + pl.getPlayerWarpGUIVersion() + " >> FAILED.");
	}
}
