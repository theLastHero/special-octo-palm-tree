package PlayerWarpGUI.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import PlayerWarpGUI.*;
import PlayerWarpGUI.PlayerWarpGUI;
import locale.LocaleLoader;

public class msgSender {

	public  PlayerWarpGUI p;

	public String msgPrefix;
	public String msgConsolePrefix;

	/**
	 * @param pl
	 */
	public msgSender(PlayerWarpGUI pl) {
		this.p = pl;
		setUp();
	}

	/**
	 * @param p
	 */
	private void setUp() {
		msgPrefix =LocaleLoader.getString("MESSAGE_PREFIX"); //p.getLanguageHandler().getMessage("MESSAGE_PREFIX");
		msgConsolePrefix = LocaleLoader.getString("CONSOLE_MSG_PREFIX");
	}

	//send message from language file (key)
	public void send(Player player, String key) {
		sendMsg(player, msgPrefix + LocaleLoader.getString(key));
	}
	
	public void send(Player player, String key, Object... object) {
		sendMsg(player, msgPrefix + LocaleLoader.getString(key, object));
	}
	
	
	public void sendConsole(String key, Object... object) {
		sendConsoleMsg(LocaleLoader.getString(key, object));
	}
	
	public void sendMsg(Player player, String str) {
		player.sendMessage(colorConvert(str));
	}
	
	public void sendConsoleMsg(String msg) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',(msg)));
	}
	
	public void sendTitle() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&b.-. .   .-. . . .-. .-. &3. . . .-. .-. .-. &a.-. . . .-. "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&b|-' |   |-|  |  |-  |(  &3| | | |-| |(  |-' &a|.. | |  |  "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&b'   `-' ` '  `  `-' ' ' &3`.'.' ` ' ' ' '   &a`-' `-' `-' "));
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

		if (!p.getNonCriticalErrors().isEmpty()) {
			for (String error : p.getNonCriticalErrors()) {
				sendConsole(LocaleLoader.getString("CONSOLE_MSG_NONCRITIAL_ERROR_PREFIX"));
			}
		}

		if (!p.getCriticalErrors().isEmpty()) {
			for (String error : p.getCriticalErrors()) {
				sendConsole(LocaleLoader.getString("CONSOLE_MSG_CRITIAL_ERROR_PREFIX"));
			}
			p.killPlugin();
		}

	}

	public void startupStatus() {
		sendConsole(LocaleLoader.getString("CONSOLE_MSG_FINAL",
				statusValue(p.getCriticalErrors().isEmpty())),"");
	}
	
	public String statusValue(boolean compare) {
		String status = LocaleLoader.getString("SUCCESS");
		if (!compare) {
			status = LocaleLoader.getString("FAILED");
		}
		return status;

	}
	
	public String colorConvert(String str) {
		return ChatColor.translateAlternateColorCodes('&',str);
	}
	
}
