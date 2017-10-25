package PlayerWarpGUI.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;

public final class MessageSender {

	/**
	 * 
	 */
	private static String msgPrefix;
	/**
	 * 
	 */
	private static String msgConsolePrefix;
	/**
	 * 
	 */
	private static boolean isInitalized;

	/**
	 * @param pl
	 */
	/**
	 * 
	 */
	private MessageSender() {
		initialize();
	}

	/**
	 * @param p
	 */
	/**
	 * 
	 */
	private static void initialize() {
		if (!isInitalized) {
			msgPrefix = LocaleLoader.getString("MESSAGE_PREFIX"); // p.getLanguageHandler().getMessage("MESSAGE_PREFIX");
			msgConsolePrefix = LocaleLoader.getString("CONSOLE_MSG_PREFIX");
			isInitalized = true;
		}
	}
	
	public static void sendRaw(Player player, String key) {
		sendMsg(player, key);
	}

	// send message from language file (key)
	/**
	 * @param player
	 * @param key
	 */
	public static void send(Player player, String key) {
		sendMsg(player, LocaleLoader.getString(key));
	}

	/**
	 * @param player
	 * @param key
	 * @param object
	 */
	public static void send(Player player, String key, Object... object) {
		sendMsg(player, LocaleLoader.getString(key, object));
	}

	/**
	 * @param key
	 * @param object
	 */
	public static void sendConsole(String key, Object... object) {
		sendConsoleMsg(LocaleLoader.getString(key, object));
	}

	/**
	 * @param key
	 */
	public static void sendConsole(String key) {
		sendConsoleMsg(key);
	}

	/**
	 * @param player
	 * @param str
	 */
	public static void sendMsg(Player player, String str) {
		initialize();
		player.sendMessage(colorConvert(msgPrefix + str));
	}

	/**
	 * @param msg
	 */
	private static void sendConsoleMsg(String msg) {
		initialize();
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', (msgConsolePrefix + msg)));
	}

	/**
	 * 
	 */
	public static void sendTitle() {
		initialize();
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&b.-. .   .-. . . .-. .-. &3. . . .-. .-. .-. &a.-. . . .-. "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&b|-' |   |-|  |  |-  |(  &3| | | |-| |(  |-' &a|.. | |  |  "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&b'   `-' ` '  `  `-' ' ' &3`.'.' ` ' ' ' '   &a`-' `-' `-' "));
		startupStatus();
		showErrors();
	}

	/**
	 * 
	 */
	private static void showErrors() {
		initialize();
		if (!PlayerWarpGUI.getNonCriticalErrors().isEmpty()) {
			for (String error : PlayerWarpGUI.getNonCriticalErrors()) {
				sendConsole(LocaleLoader.getString("CONSOLE_MSG_NONCRITIAL_ERROR_PREFIX") + error);
			}
		}

		if (!PlayerWarpGUI.getCriticalErrors().isEmpty()) {
			initialize();
			for (String error : PlayerWarpGUI.getCriticalErrors()) {
				sendConsole(LocaleLoader.getString("CONSOLE_MSG_CRITIAL_ERROR_PREFIX") + error);
			}
			PlayerWarpGUI.killPlugin();
		}

	}

	/**
	 * 
	 */
	private static void startupStatus() {
		initialize();
		sendConsole(
				LocaleLoader.getString("CONSOLE_MSG_FINAL", statusValue(PlayerWarpGUI.getCriticalErrors().isEmpty())),
				"");
	}

	/**
	 * @param compare
	 * @return
	 */
	private static String statusValue(boolean compare) {
		String status = LocaleLoader.getString("SUCCESS");
		if (!compare) {
			status = LocaleLoader.getString("FAILED");
		}
		return status;

	}

	/**
	 * @param str
	 * @return
	 */
	public static String colorConvert(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

}
