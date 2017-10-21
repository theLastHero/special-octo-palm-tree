package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import PlayerWarpGUI.PlayerWarpGUI;

public class MessageHandler {

	public static PlayerWarpGUI pl;

	public MessageHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;

	}

	public void sendPlayerMessage(Player player, String msg) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				pl.getLanguageHandler().getMessage("MESSAGE_PREFIX") + msg));
	}

	public void sendPlayerMessageBare(Player player, String msg) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void sendConsoleMessage(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				pl.getLanguageHandler().getMessage("CONSOLE_MSG_PREFIX") + msg));
	}

	public void sendConsoleMessageBare(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void sendTitle() {
		sendConsoleMessageBare("&b.-. .   .-. . . .-. .-. &3. . . .-. .-. .-. &a.-. . . .-. ");
		sendConsoleMessageBare("&b|-' |   |-|  |  |-  |(  &3| | | |-| |(  |-' &a|.. | |  |  ");
		sendConsoleMessageBare("&b'   `-' ` '  `  `-' ' ' &3`.'.' ` ' ' ' '   &a`-' `-' `-' ");
		startupStatus();
		showErrors();
	}

	public String ColorVariables(Player player, String str) {
		if (!player.hasPermission("pwarps.color.text")) {
			str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', str));
		}

		return str;

	}

	public void showErrors() {

		if (!pl.getNonCriticalErrors().isEmpty()) {
			for (String error : pl.getNonCriticalErrors()) {
				sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_NONCRITIAL_ERROR_PREFIX") + error);
			}
		}

		if (!pl.getCriticalErrors().isEmpty()) {
			for (String error : pl.getCriticalErrors()) {
				sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_CRITIAL_ERROR_PREFIX") + error);
			}
			pl.killPlugin();
		}

	}

	public void startupStatus() {
		sendConsoleMessageBare(pl.getLanguageHandler().getMessage("CONSOLE_MSG_FINAL", pl.getPlayerWarpGUIVersion(),
				pl.getLanguageHandler().statusValue(pl.getCriticalErrors().isEmpty())));
	}

}
